package sql;

import parse.ParserException;
import parse.Util;

public class CrTactic extends SQLObject {
    public CrTactic(String name) throws ParserException {
        super(name);
        Util.crTactic.register(this);
    }
    public String toSQL() {
        return "("+id+", '"+name+"'),";
    }
}
