package sql;

import java.io.Serializable;
import java.util.ArrayList;

import helper.DamageType;
import helper.Label;
import helper.Origin;
import helper.WeaponType;
import helper.Wear;
import parse.ParserException;
import parse.Util;

public class Item extends SQLObject implements Serializable {

	private static final long serialVersionUID = 1L;

	public final ArrayList<Research> researchs = new ArrayList<Research>();
    public final ArrayList<ItemToClass> classes = new ArrayList<ItemToClass>();
    public final ArrayList<Recipe> recipes = new ArrayList<Recipe>();

    public void addClass(Label label, String arg0, String arg1) throws ParserException {
        classes.add(new ItemToClass(this, label, arg0, arg1));
    }

    public boolean is(Label label) {
        for (ItemToClass itic : classes)
            if (itic.clazz.label.equals(label))
                return true;
        return false;
    }
    public ItemToClass get(Label label) {
        for (ItemToClass itic : classes)
            if (itic.clazz.label.equals(label))
                return itic;
        return null;
    }

    public void addRawClass(String rawClass) throws ParserException {
        String[] sp = rawClass.split(",");
        String classLabelName, arg0 = null, arg1 = null;
        switch (sp.length) {
        case 3:
            arg1 = sp[2].trim();
        case 2:
            arg0 = sp[1].trim();
        case 1:
            classLabelName = sp[0].trim();
            break;
        default:
            throw new ParserException("Malformed item class");
        }
        classes.add(new ItemToClass(this, Label.valueOf(classLabelName), arg0, arg1));
    }

    public WeaponType launcher;

    public void setAmmunition(String rawAmmoString) throws ParserException {
        String[] sp = rawAmmoString.split(",", 2);
        launcher = WeaponType.valueOf(sp[0].trim());
        int ammoBonus = Integer.parseInt(sp[1].trim());
        addClass(Label.Ammunition, launcher.toString(), "" + ammoBonus);
    }

    public final ArrayList<Origin> origins = new ArrayList<Origin>();

    public void setOrigin(String rawString) throws ParserException {
        try {
            origins.clear();
            String[] originSplit = rawString.split(",");
            for (String originStr : originSplit)
                origins.add(Origin.valueOf(originStr.trim()));
        } catch (IllegalArgumentException e) {
            throw new ParserException("Unknown item origin value '" + rawString + "'");
        }
    }

    public Weapon weapon;

    public void setWeapon(String weaponString) throws ParserException {
        String[] sp = weaponString.split("-", 6);
        if (sp.length != 6)
            throw new ParserException("Malformed weapon string");
        int index = 0;
        WeaponType type = WeaponType.valueOf(sp[index++]);
        Wear wear = Wear.valueOf(sp[index++]);
        DamageType dmgType = DamageType.valueOf(sp[index++]);
        int range = Integer.parseInt(sp[index++]);
        int minDmg = Integer.parseInt(sp[index++]);
        int maxDmg = Integer.parseInt(sp[index++]);
        weapon = new Weapon(type, dmgType, range, minDmg, maxDmg);
        addClass(Label.Wear, wear.toString(), null);
        addClass(Label.Weapon, null, null);
    }

    public void setTool(String rawToolString) throws ParserException {
        String[] sp = rawToolString.split(",", 2);
        if (sp.length != 2)
            throw new ParserException("Malformed tool string");

        String toolName = sp[0].trim();
        Tool tool = new Tool(toolName);

        int speed = Integer.parseInt(sp[1].trim());

        addClass(Label.Tool, tool.sqlId(), "" + speed);
    }

    public Item(String name) throws ParserException {
        super(name);
        Util.item.register(this);
        origins.add(Origin.Recipe_Product);
    }

    public static final Item rawMetal = new Item();

    private Item() {
        super("Raw Metal");
        id = 0;
    }

    public String toSQL() {
        String prefix = "(" + id + ", '" + name + "'";
        if (weapon != null)
            return prefix + ", '" + weapon.weaponType + "', '" + weapon.damageType + "', " + weapon.range + ", " + weapon.minDamage + ", " + weapon.maxDamage + "),";
        return prefix + ", null, null, null, null, null),";
    }
}
