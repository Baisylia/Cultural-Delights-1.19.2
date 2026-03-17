package com.baisylia.culturaldelights.item;

import com.baisylia.culturaldelights.CulturalDelights;
import com.baisylia.culturaldelights.block.ModBlocks;
import com.baisylia.culturaldelights.item.custom.BucketFoodItem;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.item.KelpRollItem;

import static com.baisylia.cookscollection.item.ModItems.drinkItem;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CulturalDelights.MOD_ID);

    //Items                                                                  item id

    public static final RegistryObject<Item> CUCUMBER_SEEDS = ITEMS.register("cucumber_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CUCUMBERS.get(),
                    new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));

    public static final RegistryObject<Item> CORN_KERNELS = ITEMS.register("corn_kernels",
            () -> new ItemNameBlockItem(ModBlocks.CORN.get(),
                    new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));

    public static final RegistryObject<Item> EGGPLANT_SEEDS = ITEMS.register("eggplant_seeds",
            () -> new ItemNameBlockItem(ModBlocks.EGGPLANTS.get(),
                    new Item.Properties().tab(FarmersDelight.CREATIVE_TAB)));

    public static final RegistryObject<Item> BEANS = ITEMS.register("beans",
            () -> new ItemNameBlockItem(ModBlocks.BUDDING_BEANS.get(),
                    new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.BEANS)));


    //Ingredients
    public static final RegistryObject<Item> BEAN_POD = ITEMS.register("bean_pod",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.BEAN_POD)));
    public static final RegistryObject<Item> TOFU = ITEMS.register("tofu",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.TOFU)));
    public static final RegistryObject<Item> REFRIED_BEANS = ITEMS.register("refried_beans",
            () -> new BowlFoodItem(new Item.Properties().stacksTo(16).tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.REFRIED_BEANS)));

    public static final RegistryObject<Item> AVOCADO = ITEMS.register("avocado",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.AVOCADO)));

    public static final RegistryObject<Item> CUT_AVOCADO = ITEMS.register("cut_avocado",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CUT_AVOCADO)));

    public static final RegistryObject<Item> CUCUMBER = ITEMS.register("cucumber",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CUCUMBER)));

    public static final RegistryObject<Item> PICKLE = ITEMS.register("pickle",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.PICKLE)));

    public static final RegistryObject<Item> CUT_CUCUMBER = ITEMS.register("cut_cucumber",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CUT_CUCUMBER)));

    public static final RegistryObject<Item> CUT_PICKLE = ITEMS.register("cut_pickle",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CUT_PICKLE)));

    public static final RegistryObject<Item> EGGPLANT = ITEMS.register("eggplant",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.EGGPLANT)));

    public static final RegistryObject<Item> CUT_EGGPLANT = ITEMS.register("cut_eggplant",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CUT_EGGPLANT)));

    public static final RegistryObject<Item> SMOKED_EGGPLANT = ITEMS.register("smoked_eggplant",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.SMOKED_EGGPLANT)));

    public static final RegistryObject<Item> SMOKED_TOMATO = ITEMS.register("smoked_tomato",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.SMOKED_TOMATO)));

    public static final RegistryObject<Item> SMOKED_CUT_EGGPLANT = ITEMS.register("smoked_cut_eggplant",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.SMOKED_CUT_EGGPLANT)));

    public static final RegistryObject<Item> SMOKED_WHITE_EGGPLANT = ITEMS.register("smoked_white_eggplant",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.SMOKED_WHITE_EGGPLANT)));
    public static final RegistryObject<Item> WHITE_EGGPLANT = ITEMS.register("white_eggplant",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.WHITE_EGGPLANT)));
    public static final RegistryObject<Item> CORN_COB = ITEMS.register("corn_cob",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CORN_COB)));
    //public static final RegistryObject<Item> GINGER = ITEMS.register("ginger",
    //        () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.GINGER)));

    public static final RegistryObject<Item> SQUID = ITEMS.register("squid",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.SQUID)));

    public static final RegistryObject<Item> COOKED_SQUID = ITEMS.register("cooked_squid",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.COOKED_SQUID)));

    public static final RegistryObject<Item> GLOW_SQUID = ITEMS.register("glow_squid",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.GLOW_SQUID)));

    public static final RegistryObject<Item> RAW_CALAMARI = ITEMS.register("raw_calamari",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.RAW_CALAMARI)));

    public static final RegistryObject<Item> COOKED_CALAMARI = ITEMS.register("cooked_calamari",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.COOKED_CALAMARI)));


    public static final RegistryObject<Item> BUTTER = ITEMS.register("butter",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.BUTTER)));

    public static final RegistryObject<Item> BUTTERED_TOAST = ITEMS.register("buttered_toast",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.BUTTERED_TOAST)));

    public static final RegistryObject<Item> RAW_SAUSAGE = ITEMS.register("raw_sausage",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.RAW_SAUSAGE)));

    public static final RegistryObject<Item> COOKED_SAUSAGE = ITEMS.register("cooked_sausage",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.COOKED_SAUSAGE)));

    public static final RegistryObject<Item> SNAG = ITEMS.register("snag",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.SNAG)));

    public static final RegistryObject<Item> SAUSAGES_AND_MASH = ITEMS.register("sausages_and_mash",
            () -> new BowlFoodItem(new Item.Properties().stacksTo(16).tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.SAUSAGES_AND_MASH)));

    public static final RegistryObject<Item> POPCORN_BUCKET = ITEMS.register("popcorn_bucket",
            () -> new BucketFoodItem(new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(16).tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.POPCORN_BUCKET)));


    public static final RegistryObject<Item> BEER = ITEMS.register("beer",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.BEER)));
    public static final RegistryObject<Item> WINE = ITEMS.register("wine",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.WINE)));
    public static final RegistryObject<Item> GLOW_WINE = ITEMS.register("glow_wine",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.GLOW_WINE)));
    public static final RegistryObject<Item> GINGER_BEER = ITEMS.register("ginger_beer",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.GINGER_BEER)));
    public static final RegistryObject<Item> MEAD = ITEMS.register("mead",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.MEAD)));
    public static final RegistryObject<Item> APPLE_CIDER = ITEMS.register("apple_cider",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.APPLE_CIDER)));
    public static final RegistryObject<Item> MOJITO = ITEMS.register("mojito",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.MOJITO)));
    public static final RegistryObject<Item> MARGARITA = ITEMS.register("margarita",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.MARGARITA)));
    public static final RegistryObject<Item> BLOODY_MARY = ITEMS.register("bloody_mary",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.BLOODY_MARY)));
    public static final RegistryObject<Item> LEMON_LIQUEUR = ITEMS.register("lemon_liqueur",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.LEMON_LIQUEUR)));
    public static final RegistryObject<Item> BUTTERBEER = ITEMS.register("butterbeer",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.BUTTERBEER)));
    public static final RegistryObject<Item> COLA = ITEMS.register("cola",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.COLA)));
    public static final RegistryObject<Item> TEQUILA = ITEMS.register("tequila",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.TEQUILA)));
    public static final RegistryObject<Item> GIN = ITEMS.register("gin",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.GIN)));
    public static final RegistryObject<Item> BRANDY = ITEMS.register("brandy",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.BRANDY)));
    public static final RegistryObject<Item> VODKA = ITEMS.register("vodka",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.VODKA)));
    public static final RegistryObject<Item> WHISKEY = ITEMS.register("whiskey",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.WHISKEY)));
    public static final RegistryObject<Item> RUM = ITEMS.register("rum",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.RUM)));

    public static final RegistryObject<Item> ACID = ITEMS.register("acid",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.ACID)));
    public static final RegistryObject<Item> VINEGAR = ITEMS.register("vinegar",
            () -> new DrinkableItem(drinkItem().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.VINEGAR)));
    public static final RegistryObject<Item> PICKLED_EGG = ITEMS.register("pickled_egg",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.PICKLED_EGG)));
    public static final RegistryObject<Item> CHEESE_WHEEL = ITEMS.register("cheese_wheel",
            () -> new Item(new Item.Properties().stacksTo(16).tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CHEESE_WHEEL)));
    public static final RegistryObject<Item> CHEESE_WEDGE = ITEMS.register("cheese_wedge",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CHEESE_WEDGE)));


    //Meals
    public static final RegistryObject<Item> POPCORN = ITEMS.register("popcorn",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.POPCORN)));

    public static final RegistryObject<Item> CORN_DOUGH = ITEMS.register("corn_dough",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CORN_DOUGH)));

    public static final RegistryObject<Item> TORTILLA = ITEMS.register("tortilla",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.TORTILLA)));

    public static final RegistryObject<Item> TORTILLA_CHIPS = ITEMS.register("tortilla_chips",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.TORTILLA_CHIPS)));

    public static final RegistryObject<Item> ELOTE = ITEMS.register("elote",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.ELOTE)));

    public static final RegistryObject<Item> EMPANADA = ITEMS.register("empanada",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.EMPANADA)));

    public static final RegistryObject<Item> HEARTY_SALAD = ITEMS.register("hearty_salad",
            () -> new BowlFoodItem(new Item.Properties().stacksTo(16).tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.HEARTY_SALAD)));

    public static final RegistryObject<Item> BEEF_BURRITO = ITEMS.register("beef_burrito",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.BEEF_BURRITO)));

    public static final RegistryObject<Item> MUTTON_SANDWICH = ITEMS.register("mutton_sandwich",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.MUTTON_SANDWICH)));

    public static final RegistryObject<Item> EGGPLANT_PARMESAN = ITEMS.register("eggplant_parmesan",
            () -> new BowlFoodItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).stacksTo(16).food(ModFoods.EGGPLANT_PARMESAN)));

    public static final RegistryObject<Item> POACHED_EGGPLANTS = ITEMS.register("poached_eggplants",
            () -> new BowlFoodItem(new Item.Properties().stacksTo(16).tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.POACHED_EGGPLANTS)));

    public static final RegistryObject<Item> EGGPLANT_BURGER = ITEMS.register("eggplant_burger",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.EGGPLANT_BURGER)));

    public static final RegistryObject<Item> AVOCADO_TOAST = ITEMS.register("avocado_toast",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.AVOCADO_TOAST)));

    public static final RegistryObject<Item> CREAMED_CORN = ITEMS.register("creamed_corn",
            () -> new BowlFoodItem(new Item.Properties().stacksTo(16).tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CREAMED_CORN)));

    public static final RegistryObject<Item> CHICKEN_TACO = ITEMS.register("chicken_taco",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CHICKEN_TACO)));

    public static final RegistryObject<Item> SPICY_CURRY = ITEMS.register("spicy_curry",
            () -> new BowlFoodItem(new Item.Properties().stacksTo(16).tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.SPICY_CURRY)));

    public static final RegistryObject<Item> PORK_WRAP = ITEMS.register("pork_wrap",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.PORK_WRAP)));

    public static final RegistryObject<Item> FISH_TACO = ITEMS.register("fish_taco",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.FISH_TACO)));

    public static final RegistryObject<Item> MIDORI_ROLL = ITEMS.register("midori_roll",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.MIDORI_ROLL)));

    public static final RegistryObject<Item> MIDORI_ROLL_SLICE = ITEMS.register("midori_roll_slice",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.MIDORI_ROLL_SLICE)));

    public static final RegistryObject<Item> EGG_ROLL = ITEMS.register("egg_roll",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.EGG_ROLL)));

    public static final RegistryObject<Item> CHICKEN_ROLL = ITEMS.register("chicken_roll",
            () -> new KelpRollItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CHICKEN_ROLL)));

    public static final RegistryObject<Item> CHICKEN_ROLL_SLICE = ITEMS.register("chicken_roll_slice",
            () -> new KelpRollItem(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CHICKEN_ROLL_SLICE)));

    public static final RegistryObject<Item> PUFFERFISH_ROLL = ITEMS.register("pufferfish_roll",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.PUFFERFISH_ROLL)));

    public static final RegistryObject<Item> TROPICAL_ROLL = ITEMS.register("tropical_roll",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.TROPICAL_ROLL)));

    public static final RegistryObject<Item> RICE_BALL = ITEMS.register("rice_ball",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.RICE_BALL)));

    public static final RegistryObject<Item> CALAMARI_ROLL = ITEMS.register("calamari_roll",
            () -> new Item(new Item.Properties().tab(FarmersDelight.CREATIVE_TAB).food(ModFoods.CALAMARI_ROLL)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
