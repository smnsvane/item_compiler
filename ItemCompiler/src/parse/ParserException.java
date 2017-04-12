package parse;

public class ParserException extends Exception {
    private static final long serialVersionUID = 1L;
	
	public ParserException(String msg) {
        super(msg);
    }
    public ParserException(int lineNumber, String msg) {
        super("Line: "+lineNumber+" ERROR: "+msg);
    }
}
