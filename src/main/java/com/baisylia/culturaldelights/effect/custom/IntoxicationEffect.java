package com.baisylia.culturaldelights.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.EffectCure;

import java.util.Set;

public class IntoxicationEffect extends MobEffect {

    public IntoxicationEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
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
        if (!entity.level().isClientSide && amplifier >= 4) {
            entity.addEffect(new MobEffectInstance(
                    MobEffects.CONFUSION,
                    100 * amplifier,
                    amplifier - 3,
                    false,
                    true
            ));
        }
        // Poison
        if (!entity.level().isClientSide && amplifier >= 6) {
            entity.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    50 * amplifier,
                    amplifier - 5,
                    false,
                    true
            ));
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
        // Intoxication can't be cured by milk
    }
}