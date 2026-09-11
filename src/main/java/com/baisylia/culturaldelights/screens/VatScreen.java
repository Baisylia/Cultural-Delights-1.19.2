package com.baisylia.culturaldelights.screens;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.util.VatTemperature;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class VatScreen extends AbstractContainerScreen<VatMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, "textures/gui/vat_gui.png");

    public VatScreen(VatMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 88, y + 24, 176, 14, menu.getScaledProgress(), 17);
        }
        if (menu.getTemperature() == VatTemperature.COLD) {
            guiGraphics.blit(TEXTURE, x + 31, y + 20, 176, 32, 12, 46);
        } else if (menu.getTemperature() == VatTemperature.NORMAL) {
            guiGraphics.blit(TEXTURE, x + 31, y + 20, 189, 32, 12, 46);
        } else if (menu.getTemperature() == VatTemperature.HOT) {
            guiGraphics.blit(TEXTURE, x + 31, y + 20, 202, 32, 12, 46);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        if (this.isHovering(31, 20, 12, 46, mouseX, mouseY)) {
            List<Component> tooltip = new ArrayList<>();

            VatTemperature temp = this.menu.getTemperature();
            String key = "container.culturaldelights.vat." + temp.getSerializedName();

            tooltip.add(Component.translatable(key));
            guiGraphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
        }
    }
}