package com.baisylia.culturaldelights.screens;

import com.baisylia.culturaldelights.CulturalDelights;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class OvenScreen extends AbstractContainerScreen<OvenMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "textures/gui/oven_gui.png");

    public OvenScreen(OvenMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 90, y + 35, 176, 14, menu.getScaledProgress(), 17);
        }

        if (menu.isFueled()) {
            guiGraphics.blit(TEXTURE, x + 93, y + 55, 176, 32, 17, 15);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (this.isHovering(93, 55, 17, 15, mouseX, mouseY)) {
            String key = "container.culturaldelights.oven." + (this.menu.isFueled() ? "heated" : "not_heated");
            guiGraphics.renderTooltip(this.font, Component.translatable(key), mouseX, mouseY);
        }
    }

    protected void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, (width - imageWidth) / 2, (height - imageHeight) / 2, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, (width - imageWidth) / 2 + 90, (height - imageHeight) / 2 + 35, 176, 14, menu.getScaledProgress(), 17);
        }

        if (menu.isFueled()) {
            guiGraphics.blit(TEXTURE, (width - imageWidth) / 2 + 93, (height - imageHeight) / 2 + 55, 176, 32, 17, 15);
        }
    }
}
