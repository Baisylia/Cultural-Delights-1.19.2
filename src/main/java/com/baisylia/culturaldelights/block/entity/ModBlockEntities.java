package com.baisylia.culturaldelights.block.entity;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.block.entity.custom.VatBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CulturalDelights.MOD_ID);

    public static final Supplier<BlockEntityType<VatBlockEntity>> VAT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("vat_block_entity", () ->
                    BlockEntityType.Builder.of(VatBlockEntity::new,
                            ModBlocks.VAT.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}