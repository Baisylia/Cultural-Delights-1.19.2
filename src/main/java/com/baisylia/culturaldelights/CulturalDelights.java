package com.baisylia.culturaldelights;

import com.baisylia.culturaldelights.block.entity.ModBlockEntities;
import com.baisylia.culturaldelights.effect.ModEffects;
import com.baisylia.culturaldelights.recipes.ModRecipes;
import com.baisylia.culturaldelights.screens.VatScreen;
import com.baisylia.culturaldelights.screens.ModMenuTypes;
import com.mojang.logging.LogUtils;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.item.ModItems;
import com.baisylia.culturaldelights.world.feature.ModConfiguredFeatures;
import com.baisylia.culturaldelights.world.feature.ModPlacedFeatures;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;

import java.io.IOException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CulturalDelights.MOD_ID)
public class CulturalDelights
{
    public static final String MOD_ID = "culturaldelights";

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public CulturalDelights()
    {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEffects.register(eventBus);
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModConfiguredFeatures.register(eventBus);
        ModPlacedFeatures.register(eventBus);
        ModMenuTypes.register(eventBus);
        ModRecipes.register(eventBus);


        eventBus.addListener(this::setup);
        eventBus.addListener(this::addPackFinders);
        MinecraftForge.EVENT_BUS.register(this);
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.AGAVE.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_CUCUMBERS.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_CORN.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_EGGPLANTS.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.AVOCADO_LEAVES.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.AVOCADO_SAPLING.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.AVOCADO_PIT.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CUCUMBERS.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EGGPLANTS.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CORN.get(), RenderType.cutoutMipped());

            MenuScreens.register(ModMenuTypes.VAT_MENU.get(), VatScreen::new);
        }
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            registerCompostables();
            registerPottables();
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.AGAVE.getId(), ModBlocks.POTTED_AGAVE);
            //registerAnimalFeeds();
        });
    }

    public static void registerPottables() {
        FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
        pot.addPlant(ModBlocks.AVOCADO_SAPLING.getId(), ModBlocks.POTTED_AVOCADO_SAPLING);
}

    public static void registerCompostables() {
        // 30% chance
        ComposterBlock.COMPOSTABLES.put(ModItems.CUCUMBER_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.CORN_KERNELS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModItems.EGGPLANT_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.AVOCADO_PIT.get(), 0.3F);

        // 50% chance
        ComposterBlock.COMPOSTABLES.put(ModItems.CUT_CUCUMBER.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.CUT_AVOCADO.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.CUT_EGGPLANT.get(), 0.65F);

        // 65% chance
        ComposterBlock.COMPOSTABLES.put(ModItems.AVOCADO.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.CUCUMBER.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.CORN_COB.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.EGGPLANT.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModItems.WHITE_EGGPLANT.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.WILD_CUCUMBERS.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.WILD_CORN.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.WILD_EGGPLANTS.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.AVOCADO_LEAVES.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.AVOCADO_SAPLING.get(), 0.65F);

        // 85% chance
        ComposterBlock.COMPOSTABLES.put(ModItems.POPCORN.get(), 0.85F);
    }

    public void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            registerBuiltinResourcePack(event, true, Component.literal("Pastry Sheet"), "pastry_sheet");
            registerBuiltinResourcePack(event, true, Component.literal("Apple Juice"), "apple_juice");
        }
    }

    private static void registerBuiltinResourcePack(AddPackFindersEvent event, boolean autoEnable, MutableComponent name, String folder) {
        event.addRepositorySource((consumer, constructor) -> {
            ResourceLocation res = new ResourceLocation(CulturalDelights.MOD_ID, folder);
            IModFile file = ModList.get().getModFileById(CulturalDelights.MOD_ID).getFile();
            try (PathPackResources pack = new PathPackResources(
                    res.toString(),
                    file.findResource("resourcepacks/" + folder))) {

                consumer.accept(constructor.create(
                        res.toString(),
                        name,
                        autoEnable,
                        () -> pack,
                        pack.getMetadataSection(PackMetadataSection.SERIALIZER),
                        Pack.Position.TOP,
                        PackSource.BUILT_IN,
                        false));

            } catch (IOException e) {
                if (!DatagenModLoader.isRunningDataGen())
                    e.printStackTrace();
            }
        });
    }
    //public static void registerAnimalFeeds() {
    //    Ingredient newChickenFood = Ingredient.of(ModItems.CUCUMBER_SEEDS.get(), ModItems.CORN_KERNELS.get(),
    //            ModItems.EGGPLANT_SEEDS.get(), ModItems.WHITE_EGGPLANT_SEEDS.get());
    //    Chicken.FOOD_ITEMS = new CompoundIngredient(Arrays.asList(Chicken.FOOD_ITEMS, newChickenFood))
    //    {
    //    };

    //    Ingredient newPigFood = Ingredient.of(ModItems.CUCUMBER.get(), ModItems.EGGPLANT.get(),
    //            ModItems.WHITE_EGGPLANT.get(), ModItems.CORN_COB.get());
    //    Pig.FOOD_ITEMS = new CompoundIngredient(Arrays.asList(Pig.FOOD_ITEMS, newPigFood))
    //    {
    //    };

    //    Collections.addAll(Parrot.TAME_FOOD, ModItems.CUCUMBER_SEEDS.get(), ModItems.CORN_KERNELS.get(),
    //            ModItems.EGGPLANT_SEEDS.get(), ModItems.WHITE_EGGPLANT_SEEDS.get());
    //}
}