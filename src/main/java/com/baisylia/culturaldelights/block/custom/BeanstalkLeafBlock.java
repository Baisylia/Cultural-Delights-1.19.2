package com.baisylia.culturaldelights.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Tilt;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class BeanstalkLeafBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<BeanstalkLeafBlock> CODEC = simpleCodec(BeanstalkLeafBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Tilt> TILT = BlockStateProperties.TILT;

    private static final Map<Tilt, VoxelShape> SHAPES = Map.of(
            Tilt.NONE, Block.box(0.0, 11.0, 0.0, 16.0, 15.0, 16.0),
            Tilt.UNSTABLE, Block.box(0.0, 11.0, 0.0, 16.0, 15.0, 16.0),
            Tilt.PARTIAL, Block.box(0.0, 7.0, 0.0, 16.0, 13.0, 16.0),
            Tilt.FULL, Block.box(0.0, 3.0, 0.0, 16.0, 9.0, 16.0)
    );

    private static final Map<Tilt, VoxelShape> COLLISION_SHAPES = Map.of(
            Tilt.NONE, Block.box(0.0, 11.0, 0.0, 16.0, 15.0, 16.0),
            Tilt.UNSTABLE, Block.box(0.0, 11.0, 0.0, 16.0, 15.0, 16.0),
            Tilt.PARTIAL, Block.box(0.0, 7.0, 0.0, 16.0, 11.0, 16.0),
            Tilt.FULL, Block.box(0.0, 3.0, 0.0, 16.0, 7.0, 16.0)
    );

    public BeanstalkLeafBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false)
                .setValue(TILT, Tilt.NONE));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.getOrDefault(state.getValue(TILT), SHAPES.get(Tilt.NONE));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPES.getOrDefault(state.getValue(TILT), COLLISION_SHAPES.get(Tilt.NONE));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos attachedPos = pos.relative(facing.getOpposite());
        BlockState attachedState = level.getBlockState(attachedPos);
        return attachedState.isFaceSturdy(level, attachedPos, facing);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (direction == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        Direction facing = clickedFace.getAxis().isHorizontal() ? clickedFace : context.getHorizontalDirection().getOpposite();

        BlockPos attachedPos = context.getClickedPos().relative(facing.getOpposite());
        if (context.getLevel().getBlockState(attachedPos).isFaceSturdy(context.getLevel(), attachedPos, facing)) {
            return this.defaultBlockState()
                    .setValue(FACING, facing)
                    .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
                    .setValue(TILT, Tilt.NONE);
        }

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos checkPos = context.getClickedPos().relative(dir.getOpposite());
            if (context.getLevel().getBlockState(checkPos).isFaceSturdy(context.getLevel(), checkPos, dir)) {
                return this.defaultBlockState()
                        .setValue(FACING, dir)
                        .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER)
                        .setValue(TILT, Tilt.NONE);
            }
        }
        return null;
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        entity.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
        entity.resetFallDistance();
        if (!level.isClientSide && fallDistance > 0.5F) {
            Tilt target = fallDistance > 2.5F ? Tilt.FULL : Tilt.PARTIAL;
            if (state.getValue(TILT) == Tilt.NONE) {
                level.setBlock(pos, state.setValue(TILT, target), 3);
                level.scheduleTick(pos, this, 10);
                level.playSound(null, pos, SoundEvents.BIG_DRIPLEAF_TILT_DOWN, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide) {
            if (state.getValue(TILT) == Tilt.NONE && canEntityTilt(pos, entity)) {
                setTiltAndScheduleTick(state, level, pos, Tilt.UNSTABLE, null, 8);
            }
        }
    }

    private static boolean canEntityTilt(BlockPos pos, Entity entity) {
        return entity.onGround() && entity.getY() > (double) pos.getY() + 0.6875D;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Tilt tilt = state.getValue(TILT);
        AABB box = new AABB(pos.getX(), pos.getY() + 0.6875D, pos.getZ(), pos.getX() + 1, pos.getY() + 1.2, pos.getZ() + 1);
        List<Entity> entities = level.getEntities((Entity) null, box, e -> !e.isSpectator() && e.onGround());

        if (entities.isEmpty()) {
            if (tilt != Tilt.NONE) {
                resetTilt(state, level, pos);
            }
            return;
        }

        if (tilt == Tilt.UNSTABLE) {
            setTiltAndScheduleTick(state, level, pos, Tilt.PARTIAL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN, 10);
        } else if (tilt == Tilt.PARTIAL) {
            setTiltAndScheduleTick(state, level, pos, Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN, 10);
        } else if (tilt == Tilt.FULL) {
            level.scheduleTick(pos, this, 10);
        }
    }

    public static void setTiltAndScheduleTick(BlockState state, Level level, BlockPos pos, Tilt tilt, @Nullable net.minecraft.sounds.SoundEvent sound, int delay) {
        level.setBlock(pos, state.setValue(TILT, tilt), 3);
        if (sound != null) {
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.1F);
        }
        level.scheduleTick(pos, state.getBlock(), delay);
    }

    public static void resetTilt(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(TILT, Tilt.NONE), 3);
        level.playSound(null, pos, SoundEvents.BIG_DRIPLEAF_TILT_UP, SoundSource.BLOCKS, 1.0F, 1.2F);
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        setTiltAndScheduleTick(state, level, hit.getBlockPos(), Tilt.FULL, SoundEvents.BIG_DRIPLEAF_TILT_DOWN, 25);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, TILT);
    }
}
