package com.baisylia.culturaldelights.item;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.item.custom.BucketFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import static com.baisylia.cookscollection.item.ModItems.drinkItem;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(CulturalDelights.MOD_ID);

    //Items
    public static final DeferredItem<Item> CUCUMBER_SEEDS = ITEMS.register("cucumber_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CUCUMBERS.get(),
                    new Item.Properties()));

    public static final DeferredItem<Item> CORN_KERNELS = ITEMS.register("corn_kernels",
            () -> new ItemNameBlockItem(ModBlocks.CORN.get(),
                    new Item.Properties()));

    public static final DeferredItem<Item> EGGPLANT_SEEDS = ITEMS.register("eggplant_seeds",
            () -> new ItemNameBlockItem(ModBlocks.EGGPLANTS.get(),
                    new Item.Properties()));

    public static final DeferredItem<Item> BEANS = ITEMS.register("beans",
            () -> new ItemNameBlockItem(ModBlocks.BUDDING_BEANS.get(),
                    new Item.Properties().food(ModFoods.BEANS)));


    //Ingredients
    public static final DeferredItem<Item> BEAN_POD = ITEMS.register("bean_pod",
            () -> new Item(new Item.Properties().food(ModFoods.BEAN_POD)));
    public static final DeferredItem<Item> TOFU = ITEMS.register("tofu",
            () -> new Item(new Item.Properties().food(ModFoods.TOFU)));
    public static final DeferredItem<Item> REFRIED_BEANS = ITEMS.register("refried_beans",
            () -> new ConsumableItem(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(ModFoods.REFRIED_BEANS)));

    public static final DeferredItem<Item> AVOCADO = ITEMS.register("avocado",
            () -> new Item(new Item.Properties().food(ModFoods.AVOCADO)));

    public static final DeferredItem<Item> CUT_AVOCADO = ITEMS.register("cut_avocado",
            () -> new Item(new Item.Properties().food(ModFoods.CUT_AVOCADO)));

    public static final DeferredItem<Item> CUCUMBER = ITEMS.register("cucumber",
            () -> new Item(new Item.Properties().food(ModFoods.CUCUMBER)));

    public static final DeferredItem<Item> PICKLE = ITEMS.register("pickle",
            () -> new Item(new Item.Properties().food(ModFoods.PICKLE)));

    public static final DeferredItem<Item> CUT_CUCUMBER = ITEMS.register("cut_cucumber",
            () -> new Item(new Item.Properties().food(ModFoods.CUT_CUCUMBER)));

    public static final DeferredItem<Item> CUT_PICKLE = ITEMS.register("cut_pickle",
            () -> new Item(new Item.Properties().food(ModFoods.CUT_PICKLE)));

    public static final DeferredItem<Item> EGGPLANT = ITEMS.register("eggplant",
            () -> new Item(new Item.Properties().food(ModFoods.EGGPLANT)));

    public static final DeferredItem<Item> CUT_EGGPLANT = ITEMS.register("cut_eggplant",
            () -> new Item(new Item.Properties().food(ModFoods.CUT_EGGPLANT)));

    public static final DeferredItem<Item> SMOKED_EGGPLANT = ITEMS.register("smoked_eggplant",
            () -> new Item(new Item.Properties().food(ModFoods.SMOKED_EGGPLANT)));

    public static final DeferredItem<Item> SMOKED_TOMATO = ITEMS.register("smoked_tomato",
            () -> new Item(new Item.Properties().food(ModFoods.SMOKED_TOMATO)));

    public static final DeferredItem<Item> SMOKED_CUT_EGGPLANT = ITEMS.register("smoked_cut_eggplant",
            () -> new Item(new Item.Properties().food(ModFoods.SMOKED_CUT_EGGPLANT)));

    public static final DeferredItem<Item> SMOKED_WHITE_EGGPLANT = ITEMS.register("smoked_white_eggplant",
            () -> new Item(new Item.Properties().food(ModFoods.SMOKED_WHITE_EGGPLANT)));
    public static final DeferredItem<Item> WHITE_EGGPLANT = ITEMS.register("white_eggplant",
            () -> new Item(new Item.Properties().food(ModFoods.WHITE_EGGPLANT)));
    public static final DeferredItem<Item> CORN_COB = ITEMS.register("corn_cob",
            () -> new Item(new Item.Properties().food(ModFoods.CORN_COB)));

    public static final DeferredItem<Item> SQUID = ITEMS.register("squid",
            () -> new Item(new Item.Properties().food(ModFoods.SQUID)));

    public static final DeferredItem<Item> COOKED_SQUID = ITEMS.register("cooked_squid",
            () -> new Item(new Item.Properties().food(ModFoods.COOKED_SQUID)));

    public static final DeferredItem<Item> GLOW_SQUID = ITEMS.register("glow_squid",
            () -> new Item(new Item.Properties().food(ModFoods.GLOW_SQUID)));

    public static final DeferredItem<Item> RAW_CALAMARI = ITEMS.register("raw_calamari",
            () -> new Item(new Item.Properties().food(ModFoods.RAW_CALAMARI)));

    public static final DeferredItem<Item> COOKED_CALAMARI = ITEMS.register("cooked_calamari",
            () -> new Item(new Item.Properties().food(ModFoods.COOKED_CALAMARI)));


    public static final DeferredItem<Item> BUTTER = ITEMS.register("butter",
            () -> new Item(new Item.Properties().food(ModFoods.BUTTER)));

    public static final DeferredItem<Item> BUTTERED_TOAST = ITEMS.register("buttered_toast",
            () -> new Item(new Item.Properties().food(ModFoods.BUTTERED_TOAST)));

    public static final DeferredItem<Item> RAW_SAUSAGE = ITEMS.register("raw_sausage",
            () -> new Item(new Item.Properties().food(ModFoods.RAW_SAUSAGE)));

    public static final DeferredItem<Item> COOKED_SAUSAGE = ITEMS.register("cooked_sausage",
            () -> new Item(new Item.Properties().food(ModFoods.COOKED_SAUSAGE)));

    public static final DeferredItem<Item> SNAG = ITEMS.register("snag",
            () -> new Item(new Item.Properties().food(ModFoods.SNAG)));

    public static final DeferredItem<Item> SAUSAGES_AND_MASH = ITEMS.register("sausages_and_mash",
            () -> new ConsumableItem(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(ModFoods.SAUSAGES_AND_MASH)));

    public static final DeferredItem<Item> POPCORN_BUCKET = ITEMS.register("popcorn_bucket",
            () -> new BucketFoodItem(new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(16).food(ModFoods.POPCORN_BUCKET)));


    public static final DeferredItem<Item> BEER = ITEMS.register("beer",
            () -> new DrinkableItem(drinkItem().food(ModFoods.BEER).stacksTo(16)));
    public static final DeferredItem<Item> WINE = ITEMS.register("wine",
            () -> new DrinkableItem(drinkItem().food(ModFoods.WINE).stacksTo(16)));
    public static final DeferredItem<Item> GLOW_WINE = ITEMS.register("glow_wine",
            () -> new DrinkableItem(drinkItem().food(ModFoods.GLOW_WINE).stacksTo(16)));
    public static final DeferredItem<Item> MEAD = ITEMS.register("mead",
            () -> new DrinkableItem(drinkItem().food(ModFoods.MEAD).stacksTo(16)));
    public static final DeferredItem<Item> APPLE_CIDER = ITEMS.register("apple_cider",
            () -> new DrinkableItem(drinkItem().food(ModFoods.APPLE_CIDER).stacksTo(16)));
    public static final DeferredItem<Item> MOJITO = ITEMS.register("mojito",
            () -> new DrinkableItem(drinkItem().food(ModFoods.MOJITO).stacksTo(16)));
    public static final DeferredItem<Item> MARGARITA = ITEMS.register("margarita",
            () -> new DrinkableItem(drinkItem().food(ModFoods.MARGARITA).stacksTo(16)));
    public static final DeferredItem<Item> BLOODY_MARY = ITEMS.register("bloody_mary",
            () -> new DrinkableItem(drinkItem().food(ModFoods.BLOODY_MARY).stacksTo(16)));
    public static final DeferredItem<Item> LEMON_LIQUEUR = ITEMS.register("lemon_liqueur",
            () -> new DrinkableItem(drinkItem().food(ModFoods.LEMON_LIQUEUR).stacksTo(16)));
    public static final DeferredItem<Item> BUTTERBEER = ITEMS.register("butterbeer",
            () -> new DrinkableItem(drinkItem().food(ModFoods.BUTTERBEER).stacksTo(16)));
    public static final DeferredItem<Item> COLA = ITEMS.register("cola",
            () -> new DrinkableItem(drinkItem().food(ModFoods.COLA).stacksTo(16)));
    public static final DeferredItem<Item> TEQUILA = ITEMS.register("tequila",
            () -> new DrinkableItem(drinkItem().food(ModFoods.TEQUILA).stacksTo(16)));
    public static final DeferredItem<Item> GIN = ITEMS.register("gin",
            () -> new DrinkableItem(drinkItem().food(ModFoods.GIN).stacksTo(16)));
    public static final DeferredItem<Item> BRANDY = ITEMS.register("brandy",
            () -> new DrinkableItem(drinkItem().food(ModFoods.BRANDY).stacksTo(16)));
    public static final DeferredItem<Item> VODKA = ITEMS.register("vodka",
            () -> new DrinkableItem(drinkItem().food(ModFoods.VODKA).stacksTo(16)));
    public static final DeferredItem<Item> WHISKEY = ITEMS.register("whiskey",
            () -> new DrinkableItem(drinkItem().food(ModFoods.WHISKEY).stacksTo(16)));
    public static final DeferredItem<Item> RUM = ITEMS.register("rum",
            () -> new DrinkableItem(drinkItem().food(ModFoods.RUM).stacksTo(16)));

    public static final DeferredItem<Item> ACID = ITEMS.register("acid",
            () -> new DrinkableItem(drinkItem().food(ModFoods.ACID).stacksTo(16)));
    public static final DeferredItem<Item> VINEGAR = ITEMS.register("vinegar",
            () -> new DrinkableItem(drinkItem().food(ModFoods.VINEGAR).stacksTo(16)));
    public static final DeferredItem<Item> PICKLED_EGG = ITEMS.register("pickled_egg",
            () -> new Item(new Item.Properties().food(ModFoods.PICKLED_EGG)));
    public static final DeferredItem<Item> CHEESE_WEDGE = ITEMS.register("cheese_wedge",
            () -> new Item(new Item.Properties().food(ModFoods.CHEESE_WEDGE)));

    public static final DeferredItem<Item> CORN_DOG = ITEMS.register("corn_dog",
            () -> new Item(new Item.Properties().craftRemainder(Items.STICK).food(ModFoods.CORN_DOG)));
    public static final DeferredItem<Item> HOT_DOG = ITEMS.register("hot_dog",
            () -> new Item(new Item.Properties().food(ModFoods.HOT_DOG)));
    public static final DeferredItem<Item> CHEESY_CHIP_WRAP = ITEMS.register("cheesy_chip_wrap",
            () -> new Item(new Item.Properties().food(ModFoods.CHEESY_CHIP_WRAP)));
    public static final DeferredItem<Item> CHEESE_CRACKER = ITEMS.register("cheese_cracker",
            () -> new Item(new Item.Properties().food(ModFoods.CHEESE_CRACKER)));
    public static final DeferredItem<Item> CHIPS_WITH_CHEESE = ITEMS.register("chips_with_cheese",
            () -> new Item(new Item.Properties().food(ModFoods.CHIPS_WITH_CHEESE)));
    public static final DeferredItem<Item> CINNAMON = ITEMS.register("cinnamon",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CINNAMON_MINT_CURRY = ITEMS.register("cinnamon_mint_curry",
            () -> new Item(new Item.Properties().food(ModFoods.CINNAMON_MINT_CURRY)));
    public static final DeferredItem<Item> CINNAMON_CRACKER = ITEMS.register("cinnamon_cracker",
            () -> new Item(new Item.Properties().food(ModFoods.CINNAMON_CRACKER)));
    public static final DeferredItem<Item> BUTTERSCOTCH_CINNAMON_PIE_SLICE = ITEMS.register("butterscotch_cinnamon_pie_slice",
            () -> new Item(new Item.Properties().food(FoodValues.PIE_SLICE)));


    //Meals
    public static final DeferredItem<Item> POPCORN = ITEMS.register("popcorn",
            () -> new Item(new Item.Properties().food(ModFoods.POPCORN)));

    public static final DeferredItem<Item> CORN_DOUGH = ITEMS.register("corn_dough",
            () -> new Item(new Item.Properties().food(ModFoods.CORN_DOUGH)));

    public static final DeferredItem<Item> TORTILLA = ITEMS.register("tortilla",
            () -> new Item(new Item.Properties().food(ModFoods.TORTILLA)));

    public static final DeferredItem<Item> TORTILLA_CHIPS = ITEMS.register("tortilla_chips",
            () -> new Item(new Item.Properties().food(ModFoods.TORTILLA_CHIPS)));

    public static final DeferredItem<Item> ELOTE = ITEMS.register("elote",
            () -> new Item(new Item.Properties().craftRemainder(Items.STICK).food(ModFoods.ELOTE)));

    public static final DeferredItem<Item> EMPANADA = ITEMS.register("empanada",
            () -> new Item(new Item.Properties().food(ModFoods.EMPANADA)));

    public static final DeferredItem<Item> HEARTY_SALAD = ITEMS.register("hearty_salad",
            () -> new ConsumableItem(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(ModFoods.HEARTY_SALAD)));

    public static final DeferredItem<Item> BEEF_BURRITO = ITEMS.register("beef_burrito",
            () -> new Item(new Item.Properties().food(ModFoods.BEEF_BURRITO)));

    public static final DeferredItem<Item> MUTTON_SANDWICH = ITEMS.register("mutton_sandwich",
            () -> new Item(new Item.Properties().food(ModFoods.MUTTON_SANDWICH)));

    public static final DeferredItem<Item> EGGPLANT_PARMESAN = ITEMS.register("eggplant_parmesan",
            () -> new ConsumableItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(ModFoods.EGGPLANT_PARMESAN)));

    public static final DeferredItem<Item> POACHED_EGGPLANTS = ITEMS.register("poached_eggplants",
            () -> new ConsumableItem(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(ModFoods.POACHED_EGGPLANTS)));

    public static final DeferredItem<Item> EGGPLANT_BURGER = ITEMS.register("eggplant_burger",
            () -> new Item(new Item.Properties().food(ModFoods.EGGPLANT_BURGER)));

    public static final DeferredItem<Item> AVOCADO_TOAST = ITEMS.register("avocado_toast",
            () -> new Item(new Item.Properties().food(ModFoods.AVOCADO_TOAST)));

    public static final DeferredItem<Item> CREAMED_CORN = ITEMS.register("creamed_corn",
            () -> new ConsumableItem(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(ModFoods.CREAMED_CORN)));

    public static final DeferredItem<Item> CHICKEN_TACO = ITEMS.register("chicken_taco",
            () -> new Item(new Item.Properties().food(ModFoods.CHICKEN_TACO)));

    public static final DeferredItem<Item> SPICY_CURRY = ITEMS.register("spicy_curry",
            () -> new ConsumableItem(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(ModFoods.SPICY_CURRY)));

    public static final DeferredItem<Item> PORK_WRAP = ITEMS.register("pork_wrap",
            () -> new Item(new Item.Properties().food(ModFoods.PORK_WRAP)));

    public static final DeferredItem<Item> FISH_TACO = ITEMS.register("fish_taco",
            () -> new Item(new Item.Properties().food(ModFoods.FISH_TACO)));

    public static final DeferredItem<Item> MIDORI_ROLL = ITEMS.register("midori_roll",
            () -> new Item(new Item.Properties().food(ModFoods.MIDORI_ROLL)));

    public static final DeferredItem<Item> MIDORI_ROLL_SLICE = ITEMS.register("midori_roll_slice",
            () -> new Item(new Item.Properties().food(ModFoods.MIDORI_ROLL_SLICE)));

    public static final DeferredItem<Item> EGG_ROLL = ITEMS.register("egg_roll",
            () -> new Item(new Item.Properties().food(ModFoods.EGG_ROLL)));

    public static final DeferredItem<Item> CHICKEN_ROLL = ITEMS.register("chicken_roll",
            () -> new Item(new Item.Properties().food(ModFoods.CHICKEN_ROLL)));

    public static final DeferredItem<Item> CHICKEN_ROLL_SLICE = ITEMS.register("chicken_roll_slice",
            () -> new Item(new Item.Properties().food(ModFoods.CHICKEN_ROLL_SLICE)));

    public static final DeferredItem<Item> PUFFERFISH_ROLL = ITEMS.register("pufferfish_roll",
            () -> new Item(new Item.Properties().food(ModFoods.PUFFERFISH_ROLL)));

    public static final DeferredItem<Item> TROPICAL_ROLL = ITEMS.register("tropical_roll",
            () -> new Item(new Item.Properties().food(ModFoods.TROPICAL_ROLL)));

    public static final DeferredItem<Item> RICE_BALL = ITEMS.register("rice_ball",
            () -> new Item(new Item.Properties().food(ModFoods.RICE_BALL)));

    public static final DeferredItem<Item> CALAMARI_ROLL = ITEMS.register("calamari_roll",
            () -> new Item(new Item.Properties().food(ModFoods.CALAMARI_ROLL)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
