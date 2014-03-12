package models;

public class DomError {
	private int errNum;
	private String desc;
	
	
	public DomError(int errNum, String desc){
		this.errNum = errNum;
		this.desc = desc;
	}
	
	public DomError(int errNum, char[] desc){
		this.errNum = errNum;
		this.desc = String.valueOf(desc);
	}
	
	public int getErrNum(){
		return this.errNum;
	}

	public String getDesc(){
		return this.desc;
	}
	
	public char[] getCharDesc(){
		return desc.toCharArray();
	}
	public int getDescLength(){
		return this.desc.length();
	}
	
	public String getRepresentation(){
		return "" + this.errNum + ": " +this.desc;
	}
	
}
