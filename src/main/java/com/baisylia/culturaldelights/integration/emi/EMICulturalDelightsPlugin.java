package com.baisylia.culturaldelights.integration.emi;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.recipes.FermenterRecipe;
import com.baisylia.culturaldelights.screens.ModMenuTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

@EmiEntrypoint
public class EMICulturalDelightsPlugin implements EmiPlugin {

    static final ResourceLocation TEXTURE = new ResourceLocation(CulturalDelights.MOD_ID, "textures/gui/fermenter_gui_jei.png");

    public static final EmiRecipeCategory SHAPELESS_FERMENTING = new EmiRecipeCategory(new ResourceLocation(CulturalDelights.MOD_ID, "shapeless_fermenting"), EmiStack.of(ModBlocks.FERMENTER.get()), simplifiedRenderer(0, 0));

    private static EmiRenderable simplifiedRenderer(int u, int v) {
        return (draw, x, y, delta) -> {
            RenderSystem.setShaderTexture(0, TEXTURE);
            GuiComponent.blit(draw, x, y, u, v, 124, 58, 124, 58);
        };
    }

    @Override
    public void register(EmiRegistry registry) {
        var forge = EmiStack.of(ModBlocks.FERMENTER.get());
        registry.addCategory(SHAPELESS_FERMENTING);
        registry.addWorkstation(SHAPELESS_FERMENTING, forge);
        for (FermenterRecipe recipe : registry.getRecipeManager().getAllRecipesFor(FermenterRecipe.Type.INSTANCE)) {
            registry.addRecipe(new FermenterEmiRecipe(recipe));
        }
        registry.addRecipeHandler(ModMenuTypes.FERMENTER_MENU.get(), new FermenterRecipeHandler());
    }
}
