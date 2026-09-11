package com.baisylia.culturaldelights.block.entity.custom;

import com.baisylia.culturaldelights.block.custom.VatBlock;
import com.baisylia.culturaldelights.block.entity.ModBlockEntities;
import com.baisylia.culturaldelights.recipes.ModRecipes;
import com.baisylia.culturaldelights.recipes.VatRecipe;
import com.baisylia.culturaldelights.screens.VatMenu;
import com.baisylia.culturaldelights.util.ModTags;
import com.baisylia.culturaldelights.util.VatTemperature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.Optional;

public class VatBlockEntity extends BlockEntity implements MenuProvider, WorldlyContainer {

    private static final int[] INGREDIENT_SLOTS = new int[]{0, 1, 2, 3, 4, 5, 6};
    private static final int OUTPUT_SLOT = 7;
    protected final ContainerData data;
    private final ContainerOpenersCounter openersCounter;
    private int progress = 0;
    private int maxProgress = 72;
    private VatTemperature cachedTemperature = VatTemperature.NORMAL;
    private VatRecipe currentRecipe = null;
    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (slot < 7) {
                resetProgress();
            }
        }
    };
    private final IItemHandler inputHandler = new RangedWrapper(itemHandler, 0, 7);
    private final IItemHandler outputHandler = new RangedWrapper(itemHandler, 7, 8);

    public VatBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.VAT_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
        this.data = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> VatBlockEntity.this.progress;
                    case 1 -> VatBlockEntity.this.maxProgress;
                    case 2 -> VatBlockEntity.this.cachedTemperature.ordinal();
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> VatBlockEntity.this.progress = value;
                    case 1 -> VatBlockEntity.this.maxProgress = value;
                    case 2 -> VatBlockEntity.this.cachedTemperature =
                            VatTemperature.values()[value];
                }
            }

            public int getCount() {
                return 3;
            }
        };
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                VatBlockEntity.this.playSound(state, SoundEvents.BARREL_OPEN);
                VatBlockEntity.this.updateBlockState(state, true);
            }

            protected void onClose(Level level, BlockPos pos, BlockState state) {
                VatBlockEntity.this.playSound(state, SoundEvents.BARREL_CLOSE);
                VatBlockEntity.this.updateBlockState(state, false);
            }

            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int p_155069_, int p_155070_) {
            }

            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof VatMenu) {
                    BlockEntity be = ((VatMenu) player.containerMenu).getBlockEntity();
                    return be == VatBlockEntity.this;
                } else {
                    return false;
                }
            }
        };
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, VatBlockEntity pBlockEntity) {
        pBlockEntity.recheckOpen();

        VatTemperature newTemp = getTemperature(pPos, pLevel);
        if (newTemp != pBlockEntity.cachedTemperature) {
            pBlockEntity.cachedTemperature = newTemp;
            if (pState.getValue(VatBlock.TEMPERATURE) != newTemp) {
                pLevel.setBlock(pPos, pState.setValue(VatBlock.TEMPERATURE, newTemp), 3);
            }
            pBlockEntity.resetProgress();
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

    private static boolean canInsertItemIntoOutput(ItemStackHandler handler, ItemStack outputStack) {
        ItemStack currentOutput = handler.getStackInSlot(OUTPUT_SLOT);

        if (currentOutput.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(currentOutput, outputStack)) {
            return false;
        }

        return currentOutput.getCount() + outputStack.getCount() <= currentOutput.getMaxStackSize();
    }

    private static boolean hasRecipe(VatBlockEntity entity) {
        Level level = entity.level;
        if (level == null) return false;
        BlockPos pos = entity.getBlockPos();

        VatTemperature currentTemp = getTemperature(pos, level);
        RecipeWrapper wrapper = new RecipeWrapper(entity.itemHandler);

        if (entity.currentRecipe != null) {
            if (entity.currentRecipe.matches(wrapper, level)) {
                return canInsertItemIntoOutput(entity.itemHandler, entity.currentRecipe.getResultItem(level.registryAccess()));
            }
        }

        Optional<RecipeHolder<VatRecipe>> recipeMatch = level.getRecipeManager()
                .getRecipeFor(ModRecipes.AGING_TYPE.get(), wrapper, level);
        if (recipeMatch.isPresent()) {
            VatRecipe recipe = recipeMatch.get().value();

            if (recipe.getTemperature() != currentTemp) {
                return false;
            }

            entity.currentRecipe = recipe;
            entity.maxProgress = recipe.getCookTime();
            return canInsertItemIntoOutput(entity.itemHandler, recipe.getResultItem(level.registryAccess()));
        } else {
            entity.currentRecipe = null;
        }

        return false;
    }

    private static boolean isHeatSource(BlockState state) {
        if (state.is(ModTags.Blocks.HEAT_SOURCES)) {
            if (state.hasProperty(BlockStateProperties.LIT)) {
                return state.getValue(BlockStateProperties.LIT);
            }
            return true;
        }
        return false;
    }

    private static boolean isHeated(BlockPos pos, Level level) {
        BlockState stateBelow = level.getBlockState(pos.below());
        if (isHeatSource(stateBelow)) {
            return true;
        }
        if (stateBelow.is(ModTags.Blocks.HEAT_CONDUCTORS)) {
            BlockState stateFurtherBelow = level.getBlockState(pos.below(2));
            return isHeatSource(stateFurtherBelow);
        }
        return false;
    }

    private static boolean isColdSource(BlockState state) {
        if (state.is(ModTags.Blocks.COLD_SOURCES)) {
            if (state.hasProperty(BlockStateProperties.LIT)) {
                return state.getValue(BlockStateProperties.LIT);
            }
            return true;
        }
        return false;
    }

    private static boolean isCold(BlockPos pos, Level level) {
        BlockState stateBelow = level.getBlockState(pos.below());
        if (isColdSource(stateBelow)) {
            return true;
        }
        if (stateBelow.is(ModTags.Blocks.COLD_CONDUCTORS)) {
            BlockState stateFurtherBelow = level.getBlockState(pos.below(2));
            return isColdSource(stateFurtherBelow);
        }
        return false;
    }

    static VatTemperature getTemperature(BlockPos pos, Level level) {
        if (isHeated(pos, level)) {
            return VatTemperature.HOT;
        }
        if (isCold(pos, level)) {
            return VatTemperature.COLD;
        }

        return VatTemperature.NORMAL;
    }

    private static Direction getEjectionDirection(BlockPos pos, Level level, Direction facing) {
        if (facing.getAxis().isHorizontal()) {
            return facing.getCounterClockWise();
        }
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos neighbor = pos.relative(dir);
            if (!level.getBlockState(neighbor).isSolidRender(level, neighbor)) {
                return dir;
            }
        }
        return Direction.NORTH;
    }

    private static void craftItem(VatBlockEntity entity) {
        VatRecipe recipe = entity.currentRecipe;

        if (recipe == null || entity.level == null) {
            return;
        }

        RecipeWrapper wrapper = new RecipeWrapper(entity.itemHandler);
        ItemStack resultItem = recipe.assemble(wrapper, entity.level.registryAccess());

        // Handle by-products (empty buckets, etc.)
        for (int i = 0; i < 7; ++i) {
            ItemStack slotStack = entity.itemHandler.getStackInSlot(i);
            if (slotStack.hasCraftingRemainingItem()) {
                Direction facing = entity.getBlockState().getValue(VatBlock.FACING);
                Direction direction = getEjectionDirection(entity.worldPosition, entity.level, facing);
                double offset = facing.getAxis().isHorizontal() ? 0.25 : 0.6;
                double x = (double) entity.worldPosition.getX() + 0.5 + (double) direction.getStepX() * offset;
                double y = (double) entity.worldPosition.getY() + (facing.getAxis().isHorizontal() ? 0.7 : 0.5);
                double z = (double) entity.worldPosition.getZ() + 0.5 + (double) direction.getStepZ() * offset;
                spawnItemEntity(entity.level, slotStack.getCraftingRemainingItem(), x, y, z, (float) direction.getStepX() * 0.08F, 0.25, (float) direction.getStepZ() * 0.08F);
            }
        }

        // Consume ingredients
        for (int i = 0; i < 7; ++i) {
            entity.itemHandler.extractItem(i, 1, false);
        }

        // Add result
        entity.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(resultItem.getItem(),
                entity.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + resultItem.getCount()));

        entity.resetProgress();
    }

    public static void spawnItemEntity(Level level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.setDeltaMovement(xMotion, yMotion, zMotion);
        level.addFreshEntity(entity);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.culturaldelights.vat");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new VatMenu(pContainerId, pInventory, this, this.data);
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if (side == null) {
            return this.itemHandler;
        }
        return switch (side) {
            case DOWN -> this.outputHandler;
            default -> this.inputHandler;
        };
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("vat.progress", progress);
        tag.putInt("vat.temperature", cachedTemperature.ordinal());
        tag.putInt("vat.max_progress", maxProgress);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        itemHandler.deserializeNBT(registries, nbt.getCompound("inventory"));
        progress = nbt.getInt("vat.progress");
        cachedTemperature = VatTemperature.values()[nbt.getInt("vat.temperature")];
        maxProgress = nbt.getInt("vat.max_progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private void resetProgress() {
        this.progress = 0;
        this.maxProgress = 72;
        this.currentRecipe = null;
    }

    @Override
    public int getContainerSize() {
        return this.itemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.itemHandler.getSlots(); ++i) {
            ItemStack itemStack = this.itemHandler.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return this.itemHandler.extractItem(slot, amount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return this.itemHandler.extractItem(slot, 1, false);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.itemHandler.setStackInSlot(slot, itemStack);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
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
        this.level.setBlock(this.getBlockPos(), state.setValue(VatBlock.OPEN, open), 3);
    }

    void playSound(BlockState state, SoundEvent sound) {
        Vec3i normal = state.getValue(VatBlock.FACING).getNormal();
        double x = (double) this.worldPosition.getX() + (double) 0.5F + (double) normal.getX() / (double) 2.0F;
        double y = (double) this.worldPosition.getY() + (double) 0.5F + (double) normal.getY() / (double) 2.0F;
        double z = (double) this.worldPosition.getZ() + (double) 0.5F + (double) normal.getZ() / (double) 2.0F;
        this.level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return new int[]{7};
        } else return INGREDIENT_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return slot != 7;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < 8; ++i) {
            this.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
