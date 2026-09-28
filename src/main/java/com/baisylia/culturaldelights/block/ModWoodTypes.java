package com.baisylia.culturaldelights.block;

import com.baisylia.culturaldelights.CulturalDelights;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    public static final BlockSetType BEANSTALK_SET = BlockSetType.register(new BlockSetType(CulturalDelights.MOD_ID + ":beanstalk"));
    public static final WoodType BEANSTALK = WoodType.register(new WoodType(CulturalDelights.MOD_ID + ":beanstalk", BEANSTALK_SET));
}
