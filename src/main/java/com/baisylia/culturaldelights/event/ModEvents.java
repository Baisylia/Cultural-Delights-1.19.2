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
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

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
}
