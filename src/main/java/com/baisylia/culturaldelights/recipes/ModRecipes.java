package com.baisylia.culturaldelights.recipes;

import com.baisylia.culturaldelights.CulturalDelights;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, CulturalDelights.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CulturalDelights.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<VatRecipe>> AGING_TYPE =
            RECIPE_TYPES.register("aging", () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "aging")));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<VatRecipe>> AGING_SERIALIZER =
            SERIALIZERS.register("aging", VatRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        SERIALIZERS.register(eventBus);
    }
}
