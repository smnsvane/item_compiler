package parse;

import java.util.Iterator;
import java.util.LinkedHashMap;

import sql.Clazz;
import sql.Cr;
import sql.CrAbility;
import sql.CrGroup;
import sql.CrGroupMember;
import sql.CrTactic;
import sql.CrToCrAbility;
import sql.Inventory;
import sql.Item;
import sql.ItemToClass;
import sql.Recipe;
import sql.Research;
import sql.ResearchToRecipe;
import sql.SQLObject;
import sql.Skill;
import sql.Tool;


public class Util<O extends SQLObject> implements Iterable<O> {
	public static final Util<Item> item = new Util<Item>("Item");
	public static final Util<Clazz> itemClass = new Util<Clazz>("Item-Class");
	public static final Util<ItemToClass> itemToitemClass = new Util<ItemToClass>("Item-to-Item-Class");
	public static final Util<Recipe> recipe = new Util<Recipe>("Recipe");
	public static final Util<Skill> skill = new Util<Skill>("Skill");
	public static final Util<Tool> tool = new Util<Tool>("Tool");
	public static final Util<Research> research = new Util<Research>("Research");
//	public static final Util<ItemVariable> itemVariable = new Util<ItemVariable>("Item-variable");
	public static final Util<Cr> cr = new Util<Cr>("Creature");
	public static final Util<CrAbility> crAbility = new Util<CrAbility>("Creature-Ability");
	public static final Util<CrToCrAbility> crToCrAbility = new Util<CrToCrAbility>("Creature-To-Creature_Ability");
	public static final Util<CrTactic> crTactic = new Util<CrTactic>("Creature-Tactic");
	public static final Util<CrGroup> crGroup = new Util<CrGroup>("Creature-Group");
	public static final Util<CrGroupMember> crGroupMember = new Util<CrGroupMember>("Creature-Group-Member");
	public static final Util<Inventory> inventory = new Util<Inventory>("Inventory");
//	public static final Util<Function> function = new Util<Function>("Function");
	public static final Util<ResearchToRecipe> researchToRecipe = new Util<ResearchToRecipe>("Research-To-Resipe");

	private String utilType;

	private Util(String utilObjectType) {
		utilType = utilObjectType + "-util";
	}

	private final LinkedHashMap<String, O> nameToObj = new LinkedHashMap<String, O>();

	public O get(String name) throws ParserException {
		O obj = nameToObj.get(name);
		if (obj == null)
			throw new ParserException(utilType + ": Result for key-name '" + name + "' was not found");
		return obj;
	}

	public boolean contains(String name) {
		if (name == null)
			throw new NullPointerException(utilType + ": Argument was null");
		return nameToObj.containsKey(name);
	}

	private int counter = 1;

	public void register(O obj) throws ParserException {
		if (obj == null)
			throw new NullPointerException(utilType + ": Argument was null");
		obj.id = counter++;
		if (nameToObj.containsKey(obj.name))
			throw new ParserException(utilType + ": Already contains id for " + utilType + " denoted by '" + obj.name + "' id is " +
					nameToObj.get(obj.name).id);
		nameToObj.put(obj.name, obj);
	}

	public String toSQL() {
		if (nameToObj.values().isEmpty()) {
			System.err.println(utilType + ": Contains no objects");
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if (Printer.mode == Printer.PrintMode.pretty)
			sb.append("\n");
		for (O sqlO : nameToObj.values()) {
			sb.append(sqlO.toSQL());
			if (Printer.mode == Printer.PrintMode.pretty)
				sb.append("\n");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	@Override
	public Iterator<O> iterator() {
		return nameToObj.values().iterator();
	}
}
