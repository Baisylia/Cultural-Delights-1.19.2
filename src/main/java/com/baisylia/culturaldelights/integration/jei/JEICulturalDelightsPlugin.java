package com.baisylia.culturaldelights.integration.jei;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.item.ModItems;
import com.baisylia.culturaldelights.recipes.ModRecipes;
import com.baisylia.culturaldelights.recipes.OvenRecipe;
import com.baisylia.culturaldelights.recipes.OvenShapedRecipe;
import com.baisylia.culturaldelights.recipes.VatRecipe;
import com.baisylia.culturaldelights.screens.ModMenuTypes;
import com.baisylia.culturaldelights.screens.OvenMenu;
import com.baisylia.culturaldelights.screens.OvenScreen;
import com.baisylia.culturaldelights.screens.VatMenu;
import com.baisylia.culturaldelights.screens.VatScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEICulturalDelightsPlugin implements IModPlugin {
    private static RecipeType<RecipeHolder<VatRecipe>> agingType;

    public static final RecipeType<OvenRecipe> BAKING_TYPE =
            new RecipeType<>(OvenRecipeCategory.UID, OvenRecipe.class);

    public static final RecipeType<OvenShapedRecipe> BAKING_SHAPED_TYPE =
            new RecipeType<>(OvenShapedRecipeCategory.UID, OvenShapedRecipe.class);

    public static RecipeType<RecipeHolder<VatRecipe>> getAgingType() {
        if (agingType == null) {
            agingType = RecipeType.createFromVanilla(ModRecipes.AGING_TYPE.get());
        }
        return agingType;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new VatRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new OvenRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new OvenShapedRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<RecipeHolder<VatRecipe>> recipes = rm.getAllRecipesFor(ModRecipes.AGING_TYPE.get());
        registration.addRecipes(getAgingType(), recipes);
        registration.addItemStackInfo(List.of(
                ModItems.MARSHMALLOW_ON_A_STICK.get().getDefaultInstance(),
                ModItems.CARAMELIZED_MARSHMALLOW_ON_A_STICK.get().getDefaultInstance()
        ), Component.translatable("emi.culturaldelights.marshmallow_on_a_stick.help"));

        List<OvenRecipe> ovenRecipes = rm.getAllRecipesFor(ModRecipes.BAKING.get()).stream().map(RecipeHolder::value).toList();
        List<OvenShapedRecipe> ovenShapedRecipes = rm.getAllRecipesFor(ModRecipes.BAKING_SHAPED.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(BAKING_TYPE, ovenRecipes);
        registration.addRecipes(BAKING_SHAPED_TYPE, ovenShapedRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        var vatStack = ModBlocks.VAT.get().asItem().getDefaultInstance();
        registration.addRecipeCatalyst(vatStack, getAgingType());

        var ovenStack = ModBlocks.OVEN.get().asItem().getDefaultInstance();
        registration.addRecipeCatalyst(ovenStack, BAKING_TYPE);
        registration.addRecipeCatalyst(ovenStack, BAKING_SHAPED_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(VatScreen.class, 88, 24, 26, 17, getAgingType());
        registration.addRecipeClickArea(OvenScreen.class, 89, 35, 24, 17, BAKING_TYPE, BAKING_SHAPED_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(VatMenu.class, ModMenuTypes.VAT_MENU.get(), getAgingType(), 36, 6, 0, 36);
        registration.addRecipeTransferHandler(OvenMenu.class, ModMenuTypes.OVEN_MENU.get(), BAKING_TYPE, 36, 9, 0, 36);
        registration.addRecipeTransferHandler(OvenMenu.class, ModMenuTypes.OVEN_MENU.get(), BAKING_SHAPED_TYPE, 36, 9, 0, 36);
    }
}
