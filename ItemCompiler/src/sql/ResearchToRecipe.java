package sql;

import parse.ParserException;
import parse.Util;

public class ResearchToRecipe extends SQLObject {
    public final Research research;
    public final Recipe recipe;
    public ResearchToRecipe(Research research, Recipe recipe) throws ParserException {
        super(research+" -> "+recipe);
        this.research = research;
        this.recipe = recipe;
        Util.researchToRecipe.register(this);
    }

    public String toSQL() {
        return "("+research.sqlId()+", "+recipe.sqlId()+")";
    }
}
