package com.baisylia.culturaldelights.block.entity.custom;

import com.baisylia.culturaldelights.block.custom.OvenBlock;
import com.baisylia.culturaldelights.block.entity.ModBlockEntities;
import com.baisylia.culturaldelights.recipes.ModRecipes;
import com.baisylia.culturaldelights.recipes.OvenRecipe;
import com.baisylia.culturaldelights.recipes.OvenShapedRecipe;
import com.baisylia.culturaldelights.screens.OvenMenu;
import com.baisylia.culturaldelights.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.baisylia.culturaldelights.block.custom.OvenBlock.LIT;

public class OvenBlockEntity extends BlockEntity implements MenuProvider {

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;
    private int litTime = 0;
    private ContainerOpenersCounter openersCounter;

    private final ItemStackHandler itemHandler = new ItemStackHandler(11) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public OvenBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.OVEN_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);

        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> OvenBlockEntity.this.progress;
                    case 1 -> OvenBlockEntity.this.maxProgress;
                    case 2 -> OvenBlockEntity.this.litTime;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> OvenBlockEntity.this.progress = value;
                    case 1 -> OvenBlockEntity.this.maxProgress = value;
                    case 2 -> OvenBlockEntity.this.litTime = value;
                }
            }

            public int getCount() {
                return 3;
            }
        };
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                OvenBlockEntity.this.playSound(state, ModSounds.OVEN_OPEN.get());
                OvenBlockEntity.this.updateBlockState(state, true);
            }

            protected void onClose(Level level, BlockPos pos, BlockState state) {
                OvenBlockEntity.this.playSound(state, ModSounds.OVEN_CLOSE.get());
                OvenBlockEntity.this.updateBlockState(state, false);
            }

            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int p_155069_, int p_155070_) {
            }

            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof OvenMenu) {
                    BlockEntity be = ((OvenMenu)player.containerMenu).getBlockEntity();
                    return be == OvenBlockEntity.this;
                } else {
                    return false;
                }
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.culturaldelights.oven");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new OvenMenu(pContainerId, pInventory, this, this.data);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("oven.progress");
        litTime = tag.getInt("oven.lit_time");
        maxProgress = tag.getInt("oven.max_progress");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("oven.progress", progress);
        tag.putInt("oven.lit_time", litTime);
        tag.putInt("oven.max_progress", maxProgress);
        super.saveAdditional(tag, registries);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, OvenBlockEntity pBlockEntity) {
        pBlockEntity.recheckOpen();
        if (isFueled(pBlockEntity, pPos, pLevel)) {
            pBlockEntity.litTime = 1;
            setChanged(pLevel, pPos, pState);
        } else {
            pBlockEntity.litTime = 0;
            setChanged(pLevel, pPos, pState);
        }

        if (hasRecipe(pBlockEntity)) {
            pBlockEntity.progress++;
            setChanged(pLevel, pPos, pState);
            if (pBlockEntity.progress > pBlockEntity.maxProgress) {
                craftItem(pBlockEntity);
            }
        } else {
            pBlockEntity.resetProgress();
            setChanged(pLevel, pPos, pState);
        }
    }

    private static boolean hasRecipe(OvenBlockEntity entity) {
        Level level = entity.level;
        BlockPos pos = entity.getBlockPos();

        // Check if the oven is fueled (lit)
        if (!isFueled(entity, pos, level)) {
            return false;
        }

        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        // Check for OvenShapedRecipe
        Optional<RecipeHolder<OvenShapedRecipe>> shapedMatch =
                level.getRecipeManager().getRecipeFor(ModRecipes.BAKING_SHAPED.get(), new RecipeWrapper(entity.itemHandler), level);

        // Check for OvenRecipe
        Optional<RecipeHolder<OvenRecipe>> recipeMatch =
                level.getRecipeManager().getRecipeFor(ModRecipes.BAKING.get(), new RecipeWrapper(entity.itemHandler), level);

        if (shapedMatch.isPresent()) {
            OvenShapedRecipe recipe = shapedMatch.get().value();
            entity.maxProgress = recipe.getCookTime();
            return true;
        }
        if (recipeMatch.isPresent()) {
            OvenRecipe recipe = recipeMatch.get().value();
            entity.maxProgress = recipe.getCookTime();
            return true;
        }
        return false;
    }

    static boolean isFueled(OvenBlockEntity entity, BlockPos pos, Level level) {
        BlockState stateBelow = level.getBlockState(pos.below());
        if (stateBelow.hasProperty(BlockStateProperties.LIT) ? stateBelow.getValue(BlockStateProperties.LIT) : true) {
            if (stateBelow.is(ModTags.Blocks.HEAT_SOURCES) || stateBelow.is(ModTags.Blocks.HEAT_CONDUCTORS)) {
                level.setBlock(pos, entity.getBlockState().setValue(LIT, true), 3);
                return true;
            } else {
                level.setBlock(pos, entity.getBlockState().setValue(LIT, false), 3);
                return false;
            }
        } else {
            level.setBlock(pos, entity.getBlockState().setValue(LIT, false), 3);
            return false;
        }
    }

    private static void craftItem(OvenBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }
        Optional<RecipeHolder<OvenRecipe>> recipeMatch = level.getRecipeManager()
                .getRecipeFor(ModRecipes.BAKING.get(), new RecipeWrapper(entity.itemHandler), level);
        Optional<RecipeHolder<OvenShapedRecipe>> shapedMatch = level.getRecipeManager()
                .getRecipeFor(ModRecipes.BAKING_SHAPED.get(), new RecipeWrapper(entity.itemHandler), level);

        if (recipeMatch.isPresent() || shapedMatch.isPresent()) {
            for (int i = 0; i < 9; ++i) {
                ItemStack slotStack = entity.itemHandler.getStackInSlot(i);
                if (slotStack.hasCraftingRemainingItem()) {
                    Direction direction = entity.getBlockState().getValue(OvenBlock.FACING).getCounterClockWise();
                    double x = (double)entity.worldPosition.getX() + 0.5 + (double)direction.getStepX() * 0.25;
                    double y = (double)entity.worldPosition.getY() + 0.7;
                    double z = (double)entity.worldPosition.getZ() + 0.5 + (double)direction.getStepZ() * 0.25;
                    spawnItemEntity(entity.level, entity.itemHandler.getStackInSlot(i).getCraftingRemainingItem(), x, y, z, (double)((float)direction.getStepX() * 0.08F), 0.25, (double)((float)direction.getStepZ() * 0.08F));
                }
            }

            for (int i = 0; i < 9; ++i) {
                entity.itemHandler.extractItem(i, 1, false);
            }

            ItemStack result;
            if (shapedMatch.isPresent()) {
                OvenShapedRecipe recipe = shapedMatch.get().value();
                result = recipe.getResultItem(level.registryAccess());
            } else {
                OvenRecipe recipe = recipeMatch.get().value();
                result = recipe.getResultItem(level.registryAccess());
            }

            inventory.getItem(9).is(result.getItem());

            entity.itemHandler.setStackInSlot(9, new ItemStack(result.getItem(),
                    entity.itemHandler.getStackInSlot(9).getCount() + entity.getTheCount(result)));

            entity.resetProgress();
        }
    }

    public static void spawnItemEntity(Level level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.setDeltaMovement(xMotion, yMotion, zMotion);
        level.addFreshEntity(entity);
    }

    private int getTheCount(ItemStack itemIn) {
        return itemIn.getCount();
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    void updateBlockState(BlockState state, boolean open) {
        this.level.setBlock(this.getBlockPos(), state.setValue(OvenBlock.OPEN, open), 3);
    }

    void playSound(BlockState state, SoundEvent sound) {
        Vec3i normal = state.getValue(OvenBlock.FACING).getNormal();
        double x = (double)this.worldPosition.getX() + (double)0.5F + (double)normal.getX() / (double)2.0F;
        double y = (double)this.worldPosition.getY() + (double)0.5F + (double)normal.getY() / (double)2.0F;
        double z = (double)this.worldPosition.getZ() + (double)0.5F + (double)normal.getZ() / (double)2.0F;
        this.level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }
}
