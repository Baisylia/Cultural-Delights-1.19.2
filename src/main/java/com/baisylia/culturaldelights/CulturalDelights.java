package com.baisylia.culturaldelights;

import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.block.entity.ModBlockEntities;
import com.baisylia.culturaldelights.block.entity.custom.VatBlockEntity;
import com.baisylia.culturaldelights.effect.ModEffects;
import com.baisylia.culturaldelights.item.ModItems;
import com.baisylia.culturaldelights.recipes.ModRecipes;
import com.baisylia.culturaldelights.screens.ModMenuTypes;
import com.baisylia.culturaldelights.screens.VatScreen;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(CulturalDelights.MOD_ID)
public class CulturalDelights {
    public static final String MOD_ID = "culturaldelights";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CulturalDelights(IEventBus eventBus, ModContainer modContainer) {
        ModEffects.register(eventBus);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModMenuTypes.register(eventBus);
        ModRecipes.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::registerCapabilities);
        eventBus.addListener(this::buildCreativeTab);
        eventBus.addListener(this::addPackFinders);
    }

    public static void registerPottables() {
        FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
        pot.addPlant(ModBlocks.AVOCADO_SAPLING.getId(), ModBlocks.POTTED_AVOCADO_SAPLING);
        pot.addPlant(ModBlocks.AGAVE.getId(), ModBlocks.POTTED_AGAVE);
        pot.addPlant(ModBlocks.MINT.getId(), ModBlocks.POTTED_MINT);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CulturalDelights::registerPottables);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.VAT_BLOCK_ENTITY.get(),
                VatBlockEntity::getItemHandler
        );
    }

    private void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().location().equals(ResourceLocation.fromNamespaceAndPath("farmersdelight", "farmersdelight"))) {
            ModItems.ITEMS.getEntries().forEach(item -> event.accept(item.get()));
        }
    }

    public void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            event.addPackFinders(ResourceLocation.fromNamespaceAndPath(MOD_ID, "resourcepacks/pastry_sheet"),
                    PackType.CLIENT_RESOURCES, Component.literal("Pastry Sheet"), PackSource.BUILT_IN, false, Pack.Position.TOP);
            event.addPackFinders(ResourceLocation.fromNamespaceAndPath(MOD_ID, "resourcepacks/apple_juice"),
                    PackType.CLIENT_RESOURCES, Component.literal("Apple Juice"), PackSource.BUILT_IN, false, Pack.Position.TOP);
            if (ModList.get().isLoaded("supplementaries")) {
                event.addPackFinders(ResourceLocation.fromNamespaceAndPath(MOD_ID, "resourcepacks/supplementaries"),
                        PackType.CLIENT_RESOURCES, Component.literal("Supplementaries Beans"), PackSource.BUILT_IN, true, Pack.Position.TOP);
            }
        }
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.VAT_MENU.get(), VatScreen::new);
        }
    }
}