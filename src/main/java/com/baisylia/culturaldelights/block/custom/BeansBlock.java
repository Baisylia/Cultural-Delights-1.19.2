package com.baisylia.culturaldelights.block.custom;

import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.block.TomatoVineBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;
import net.minecraft.sounds.SoundSource;

public class BeansBlock extends TomatoVineBlock {

    public BeansBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(this.getAgeProperty(), 0)).setValue(ROPELOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int age = (Integer)state.getValue(this.getAgeProperty());
        boolean isMature = age == this.getMaxAge();
        if (!isMature && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (isMature) {
            int quantity = 1 + level.random.nextInt(2);
            popResource(level, pos, new ItemStack((ItemLike) ModItems.BEAN_POD.get(), quantity));

            level.playSound((Player)null, pos, (SoundEvent)ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            level.setBlock(pos, (BlockState)state.setValue(this.getAgeProperty(), 0), 2);
            return InteractionResult.SUCCESS;
        } else {
            return super.use(state, level, pos, player, hand, hit);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        if (!(Boolean)state.getValue(ROPELOGGED)) {
            return super.canSurvive(state, level, pos);
        } else {
            return belowState.is((Block) ModBlocks.BEANS.get()) && this.hasGoodCropConditions(level, pos);
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return (ItemLike) ModItems.BEANS.get();
    }
}