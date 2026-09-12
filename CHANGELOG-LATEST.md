### Added

- **Vats & Fermentation!**
    - Added the Vat for fermenting drinks and brewing ingredients, with temperature requirements and a GUI.
    - Added fermented drinks, spirits, and cocktails: Beer, Wine, Glow Wine, Mead, Apple Cider, Mojito, Margarita,
      Bloody Mary, Lemon Liqueur, Butterbeer, Cola, Tequila, Gin, Brandy, Vodka, Whiskey, and Rum.
    - Added fermentation recipes for Acid, Vinegar, and Fermented Spider Eyes.
    - Added the Intoxication status effect with camera wobble and escalating effects.
    - Added advancements for using the Vat, obtaining Acid, achieving the Cheesy effect, becoming intoxicated, and
      reaching Maximum
      Intoxication.
- **Dairy & Cheese!**
    - Added Cheese Blocks, Cheese Wheel Slabs, and Cheese Wedges.
    - Added the Cheesy status effect, which prevents you from receiving most effects.
    - Added Pickled Eggs, Cheesy Chip Wraps, Chips with Cheese, and Cheese Crackers.
    - Added Butter and Buttered Toast.
- **Beans, Tofu & Legumes!**
    - Added Beans: bean crop, wild bean patches, bean pods, and beans.
    - Added Bean processing: shelling pods (via cutting board or crafting), Soy/Bean Milk, and Refried Beans.
    - Added Tofu along with tofu-based alternative recipes for meat dishes (hamburgers, dumplings, pasta with meatballs,
      stuffed potatoes, barbecue sticks, soups, and curries).
- **Agave, Mint & Cinnamon!**
    - Added Agave plants with wild world generation and potted variants.
    - Added Mint plants with wild world generation, potted variants, and culinary recipes.
    - Added Cinnamon, Cinnamon Crackers, Cinnamon Mint Curry, and Butterscotch Cinnamon Pie & Slices.
- **More Snacks & Meat Dishes!**
    - Added Raw Sausage, Cooked Sausage, Snags, Sausages & Mash, Hot Dogs, and Corn Dogs.
    - Added Popcorn Buckets.
    - Added tomato and potato spoiling.
    - Added the `#culturaldelights:full_meats` item tag for whole meats and tofu, used as ingredients for Raw Sausage
      and Empanadas.
    - Added optional resource packs for renaming/retexture Farmer's Delight Pie Crusts into Pastry Sheets
      and an Apple Juice rename pack to avoid overlap.
- **Compatibility & Integration**
    - Added Cook's Collection as a required dependency.
    - Added JEI and EMI support for Vat recipes.
    - Added Supplementaries compatibility: beans can now grow up ropes and sticks.
    - Added Right-Click Harvest mod compatibility for corn.
    - Added Create harvesting compatibility for corn.

### Changed

- Updated Corn textures and block models for corn growth stages.
- Added custom handheld display models for Elote.
- Updated Corn Kernel drop/cutting recipes and added a shapeless inventory crafting recipe.
- Added Beans, Bean Pods, and Refried Beans as ingredients in existing dishes: Beef Burritos, Chicken Tacos, Hearty
  Salad, Mutton
  Wraps, and Spicy Curry.
- Updated the Pie Crust recipe.

### Fixed

- Fixed Corn and Wild Corn loot tables, growth checks, and bonemealing.
- Fixed Avocado Toast crafting recipe.
- Fixed Spicy Curry cooking pot recipe.
- Fixed the Pickle advancement trigger criteria.

### Removed

- Removed the separate `CornUpperBlock` class in favor of the unified crop block.