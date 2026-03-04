package com.baisylia.culturaldelights.recipes;

import com.baisylia.culturaldelights.CulturalDelights;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CulturalDelights.MOD_ID);


    public static final RegistryObject<RecipeSerializer<FermenterRecipe>> FERMENTING_SERIALIZER =
            SERIALIZERS.register("fermenting", () -> FermenterRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<FermenterShapedRecipe>> FERMENTING_SHAPED_SERIALIZER =
            SERIALIZERS.register("fermenting_shaped", () -> FermenterShapedRecipe.Serializer.INSTANCE);


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
