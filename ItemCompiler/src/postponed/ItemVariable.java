package postponed;

import parse.ParserException;
import sql.Item;
import sql.SQLObject;

public class ItemVariable extends SQLObject {
	public Item item;
	public String key; // not null
	public String defaultValue; // not null
	public String type;

	public ItemVariable(Item item, String key, String defaultValue, String type) throws ParserException {
		super(item.name + "." + key);
//		Util.itemVariable.register(this);
		this.item = item;
		this.key = key;
		this.defaultValue = defaultValue;
		if (!type.equals("boolean") && !type.equals("integer")) {
			this.type = "";
			throw new ParserException("Variable type must be 'boolean' or 'integer', type was '"+type+"'");
		} else {
			this.type = type;
		}
	}

	public String toSQL() {
		return "(" + item.sqlId() + ", '" + key + "', '" + defaultValue + "', '" + type + "'),";
	}
}
