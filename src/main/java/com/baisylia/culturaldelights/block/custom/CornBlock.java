package com.baisylia.culturaldelights.block.custom;

import com.baisylia.culturaldelights.item.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

import javax.annotation.Nullable;

public class CornBlock extends CropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final int MAX_HEIGHT = 2;
    public static final IntegerProperty HEIGHT = IntegerProperty.create("height", 0, MAX_HEIGHT);
    public static final MapCodec<CornBlock> CODEC = simpleCodec(CornBlock::new);

    private static final VoxelShape SHAPE_BOTTOM_0 = Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0);
    private static final VoxelShape SHAPE_BOTTOM_1 = Block.box(3.0, 0.0, 3.0, 13.0, 9.0, 13.0);
    private static final VoxelShape SHAPE_BOTTOM_2 = Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0);
    private static final VoxelShape SHAPE_FULL = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    private static final VoxelShape SHAPE_MIDDLE_3 = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);
    private static final VoxelShape SHAPE_TOP_5 = Block.box(2.0, 0.0, 2.0, 14.0, 10.0, 14.0);
    private static final VoxelShape SHAPE_TOP_6 = Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0);

    public CornBlock(Properties props) {
        super(props);
        registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(HEIGHT, 0));
    }

    @Override
    public MapCodec<CornBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, HEIGHT);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 7;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.CORN_KERNELS.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int height = state.getValue(HEIGHT);
        int age = state.getValue(AGE);
        if (height == 0) {
            return switch (age) {
                case 0 -> SHAPE_BOTTOM_0;
                case 1 -> SHAPE_BOTTOM_1;
                case 2 -> SHAPE_BOTTOM_2;
                default -> SHAPE_FULL;
            };
        } else if (height == 1) {
            return age == 3 ? SHAPE_MIDDLE_3 : SHAPE_FULL;
        } else {
            return switch (age) {
                case 5 -> SHAPE_TOP_5;
                case 6 -> SHAPE_TOP_6;
                default -> SHAPE_FULL;
            };
        }
    }

    public static int getExpectedMaxHeight(int age) {
        if (age >= 5) return 2;
        if (age >= 3) return 1;
        return 0;
    }

    public BlockPos getBottom(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(this)) {
            int height = state.getValue(HEIGHT);
            if (height > 0) {
                BlockPos candidate = pos.below(height);
                BlockState candidateState = level.getBlockState(candidate);
                if (candidateState.is(this) && candidateState.getValue(HEIGHT) == 0) {
                    return candidate;
                }
            }
        }
        while (level.getBlockState(pos.below()).is(this)) {
            pos = pos.below();
        }
        return pos;
    }

    public boolean canGrowUp(BlockGetter level, BlockPos bottom, int currentAge, int newAge) {
        int currentMaxHeight = getExpectedMaxHeight(currentAge);
        int newMaxHeight = getExpectedMaxHeight(newAge);
        for (int h = currentMaxHeight + 1; h <= newMaxHeight; h++) {
            BlockPos targetPos = bottom.above(h);
            BlockState targetState = level.getBlockState(targetPos);
            if (!targetState.isAir() && !targetState.is(this) && !targetState.canBeReplaced()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HEIGHT) == 0 && !this.isMaxAge(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;
        if (state.getValue(HEIGHT) != 0) return;

        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                float speed = getGrowthSpeed(state, level, pos);
                if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int) (25.0F / speed) + 1) == 0)) {
                    this.growCropBy(level, pos, state, 1);
                    CommonHooks.fireCropGrowPost(level, pos, state);
                }
            }
        }
    }

    public void growCropBy(Level level, BlockPos pos, BlockState state, int increment) {
        BlockPos bottom = getBottom(level, pos);
        BlockState bottomState = level.getBlockState(bottom);
        if (!bottomState.is(this)) return;

        int currentAge = this.getAge(bottomState);
        int newAge = Math.min(currentAge + increment, this.getMaxAge());
        if (newAge <= currentAge) return;

        if (!canGrowUp(level, bottom, currentAge, newAge)) {
            return;
        }

        int targetHeight = getExpectedMaxHeight(newAge);
        for (int h = 0; h <= targetHeight; h++) {
            BlockPos targetPos = bottom.above(h);
            BlockState targetState = defaultBlockState().setValue(AGE, newAge).setValue(HEIGHT, h);
            level.setBlock(targetPos, targetState, 3);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        int height = state.getValue(HEIGHT);
        int age = state.getValue(AGE);

        if (height > getExpectedMaxHeight(age)) {
            return false;
        }

        if (height == 0) {
            return super.canSurvive(state, level, pos);
        }

        BlockState belowState = level.getBlockState(pos.below());
        if (!belowState.is(this)) return false;
        if (belowState.getValue(HEIGHT) != height - 1) return false;
        return height <= getExpectedMaxHeight(belowState.getValue(AGE));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        int height = state.getValue(HEIGHT);
        int age = state.getValue(AGE);
        int maxHeight = getExpectedMaxHeight(age);

        if (facing == Direction.DOWN && !state.canSurvive(level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }

        if (facing == Direction.UP && height < maxHeight) {
            if (!facingState.is(this) || facingState.getValue(HEIGHT) != height + 1) {
                return Blocks.AIR.defaultBlockState();
            }
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (player.isCreative()) {
                BlockPos bottom = getBottom(level, pos);
                int maxHeight = getExpectedMaxHeight(state.getValue(AGE));
                for (int h = maxHeight; h >= 0; h--) {
                    BlockPos p = bottom.above(h);
                    if (!p.equals(pos)) {
                        BlockState s = level.getBlockState(p);
                        if (s.is(this)) {
                            level.setBlock(p, Blocks.AIR.defaultBlockState(), 35);
                            level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, p, Block.getId(s));
                        }
                    }
                }
            } else {
                dropResources(state, level, pos, null, player, player.getMainHandItem());
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), te, stack);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        BlockPos bottom = getBottom(level, pos);
        BlockState bottomState = level.getBlockState(bottom);
        if (!bottomState.is(this)) return false;
        int age = this.getAge(bottomState);
        if (age >= this.getMaxAge()) return false;
        return canGrowUp(level, bottom, age, age + 1);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        growCropBy(level, pos, state, getBonemealAgeIncrease(level));
    }
}
