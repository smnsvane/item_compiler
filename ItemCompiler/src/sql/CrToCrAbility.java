package sql;

import parse.ParserException;
import parse.Util;

public class CrToCrAbility extends SQLObject {
	public final Cr cr;
	public final CrAbility crAbility;
	public final int x;
	public CrToCrAbility(Cr cr, CrAbility crAbility, int x) throws ParserException {
		super(cr.name+"."+crAbility.name);
        Util.crToCrAbility.register(this);
		this.cr = cr;
		this.crAbility = crAbility;
		this.x = x;
	}

	@Override
	public String toSQL() {
		return "("+cr.sqlId()+", "+crAbility.sqlId()+", "+x+")";
	}
}
