package net;


public class DominoLayer {

	
	
	
	public enum Id{
		HELLO(10), 
		INIT(20),
		MOVE(11),
		PIECE(21),
		ENDGAME(22),
		ERROR(99);
		
		
		private int id;
		private Id(int id){this.id = id;}
		public int getVal(){ return id;}
		
		public static boolean validId(int otherId){
			
			for(Id id : Id.values()){
				if (id.getVal() ==  otherId) return true;
			}
			
			return false;
		}
		
	}
}
