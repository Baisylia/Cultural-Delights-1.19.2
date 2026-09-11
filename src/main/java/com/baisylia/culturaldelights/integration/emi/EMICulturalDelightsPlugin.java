package com.baisylia.culturaldelights.integration.emi;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.recipes.ModRecipes;
import com.baisylia.culturaldelights.recipes.VatRecipe;
import com.baisylia.culturaldelights.screens.ModMenuTypes;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

@EmiEntrypoint
public class EMICulturalDelightsPlugin implements EmiPlugin {

    static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "textures/gui/vat_gui_jei.png");

    public static final EmiRecipeCategory AGING = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "aging"),
            EmiStack.of(ModBlocks.VAT.get()),
            simplifiedRenderer(0, 0)
    );

    private static EmiRenderable simplifiedRenderer(int u, int v) {
        return (draw, x, y, delta) -> draw.blit(TEXTURE, x, y, u, v, 16, 16, 124, 58);
    }

    @Override
    public void register(EmiRegistry registry) {
        var forge = EmiStack.of(ModBlocks.VAT.get());
        registry.addCategory(AGING);
        registry.addWorkstation(AGING, forge);
        for (RecipeHolder<VatRecipe> recipeHolder : registry.getRecipeManager().getAllRecipesFor(ModRecipes.AGING_TYPE.get())) {
            registry.addRecipe(new VatEmiRecipe(recipeHolder));
        }
        registry.addRecipeHandler(ModMenuTypes.VAT_MENU.get(), new VatRecipeHandler());
    }
}
