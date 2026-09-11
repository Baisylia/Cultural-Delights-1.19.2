package com.baisylia.culturaldelights.integration.supplementaries;

import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.block.custom.BeansBlock;
import com.baisylia.culturaldelights.block.custom.RopeBeansBlock;
import com.baisylia.culturaldelights.block.custom.StickBeansBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class SupplementariesCompat {

    public static Block makeRopeBeans(Block.Properties properties) {
        return new RopeBeansBlock(properties);
    }

    public static Block makeStickBeans(Block.Properties properties) {
        return new StickBeansBlock(properties);
    }

    public static BlockState getRopeOrStickBeansToPlace(BlockState stateAbove, BlockState defaultBeansState) {
        if (stateAbove.is(ModRegistry.ROPE.get())) {
            return ModBlocks.ROPE_BEANS.get().withPropertiesOf(stateAbove);
        } else if (stateAbove.is(ModRegistry.STICK_BLOCK.get())) {
            return ModBlocks.STICK_BEANS.get().withPropertiesOf(stateAbove);
        }
        return defaultBeansState.setValue(BeansBlock.ROPELOGGED, true);
    }

    public static InteractionResult tryUseStick(BlockState state, Level level, BlockPos pos, Player player,
                                                InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.STICK)) {
            return InteractionResult.PASS;
        }

        BlockState newState;
        if (state.is(ModBlocks.STICK_BEANS.get())) {
            BooleanProperty axis = switch (hit.getDirection().getAxis()) {
                case X -> StickBeansBlock.AXIS_X;
                case Z -> StickBeansBlock.AXIS_Z;
                case Y -> null;
            };
            if (axis == null || state.getValue(axis)) return InteractionResult.PASS;
            newState = state.setValue(axis, true);
        } else if (state.is(ModBlocks.BEANS.get())) {
            newState = StickBeansBlock.fromBeans(state);
        } else {
            return InteractionResult.PASS;
        }

        if (!level.setBlock(pos, newState, 3)) return InteractionResult.FAIL;

        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
        }
        level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
        SoundType sound = SoundType.WOOD;
        level.playSound(player, pos, sound.getPlaceSound(), SoundSource.BLOCKS,
                (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
