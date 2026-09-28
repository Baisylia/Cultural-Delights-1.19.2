package com.baisylia.culturaldelights.block;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.custom.*;
import com.baisylia.culturaldelights.integration.supplementaries.SupplementariesCompat;
import com.baisylia.culturaldelights.item.ModItems;
import com.baisylia.culturaldelights.world.feature.tree.AvocadoPitGrower;
import com.baisylia.culturaldelights.world.feature.tree.AvocadoTreeGrower;
import com.baisylia.culturaldelights.world.feature.tree.LemonTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.block.WildCropBlock;
import static vectorwing.farmersdelight.common.registry.ModBlocks.APPLE_PIE;
import static vectorwing.farmersdelight.common.registry.ModBlocks.CARROT_CRATE;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CulturalDelights.MOD_ID);

    public static final DeferredBlock<Block> VAT = registerBlock("vat",
            () -> new VatBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY)
                    .strength(5.0f, 6.0f).requiresCorrectToolForDrops().sound(SoundType.METAL)), false, 0);

    public static final DeferredBlock<Block> AGAVE = registerBlock("agave",
            () -> new WildCropBlock(MobEffects.CONFUSION, 6,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)), true, 40);
    public static final DeferredBlock<FlowerPotBlock> POTTED_AGAVE = registerBlockWithoutBlockItem("potted_agave",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, AGAVE, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_ALLIUM)));
    public static final DeferredBlock<Block> MINT = registerBlock("mint",
            () -> new WildCropBlock(MobEffects.DAMAGE_BOOST, 6,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)), true, 40);
    public static final DeferredBlock<FlowerPotBlock> POTTED_MINT = registerBlockWithoutBlockItem("potted_mint",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MINT, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_ALLIUM)));

    public static final DeferredBlock<Block> WILD_CUCUMBERS = registerBlock("wild_cucumbers",
            () -> new WildCropBlock(MobEffects.FIRE_RESISTANCE, 6,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)), false, 0);
    public static final DeferredBlock<Block> WILD_CORN = registerBlock("wild_corn",
            () -> new WildCropBlock(MobEffects.HUNGER, 6,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)), false, 0);
    public static final DeferredBlock<Block> WILD_EGGPLANTS = registerBlock("wild_eggplants",
            () -> new WildCropBlock(MobEffects.DAMAGE_BOOST, 6,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)), false, 0);
    public static final DeferredBlock<Block> WILD_BEANS = registerBlock("wild_beans",
            () -> new WildCropBlock(MobEffects.ABSORPTION, 6,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS)), false, 0);

    public static final DeferredBlock<Block> BEANSTALK = registerBlock("beanstalk",
            () -> new BeanstalkBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN)
                    .strength(1.0F).sound(SoundType.STEM).pushReaction(PushReaction.DESTROY)), false, 0);

    public static final DeferredBlock<Block> BEANSTALK_LEAF = registerBlock("beanstalk_leaf",
            () -> new BeanstalkLeafBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN)
                    .noOcclusion().strength(0.2F).sound(SoundType.BIG_DRIPLEAF).pushReaction(PushReaction.DESTROY)), false, 0);

    public static final DeferredBlock<Block> MAGIC_BEANS = registerBlock("magic_beans",
            () -> new MagicBeansBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                    .noCollission().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY)), false, 0);

    public static final DeferredBlock<Block> STRIPPED_BEANSTALK = registerBlock("stripped_beanstalk",
            () -> new StrippedBeanstalkBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .instrument(NoteBlockInstrument.BASS).strength(1.0F).sound(SoundType.STEM).ignitedByLava()), false, 0);

    public static final DeferredBlock<Block> BEANSTALK_PLANKS = registerBlock("beanstalk_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(MapColor.COLOR_LIGHT_GREEN)), false, 0);
    public static final DeferredBlock<Block> BEANSTALK_STAIRS = registerBlock("beanstalk_stairs",
            () -> new StairBlock(BEANSTALK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(BEANSTALK_PLANKS.get())), false, 0);
    public static final DeferredBlock<Block> BEANSTALK_SLAB = registerBlock("beanstalk_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).mapColor(MapColor.COLOR_LIGHT_GREEN)), false, 0);
    public static final DeferredBlock<Block> BEANSTALK_FENCE = registerBlock("beanstalk_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).mapColor(MapColor.COLOR_LIGHT_GREEN)), false, 0);
    public static final DeferredBlock<Block> BEANSTALK_FENCE_GATE = registerBlock("beanstalk_fence_gate",
            () -> new FenceGateBlock(ModWoodTypes.BEANSTALK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).mapColor(MapColor.COLOR_LIGHT_GREEN)), false, 0);
    public static final DeferredBlock<Block> BEANSTALK_DOOR = registerBlock("beanstalk_door",
            () -> new DoorBlock(ModWoodTypes.BEANSTALK_SET, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).mapColor(MapColor.COLOR_LIGHT_GREEN)),
            block -> new DoubleHighBlockItem(block, new Item.Properties()));
    public static final DeferredBlock<Block> BEANSTALK_TRAPDOOR = registerBlock("beanstalk_trapdoor",
            () -> new TrapDoorBlock(ModWoodTypes.BEANSTALK_SET, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.COLOR_LIGHT_GREEN)), false, 0);
    public static final DeferredBlock<Block> BEANSTALK_PRESSURE_PLATE = registerBlock("beanstalk_pressure_plate",
            () -> new PressurePlateBlock(ModWoodTypes.BEANSTALK_SET, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).mapColor(MapColor.COLOR_LIGHT_GREEN)), false, 0);
    public static final DeferredBlock<Block> BEANSTALK_BUTTON = registerBlock("beanstalk_button",
            () -> new ButtonBlock(ModWoodTypes.BEANSTALK_SET, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)), false, 0);

    public static final DeferredBlock<Block> BEANSTALK_SIGN = registerBlockWithoutBlockItem("beanstalk_sign",
            () -> new StandingSignBlock(ModWoodTypes.BEANSTALK, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava()));
    public static final DeferredBlock<Block> BEANSTALK_WALL_SIGN = registerBlockWithoutBlockItem("beanstalk_wall_sign",
            () -> new WallSignBlock(ModWoodTypes.BEANSTALK, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(BEANSTALK_SIGN.get()).ignitedByLava()));
    public static final DeferredBlock<Block> BEANSTALK_HANGING_SIGN = registerBlockWithoutBlockItem("beanstalk_hanging_sign",
            () -> new CeilingHangingSignBlock(ModWoodTypes.BEANSTALK, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).ignitedByLava()));
    public static final DeferredBlock<Block> BEANSTALK_WALL_HANGING_SIGN = registerBlockWithoutBlockItem("beanstalk_wall_hanging_sign",
            () -> new WallHangingSignBlock(ModWoodTypes.BEANSTALK, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(1.0F).dropsLike(BEANSTALK_HANGING_SIGN.get()).ignitedByLava()));

    public static final DeferredBlock<Block> BEANSTALK_CABINET = registerBlock("beanstalk_cabinet",
            () -> new CabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)), false, 0);

    public static final DeferredBlock<Block> AVOCADO_PIT = registerBlock("avocado_pit",
            () -> new AvocadoPitBlock(AvocadoPitGrower.AVOCADO_PIT_GROWER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)), false, 0);
    public static final DeferredBlock<Block> AVOCADO_SAPLING = registerBlock("avocado_sapling",
            () -> new SaplingBlock(AvocadoTreeGrower.AVOCADO_TREE_GROWER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)), true, 100);
    public static final DeferredBlock<FlowerPotBlock> POTTED_AVOCADO_SAPLING = registerBlockWithoutBlockItem("potted_avocado_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, AVOCADO_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_OAK_SAPLING)));

    public static final DeferredBlock<Block> AVOCADO_LOG = registerBlock("avocado_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LOG)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 30;
                }

                @Override
                public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }, true, 300);

    public static final DeferredBlock<Block> AVOCADO_WOOD = registerBlock("avocado_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_WOOD)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 30;
                }

                @Override
                public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return Blocks.STRIPPED_JUNGLE_WOOD.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }, true, 300);

    public static final DeferredBlock<Block> AVOCADO_LEAVES = registerBlock("avocado_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 30;
                }
            }, false, 0);

    public static final DeferredBlock<Block> FRUITING_AVOCADO_LEAVES = registerBlock("fruiting_avocado_leaves",
            () -> new FruitingLeaves(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 30;
                }
            }, false, 0);

    public static final DeferredBlock<Block> CUCUMBERS = registerBlockWithoutBlockItem("cucumbers",
            () -> new CucumbersBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noOcclusion()));

    public static final DeferredBlock<Block> EGGPLANTS = registerBlockWithoutBlockItem("eggplants",
            () -> new EggplantsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noOcclusion()));

    public static final DeferredBlock<Block> CORN = registerBlockWithoutBlockItem("corn",
            () -> new CornBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noOcclusion()));

    public static final DeferredBlock<Block> BEANS = registerBlockWithoutBlockItem("beans",
            () -> new BeansBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP).noOcclusion()));

    public static final DeferredBlock<Block> ROPE_BEANS = registerBlockWithoutBlockItem("rope_beans",
            () -> ModList.get().isLoaded("supplementaries")
                    ? SupplementariesCompat.makeRopeBeans(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP).noOcclusion())
                    : new BeansBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP).noOcclusion()));

    public static final DeferredBlock<Block> STICK_BEANS = registerBlockWithoutBlockItem("stick_beans",
            () -> ModList.get().isLoaded("supplementaries")
                    ? SupplementariesCompat.makeStickBeans(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP).noOcclusion())
                    : new BeansBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP).noOcclusion()));

    public static final DeferredBlock<Block> BUDDING_BEANS = registerBlockWithoutBlockItem("budding_beans",
            () -> new BuddingBeansBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).noOcclusion()));

    public static final DeferredBlock<Block> AVOCADO_CRATE = registerBlock("avocado_crate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)), false, 0);

    public static final DeferredBlock<Block> CUCUMBER_CRATE = registerBlock("cucumber_crate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)), false, 0);

    public static final DeferredBlock<Block> PICKLE_CRATE = registerBlock("pickle_crate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)), false, 0);

    public static final DeferredBlock<Block> CORN_COB_CRATE = registerBlock("corn_cob_crate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)), false, 0);

    public static final DeferredBlock<Block> EGGPLANT_CRATE = registerBlock("eggplant_crate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)), false, 0);

    public static final DeferredBlock<Block> WHITE_EGGPLANT_CRATE = registerBlock("white_eggplant_crate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)), false, 0);

    public static final DeferredBlock<Block> BEAN_CRATE = registerBlock("bean_crate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)), false, 0);

    public static final DeferredBlock<Block> BEAN_BAG = registerBlock("bean_bag",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)), false, 0);

    public static final DeferredBlock<Block> EXOTIC_ROLL_MEDLEY = registerBlock("exotic_roll_medley",
            () -> new ExoticRollMedleyBlock(BlockBehaviour.Properties.of().forceSolidOn().strength(0.5F).mapColor(MapColor.WOOD).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).noOcclusion()), false, 0);

    public static final DeferredBlock<Block> EGGPLANT_PARMESAN_BLOCK = registerBlock("eggplant_parmesan_block",
            () -> new EggplantFeastBlock(BlockBehaviour.Properties.of().forceSolidOn().strength(0.5F).mapColor(MapColor.WOOD).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).noOcclusion(), ModItems.EGGPLANT_PARMESAN, true), false, 0);

    public static final DeferredBlock<Block> CHEESE_BLOCK = registerBlock("cheese_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_WART_BLOCK)), false, 0);

    public static final DeferredBlock<Block> CHEESE_WHEEL = registerBlock("cheese_wheel",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.CHEESE_BLOCK.get())), false, 0);

    public static final DeferredBlock<Block> BUTTERSCOTCH_CINNAMONN_PIE = registerBlock("butterscotch_cinnamon_pie",
            () -> new PieBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), ModItems.BUTTERSCOTCH_CINNAMON_PIE_SLICE), false, 0);

    public static final DeferredBlock<Block> BRICK_COUNTER = registerBlock("brick_counter",
            () -> new CounterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS)), false, 0);
    public static final @Nullable DeferredBlock<Block> ASH_BRICK_COUNTER = ModList.get().isLoaded("supplementaries")
            ? registerBlock("ash_brick_counter", () -> new CounterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS)), false, 0)
            : null;
    public static final @Nullable DeferredBlock<Block> SILT_BRICK_COUNTER = ModList.get().isLoaded("twigs")
            ? registerBlock("silt_brick_counter", () -> new CounterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS)), false, 0)
            : null;

    public static final DeferredBlock<Block> LEMON_CRATE = registerBlock("lemon_crate",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(CARROT_CRATE.get())), false, 0);

    public static final DeferredBlock<Block> LEMON_SAPLING = registerBlock("lemon_sapling",
            () -> new SaplingBlock(LemonTreeGrower.LEMON_TREE_GROWER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)), true, 100);

    public static final DeferredBlock<Block> LEMON_LOG = registerBlock("lemon_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 30;
                }

                @Override
                public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }, true, 300);

    public static final DeferredBlock<Block> LEMON_WOOD = registerBlock("lemon_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
                    return 30;
                }

                @Override
                public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
                    if (itemAbility == ItemAbilities.AXE_STRIP) {
                        return Blocks.STRIPPED_OAK_WOOD.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
                    }
                    return super.getToolModifiedState(state, context, itemAbility, simulate);
                }
            }, true, 300);

    public static final DeferredBlock<Block> LEMON_LEAVES = registerBlock("lemon_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 30;
                }
            }, false, 0);

    public static final DeferredBlock<Block> FRUITING_LEMON_LEAVES = registerBlock("fruiting_lemon_leaves",
            () -> new FruitingLeaves(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES), ModItems.LEMON) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 30;
                }
            }, false, 0);

    public static final DeferredBlock<Block> RUSTIC_LOAF = registerBlock("rustic_loaf",
            () -> new RusticLoafBlock(BlockBehaviour.Properties.ofFullCopy(APPLE_PIE.get()).noOcclusion(),
                    ModItems.RUSTIC_LOAF_SLICE), false, 0);

    public static final DeferredBlock<Block> SALT_BLOCK = registerBlock("salt_block",
            () -> new SaltBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK).randomTicks()), false, 0);

    public static final DeferredBlock<Block> SALT_SPIKE = registerBlock("salt_spike",
            () -> new SaltSpikeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POINTED_DRIPSTONE)
                    .offsetType(BlockBehaviour.OffsetType.NONE)
                    .noOcclusion()
                    .sound(SoundType.POINTED_DRIPSTONE)
                    .strength(1.5F, 3.0F)
                    .pushReaction(PushReaction.DESTROY)), false, 0);

    public static final DeferredBlock<Block> OVEN = registerBlock("oven",
            () -> new OvenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS)), false, 0);

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, boolean isFuel, int fuelAmount) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, isFuel, fuelAmount);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, Function<T, Item> item) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> item.apply(toReturn.get()));
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredItem<Item> registerBlockItem(String name, DeferredBlock<T> block, boolean isFuel, int fuelAmount) {
        if (!isFuel) {
            return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        } else {
            return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()) {
                @Override
                public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
                    return fuelAmount;
                }
            });
        }
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}