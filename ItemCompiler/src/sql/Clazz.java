package sql;

import helper.Label;
import parse.ParserException;
import parse.Util;

public class Clazz extends SQLObject {
    public final Label label;
    public Clazz(Label label) throws ParserException {
        super(label.name());
        this.label = label;
        Util.itemClass.register(this);
    }
    public String toSQL() {
        return "("+id+", "+label+")";
    }
}
