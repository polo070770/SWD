import controllers.ServerDomino;
import net.DominoLayer.Id;
import models.Side;

public class Test2 {

	/**
	 * @param args
	 */
	
	ServerDomino domino; 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test2 test2 = new Test2(); 

	}
	public Test2(){
		//domino = new ServerDomino();
		test();
	}
	

	public void test(){
		Id id = Id.INIT;
		int init = 20;
		if( Id.validId(init)) System.out.println("id ok");
		if(Id.HELLO.getVal() == 10){
			System.out.println("HELLO ok");
		}
		if(Side.validSide('R') && Side.validSide("R")){
			 System.out.println("side ok");
		}
		
	}
	

}
