package com.baisylia.culturaldelights.integration.jei;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.recipes.FermenterShapedRecipe;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FermenterShapedRecipeCategory implements IRecipeCategory<FermenterShapedRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(CulturalDelights.MOD_ID, "fermenting_shaped");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(CulturalDelights.MOD_ID, "textures/gui/fermenter_gui_jei.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final int regularCookTime = 400;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;


    public FermenterShapedRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 124, 58);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.FERMENTER.get()));
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return helper.drawableBuilder(TEXTURE, 126, 0, 23, 18)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    @Override
    public void draw(FermenterShapedRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 63, 19);
        drawCookTime(recipe, poseStack, 45);
    }

    protected void drawCookTime(FermenterShapedRecipe recipe, PoseStack poseStack, int y) {
        int cookTime = recipe.getCookTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(poseStack, timeString, getWidth() - stringWidth, y, 0xFF808080);
        }
    }

    protected IDrawableAnimated getArrow(FermenterShapedRecipe recipe) {
        int cookTime = recipe.getCookTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    @Override
    public RecipeType<FermenterShapedRecipe> getRecipeType() {
        return JEICulturalDelightsPlugin.FERMENTING_SHAPED_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.CulturalDelights.shapeless_fermenting");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FermenterShapedRecipe recipe, IFocusGroup focuses) {
        int start = 3;
        int index = 0;

        for (int y = 0; y < recipe.getHeight(); y++) {
            for (int x = 0; x < recipe.getWidth(); x++) {
                builder.addSlot(RecipeIngredientRole.INPUT, start + x * 18, start + y * 18)
                        .addIngredients(recipe.getIngredients().get(index));
                index++;
            }
        }

        // Add output slot
        builder.addSlot(RecipeIngredientRole.OUTPUT, 97, 21).addItemStack(recipe.getResultItem());
    }
}