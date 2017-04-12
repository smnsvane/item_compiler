package parse;

import java.util.HashSet;

import helper.Label;
import helper.Origin;
import sql.Cr;
import sql.Item;
import sql.Recipe;
import sql.Research;




public class Validator {
    public void validate() {
        HashSet<Item> recipeProducts = new HashSet<Item>();
        HashSet<Item> recipeComponents = new HashSet<Item>();
        for (Recipe rcp : Util.recipe) {
            for (ItemStack prodStack : rcp.products)
                recipeProducts.add(prodStack.item);
            for (ItemStack compStack : rcp.components)
                recipeComponents.add(compStack.item);
        }

        HashSet<Item> researchSources = new HashSet<Item>();
        HashSet<Recipe> researchResults = new HashSet<Recipe>();
        for (Research rsh : Util.research) {
            for (Recipe r : rsh.recipes)
                researchResults.add(r);
            if (rsh.researchedItem != null)
                researchSources.add(rsh.researchedItem);
        }
        HashSet<Item> lootOriginItems = new HashSet<Item>();
        HashSet<Item> wasteOriginItems = new HashSet<Item>();
        HashSet<Item> itemsWithRarity = new HashSet<Item>();
        HashSet<Item> compositeItems = new HashSet<Item>();
        for (Item i : Util.item) {
            if (i.origins.contains(Origin.Loot))
                lootOriginItems.add(i);
            if (i.origins.contains(Origin.Waste))
                wasteOriginItems.add(i);
            if (i.is(Label.Waste))
                itemsWithRarity.add(i);
            if (i.is(Label.Composite))
                compositeItems.add(i);
        }
        HashSet<Item> creaturelootItems = new HashSet<Item>();
        for (Cr cr : Util.cr)
            if (cr.loot != null)
                for (ItemStack is : cr.loot)
                    creaturelootItems.add(is.item);

        // waste item validation
        for (Item i : itemsWithRarity)
            if (!wasteOriginItems.contains(i))
                System.err.println("Item '" + i + "' have rarity, but is NOT marked with origin containing WASTE");

        for (Item i : wasteOriginItems)
            if (!itemsWithRarity.contains(i))
                System.err.println("Item '" + i + "' with origin containing WASTE, but have no rarity");

        // loot item validation
        for (Item i : lootOriginItems)
            if (!creaturelootItems.contains(i))
                System.err.println("Item '" + i + "' with origin containing LOOT is not looted from any creature");

        for (Item i : creaturelootItems)
            if (!lootOriginItems.contains(i))
                System.err.println("Loot Item '" + i + "' is NOT marked with origin containing LOOT");

        // validation: item recipe knowledge (how do players come to know how to produce a given item)
        for (Recipe rec : Util.recipe)
            if (!researchResults.contains(rec))
                System.err.println("Missing RESEARCH for: '" + rec + "'");

        // validation: item spring (where do a given item come from)
        for (Item i : Util.item)
            if (i.origins.contains(Origin.Recipe_Product) && !recipeProducts.contains(i))
                System.err.println("Missing RECIPE for: '" + i + "'");

        for (Recipe r : Util.recipe) {
            if (r.components.itemStacks.isEmpty() && !r.requiresRawMetal())
                System.err.println("No components in resipe '" + r + "'");
            if (r.products.itemStacks.isEmpty())
                System.err.println("No products in resipe '" + r + "'");
        }

        // dead end item validation
        for (Item i : Util.item)
            if (!i.is(Label.Armor) && !i.is(Label.Clothing) && !i.is(Label.Edible) && !i.is(Label.Drinkable) && !i.is(Label.Tool) && !i.is(Label.Weapon) &&
                !i.is(Label.Ammunition) && !recipeComponents.contains(i) && !researchSources.contains(i))
                System.err.println("Useless item '" + i + "'");

        // recipe metal vs item metal check
        for (Item i : Util.item)
            if (i.is(Label.Metal))
                for (Recipe r : i.recipes) {
                    int recipeMetal = r.getRawMetal();
                    for (ItemStack is : r.components)
                        if (is.item.is(Label.Metal))
                            recipeMetal += is.amount * Integer.parseInt(is.item.get(Label.Metal).arg0);
                    boolean match = Integer.parseInt(i.get(Label.Metal).arg0) == recipeMetal;
                    if (!match)
                        System.err.println("Mismatch between item '" + i + "' metal " + i.get(Label.Metal).arg0 + " and recipe metal " + recipeMetal);
                }

        // check that weapons, clothing and armors can be equipped
        for (Item i : Util.item)
            if (i.is(Label.Weapon) || i.is(Label.Clothing) || i.is(Label.Armor))
                if (!i.is(Label.Wear))
                    System.err.println("Item '" + i + "' is either weapon, clothing or armor, but is NOT wearable");

        // check that composite items only have one recipe and are the only product of that recipe
        for (Item i : compositeItems)
            if (i.recipes.size() != 1)
                System.err.println("Composite item '" + i + "' do NOT have ONE recipe");
            else if (i.recipes.get(0).products.itemStacks.size() != 1)
                System.err.println("Recipe of composite item '" + i + "' have other types of products apart from '" + i + "'");
            else if (i.recipes.get(0).products.itemStacks.get(0).amount != 1)
                System.err.println("Recipe of composite item '" + i + "' have more than one instance of '" + i + "' as product");
    }
}
/*
        // validation: item variables
        for (ItemVariable iv : Util.itemVariable)
            if (iv.key == null || iv.defaultValue == null)
                System.err.println(iv + " key or value is empty");
*/
        /*
        HashSet<Item> itemsWithfunction = new HashSet<Item>();
        HashSet<Item> functionProducts = new HashSet<Item>();
        HashSet<Item> functionComponents = new HashSet<Item>();
        for (Function f : Util.function) {
            itemsWithfunction.add(f.functionItem);
            for (Item i : f.consumedItems)
                functionComponents.add(i);
            for (Item i : f.producedItems)
                functionProducts.add(i);
        }
*/
        /*
        // function product item validation
        for (Item i : functionProducts)
            if (!functionProductOriginItems.contains(i))
                System.err.println("Function product Item '" + i + "' is NOT marked with origin containing FUNCTION_PRODUCT");

        for (Item i : functionProductOriginItems)
            if (!functionProducts.contains(i))
                System.err.println("Item '" + i + "' with origin containing FUNCTION_PRODUCT is not a product of any function");
*/

/*
        // function var init check
        for (Item i : Util.item)
            if (!i.functions.isEmpty()) {
                System.out.println("For " + i);
                for (Function f : i.functions) {

                    vars:
                    for (String predicateName : f.getPredicates()) {
                        if (predicateName.equals("at") || predicateName.equalsIgnoreCase(i.name))
                            continue;
                        for (ItemVariable iv : f.functionItem.variables) {
                            String initVarName = iv.name.substring(f.functionItem.name.length() + 1);
                            if (initVarName.equals(predicateName))
                                continue vars;
                            System.err.println("Item " + f.functionItem + " needs to init " + predicateName);
                        }
                    }
                }
            }
        // TO DO: initialized vars, unused in functions
*/