package postponed;

import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parse.ParserException;
import sql.Item;
import sql.SQLObject;
import sql.Skill;

public class Function extends SQLObject {
	public final Item functionItem;
	public boolean autoEffect;
	public Skill requiredSkill;
	public ArrayList<String> atoms = new ArrayList<String>();
	public ArrayList<Item> toolItems = new ArrayList<Item>();
	public ArrayList<Item> consumedItems = new ArrayList<Item>();
	public ArrayList<Item> producedItems = new ArrayList<Item>();
	public ArrayList<String> preconditions = new ArrayList<String>();
	public ArrayList<String> effects = new ArrayList<String>();
	public ArrayList<String> fluentEffects = new ArrayList<String>();
	public String functionDescription;

	public void finalize() {
		atoms.add(functionItem.name);
		preconditions.add(functionItem.name.toLowerCase()+"("+functionItem.name+")");
		preconditions.add("at("+functionItem.name+")");
		for (Item i : toolItems)
			preconditions.add("0 < inventory(" + i + ")");
		for (Item i : consumedItems) {
			preconditions.add("0 < inventory(" + i + ")");
			effects.add("inventory(" + i + ") <- inventory(" + i + ") - 1");
		}
		for (Item i : producedItems) {
			effects.add("inventory(" + i + ") <- inventory(" + i + ") + 1");
		}
	}

	public ArrayList<String> getPredicates() {
		ArrayList<String> matches = new ArrayList<String>();
		Pattern p = Pattern.compile("([a-z_]+?)\\(");
		for (String pre : preconditions) {
			Matcher m = p.matcher(pre);
			while (m.find())
				matches.add(m.group(1));
		}
		return matches;
	}

	public Function(Item functionItem, String functionName) throws ParserException {
		super(functionItem + "." + functionName);
		this.functionItem = functionItem;
		//       functionItem.functions.add(this);
		//        Util.function.register(this);
	}

	public String toSQL() {
		return "IMPLEMENT !!!";/* TODO "(" + id + ", '" + name + "', " + functionItem.sqlId() + ", " + autoEffect + ", " + sqlId(requiredSkill) + ", " + sqlId(consumedItem) + ", " +
            sqlId(producedItem) + ", '" + sqlEsc(precondition) + "', '" + sqlEsc(effects) + "', '" + sqlEsc(fluentEffects) + "', " +
            toSQL(functionDescription) + ")";*/
	}

	private String sqlEsc(String source) {
		return source.replaceAll("'", "''");
	}
}
