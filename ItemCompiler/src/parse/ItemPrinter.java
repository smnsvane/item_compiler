package parse;

import sql.Item;
import sql.ItemToClass;
import sql.Recipe;
import sql.Research;

public class ItemPrinter {
    public void print(int id) {
        for (Item i : Util.item)
            if (i.id == id)
                print(i);
    }

    public void print(String name) {
        try {
            print(Util.item.get(name));
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    public void print(Item i) {
        System.out.println(i.id + " " + i.name);
        System.out.println("Origin: " + i.origins);
        System.out.println(classesToString(i));
        if (!i.researchs.isEmpty())
            for (Research res : i.researchs) {
                System.out.println("Research: " + res.skill);
                for (Recipe rec : res.recipes)
                    System.out.println("\tRecipe: " + rec);
            }
        if (i.weapon != null)
            System.out.println("Weapon: Type: " + i.weapon.weaponType + ", Range: " + i.weapon.range + ", Damage: " + i.weapon.minDamage + "-" +
                               i.weapon.maxDamage + ", " + i.weapon.damageType);
    }

    private String classesToString(Item i) {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ");
        for (ItemToClass itc : i.classes) {
            sb.append(itc.name.substring(i.name.length() + 4) + "(");
            if (itc.arg0 != null)
                sb.append(itc.arg0);
            if (itc.arg1 != null)
                sb.append(", " + itc.arg1);
            sb.append("), ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}
