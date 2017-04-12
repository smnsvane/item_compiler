package sql;

import parse.Printer;

public abstract class SQLObject {
	public int id;
    public final String name;
    
    public SQLObject(String name) {
        this.name = name;
    }
    
    public abstract String toSQL();
    
    public String toString() {
        return name;
    }
    
    public String sqlId() {
        if (Printer.mode == Printer.PrintMode.pretty)
            return name;
        return Integer.toString(id);
    }
    
    public String sqlId(SQLObject obj) {
        if (obj == null)
            return "null";
        return obj.sqlId();
    }
    
    public String toSQL(String str) {
        if (str == null)
            return "null";
        return "'"+str+"'";
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof SQLObject))
            return false;
        SQLObject other = (SQLObject) o;
        return name.equals(other.name);
    }
    public int hashCode() {
        return name.hashCode();
    }
}
