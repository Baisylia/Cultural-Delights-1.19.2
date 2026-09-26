package com.baisylia.culturaldelights.event;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.advancement.ModAdvancements;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.block.custom.SaltBlock;
import com.baisylia.culturaldelights.item.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import com.baisylia.culturaldelights.block.custom.BeanstalkLeafBlock;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Tilt;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@EventBusSubscriber(modid = CulturalDelights.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            ItemStack stack = new ItemStack(Items.EMERALD, 1);
            int villagerLevel = 1;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemCost(ModItems.CUCUMBER.get(), 22),
                    stack, 10, 2, 0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemCost(ModItems.EGGPLANT.get(), 15),
                    stack, 10, 2, 0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemCost(ModItems.WHITE_EGGPLANT.get(), 20),
                    stack, 10, 2, 0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemCost(ModItems.CORN_COB.get(), 15),
                    stack, 10, 2, 0.02F));

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemCost(ModItems.AVOCADO.get(), 20),
                    stack, 10, 2, 0.02F));
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            BlockPos pos = event.getPos();
            BlockState placed = event.getPlacedBlock();
            LevelAccessor level = event.getLevel();

            if (placed.is(Blocks.POINTED_DRIPSTONE) && placed.getValue(PointedDripstoneBlock.TIP_DIRECTION) == Direction.DOWN) {
                BlockState above = level.getBlockState(pos.above());
                if (above.is(ModBlocks.SALT_BLOCK.get()) && SaltBlock.isWaterAbove(level, pos.above())) {
                    ModAdvancements.GROW_SALT_SPIKE.get().trigger(player);
                }
            } else if (placed.is(ModBlocks.SALT_BLOCK.get())) {
                BlockState below = level.getBlockState(pos.below());
                if (below.is(Blocks.POINTED_DRIPSTONE) && below.getValue(PointedDripstoneBlock.TIP_DIRECTION) == Direction.DOWN) {
                    if (SaltBlock.isWaterAbove(level, pos)) {
                        ModAdvancements.GROW_SALT_SPIKE.get().trigger(player);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        BlockPos pos = entity.getOnPos();
        BlockState state = level.getBlockState(pos);

        if (!state.is(ModBlocks.BEANSTALK_LEAF.get())) {
            pos = entity.blockPosition();
            state = level.getBlockState(pos);
            if (!state.is(ModBlocks.BEANSTALK_LEAF.get())) {
                pos = entity.blockPosition().below();
                state = level.getBlockState(pos);
            }
        }

        if (state.is(ModBlocks.BEANSTALK_LEAF.get())) {
            Tilt tilt = state.getValue(BeanstalkLeafBlock.TILT);
            double boost;
            if (tilt == Tilt.FULL) {
                boost = 0.60;
            } else if (tilt == Tilt.PARTIAL) {
                boost = 0.38;
            } else {
                boost = 0.18;
            }

            Vec3 motion = entity.getDeltaMovement();
            entity.setDeltaMovement(motion.x, motion.y + boost, motion.z);
            entity.hasImpulse = true;

            if (!level.isClientSide && tilt != Tilt.NONE) {
                BeanstalkLeafBlock.resetTilt(state, level, pos);
                level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 1.4F);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5, 6, 0.25, 0.1, 0.25, 0.02);
                }
            }
        }
    }
}
