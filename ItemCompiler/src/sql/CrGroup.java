package sql;

import java.util.ArrayList;

import parse.ParserException;
import parse.Util;



public class CrGroup extends SQLObject {
    private final ArrayList<CrGroupMember> members = new ArrayList<CrGroupMember>();

    public void addMember(String rawString) throws ParserException {
        String[] sp0 = rawString.split(",");
        String tacticName = sp0[1].trim();
        String[] sp1 = sp0[0].split(" ", 2);
        String[] amounts = sp1[0].split("-");
        int amountMin = Integer.parseInt(amounts[0].trim());
        int amountMax;
        if (amounts.length == 2)
            amountMax = Integer.parseInt(amounts[1].trim());
        else
            amountMax = amountMin;
        String creatureName = sp1[1].trim();
        members.add(new CrGroupMember(creatureName, this, amountMin, amountMax, tacticName));
    }

    public CrGroup(String name) throws ParserException {
        super(name);
        Util.crGroup.register(this);
    }

    public String toSQL() {
        return "("+id+", '"+name+"'),";
    }
}
