package sql;

import parse.ParserException;
import parse.Util;

public class CrGroupMember extends SQLObject {
    public int amountMin, amountMax;
    public CrTactic tactic;
    public CrGroup group;
    public Cr creature;
    public CrGroupMember(String creatureName, CrGroup group, int amountMin, int amountMax, String tacticName) throws ParserException {
        super(group.name+"."+creatureName);
        tactic = Util.crTactic.get(tacticName);
        creature = Util.cr.get(creatureName);
        this.amountMin = amountMin;
        this.amountMax = amountMax;
        this.group = group;
        Util.crGroupMember.register(this);
    }

    public String toSQL() {
        return "("+group.sqlId()+", "+creature.sqlId()+", "+tactic.sqlId()+", "+amountMin+", "+amountMax+"),";
    }
}
