package com.baisylia.culturaldelights.integration.jei;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.recipes.VatRecipe;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class VatRecipeCategory implements IRecipeCategory<VatRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(CulturalDelights.MOD_ID, "aging");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(CulturalDelights.MOD_ID, "textures/gui/vat_gui_jei.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable coldThermometer;
    private final IDrawable normalThermometer;
    private final IDrawable hotThermometer;
    private final int regularCookTime = 400;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public VatRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 124, 58);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.VAT.get()));
        this.coldThermometer = helper.createDrawable(TEXTURE, 126, 20, 12, 46);
        this.normalThermometer = helper.createDrawable(TEXTURE, 139, 20, 12, 46);
        this.hotThermometer = helper.createDrawable(TEXTURE, 152, 20, 12, 46);
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
        arrow.draw(poseStack, 63, 10);

        IDrawable thermometer = switch (recipe.getTemperature()) {
            case COLD -> coldThermometer;
            case NORMAL -> normalThermometer;
            case HOT -> hotThermometer;
        };
        thermometer.draw(poseStack, 5, 6);
    }

    @Override
    public List<Component> getTooltipStrings(VatRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 5 && mouseX <= 17 && mouseY >= 6 && mouseY <= 52) {
            return List.of(Component.translatable("container.culturaldelights.vat." + recipe.getTemperature().getSerializedName()));
        }
        if (mouseX >= 63 && mouseX <= 86 && mouseY >= 10 && mouseY <= 28) {
            int cookTime = recipe.getCookTime();
            if (cookTime > 0) {
                int cookTimeSeconds = cookTime / 20;
                return List.of(Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds));
            }
        }
        return List.of();
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
                {20, 3}, {38, 3},
                {20, 21}, {38, 21},
                {20, 39}, {38, 39}
        };
        for (int i = 0; i < recipe.getIngredients().size() && i < positions.length; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, positions[i][0], positions[i][1])
                    .addIngredients(recipe.getIngredients().get(i));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 10)
                .addItemStack(recipe.getResultItem());

        if (!recipe.getContainer().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 96, 39)
                    .addIngredients(recipe.getContainer());
        }
    }
}