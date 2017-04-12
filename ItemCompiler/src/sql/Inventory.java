package sql;


import java.util.ArrayList;
import java.util.Iterator;

import parse.ItemStack;
import parse.ParserException;
import parse.Util;

public class Inventory extends SQLObject implements Iterable<ItemStack> {
    public ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();
    public void addItems(String rawString) throws ParserException {
        String[] split = rawString.split("\\+");
        for (String rawItem : split)
            itemStacks.add(new ItemStack(rawItem.trim()));
    }
    public Iterator<ItemStack> iterator() {
        return itemStacks.iterator();
    }
    
    private static int counter = 1;
    public Inventory() throws ParserException {
        super(""+counter++);
        Util.inventory.register(this);
    }
    public Inventory(String rawString) throws ParserException {
        this();
        addItems(rawString);
    }
    public String toSQL() {
        StringBuilder sb = new StringBuilder();
        for (ItemStack s : itemStacks)
            sb.append("("+id+", "+s.item.sqlId()+", "+s.amount+"),");
        return sb.toString();
    }
}
