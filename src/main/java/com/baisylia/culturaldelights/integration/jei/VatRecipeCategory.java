package com.baisylia.culturaldelights.integration.jei;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.recipes.VatRecipe;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class VatRecipeCategory implements IRecipeCategory<RecipeHolder<VatRecipe>> {
    public static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "textures/gui/vat_gui_jei.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable coldThermometer;
    private final IDrawable normalThermometer;
    private final IDrawable hotThermometer;
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
    public void draw(RecipeHolder<VatRecipe> holder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
        IDrawableAnimated arrow = getArrow(holder.value());
        arrow.draw(guiGraphics, 63, 10);

        IDrawable thermometer = switch (holder.value().getTemperature()) {
            case COLD -> coldThermometer;
            case NORMAL -> normalThermometer;
            case HOT -> hotThermometer;
        };
        thermometer.draw(guiGraphics, 5, 6);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<VatRecipe> holder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 5 && mouseX <= 17 && mouseY >= 6 && mouseY <= 52) {
            tooltip.add(Component.translatable("container.culturaldelights.vat." + holder.value().getTemperature().getSerializedName()));
        }
        if (mouseX >= 63 && mouseX <= 86 && mouseY >= 10 && mouseY <= 28) {
            int cookTime = holder.value().getCookTime();
            if (cookTime > 0) {
                int cookTimeSeconds = cookTime / 20;
                tooltip.add(Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds));
            }
        }
    }

    protected IDrawableAnimated getArrow(VatRecipe recipe) {
        int cookTime = recipe.getCookTime();
        if (cookTime <= 0) {
            int regularCookTime = 400;
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
    }

    @Override
    public RecipeType<RecipeHolder<VatRecipe>> getRecipeType() {
        return JEICulturalDelightsPlugin.getAgingType();
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.culturaldelights.aging");
    }

    @Override
    public int getWidth() {
        return 124;
    }

    @Override
    public int getHeight() {
        return 58;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<VatRecipe> holder, IFocusGroup focuses) {
        VatRecipe recipe = holder.value();
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