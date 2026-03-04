package com.baisylia.culturaldelights.screens;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.util.FermenterTemperature;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class FermenterScreen extends AbstractContainerScreen<FermenterMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(CulturalDelights.MOD_ID, "textures/gui/fermenter_gui.png");

    public FermenterScreen(FermenterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        if(menu.isCrafting()) {
            blit(pPoseStack, x + 81, y + 24, 176, 14,  menu.getScaledProgress(), 17);
        }
        if(menu.getTemperature() == FermenterTemperature.HOT) {
            blit(pPoseStack, x + 84, y + 55, 176, 32, 17, 15);
        }

    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
        if (this.isHovering(93, 55, 17, 15, mouseX, mouseY)) {
            List<Component> tooltip = new ArrayList<>();

            FermenterTemperature temp = this.menu.getTemperature();
            String key = "container.culturaldelights.fermenter." + temp.getSerializedName();

            tooltip.add(Component.translatable(key));
            this.renderComponentTooltip(pPoseStack, tooltip, mouseX, mouseY);
        }

    }


}