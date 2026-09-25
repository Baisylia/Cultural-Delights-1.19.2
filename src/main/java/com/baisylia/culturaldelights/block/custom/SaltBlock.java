package com.baisylia.culturaldelights.block.custom;

import com.baisylia.culturaldelights.advancement.ModAdvancements;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.WaterloggedTransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SaltBlock extends Block {
    public static final MapCodec<SaltBlock> CODEC = simpleCodec(SaltBlock::new);

    private static final float GROWTH_PROBABILITY_PER_RANDOM_TICK = 0.2F;
    private static final int MAX_STALAGMITE_SEARCH_RANGE_WHEN_GROWING = 10;
    private static final int MAX_STALACTITE_SEARCH_LENGTH = 11;
    private static final VoxelShape REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public SaltBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static boolean isWaterAbove(LevelReader level, BlockPos saltPos) {
        BlockPos abovePos = saltPos.above();
        FluidState directFluid = level.getFluidState(abovePos);

        if (directFluid.is(FluidTags.WATER)) {
            return true;
        }

        BlockState aboveState = level.getBlockState(abovePos);
        if (aboveState.getBlock() instanceof WaterloggedTransparentBlock || canDripThrough(level, abovePos, aboveState)) {
            FluidState higherFluid = level.getFluidState(abovePos.above());
            return higherFluid.is(FluidTags.WATER);
        }

        return false;
    }

    @Nullable
    private static BlockPos findStalactiteTip(LevelReader level, BlockPos startPos) {
        BlockPos.MutableBlockPos current = startPos.mutable();
        for (int i = 0; i < MAX_STALACTITE_SEARCH_LENGTH; i++) {
            BlockState state = level.getBlockState(current);
            if (!state.is(Blocks.POINTED_DRIPSTONE) || state.getValue(PointedDripstoneBlock.TIP_DIRECTION) != Direction.DOWN) {
                return null;
            }
            if (state.getValue(PointedDripstoneBlock.THICKNESS) == DripstoneThickness.TIP) {
                return current.immutable();
            }
            current.move(Direction.DOWN);
        }
        return null;
    }

    private static void growSaltSpikeBelow(ServerLevel level, BlockPos tipPos) {
        BlockPos.MutableBlockPos searchPos = tipPos.mutable();

        for (int i = 0; i < MAX_STALAGMITE_SEARCH_RANGE_WHEN_GROWING; i++) {
            searchPos.move(Direction.DOWN);
            BlockState current = level.getBlockState(searchPos);

            if (!current.getFluidState().isEmpty()) {
                return;
            }

            if (current.is(ModBlocks.SALT_SPIKE.get())) {
                return;
            }

            BlockPos floorPos = searchPos.below();
            BlockState floorState = level.getBlockState(floorPos);

            if ((current.isAir() || current.canBeReplaced()) && floorState.isFaceSturdy(level, floorPos, Direction.UP) && !level.isWaterAt(floorPos)) {
                level.setBlockAndUpdate(searchPos, ModBlocks.SALT_SPIKE.get().defaultBlockState());
                for (ServerPlayer player : level.players()) {
                    if (player.blockPosition().closerThan(searchPos, 32.0)) {
                        ModAdvancements.GROW_SALT_SPIKE.get().trigger(player);
                    }
                }
                return;
            }

            if (!canDripThrough(level, searchPos, current)) {
                return;
            }
        }
    }

    private static boolean canDripThrough(BlockGetter level, BlockPos pos, BlockState state) {
        if (state.isAir()) {
            return true;
        } else if (state.isSolidRender(level, pos)) {
            return false;
        } else if (!state.getFluidState().isEmpty()) {
            return false;
        } else {
            VoxelShape voxelshape = state.getCollisionShape(level, pos);
            return !Shapes.joinIsNotEmpty(REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK, voxelshape, BooleanOp.AND);
        }
    }

    @Override
    public MapCodec<SaltBlock> codec() {
        return CODEC;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() >= GROWTH_PROBABILITY_PER_RANDOM_TICK) {
            return;
        }

        if (!isWaterAbove(level, pos)) {
            return;
        }

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        if (!belowState.is(Blocks.POINTED_DRIPSTONE) || belowState.getValue(PointedDripstoneBlock.TIP_DIRECTION) != Direction.DOWN) {
            return;
        }

        BlockPos tipPos = findStalactiteTip(level, belowPos);
        if (tipPos == null) {
            return;
        }

        BlockState tipState = level.getBlockState(tipPos);
        if (!PointedDripstoneBlock.canDrip(tipState)) {
            return;
        }

        growSaltSpikeBelow(level, tipPos);
    }
}
