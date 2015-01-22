import java.util.Arrays;


public class Identifier {
	//#missionaries on left, #cannibals on left, position of boat: 0 means left, 1 means right
	public int[] nodeState =  new int[3];
	
	@Override
	public boolean equals(Object obj) {
		return Arrays.equals(nodeState, ((Identifier)obj).nodeState);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(nodeState);
	}
	
	public static void main(String args[]){
		
	}
}
