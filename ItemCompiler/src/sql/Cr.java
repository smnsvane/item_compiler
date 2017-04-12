package sql;

import java.util.ArrayList;

import parse.ParserException;
import parse.Util;


public class Cr extends SQLObject {
    public String comment = null;

    public String dmgType = null; // '' or null is not a legal outcome
    public int dmgMin = 0;
    public int dmgMax = 0;
    public String moveType = null; // '' or null is not a legal outcome
    public Inventory loot;
    public int cep = 0; // Combat Experience Points
    public int health = 0; // zero is not a legal outcome

    public void setLoot(String rawLootString) throws ParserException {
        loot = new Inventory(rawLootString);
    }

    public ArrayList<CrToCrAbility> abilities = new ArrayList<CrToCrAbility>();

    public void setAbilities(String rawAbilityString) throws ParserException {
        String[] sp0 = rawAbilityString.split(",");
        for (String s : sp0) {
            String[] sp1 = s.trim().split(" ");
            String name = sp1[0];
            CrAbility ability = Util.crAbility.get(name);
            int x = Integer.parseInt(sp1[1]);
            abilities.add(new CrToCrAbility(this, ability, x));
        }
    }

    public Cr(String name) throws ParserException {
        super(name);
        Util.cr.register(this);
    }

    public String toSQL() {
        return "(" + id + ", '" + name + "', " + dmgMin + ", " + dmgMax + ", '" + dmgType + "', '" + moveType + "', " + cep + ", " + health + ", " + sqlId(loot) +
            ", " + toSQL(comment) + "),";
    }
}
