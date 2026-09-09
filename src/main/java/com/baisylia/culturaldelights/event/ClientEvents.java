package com.baisylia.culturaldelights.event;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CulturalDelights.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    private static float currentPitch = 0.0F;
    private static float currentYaw = 0.0F;
    private static float currentRoll = 0.0F;
    private static Entity lastCameraEntity = null;

    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Entity cameraEntity = event.getCamera().getEntity();
        if (cameraEntity != lastCameraEntity) {
            currentPitch = 0.0F;
            currentYaw = 0.0F;
            currentRoll = 0.0F;
            lastCameraEntity = cameraEntity;
        }

        float targetPitch = 0.0F;
        float targetYaw = 0.0F;
        float targetRoll = 0.0F;
        boolean hasEffect = false;

        if (cameraEntity instanceof LivingEntity living && living.isAlive() && living.hasEffect(ModEffects.INTOXICATION.get())) {
            MobEffectInstance effect = living.getEffect(ModEffects.INTOXICATION.get());
            if (effect != null) {
                hasEffect = true;
                int amplifier = effect.getAmplifier();
                float partialTick = (float) event.getPartialTick();
                float time = (living.tickCount + partialTick) * 0.05F;
                float durationFade = Math.min(1.0F, effect.getDuration() / 40.0F);

                // Sway
                float swayAmp = 0.5F * (amplifier + 1) * durationFade;
                targetPitch = (float) Math.sin(time) * swayAmp * 1.5F;
                targetYaw = (float) Math.cos(time * 0.9F) * swayAmp * 2.0F;
                targetRoll = (float) Math.sin(time * 0.75F) * swayAmp * 2.5F;

                // Wobble
                if (amplifier >= 3) {
                    float wobbleStrength = 0.3F * (amplifier - 2) * durationFade;
                    targetPitch += (float) Math.sin(time * 1.3F) * wobbleStrength * 2.0F;
                    targetYaw += (float) Math.cos(time * 1.1F) * wobbleStrength * 2.0F;
                    targetRoll += (float) Math.sin(time * 1.5F) * wobbleStrength * 2.5F;
                }
            }
        }

        if (!hasEffect && Math.abs(currentPitch) < 0.001F && Math.abs(currentYaw) < 0.001F && Math.abs(currentRoll) < 0.001F) {
            currentPitch = 0.0F;
            currentYaw = 0.0F;
            currentRoll = 0.0F;
            return;
        }

        // Interpolate towards target
        float deltaTicks = Minecraft.getInstance().getDeltaFrameTime();
        float factor = Math.min(1.0F, deltaTicks * 0.15F);
        currentPitch = Mth.lerp(factor, currentPitch, targetPitch);
        currentYaw = Mth.lerp(factor, currentYaw, targetYaw);
        currentRoll = Mth.lerp(factor, currentRoll, targetRoll);

        event.setPitch(event.getPitch() + currentPitch);
        event.setYaw(event.getYaw() + currentYaw);
        event.setRoll(event.getRoll() + currentRoll);
    }
}
