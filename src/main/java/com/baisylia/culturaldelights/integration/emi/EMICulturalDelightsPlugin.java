package com.baisylia.culturaldelights.integration.emi;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.item.ModItems;
import com.baisylia.culturaldelights.recipes.ModRecipes;
import com.baisylia.culturaldelights.recipes.VatRecipe;
import com.baisylia.culturaldelights.screens.ModMenuTypes;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

@EmiEntrypoint
public class EMICulturalDelightsPlugin implements EmiPlugin {

    static final ResourceLocation VAT_TEXTURE = ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "textures/gui/vat_gui_jei.png");
    static final ResourceLocation OVEN_TEXTURE = ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "textures/gui/oven_gui_jei.png");

    public static final EmiRecipeCategory AGING = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "aging"),
            EmiStack.of(ModBlocks.VAT.get()),
            simplifiedRenderer(VAT_TEXTURE, 0, 0, 16, 16, 124, 58)
    );

    public static final EmiRecipeCategory SHAPELESS_BAKING = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "shapeless_baking"),
            EmiStack.of(ModBlocks.OVEN.get()),
            simplifiedRenderer(OVEN_TEXTURE, 0, 0, 124, 58, 124, 58)
    );

    public static final EmiRecipeCategory SHAPED_BAKING = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "shaped_baking"),
            EmiStack.of(ModBlocks.OVEN.get()),
            simplifiedRenderer(OVEN_TEXTURE, 0, 0, 124, 58, 124, 58)
    );

    private static EmiRenderable simplifiedRenderer(ResourceLocation texture, int u, int v, int width, int height, int texWidth, int texHeight) {
        return (draw, x, y, delta) -> draw.blit(texture, x, y, u, v, width, height, texWidth, texHeight);
    }

    @Override
    public void register(EmiRegistry registry) {
        var vat = EmiStack.of(ModBlocks.VAT.get());
        registry.addCategory(AGING);
        registry.addWorkstation(AGING, vat);
        for (RecipeHolder<VatRecipe> recipeHolder : registry.getRecipeManager().getAllRecipesFor(ModRecipes.AGING_TYPE.get())) {
            registry.addRecipe(new VatEmiRecipe(recipeHolder));
        }
        registry.addRecipeHandler(ModMenuTypes.VAT_MENU.get(), new VatRecipeHandler());

        var oven = EmiStack.of(ModBlocks.OVEN.get());
        registry.addCategory(SHAPELESS_BAKING);
        registry.addWorkstation(SHAPELESS_BAKING, oven);
        registry.addCategory(SHAPED_BAKING);
        registry.addWorkstation(SHAPED_BAKING, oven);

        var level = Minecraft.getInstance().level;
        if (level != null) {
            var access = level.registryAccess();
            for (var recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipes.BAKING.get())) {
                registry.addRecipe(new OvenEmiRecipe(recipe.value(), recipe.id(), access));
            }
            for (var recipe : registry.getRecipeManager().getAllRecipesFor(ModRecipes.BAKING_SHAPED.get())) {
                registry.addRecipe(new OvenShapedEmiRecipe(recipe.value(), recipe.id(), access));
            }
        }
        registry.addRecipeHandler(ModMenuTypes.OVEN_MENU.get(), new OvenRecipeHandler());

        registry.addRecipe(new EmiMarshmallowCookingRecipe(100, ModItems.MARSHMALLOW_ON_A_STICK.get(), ModItems.CARAMELIZED_MARSHMALLOW_ON_A_STICK.get()));
        registry.addRecipe(new EmiMarshmallowCookingRecipe(100, ModItems.CARAMELIZED_MARSHMALLOW_ON_A_STICK.get(), ModItems.CHARRED_MARSHMALLOW_ON_A_STICK.get()));
    }
}
