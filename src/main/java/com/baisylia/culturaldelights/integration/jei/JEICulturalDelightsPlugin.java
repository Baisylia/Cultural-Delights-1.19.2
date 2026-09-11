package com.baisylia.culturaldelights.integration.jei;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.recipes.ModRecipes;
import com.baisylia.culturaldelights.recipes.VatRecipe;
import com.baisylia.culturaldelights.screens.ModMenuTypes;
import com.baisylia.culturaldelights.screens.VatMenu;
import com.baisylia.culturaldelights.screens.VatScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEICulturalDelightsPlugin implements IModPlugin {
    private static RecipeType<RecipeHolder<VatRecipe>> agingType;

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
                new VatRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<RecipeHolder<VatRecipe>> recipes = rm.getAllRecipesFor(ModRecipes.AGING_TYPE.get());
        registration.addRecipes(getAgingType(), recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        var stack = ModBlocks.VAT.get().asItem().getDefaultInstance();
        registration.addRecipeCatalyst(stack, getAgingType());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(VatScreen.class, 88, 24, 26, 17, getAgingType());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(VatMenu.class, ModMenuTypes.VAT_MENU.get(), getAgingType(), 36, 6, 0, 36);
    }
}
