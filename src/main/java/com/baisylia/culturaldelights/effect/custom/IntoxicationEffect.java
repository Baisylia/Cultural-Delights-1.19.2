package com.baisylia.culturaldelights.effect.custom;

import com.baisylia.culturaldelights.effect.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class IntoxicationEffect extends MobEffect {

    public IntoxicationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        var tag = entity.getPersistentData();

        // Slowness
        double speedFactor = 1.0 - 0.05 * amplifier;
        speedFactor = Math.max(speedFactor, 0.2);
        Vec3 movement = entity.getDeltaMovement();
        movement = movement.multiply(speedFactor, 1.0, speedFactor);

        // Stumbling
        if (amplifier >= 4) {
            double stumbleX = tag.getDouble("cd_intox_stumbleX");
            double stumbleZ = tag.getDouble("cd_intox_stumbleZ");

            if (entity.getRandom().nextDouble() < 0.1) {
                stumbleX = (entity.getRandom().nextDouble() - 0.5) * 0.15;
                stumbleZ = (entity.getRandom().nextDouble() - 0.5) * 0.15;
            }

            tag.putDouble("cd_intox_stumbleX", stumbleX);
            tag.putDouble("cd_intox_stumbleZ", stumbleZ);

            movement = movement.add(stumbleX, 0, stumbleZ);
        }

        entity.setDeltaMovement(movement);

        // Nausea
        if (!entity.level.isClientSide && amplifier >= 4) {
            entity.addEffect(new MobEffectInstance(
                    MobEffects.CONFUSION,
                    100 * amplifier,
                    amplifier - 3,
                    false,
                    true
            ));
        }
        // Poison
        if (!entity.level.isClientSide && amplifier >= 6) {
            entity.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    50 * amplifier,
                    amplifier - 5,
                    false,
                    true
            ));
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, net.minecraft.world.entity.ai.attributes.AttributeMap map, int amplifier) {
        var tag = entity.getPersistentData();
        tag.remove("cd_intox_wobbleTime");
        tag.remove("cd_intox_stumbleX");
        tag.remove("cd_intox_stumbleZ");

        if (entity.hasEffect(ModEffects.CHEESY.get())) {
            entity.removeEffect(MobEffects.CONFUSION);
            entity.removeEffect(MobEffects.POISON);
        }
        super.removeAttributeModifiers(entity, map, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public java.util.List<net.minecraft.world.item.ItemStack> getCurativeItems() {
        return java.util.Collections.emptyList();
    }
}