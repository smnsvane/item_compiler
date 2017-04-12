package parse;

import java.util.HashSet;

import sql.Clazz;
import sql.Item;
import sql.ItemToClass;


public class Printer {

	public enum PrintMode {
		pretty, //break lines and replace IDs in SQL output with names
		normal,
		no_print;
	}

	public static PrintMode mode;

	public void printSQL() {
		if (mode == Printer.PrintMode.no_print)
			return;

		System.out.println("*** REMEMBER THIS IS THE SYSTEM TABLES (create a dynamic tables for player inventories etc) ***");
		System.out.print("INSERT INTO item (item_id, item_name, weapon_hand, damage_type, weapon_range, minimum_damage, maximum_damage) VALUES ");
		System.out.println(Util.item.toSQL());
		System.out.print("INSERT INTO item_class (label_id, label_name) VALUES ");
		System.out.println(Util.itemClass.toSQL());
		System.out.print("INSERT INTO item_class (item_id, label_id, arg0, arg1) VALUES ");
		System.out.println(Util.itemToitemClass.toSQL());
//		System.out.print("INSERT INTO item_variable (item_id, var_key, var_value) VALUES ");
//		System.out.println(Util.itemVariable.toSQL());
		System.out.print("INSERT INTO skill (skill_id, skill_name, req_skill_id, tool_id) VALUES ");
		System.out.println(Util.skill.toSQL());
		System.out.print("INSERT INTO tool (tool_name_id, tool_name) VALUES ");
		System.out.println(Util.tool.toSQL());
		System.out.print("INSERT INTO recipe (recipe_id, skill_id, recipe_level, raw_metal_amount) VALUES ");
		System.out.println(Util.recipe.toSQL());
		System.out.print("INSERT INTO research (research_id, skill_id, item_id) VALUES ");
		System.out.println(Util.research.toSQL());
		System.out.print("INSERT INTO research_to_recipe (research_id, recipe_id) VALUES ");
		System.out.println(Util.researchToRecipe.toSQL());
//		System.out.print("INSERT INTO function (function_id, function_name, function_item_id, auto_effect, skill_id, consume_item_id, requirement_code, effect_code, effect_description) VALUES ");
//		System.out.println(Util.function.toSQL());
		System.out.print("INSERT INTO cr_ability (creature_ability_id, creature_ability_name) VALUES ");
		System.out.println(Util.crAbility.toSQL());
		System.out.print("INSERT INTO cr (creature_id, creature_name, creature_min_dmg, creature_max_dmg, creature_damage_type, creature_movement, creature_cep, creature_health, inventory_id, creature_comment) VALUES ");
		System.out.println(Util.cr.toSQL());
		System.out.print("INSERT INTO cr_to_cr_ability (creature_id, cr_ability_id, param) VALUES ");
		System.out.println(Util.crToCrAbility.toSQL());
		System.out.print("INSERT INTO cr_tactic (creature_tactic_id, creature_tactic_name) VALUES ");
		System.out.println(Util.crTactic.toSQL());
		System.out.print("INSERT INTO cr_group (creature_group_id, creature_group_name) VALUES ");
		System.out.println(Util.crGroup.toSQL());
		System.out.print("INSERT INTO cr_group_member (creature_group_id, creature_id, tactic_id, creature_group_member_min, creature_group_member_max) VALUES ");
		System.out.println(Util.crGroupMember.toSQL());
		System.out.print("INSERT INTO inventory (inventory_id, item_id, amount) VALUES ");
		System.out.println(Util.inventory.toSQL());
	}

	public void printItemClasses() throws ParserException {
		HashSet<Item> itemsWithClass = new HashSet<Item>();
		for (Clazz label : Util.itemClass) {
			System.out.print(label+": ");
			for (ItemToClass clazz : Util.itemToitemClass)
				if (clazz.clazz.equals(label)) {
					System.out.print(clazz.item+", ");
					itemsWithClass.add(clazz.item);
				}
			System.out.println();
		}
		System.out.print("\nITEMS WITH NO CLASS: ");
		for (Item i : Util.item)
			if (!itemsWithClass.contains(i))
				System.out.print(i+", ");
		/*
        HashSet<Item> waste = new HashSet<Item>();
        HashSet<Item> metal = new HashSet<Item>();
        HashSet<Item> weapon = new HashSet<Item>();
        HashSet<Item> edible = new HashSet<Item>();
        HashSet<Item> drinkable = new HashSet<Item>();
        HashSet<Item> tool = new HashSet<Item>();
        HashSet<Item> loot = new HashSet<Item>();
        HashSet<Item> armor = new HashSet<Item>();
        HashSet<Item> clothing = new HashSet<Item>();
        HashSet<Item> oil = new HashSet<Item>();
        HashSet<Item> water = new HashSet<Item>();
        HashSet<Item> rawGlass = new HashSet<Item>();
        HashSet<Item> ammunition = new HashSet<Item>();
        HashSet<Item> clothingComponent = new HashSet<Item>();
        HashSet<Item> noClass = new HashSet<Item>();

        for (Item i : Util.item) {
            boolean haveClass = false;
            if (i.origins.contains(Origin.Waste)) {
                waste.add(i);
                haveClass = true;
            }
            if (i.metal > 0) {
                metal.add(i);
                haveClass = true;
            }
            if (!i.hand.isEmpty()) {
                weapon.add(i);
                haveClass = true;
            }
            if (i.ingestType.equals("Edible")) {
                edible.add(i);
                haveClass = true;
            }
            if (i.ingestType.equals("Drinkable")) {
                drinkable.add(i);
                haveClass = true;
            }
            if (i.tool != null) {
                tool.add(i);
                haveClass = true;
            }
            if (i.origins.contains(Origin.Loot)) {
                loot.add(i);
                haveClass = true;
            }
            if (!i.wear.isEmpty()) {
                clothing.add(i);
                haveClass = true;
            }
            if (i.armor > 0) {
                armor.add(i);
                haveClass = true;
            }
            if (i.liquid == Liquid.Oil) {
                haveClass = true;
                oil.add(i);
            } else if (i.liquid == Liquid.Water) {
                haveClass = true;
                water.add(i);
            }
            if (i.glassValue > 0) {
                rawGlass.add(i);
                haveClass = true;
            }
            if (i.launcher != null) {
                ammunition.add(i);
                haveClass = true;
            }
            if (!haveClass)
                noClass.add(i);
        }

        System.out.println("waste (rarity):" + waste);
        System.out.println("metal (value):" + metal);
        System.out.println("weapon (hand, min_dmg, max_dmg, dmg_type, range):" + weapon);
        System.out.println("edible (benefit, drawback):" + edible);
        System.out.println("drinkable (benefit, drawback):" + drinkable);
        System.out.println("tool (tool_name):" + tool);
        System.out.println("loot:" + loot);
        System.out.println("clothing (wear_location, cold resist):" + clothing);
        System.out.println("armor (wear_location, armor):" + armor);
        System.out.println("oil:" + oil);
        System.out.println("water:" + water);
        System.out.println("raw glass (value):" + rawGlass);
        System.out.println("ammunition (launcher, bonus):" + ammunition);
        System.out.println("no Class:" + noClass);
		 */
	}
}
