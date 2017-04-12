package sql;

import parse.ItemStack;
import parse.ParserException;
import parse.Util;

public class Recipe extends SQLObject {
    public final Skill skill;
    private int rawMetal = 0;
    public final Inventory components, products;

    public Recipe(String line) throws ParserException {
        super(line);
        Util.recipe.register(this);
        
        String[] split = line.split(":", 2);
        String skillName = split[0];
        skill = Util.skill.get(skillName);
        
        String[] compProd = split[1].split("->");
        components = new Inventory(compProd[0]);
        products = new Inventory(compProd[1]);
        
        // register recipe at all item products
        for (ItemStack is : products)
            is.item.recipes.add(this);
        
        // remove raw metal from components
        for (int i = 0; i < components.itemStacks.size(); i++)
            if (components.itemStacks.get(i).item == Item.rawMetal)
                setMetal(components.itemStacks.remove(i--).amount);
    }

    public boolean requiresRawMetal() {
        return rawMetal > 0;
    }
    public int getRawMetal() {
        return rawMetal;
    }

    private void setMetal(int amount) throws ParserException {
        if (rawMetal != 0)
            throw new ParserException("Dublicate raw metal definition");
        if (amount < 1)
            throw new ParserException("Raw Metal set to '"+amount+"'");
        rawMetal = amount;
    }

    public String toSQL() {

        return "(" + id + ", " + skill.sqlId() + ", " + rawMetal + "),";
    }
}
