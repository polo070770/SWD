package models;

public enum Side {
	LEFT('L'), RIGHT('R');

	private final char repr;

	Side(char repr) {
		this.repr = Character.toUpperCase(repr);
	}

	Side(String repr) {
		this(repr.charAt(0));
	}

	public char asChar() {
		return this.repr;
	}

	public static Side fromChar(char side) {
		side = Character.toUpperCase(side);
		return side == LEFT.asChar() ? LEFT : RIGHT;
	}

	public static Side fromString(String side) {
		return fromChar(side.charAt(0));
	}

	public static boolean validSide(char side) {
		side = Character.toUpperCase(side);
		return side == LEFT.asChar() || side == RIGHT.asChar();
	}

	public static boolean validSide(String side) {
		return validSide(side.charAt(0));
	}

}
