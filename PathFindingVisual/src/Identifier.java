import java.util.Arrays;


public class Identifier {
	//x-coordinate, y-coordinate
	public int[] nodeState =  new int[2];
	
	@Override
	public boolean equals(Object obj) {
		return Arrays.equals(nodeState, ((Identifier)obj).nodeState);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(nodeState);
	}
}
