package models.exceptions;

/**
 * Exception handler for the pile
 * @author swd
 *
 */
public class PileException extends Exception {
	private static final long serialVersionUID = 1L;
	public PileException() { super(); }
	  public PileException(String message) { super(message); }
	  public PileException(String message, Throwable cause) { super(message, cause); }
	  public PileException(Throwable cause) { super(cause); }
	}