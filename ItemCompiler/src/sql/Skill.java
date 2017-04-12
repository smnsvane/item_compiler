package sql;

import parse.ParserException;
import parse.Util;

public class Skill extends SQLObject {
    public Skill parent;
    public Tool tool;

    public Skill(String name, Skill parent) throws ParserException {
        super(name);
        Util.skill.register(this);
        this.parent = parent;
    }

    public String toSQL() {
        return "(" + id + ", '" + name + "', " + sqlId(parent) + ", " + sqlId(tool) + "),";
    }
}
