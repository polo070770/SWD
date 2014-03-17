package models;



public class DomError {
	
	public enum Id {

		SYNTAX(1), ILLEGALACTION(2), RESOURCES(3), INTERNAL(4), UNDEFINED(5), TIMEOUT(6);

		public static Id fromInt(int num) {
			for (Id id : Id.values()) {
				if (id.getVal() == num)
					return id;
			}
			return UNDEFINED;
		}

		public static boolean validId(int otherId) {
			for (Id id : Id.values()) {
				if (id.getVal() == otherId)
					return true;
			}
			return false;
		}

		private int id;

		private Id(int id) {
			this.id = id;
		}

		public int getVal() {
			return id;
		}

		public int asInt() {
			return id;
		}

	}
	
	
	
	
	private int errNum;
	private String desc;

	public DomError(int errNum, String desc) {
		this.errNum = errNum;
		this.desc = desc;
	}

	public DomError(int errNum, char[] desc) {
		this.errNum = errNum;
		this.desc = String.valueOf(desc);

	}

	public int getErrNum() {
		return this.errNum;
	}

	public String getDesc() {
		return this.desc;
	}

	public char[] getCharDesc() {
		return desc.toCharArray();
	}

	public int getDescLength() {
		return this.desc.length();
	}

	public String getRepresentation() {
		return "" + this.errNum + ": " + this.desc;
	}

}
