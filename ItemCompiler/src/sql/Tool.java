package sql;

import parse.ParserException;
import parse.Util;

public class Tool extends SQLObject {
    public Tool(String name) throws ParserException {
        super(name);
        if (!Util.tool.contains(name))
            Util.tool.register(this);
    }
    public String toSQL() {
        return "("+id+", '"+name+"')";
    }
}
