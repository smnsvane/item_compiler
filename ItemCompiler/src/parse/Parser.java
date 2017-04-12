package parse;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;

import helper.Label;
import helper.Wear;
import postponed.Function;
import sql.Cr;
import sql.CrAbility;
import sql.CrGroup;
import sql.CrTactic;
import sql.Item;
import sql.Research;
import sql.Skill;

public class Parser {

	private final int ignore = 0, item = 1, skill = 2, research = 3, function = 5, creatureAbility = 6, creature = 7, creatureTactic = 8, creatureGroup = 9;

	private int state = ignore;

	public void parse(String filename) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String prefix = "h1. List of ";
		for (int lineNumber = 1; scanner.hasNextLine(); lineNumber++) {
			String line = scanner.nextLine();
			line = line.replaceAll("#.*", "");
			if (line.isEmpty())
				continue;
			if (line.equals("IGNORE"))
				state = ignore;
			else if (line.equals(prefix + "Items"))
				state = item;
			else if (line.equals(prefix + "Skills"))
				state = skill;
			else if (line.equals(prefix + "Research"))
				state = research;
//			else if (line.equals(prefix + "Functions"))
//				state = function;
			else if (line.equals(prefix + "Creature Abilities"))
				state = creatureAbility;
			else if (line.equals(prefix + "Creatures"))
				state = creature;
			else if (line.equals(prefix + "Creature Tactics"))
				state = creatureTactic;
			else if (line.equals(prefix + "Creature Groups"))
				state = creatureGroup;

			else {
				int wLevel = getWikiLevel(line);
				if (wLevel == 0)
					continue;
				line = line.substring(wLevel + 1).trim();
				if (state == item)
					parseItemLine(lineNumber, wLevel, line);
				else if (state == skill)
					parseSkillLine(lineNumber, wLevel, line);
				else if (state == research)
					parseResearchLine(lineNumber, wLevel, line);
				else if (state == function)
					parseFunction(lineNumber, wLevel, line);
				else if (state == creatureAbility)
					parseCreatureAbility(lineNumber, wLevel, line);
				else if (state == creature)
					parseCreature(lineNumber, wLevel, line);
				else if (state == creatureTactic)
					parseCreatureTactic(lineNumber, wLevel, line);
				else if (state == creatureGroup)
					parseCreatureGroup(lineNumber, wLevel, line);
			}
		}

