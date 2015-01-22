import java.util.Arrays;
import java.util.Comparator;


public class Utils { 
	public static int N = 3;
	public static int MaxAllowed = 2;
	
	public static int compareId(Identifier arg1, Identifier arg2){
		for(int i=0; i<3; i++){
			if(arg1.nodeState[i] < arg2.nodeState[i]) return -1;
			else if(arg1.nodeState[i] > arg2.nodeState[i]) return 1;
		}
		return 0;
	}
	
	public static Comparator<GraphNode> compareNodeState =  new Comparator<GraphNode>() {

		@Override
		public int compare(GraphNode o1, GraphNode o2) {
			return compareId(o1.id, o2.id);
		}
	};
	
	//Comparator to compare based on f value
	public static Comparator<GraphNode> comGraphNodeFVal = new Comparator<GraphNode>() {
		
		@Override
		public int compare(GraphNode o1, GraphNode o2) {
			int compareo12 = compareId(o1.id, o2.id);
			if(compareo12 == 0) return 0;
			if(o1.fVal == o2.fVal) {
				return compareo12;
			}
			else if(o1.fVal < o2.fVal) return -1;
			else return 1;
		}
		
	};
	
	public static void generateNeighborNodes(GraphNode g){
		g.neighborNodes.clear();
		g.neighborEdgeCosts.clear();
		//missionaries cannot be out numbered 
		//atmost 2 people can ride a boat
		//boat is on left
		//MM, MC, CC, M, C can ride a boat
		int numM, numC;
		if(g.id.nodeState[2] == 0){
			numM = g.id.nodeState[0];
			numC = g.id.nodeState[1];
		}else{
			numM = N-g.id.nodeState[0];
			numC = N-g.id.nodeState[1];
		}
		
		for(int i=0; i<=MaxAllowed;i++){
			for(int j=0; j<= (MaxAllowed-i); j++){
				if (i==0 && j==0) continue;
				if(numM >= i && numC >= j) generateNeighborNodeHelper(g, i, j);
			}
		}
		/*
		//MM
		if(numM >= 2) generateNeighborNodeHelper(g, 2, 0);
		//MC
		if(numM >= 1 && numC>=1) generateNeighborNodeHelper(g, 1, 1);
		
		//CC
		if(numC>=2) generateNeighborNodeHelper(g, 0, 2);
		
		//M
		if(numM >= 1) generateNeighborNodeHelper(g, 1, 0);
		
		//C
		if(numC>=1) generateNeighborNodeHelper(g, 0, 1);
		*/
	}
	
	public static void generateNeighborNodeHelper(GraphNode gnode, int M, int C){
		int newBoatPos;
		if(gnode.id.nodeState[2] == 0){
			newBoatPos = 1;
		}else{
			newBoatPos = 0;
			M*=-1;
			C*=-1;
		}
		int[] newState = new int[3];
		newState[0]=gnode.id.nodeState[0]-M;
		newState[1]=gnode.id.nodeState[1]-C;
		newState[2]=newBoatPos;
		if(gnode.parentId == null || !Arrays.equals(newState, gnode.parentId.nodeState)){
			if(newState[0]==newState[1] || newState[0]==0 || newState[0]==N){
				Identifier newgid = new Identifier();
				newgid.nodeState = newState;
				gnode.neighborNodes.add(newgid);
				gnode.neighborEdgeCosts.add((float)1.0);
			}
		}
	}
	
	public static void printIdentifier(Identifier id){
		System.out.print("<"+id.nodeState[0]+","+id.nodeState[1]+","+id.nodeState[2]+">");
		//System.out.println();
	}
	
	public static void printNodeDetails(GraphNode gnode){
		System.out.print(" {");
		printIdentifier(gnode.id);
		//System.out.print(","+zeroPos[0]+zeroPos[1]+",");
		System.out.print(","+gnode.fVal+","+gnode.gVal+","+gnode.expanded+",");
		//System.out.print(" ");
		if(gnode.parentId != null) printIdentifier(gnode.parentId);
		//System.out.print("} ");
		System.out.print(",{");
		
		for (int i = 0; i <gnode.nodeDegree(); i++) {
			printIdentifier(gnode.neighborNodes.get(i));
			System.out.print(",");
		}
		System.out.print("}");
		System.out.println("} ");
	}
}
