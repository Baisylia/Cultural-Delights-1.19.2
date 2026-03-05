package com.baisylia.culturaldelights.block.custom;

import com.baisylia.culturaldelights.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

public class CornBlock extends CropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    public static final IntegerProperty HEIGHT = IntegerProperty.create("height", 0, 2);

    public CornBlock(Properties props) {
        super(props);
        registerDefaultState(this.stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(HEIGHT, 0));
    }

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
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;

        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = getAge(state);

            if (age < getMaxAge()) {
                float speed = getGrowthSpeed(this, level, pos);

                if (ForgeHooks.onCropsGrowPre(level, pos, state,
                        random.nextInt((int)(25.0F / speed) + 1) == 0)) {

                    BlockPos bottom = getBottom(level, pos);
                    int nextAge = getAge(level.getBlockState(bottom)) + 1;

                    if (canAdvanceAge(level, bottom, nextAge)) {
                        syncPlantAges(level, bottom, nextAge);
                        handleVerticalGrowth(level, bottom, nextAge);
                        ForgeHooks.onCropsGrowPost(level, pos, state);
                    }
                }
            }
        }
    }

    private void handleVerticalGrowth(ServerLevel level, BlockPos bottom, int age) {
        level.setBlock(bottom,
                level.getBlockState(bottom)
                        .setValue(HEIGHT, 0)
                        .setValue(AGE, age),
                2);

        if (age >= 3) {
            BlockPos middlePos = bottom.above();

            if (level.isEmptyBlock(middlePos) || level.getBlockState(middlePos).is(this)) {
                level.setBlock(middlePos,
                        defaultBlockState()
                                .setValue(AGE, age)
                                .setValue(HEIGHT, 1),
                        2);
            }
        }

        if (age >= 5) {
            BlockPos topPos = bottom.above(2);

            if (level.isEmptyBlock(topPos) || level.getBlockState(topPos).is(this)) {
                level.setBlock(topPos,
                        defaultBlockState()
                                .setValue(AGE, age)
                                .setValue(HEIGHT, 2),
                        2);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {

        int height = state.getValue(HEIGHT);

        BlockState below = level.getBlockState(pos.below());

        if (height == 0) {
            return super.canSurvive(state, level, pos);
        }

        return below.is(this);
    }

    private boolean canAdvanceAge(ServerLevel level, BlockPos bottom, int nextAge) {

        if (nextAge >= 3) {
            BlockPos middle = bottom.above();
            BlockState middleState = level.getBlockState(middle);

            if (!(middleState.isAir() || middleState.is(this))) {
                return false;
            }
        }

        if (nextAge >= 5) {
            BlockPos middle = bottom.above();
            BlockPos top = bottom.above(2);

            BlockState middleState = level.getBlockState(middle);
            BlockState topState = level.getBlockState(top);

            if (!(middleState.isAir() || middleState.is(this))) {
                return false;
            }

            if (!(topState.isAir() || topState.is(this))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockPos bottom = getBottom(level, pos);
            BlockPos current = bottom;

            boolean dropped = false;

            while (level.getBlockState(current).is(this)) {
                if (!dropped) {
                    level.destroyBlock(current, true);
                    dropped = true;
                } else {
                    level.setBlock(current, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                            35);
                }
                current = current.above();
            }
        }
    }

    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        BlockPos bottom = getBottom((LevelReader) level, pos);
        BlockState bottomState = level.getBlockState(bottom);
        return bottomState.getValue(AGE) < getMaxAge();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos bottom = getBottom(level, pos);
        BlockState bottomState = level.getBlockState(bottom);

        int nextAge = Math.min(bottomState.getValue(AGE) + getBonemealAgeIncrease(level), getMaxAge());

        if (canAdvanceAge(level, bottom, nextAge)) {
            syncPlantAges(level, bottom, nextAge);
            handleVerticalGrowth(level, bottom, nextAge);
        }
    }

    private BlockPos getBottom(LevelReader level, BlockPos pos) {
        while (level.getBlockState(pos.below()).is(this)) {
            pos = pos.below();
        }
        return pos;
    }

    private void syncPlantAges(ServerLevel level, BlockPos bottom, int age) {
        BlockPos current = bottom;
        int height = 0;

        while (level.getBlockState(current).is(this)) {
            level.setBlock(current,
                    level.getBlockState(current)
                            .setValue(AGE, age)
                            .setValue(HEIGHT, height),
                    2);
            current = current.above();
            height++;
        }
    }
}
