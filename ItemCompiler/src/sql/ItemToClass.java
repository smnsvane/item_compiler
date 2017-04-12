package sql;

import helper.Label;
import parse.ParserException;
import parse.Util;


public class ItemToClass extends SQLObject {
    public final Item item;
    public final Clazz clazz;
    public final String arg0, arg1;
    
    public ItemToClass(Item item, Label label, String arg0, String arg1) throws ParserException {
        super(item+" is "+label.name());
        this.item = item;
        if (Util.itemClass.contains(label.name()))
            clazz = Util.itemClass.get(label.name());
        else
            clazz = new Clazz(label);
        this.arg0 = arg0;
        this.arg1 = arg1;
        Util.itemToitemClass.register(this);
    }

    public String toSQL() {
        return "("+item.sqlId()+", "+clazz+", "+toSQL(arg0)+", "+toSQL(arg1)+")";
    }
}
