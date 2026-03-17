package com.baisylia.culturaldelights.block.custom;

import com.baisylia.culturaldelights.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.BuddingBushBlock;
import vectorwing.farmersdelight.common.block.BuddingTomatoBlock;

public class BuddingBeansBlock extends BuddingTomatoBlock {

    public BuddingBeansBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        return ((Block) ModBlocks.BUDDING_BEANS.get()).defaultBlockState();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if ((Integer)state.getValue(BuddingBushBlock.AGE) == 4) {
            level.setBlock(currentPos, ((Block) ModBlocks.BEANS.get()).defaultBlockState(), 3);
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public void growPastMaxAge(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlockAndUpdate(pos, ((Block) ModBlocks.BEANS.get()).defaultBlockState());
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int maxAge = this.getMaxAge();
        int ageGrowth = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(level), 7);
        if (ageGrowth <= maxAge) {
            level.setBlockAndUpdate(pos, (BlockState)state.setValue(AGE, ageGrowth));
        } else {
            int remainingGrowth = ageGrowth - maxAge - 1;
            level.setBlockAndUpdate(pos, (BlockState)((Block) ModBlocks.BEANS.get()).defaultBlockState().setValue(BeansBlock.VINE_AGE, remainingGrowth));
        }

    }
}