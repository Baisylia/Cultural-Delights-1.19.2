package com.baisylia.culturaldelights.block.custom;

import com.baisylia.culturaldelights.advancement.ModAdvancements;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class BeanstalkBlock extends DirectionalBlock implements SimpleWaterloggedBlock, BonemealableBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty SPIRAL = IntegerProperty.create("spiral", 0, 3);
    public static final IntegerProperty COLUMN = IntegerProperty.create("column", 1, 5);
    public static final BooleanProperty FLOWERING = BooleanProperty.create("flowering");
    public static final MapCodec<BeanstalkBlock> CODEC = simpleCodec(BeanstalkBlock::new);
    private static final int MIN_COLUMN_HEIGHT = 4;
    private static final int MAX_COLUMN_HEIGHT = 5;
    private static final double HEIGHT_LIMIT_ADVANCEMENT_RANGE = 64.0;

    public BeanstalkBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.UP)
                .setValue(SPIRAL, 0)
                .setValue(COLUMN, 1)
                .setValue(FLOWERING, false)
                .setValue(WATERLOGGED, false));
    }

    private static Direction axisA(Direction dir) {
        return dir.getAxis() == Direction.Axis.Y ? Direction.EAST : dir.getClockWise();
    }

    private static Direction axisB(Direction dir) {
        return switch (dir) {
            case UP -> Direction.SOUTH;
            case DOWN -> Direction.NORTH;
            default -> Direction.UP;
        };
    }

    private static BlockPos cellPos(BlockPos footprintOrigin, Direction dir, int cell) {
        return switch (cell) {
            case 1 -> footprintOrigin.relative(axisA(dir));
            case 2 -> footprintOrigin.relative(axisA(dir)).relative(axisB(dir));
            case 3 -> footprintOrigin.relative(axisB(dir));
            default -> footprintOrigin;
        };
    }

    private static Direction[] outwardDirs(Direction dir, int cell) {
        Direction a = axisA(dir);
        Direction b = axisB(dir);
        return switch (cell) {
            case 1 -> new Direction[]{a, b.getOpposite()};
            case 2 -> new Direction[]{a, b};
            case 3 -> new Direction[]{a.getOpposite(), b};
            default -> new Direction[]{a.getOpposite(), b.getOpposite()};
        };
    }

    private static boolean canGrowInto(LevelReader level, BlockPos pos) {
        if (level.isOutsideBuildHeight(pos)) {
            return false;
        }
        BlockState state = level.getBlockState(pos);
        return state.canBeReplaced() || state.is(ModBlocks.MAGIC_BEANS.get());
    }

    private static void placeStalk(ServerLevel level, Tip tip, Direction dir) {
        BlockPos pos = tip.pos();
        level.setBlockAndUpdate(pos, ModBlocks.BEANSTALK.get().defaultBlockState()
                .setValue(FACING, dir)
                .setValue(SPIRAL, tip.cell())
                .setValue(COLUMN, Math.min(tip.height(), MAX_COLUMN_HEIGHT))
                .setValue(FLOWERING, true)
                .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER));
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 4, 0.3, 0.3, 0.3, 0.05);
    }

    private static void placeLeaf(ServerLevel level, BlockPos stalkPos, Direction leafDir) {
        BlockPos leafPos = stalkPos.relative(leafDir);
        if (level.getBlockState(leafPos).canBeReplaced()) {
            level.setBlockAndUpdate(leafPos, ModBlocks.BEANSTALK_LEAF.get().defaultBlockState()
                    .setValue(BeanstalkLeafBlock.FACING, leafDir)
                    .setValue(BeanstalkLeafBlock.WATERLOGGED, level.getFluidState(leafPos).getType() == Fluids.WATER));
        }
    }

    /**
     * Plants the first block of a new stalk at {@code origin} and grows {@code amount} blocks from it
     */
    public static void sprout(ServerLevel level, RandomSource random, BlockPos origin, Direction dir, int amount) {
        Tip start = new Tip(origin, 0, 1);
        placeStalk(level, start, dir);
        growSpiral(level, random, dir, start, amount);
    }

    /**
     * Grows up to {@code amount} blocks onward from {@code tip}
     */
    public static void growSpiral(ServerLevel level, RandomSource random, Direction dir, Tip tip, int amount) {
        for (int i = 0; i < amount; i++) {
            Tip up = tip.up(dir);
            Tip side = tip.side(dir);
            Tip next;
            if (tip.height() < MIN_COLUMN_HEIGHT) {
                next = up;
            } else if (tip.height() >= MAX_COLUMN_HEIGHT) {
                next = side;
            } else {
                boolean preferSide = random.nextBoolean();
                Tip first = preferSide ? side : up;
                next = canGrowInto(level, first.pos()) ? first : (preferSide ? up : side);
            }
            if (!canGrowInto(level, next.pos())) {
                break;
            }
            placeStalk(level, next, dir);
            tip = next;

            Direction[] outward = outwardDirs(dir, tip.cell());
            if (dir.getAxis() == Direction.Axis.Y) {
                if (tip.height() % 2 == 0) {
                    placeLeaf(level, tip.pos(), outward[random.nextInt(2)]);
                }
            } else if (random.nextBoolean()) {
                placeLeaf(level, tip.pos(), outward[0]);
            }
        }
        level.playSound(null, tip.pos(), SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.playSound(null, tip.pos(), SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 1.0F, 1.2F);

        if (dir == Direction.UP && tip.pos().getY() >= level.getMaxBuildHeight() - 1) {
            for (ServerPlayer player : level.players()) {
                double dx = player.getX() - (tip.pos().getX() + 0.5);
                double dz = player.getZ() - (tip.pos().getZ() + 0.5);
                if (dx * dx + dz * dz <= HEIGHT_LIMIT_ADVANCEMENT_RANGE * HEIGHT_LIMIT_ADVANCEMENT_RANGE) {
                    ModAdvancements.GROW_BEANSTALK_TO_HEIGHT_LIMIT.get().trigger(player);
                }
            }
        }
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        BlockState state = this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
        return state.setValue(FLOWERING, !hasNext(context.getLevel(), context.getClickedPos(), state));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos)
                .setValue(FLOWERING, !hasNext(level, pos, state));
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
        builder.add(FACING, SPIRAL, COLUMN, FLOWERING, WATERLOGGED);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        Direction dir = state.getValue(FACING);
        Tip tip = findTip(level, pos, state);
        return (tip.height() < MAX_COLUMN_HEIGHT && canGrowInto(level, tip.up(dir).pos()))
                || (tip.height() >= MIN_COLUMN_HEIGHT && canGrowInto(level, tip.side(dir).pos()));
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        Tip tip = findTip(level, pos, state);
        int growAmount = 3 + random.nextInt(3);
        growSpiral(level, random, state.getValue(FACING), tip, growAmount);
    }

    private boolean isStalkAt(LevelReader level, Tip expected, Direction dir) {
        BlockState state = level.getBlockState(expected.pos());
        return state.is(this) && state.getValue(FACING) == dir && state.getValue(SPIRAL) == expected.cell()
                && (expected.height() > 1 || state.getValue(COLUMN) == 1);
    }

    private boolean hasNext(LevelReader level, BlockPos pos, BlockState state) {
        Direction dir = state.getValue(FACING);
        Tip tip = new Tip(pos, state.getValue(SPIRAL), state.getValue(COLUMN));
        return isStalkAt(level, tip.up(dir), dir) || isStalkAt(level, tip.side(dir), dir);
    }

    private Tip findTip(LevelReader level, BlockPos pos, BlockState state) {
        Direction dir = state.getValue(FACING);
        Tip tip = new Tip(pos, state.getValue(SPIRAL), state.getValue(COLUMN));
        for (int i = 0; i < 1024; i++) {
            Tip up = tip.up(dir);
            Tip side = tip.side(dir);
            if (isStalkAt(level, up, dir)) {
                tip = new Tip(up.pos(), up.cell(), level.getBlockState(up.pos()).getValue(COLUMN));
            } else if (isStalkAt(level, side, dir)) {
                tip = side;
            } else {
                break;
            }
        }
        return tip;
    }

    public record Tip(BlockPos pos, int cell, int height) {
        Tip up(Direction dir) {
            return new Tip(pos.relative(dir), cell, height + 1);
        }

        Tip side(Direction dir) {
            BlockPos footprintOrigin = pos.subtract(cellPos(BlockPos.ZERO, dir, cell));
            int nextCell = (cell + 1) % 4;
            return new Tip(cellPos(footprintOrigin, dir, nextCell), nextCell, 1);
        }
    }
}
