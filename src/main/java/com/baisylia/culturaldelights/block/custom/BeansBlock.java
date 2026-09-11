package com.baisylia.culturaldelights.block.custom;

import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.integration.supplementaries.SupplementariesCompat;
import com.baisylia.culturaldelights.item.ModItems;
import com.mojang.serialization.MapCodec;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.fml.ModList;
import vectorwing.farmersdelight.common.block.TomatoBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Supplier;

public class BeansBlock extends TomatoBlock {
    public static final MapCodec<BeansBlock> CODEC = simpleCodec(BeansBlock::new);
    private static final Supplier<Boolean> ENABLE_BEAN_VINE_CLIMBING_TAGGED_ROPES = () -> true;

    public BeansBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0).setValue(ROPELOGGED, false));
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (ModList.get().isLoaded("supplementaries")) {
            InteractionResult stickResult = SupplementariesCompat.tryUseStick(state, level, pos, player, hand, hit);
            if (stickResult != InteractionResult.PASS) {
                return stickResult.consumesAction() ? ItemInteractionResult.sidedSuccess(level.isClientSide) : ItemInteractionResult.FAIL;
            }
        }

        int age = state.getValue(this.getAgeProperty());
        boolean isMature = age == this.getMaxAge();
        if (!isMature && stack.is(Items.BONE_MEAL)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        int age = state.getValue(this.getAgeProperty());
        boolean isMature = age == this.getMaxAge();
        if (isMature) {
            int quantity = 1 + level.random.nextInt(2);
            popResource(level, pos, new ItemStack(ModItems.BEAN_POD.get(), quantity));

            level.playSound(null, pos, ModSounds.BLOCK_TOMATOES_PICK_TOMATOES.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            level.setBlock(pos, state.setValue(this.getAgeProperty(), 0), 2);
            return InteractionResult.SUCCESS;
        } else {
            return super.useWithoutItem(state, level, pos, player, hit);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        if (!state.getValue(ROPELOGGED)) {
            return super.canSurvive(state, level, pos);
        } else {
            return belowState.is(ModBlocks.BEANS.get()) && this.hasGoodCropConditions(level, pos);
        }
    }

    @Override
    public boolean canClimbBlock(BlockState stateAbove) {
        if (ModList.get().isLoaded("supplementaries")) {
            if (stateAbove.is(ModRegistry.ROPE.get()) || stateAbove.is(ModRegistry.STICK_BLOCK.get())) {
                return true;
            }
        }
        return ENABLE_BEAN_VINE_CLIMBING_TAGGED_ROPES.get() ? stateAbove.is(ModTags.Blocks.ROPES) : stateAbove.is(vectorwing.farmersdelight.common.registry.ModBlocks.ROPE.get());
    }

    @Override
    public BlockState getClimbingState(BlockState stateAbove) {
        if (this.canClimbBlock(stateAbove)) {
            if (ModList.get().isLoaded("supplementaries")) {
                return SupplementariesCompat.getRopeOrStickBeansToPlace(stateAbove, defaultBlockState());
            }
            return defaultBlockState().setValue(ROPELOGGED, true);
        }
        return null;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.BEANS.get();
    }
}