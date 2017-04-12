package sql;


import java.util.ArrayList;

import parse.ParserException;
import parse.Util;

public class Research extends SQLObject {
    public final Item researchedItem;
    public final ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    public final Skill skill;
    
    public void addRecipe(String recipeName) throws ParserException {
        Recipe recipe = new Recipe(recipeName);
        recipes.add(recipe);
        new ResearchToRecipe(this, recipe);
    }
    
    public Research(String rawResearch) throws ParserException {
        super(rawResearch);
        
        String[] sp = rawResearch.split(":", 2);
        
        skill = Util.skill.get(sp[0]);
        
        String researchedItemName = sp[1].trim();
        if (researchedItemName.isEmpty())
            researchedItem = null;
        else {
            researchedItem = Util.item.get(sp[1].trim());
            researchedItem.researchs.add(this);
        }
        Util.research.register(this);
    }
    
    public String toSQL() {
        return "("+id+", "+skill.sqlId()+", "+sqlId(researchedItem)+"),";
    }
}
