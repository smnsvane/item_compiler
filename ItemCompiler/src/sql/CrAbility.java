package sql;

import parse.ParserException;
import parse.Util;

public class CrAbility extends SQLObject {
    public int paramNo;
    public CrAbility(String name, int paramNo) throws ParserException {
        super(name);
        Util.crAbility.register(this);
        this.paramNo = paramNo;
    }

    public String toSQL() {
        return "("+id+", '"+name+"'),";
    }
}
