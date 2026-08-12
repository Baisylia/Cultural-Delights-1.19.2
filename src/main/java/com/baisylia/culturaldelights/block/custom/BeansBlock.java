package com.baisylia.culturaldelights.block.custom;

import ca.weblite.objc.Proxy;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.TomatoVineBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Objects;
import java.util.function.Supplier;

import static vectorwing.farmersdelight.common.registry.ModBlocks.*;

public class BeansBlock extends TomatoVineBlock {

    // todo move to config?
    private static final Supplier<Boolean> ENABLE_BEAN_VINE_CLIMBING_TAGGED_ROPES = () -> true;
    public static final ResourceLocation STICK = new ResourceLocation("supplementaries", "stick");

    public BeansBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0).setValue(ROPELOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int age = state.getValue(this.getAgeProperty());
        boolean isMature = age == this.getMaxAge();
        if (!isMature && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (isMature) {
            int quantity = 1 + level.random.nextInt(2);
            popResource(level, pos, new ItemStack(ModItems.BEAN_POD.get(), quantity));

            level.playSound(null, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            level.setBlock(pos, state.setValue(this.getAgeProperty(), 0), 2);
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
            return belowState.is(ModBlocks.BEANS.get()) && this.hasGoodCropConditions(level, pos);
        }
    }

    // copy from TomatoVineBlock to prevent Supplementaries from mixining us
    @Override
    public void attemptRopeClimb(ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.3F) {
            BlockPos posAbove = pos.above();
            BlockState stateAbove = level.getBlockState(posAbove);
            if (ModList.get().isLoaded("supplementaries") && Objects.equals(ForgeRegistries.BLOCKS.getKey(stateAbove.getBlock()), STICK)) {
                return;
            }
            boolean canClimb = ENABLE_BEAN_VINE_CLIMBING_TAGGED_ROPES.get() ? stateAbove.is(ModTags.ROPES) : stateAbove.is(vectorwing.farmersdelight.common.registry.ModBlocks.ROPE.get());
            if (canClimb) {
                int vineHeight;
                for (vineHeight = 1; level.getBlockState(pos.below(vineHeight)).is(this); ++vineHeight) {
                }
                if (vineHeight < 3) {
                    level.setBlockAndUpdate(posAbove, defaultBlockState().setValue(ROPELOGGED, true));
                }
            }
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.BEANS.get();
    }
}