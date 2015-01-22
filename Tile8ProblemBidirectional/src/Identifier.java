import java.util.Arrays;
import java.util.HashMap;


public class Identifier {
	public int[][] nodeState =  new int[3][3];
	public int[] zeroPos = new int[2];
	
	@Override
	public boolean equals(Object obj) {
		return Arrays.deepEquals(nodeState, ((Identifier)obj).nodeState);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		/*
		String hash="";
		for(int i=0;i<3;i++) for(int j=0;j<3;j++) hash+=nodeState[i][j];
		return hash.hashCode();
		*/
		return Arrays.deepHashCode(nodeState);
	}
	
	public static void main(String[] args){
		HashMap<Identifier, GraphNode> hm = new HashMap<Identifier, GraphNode>();
		Identifier nid = new Identifier();
		int[][] nstate = new int[][]{
				{9,8,7},
				{6,5,4},
				{3,2,1}
		};
		nid.nodeState = nstate;
		nid.zeroPos = new int[]{0,0};
		
		Identifier gid = new Identifier();
		int[][] gstate = new int[][]{
				{1,2,3},
				{4,5,6},
				{7,8,9}
		};
		gid.nodeState = gstate;
		gid.zeroPos = new int[]{2,2};
		
		GraphNode n1 = new GraphNode(nid);
		GraphNode n2 = new GraphNode(gid);
		hm.put(nid, n1);
		hm.put(gid, n2);
		Identifier tid = new Identifier();
		int[][] tstate = new int[][]{
				{9,8,7},
				{6,5,4},
				{3,2,1}
		};
		tid.nodeState = tstate;
		tid.zeroPos = new int[]{0,0};
		GraphNode test = hm.get(tid);
		if(test == null) System.out.println("null");
		System.out.println(nid.equals(tid));
		//Utils.printIdentifier(test.id);
	}
}
