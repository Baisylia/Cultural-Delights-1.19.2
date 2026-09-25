package com.baisylia.culturaldelights.sound;

import com.baisylia.culturaldelights.CulturalDelights;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, CulturalDelights.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MARSHMALLOW_SIZZLE = register("item.marshmallow.sizzle");
    public static final DeferredHolder<SoundEvent, SoundEvent> MARSHMALLOW_CARAMELIZE = register("item.marshmallow.caramelize");
    public static final DeferredHolder<SoundEvent, SoundEvent> MARSHMALLOW_CHAR = register("item.marshmallow.char");
    public static final DeferredHolder<SoundEvent, SoundEvent> OVEN_CRACKLE = register("block.oven.crackle");
    public static final DeferredHolder<SoundEvent, SoundEvent> OVEN_OPEN = register("block.oven.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> OVEN_CLOSE = register("block.oven.close");
    public static final DeferredHolder<SoundEvent, SoundEvent> LEAVES_PICKED = register("block.fruiting_leaves.pick_fruit");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CulturalDelights.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
