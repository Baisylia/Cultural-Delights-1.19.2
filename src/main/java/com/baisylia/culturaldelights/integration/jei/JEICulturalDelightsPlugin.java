package com.baisylia.culturaldelights.integration.jei;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.recipes.FermenterRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEICulturalDelightsPlugin implements IModPlugin {
    public static RecipeType<FermenterRecipe> FERMENTING_TYPE =
            new RecipeType<>(FermenterRecipeCategory.UID, FermenterRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CulturalDelights.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new FermenterRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<FermenterRecipe> recipes = rm.getAllRecipesFor(FermenterRecipe.Type.INSTANCE);
        registration.addRecipes(FERMENTING_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        var stack = ModBlocks.FERMENTER.get().asItem().getDefaultInstance();
        registration.addRecipeCatalyst(stack, FERMENTING_TYPE);
    }
}