		scanner.close();
	}

	private int getWikiLevel(String s) {
		int level = 0;
		for (int i = 0; i < s.length() && s.charAt(i) == '*'; i++)
			level++;
		return level;
	}

	private Item curItem;

	private void parseItemLine(int lineNumber, int wLevel, String line) {
		try {
			if (wLevel == 1)
				curItem = new Item(line);
			else if (wLevel == 2) {
				String[] split = line.split(":", 2);
				String key = split[0].trim();
				String value = split[1].trim();
				if (key.equals("Metal")) {
					int metal = Integer.parseInt(value);
					curItem.addClass(Label.Metal, "" + metal, null);
				} else if (key.equals("Rarity")) {
					int findProb;
					if (value.equals("Common"))
						findProb = 5;
					else if (value.equals("Uncommon"))
						findProb = 2;
					else if (value.equals("Rare"))
						findProb = 1;
					else
						throw new ParserException("Unknown rarity category");
					curItem.addClass(Label.Waste, value, "" + findProb);
				} else if (key.equals("Drinkable") || key.equals("Edible")) {
					Label label = Label.valueOf(key);
					String[] sp = value.split(",", 2);
					String benefit = sp[0].trim(); // TODO create enum for type safety
					String drawback = sp[1].trim(); // TODO create enum for type safety
					curItem.addClass(label, benefit, drawback);
				} else if (key.equals("Wear")) {
					Wear wear = Wear.valueOf(value);
					curItem.addClass(Label.Wear, wear.toString(), null);
				} else if (key.equals("Clothing")) {
					int coldResist = Integer.parseInt(value);
					curItem.addClass(Label.Clothing, "" + coldResist, null);
				} else if (key.equals("Armor")) {
					int armor = Integer.parseInt(value);
					curItem.addClass(Label.Armor, "" + armor, null);
				} else if (key.equals("Tool"))
					curItem.setTool(value);
				else if (key.equals("Ammunition")) {
					curItem.setAmmunition(value);
				} else if (key.equals("Magazine")) {
					int magazineSize = Integer.parseInt(value);
					curItem.addClass(Label.Ranged_Weapon, "" + magazineSize, null);
				} else if (key.equals("Stationary")) {
					String[] sp = value.split("x", 2);
					int arg0 = Integer.parseInt(sp[0].trim());
					int arg1 = Integer.parseInt(sp[1].trim());
					curItem.addClass(Label.Stationary, "" + arg0, "" + arg1);
				}
//				else if (key.equals("Glass")) {
//					int glassValue = Integer.parseInt(value);
//					curItem.addClass(Label.Raw_Glass, "" + glassValue, null);
//				}
				else if (key.equals("Weapon"))
					curItem.setWeapon(value);
				else if (key.equals("Origin"))
					curItem.setOrigin(value);
				else if (key.equals("Class")) {
					String[] sp = value.split(";");
					for (String rawClass : sp)
						curItem.addRawClass(rawClass);
				} else if (key.equals("Clothing")) {
					int coldResist = Integer.parseInt(value);
					curItem.addClass(Label.Clothing, "" + coldResist, null);
				} else if (key.equals("Armor")) {
					int armor = Integer.parseInt(value);
					curItem.addClass(Label.Armor, "" + armor, null);
				} else if (key.equals("Tool"))
					curItem.setTool(value);
				else if (key.equals("Ammunition")) {
					curItem.setAmmunition(value);
				} else if (key.equals("Magazine")) {
					int magazineSize = Integer.parseInt(value);
					curItem.addClass(Label.Ranged_Weapon, "" + magazineSize, null);
				} else if (key.equals("Stationary")) {
					String[] sp = value.split("x", 2);
					int arg0 = Integer.parseInt(sp[0].trim());
					int arg1 = Integer.parseInt(sp[1].trim());
					curItem.addClass(Label.Stationary, "" + arg0, "" + arg1);
				} else if (key.equals("Glass")) {
					int glassValue = Integer.parseInt(value);
					curItem.addClass(Label.Raw_Glass, "" + glassValue, null);
				} else if (key.equals("Weapon"))
					curItem.setWeapon(value);
				else if (key.equals("Origin"))
					curItem.setOrigin(value);
				else if (key.equals("Class")) {
					String[] sp = value.split(";");
					for (String rawClass : sp)
						curItem.addRawClass(rawClass);
				} else
					System.err.println(lineNumber + " Unknown line '" + line + "'");
			} else
				throw new ParserException("Malformed wiki level structure in items");
		} catch (ParserException e) {
			System.err.println(lineNumber + " " + e.getMessage());
		} catch (NumberFormatException e) {
			System.err.println(lineNumber + " NumberFormat: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.err.println(lineNumber + " " + e.getMessage());
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println(lineNumber + " Array index out of bounds. Index: " + e.getMessage());
		}
	}

	private Skill[] ancestors = new Skill[10];
	private Skill curSkill;

	private void parseSkillLine(int lineNumber, int wLevel, String line) {
		try {
			if (line.startsWith("Tool:")) {
				String toolName = line.substring(5).trim();
				curSkill.tool = Util.tool.get(toolName);
			} else {
				int depth = wLevel - 1;
				Skill parent = null;
				if (depth > 0)
					parent = ancestors[depth - 1];
				curSkill = new Skill(line, parent);
				ancestors[depth] = curSkill;
			}
		} catch (ParserException e) {
			System.err.println(lineNumber + " " + e.getMessage());
		}
	}

	private Research curResearch;

	private void parseResearchLine(int lineNumber, int wLevel, String line) {
		try {
			if (wLevel == 1)
				curResearch = new Research(line);
			else if (wLevel == 2)
				curResearch.addRecipe(line);
			else
				throw new ParserException("Malformed wiki level structure in researchs");
		} catch (ParserException e) {
			System.err.println(lineNumber + " " + e.getMessage());
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println(lineNumber + " ArrayIndexOutOfBounds, index: " + e.getMessage());
		}
	}

	private Function curFunction;

	public void parseFunction(int lineNumber, int wLevel, String line) {
		try {
			if (wLevel == 1) {
				// finalize last function
				if (curFunction != null)
					curFunction.finalize();
				String[] sp = line.split(":", 2);
				Item functionItem = Util.item.get(sp[0]);
				String functionName = sp[1].trim();
				curFunction = new Function(functionItem, functionName);
			} else if (wLevel == 2) {
				String[] split = line.split(":", 2);
				String key = split[0].trim();
				String value = split[1].trim();
				if (key.equals("Auto-Effect"))
					curFunction.autoEffect = Boolean.parseBoolean(value);
				else if (key.equals("Skill"))
					curFunction.requiredSkill = Util.skill.get(value);
				else if (key.equals("Atom"))
					curFunction.atoms = splitString(value, ",");
				else if (key.equals("Consume"))
					curFunction.consumedItems = splitStringToItems(value, ",");
				else if (key.equals("Produce"))
					curFunction.producedItems = splitStringToItems(value, ",");
				else if (key.equals("Precondition"))
					curFunction.preconditions = splitString(value, "&");
				else if (key.equals("Effect"))
					curFunction.effects = splitString(value, ",");
				else if (key.equals("Fluent-Effect"))
					curFunction.fluentEffects = splitString(value, ",");
				else if (key.equals("Effect-Description"))
					curFunction.functionDescription = value;
				else
					throw new ParserException("Unknown function property '" + key + "'");
			} else
				throw new ParserException("Malformed wiki level structure in functions");
		} catch (ParserException e) {
			System.err.println(lineNumber + " " + e.getMessage());
		}
	}

	private void parseCreatureAbility(int lineNumber, int wLevel, String line) {
		try {
			if (wLevel == 1) {
				String[] sp = line.split(" ");
				String name = sp[0];
				int paramNo = sp.length - 1;
				new CrAbility(name, paramNo);
			} else
				throw new ParserException("Malformed wiki level structure in creature ability");
		} catch (ParserException e) {
			System.err.println(lineNumber + " " + e.getMessage());
		}
	}

	private Cr curCreature;

	private void parseCreature(int lineNumber, int wLevel, String line) {
		try {
			if (wLevel == 1)
				curCreature = new Cr(line);
			else if (wLevel == 2) {
				String[] split = line.split(":", 2);
				String key = split[0].trim();
				String value = split[1].trim();
				if (key.equals("Attack")) {
					String[] sp0 = value.split(" ");
					curCreature.dmgType = sp0[0];
					String[] sp1 = sp0[1].split("-");
					curCreature.dmgMin = Integer.parseInt(sp1[0]);
					curCreature.dmgMax = Integer.parseInt(sp1[1]);
				} else if (key.equals("Move"))
					curCreature.moveType = value;
				else if (key.equals("Comment"))
					curCreature.comment = value;
				else if (key.equals("Abilities"))
					curCreature.setAbilities(value);
				else if (key.equals("Loot"))
					curCreature.setLoot(value);
				else if (key.equals("CEP"))
					curCreature.cep = Integer.parseInt(value);
				else if (key.equals("Health"))
					curCreature.health = Integer.parseInt(value);
				else
					System.err.println(lineNumber + " Unknown line during creature parsing '" + line + "'");
			} else
				throw new ParserException("Malformed wiki level structure in cretures");
		} catch (ParserException e) {
			System.err.println(lineNumber + " " + e.getMessage());
		}
	}

	private void parseCreatureTactic(int lineNumber, int wLevel, String line) {
		try {
			new CrTactic(line);
		} catch (ParserException e) {
			System.err.println(lineNumber + " " + e.getMessage());
		}
	}

	private CrGroup curCrGroup;

	private void parseCreatureGroup(int lineNumber, int wLevel, String line) {
		try {
			if (wLevel == 1)
				curCrGroup = new CrGroup(line);
			else if (wLevel == 2)
				curCrGroup.addMember(line);
			else
				throw new ParserException("Malformed wiki level structure in creature groups");
		} catch (ParserException e) {
			System.err.println(lineNumber + " " + e.getMessage());
		}
	}

	private ArrayList<String> splitString(String rawString, String regex) {
		String[] sp = rawString.split(regex);
		ArrayList<String> strings = new ArrayList<String>();
		for (String s : sp)
			strings.add(s.trim());
		return strings;
	}

	private ArrayList<Item> splitStringToItems(String rawString, String regex) throws ParserException {
		String[] sp = rawString.split(regex);
		ArrayList<Item> items = new ArrayList<Item>();
		for (String s : sp)
			items.add(Util.item.get(s.trim()));
		return items;
	}
}
