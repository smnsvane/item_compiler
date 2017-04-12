package parse;

import sql.Item;

public class ItemStack {
    public Item item;
    public int amount;
	
	/**
	 * Constuct by parsing
	 */
    public ItemStack(String rawItem) throws ParserException {
        String[] sp = rawItem.split(" ", 2);

        if (sp[0].matches("[0-9]+x")) {
            amount = Integer.parseInt(sp[0].substring(0, sp[0].length() - 1));
            rawItem = sp[1];
        } else
            amount = 1;
        if (rawItem.equals("Raw Metal"))
            item = Item.rawMetal;
        else
            item = Util.item.get(rawItem);
    }
}
