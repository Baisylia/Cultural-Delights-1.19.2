package com.baisylia.culturaldelights.integration.jei;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.recipes.VatRecipe;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.systems.RenderSystem;
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

import static net.minecraft.client.gui.GuiComponent.blit;

public class VatRecipeCategory implements IRecipeCategory<VatRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(CulturalDelights.MOD_ID, "aging");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(CulturalDelights.MOD_ID, "textures/gui/vat_gui_jei.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final int regularCookTime = 400;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public VatRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 124, 58);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.VAT.get()));
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
    public void draw(VatRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 63, 20);
        drawCookTime(recipe, poseStack, 45);

        int xOffset = switch (recipe.getTemperature()) {
            case COLD -> 0;
            case NORMAL -> 13;
            case HOT -> 26;
        };
        Minecraft.getInstance().getTextureManager().bindForSetup(TEXTURE);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(poseStack, 31, 20,176 + xOffset, 32, 12, 46, 256, 256
        );
        if (mouseX >= 31 && mouseX <= 43 && mouseY >= 20 && mouseY <= 66) {
            Component text = Component.translatable(
                    "container.culturaldelights.vat." + recipe.getTemperature().getSerializedName()
            );

            Minecraft.getInstance().screen.renderComponentTooltip(poseStack, java.util.List.of(text), (int) mouseX, (int) mouseY
            );
        }
    }

    protected void drawCookTime(VatRecipe recipe, PoseStack poseStack, int y) {
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

    protected IDrawableAnimated getArrow(VatRecipe recipe) {
        int cookTime = recipe.getCookTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    @Override
    public RecipeType<VatRecipe> getRecipeType() {
        return JEICulturalDelightsPlugin.AGING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.culturaldelights.aging");
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
    public void setRecipe(IRecipeLayoutBuilder builder, VatRecipe recipe, IFocusGroup focuses) {
        int[][] positions = {
                {19, 2}, {37, 2},
                {19, 20}, {37, 20},
                {19, 38}, {37, 38}
        };
        for (int i = 0; i < recipe.getIngredients().size() && i < positions.length; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, positions[i][0], positions[i][1]).addIngredients(recipe.getIngredients().get(i));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 9).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.INPUT, 97, 39).addIngredients(recipe.getContainer());
    }
}