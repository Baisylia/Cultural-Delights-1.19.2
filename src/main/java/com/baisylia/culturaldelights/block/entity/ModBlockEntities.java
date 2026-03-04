package com.baisylia.culturaldelights.block.entity;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.block.entity.custom.FermenterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CulturalDelights.MOD_ID);

    public static final RegistryObject<BlockEntityType<FermenterBlockEntity>> FERMENTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fermenter_block_entity", () ->
                    BlockEntityType.Builder.of(FermenterBlockEntity::new,
                            ModBlocks.FERMENTER.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}