package com.baisylia.culturaldelights.block.custom;

import com.baisylia.culturaldelights.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class BeanstalkBlock extends DirectionalBlock implements SimpleWaterloggedBlock, BonemealableBlock {
    public static final MapCodec<BeanstalkBlock> CODEC = simpleCodec(BeanstalkBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BeanstalkBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.UP)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        Direction dir = state.getValue(FACING);
        BlockPos.MutableBlockPos cursor = pos.mutable();
        while (level.getBlockState(cursor.relative(dir)).is(this)) {
            cursor.move(dir);
        }
        BlockPos next = cursor.relative(dir);
        return !level.isOutsideBuildHeight(next) && level.getBlockState(next).canBeReplaced();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        Direction dir = state.getValue(FACING);
        BlockPos.MutableBlockPos cursor = pos.mutable();
        while (level.getBlockState(cursor.relative(dir)).is(this)) {
            cursor.move(dir);
        }
        int growAmount = 3 + random.nextInt(3);
        growStemSegment(level, random, cursor, dir, growAmount);
    }

    public static void growStemSegment(ServerLevel level, RandomSource random, BlockPos.MutableBlockPos cursor, Direction dir, int amount) {
        Direction[] horizontalDirs = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        for (int i = 0; i < amount; i++) {
            BlockPos nextStalk = cursor.relative(dir);
            if (level.isOutsideBuildHeight(nextStalk) || !level.getBlockState(nextStalk).canBeReplaced()) {
                break;
            }
            cursor.move(dir);
            level.setBlockAndUpdate(cursor, ModBlocks.BEANSTALK.get().defaultBlockState()
                    .setValue(FACING, dir)
                    .setValue(WATERLOGGED, level.getFluidState(cursor).getType() == Fluids.WATER));

            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, cursor.getX() + 0.5, cursor.getY() + 0.5, cursor.getZ() + 0.5, 4, 0.3, 0.3, 0.3, 0.05);

            if (dir.getAxis() == Direction.Axis.Y) {
                int y = cursor.getY();
                if (Math.abs(y) % 2 == 0) {
                    int dirIndex = Math.floorMod(y / 2, 4);
                    Direction leafDir = horizontalDirs[dirIndex];
                    BlockPos leafPos = cursor.relative(leafDir);
                    if (level.getBlockState(leafPos).canBeReplaced()) {
                        BlockState leafState = ModBlocks.BEANSTALK_LEAF.get().defaultBlockState()
                                .setValue(BeanstalkLeafBlock.FACING, leafDir)
                                .setValue(BeanstalkLeafBlock.WATERLOGGED, level.getFluidState(leafPos).getType() == Fluids.WATER);
                        level.setBlockAndUpdate(leafPos, leafState);
                    }
                }
            } else {
                if (random.nextBoolean()) {
                    Direction leafDir = dir.getClockWise();
                    BlockPos leafPos = cursor.relative(leafDir);
                    if (level.getBlockState(leafPos).canBeReplaced()) {
                        BlockState leafState = ModBlocks.BEANSTALK_LEAF.get().defaultBlockState()
                                .setValue(BeanstalkLeafBlock.FACING, leafDir)
                                .setValue(BeanstalkLeafBlock.WATERLOGGED, level.getFluidState(leafPos).getType() == Fluids.WATER);
                        level.setBlockAndUpdate(leafPos, leafState);
                    }
                }
            }
        }
        level.playSound(null, cursor, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.playSound(null, cursor, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 1.0F, 1.2F);
    }
}
