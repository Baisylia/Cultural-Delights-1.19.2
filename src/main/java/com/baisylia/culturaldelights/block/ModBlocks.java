package com.baisylia.culturaldelights.block;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.custom.*;
import com.baisylia.culturaldelights.integration.supplementaries.SupplementariesCompat;
import com.baisylia.culturaldelights.item.ModItems;
import com.baisylia.culturaldelights.world.feature.tree.AvocadoPitGrower;
import com.baisylia.culturaldelights.world.feature.tree.AvocadoTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.block.WildCropBlock;

import javax.annotation.Nullable;
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

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block, boolean isFuel, int fuelAmount) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, isFuel, fuelAmount);
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