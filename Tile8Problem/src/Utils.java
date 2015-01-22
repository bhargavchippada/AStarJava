import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class Utils { 
	public static int[][] cloneArray(int[][] src) {
	    int length = src.length;
	    int[][] target = new int[length][src[0].length];
	    for (int i = 0; i < length; i++) {
	        System.arraycopy(src[i], 0, target[i], 0, src[i].length);
	    }
	    return target;
	}
	
	public static int compareId(Identifier arg1, Identifier arg2){
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(arg1.nodeState[i][j] < arg2.nodeState[i][j]) return -1;
				else if(arg1.nodeState[i][j] > arg2.nodeState[i][j]) return 1;
			}
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
		//if 0 is at the corners there are only 2 degrees of freedom
		//if 0 is at the edges there are only 3 degrees of freedom
		//if 0 is in the middle there are 4 degrees of freedom
		
		// first row 0 can move down
		if(g.id.zeroPos[0] == 0){
			generateNeighborNodeHelper(g, g.id.zeroPos[0] + 1, g.id.zeroPos[1]);
		}
		// first row 0 can move up
		else if(g.id.zeroPos[0] == 2){
			generateNeighborNodeHelper(g, g.id.zeroPos[0] - 1, g.id.zeroPos[1]);
		}
		//middle row 0 can move up and down
		else{
			generateNeighborNodeHelper(g, g.id.zeroPos[0] + 1, g.id.zeroPos[1]);
			generateNeighborNodeHelper(g, g.id.zeroPos[0] - 1, g.id.zeroPos[1]);
		}
		
		// first column 0 can move right
		if(g.id.zeroPos[1] == 0){
			generateNeighborNodeHelper(g, g.id.zeroPos[0], g.id.zeroPos[1]+1);
		}
		//last column 0 can move left
		else if(g.id.zeroPos[1] == 2){
			generateNeighborNodeHelper(g, g.id.zeroPos[0], g.id.zeroPos[1]-1);
		}
		//middle column 0 can move left and right
		else{
			generateNeighborNodeHelper(g, g.id.zeroPos[0], g.id.zeroPos[1]+1);
			generateNeighborNodeHelper(g, g.id.zeroPos[0], g.id.zeroPos[1]-1);
		}
	}
	
	public static void generateNeighborNodeHelper(GraphNode gnode, int row, int column){
		int[] oldZeroPos = gnode.id.zeroPos;
		int[] newZeroPos = new int[2];
		newZeroPos[0] = row;
		newZeroPos[1] = column;
		if(gnode.parentId == null || !Arrays.equals(newZeroPos, gnode.parentId.zeroPos)){
			Identifier newgid = new Identifier();
			newgid.nodeState = Utils.cloneArray(gnode.id.nodeState);
			newgid.zeroPos = newZeroPos;
			newgid.nodeState[oldZeroPos[0]][oldZeroPos[1]] = gnode.id.nodeState[newZeroPos[0]][newZeroPos[1]];
			newgid.nodeState[newZeroPos[0]][newZeroPos[1]] = 9;
			gnode.neighborNodes.add(newgid);
			gnode.neighborEdgeCosts.add((float)1.0);
		}
	}
	
	public static boolean monotonicity_check(Identifier start_id, Identifier goal_id){
		HashMap<Identifier,GraphNode> visitedNodes = new HashMap<Identifier,GraphNode>();
		Queue<Identifier> non_expanded = new LinkedList<Identifier>();
		non_expanded.add(start_id);
		int count=0;
		while (!non_expanded.isEmpty()){
			Identifier current_id = non_expanded.poll();
			GraphNode current_node = visitedNodes.get(current_id);
			if(current_node == null){
				current_node = new GraphNode(current_id);
				current_node.hVal = AStarAlgo.heuristic_estimation(current_id, goal_id);
				visitedNodes.put(current_id, current_node);
			}
			current_node.expanded = true;
			count++;
			generateNeighborNodes(current_node);
			
			for(int i=0; i<current_node.nodeDegree(); i++){
				Identifier neighbor_id = current_node.neighborNodes.get(i);
				GraphNode neighbor_node = visitedNodes.get(neighbor_id);
				if(neighbor_node == null){
					neighbor_node = new GraphNode(neighbor_id);
					neighbor_node.hVal = AStarAlgo.heuristic_estimation(neighbor_id, goal_id);
					visitedNodes.put(neighbor_id, neighbor_node);
					non_expanded.add(neighbor_id);
				}
				if((current_node.hVal)>(neighbor_node.hVal+1)) return false;
				//System.out.println((current_node.hVal)+" "+(neighbor_node.hVal));
			}
		}
		if(visitedNodes.containsKey(goal_id)){
			System.out.println(count+" "+visitedNodes.size());
			return true;
		}
		else{
			System.out.println("no path found");
			return true;
		}
	}
	
	public static boolean localmonotonicity_check(GraphNode gnode, Identifier goalId, HashMap<Identifier,GraphNode> visitedList){
		float hc;
		float c;
		float hp;
		for(int i=0; i<gnode.nodeDegree(); i++){
			Identifier neighborId = gnode.neighborNodes.get(i);
			if(visitedList.containsKey(neighborId)) hc = visitedList.get(neighborId).hVal;
			else hc = AStarAlgo.heuristic_estimation(neighborId, goalId);
			c = gnode.neighborEdgeCosts.get(i);
			hp = gnode.hVal;
			if((hc+c) < hp) {
				AStarAlgo.monotonicity = false;
				return false;
			}
			//System.out.println((hc+c)+" "+hp+" "+gnode.nodeDegree());
		}
		return true;
	}
	
	public static void printIdentifier(Identifier id){
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				System.out.print(id.nodeState[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void printNodeDetails(GraphNode gnode){
		System.out.print(" {");
		for(int i=0; i<3; i++) for(int j=0; j<3; j++) System.out.print(gnode.id.nodeState[i][j]);
		//System.out.print(","+zeroPos[0]+zeroPos[1]+",");
		System.out.print(","+gnode.fVal+","+gnode.gVal+","+gnode.expanded+",");
		//System.out.print(" ");
		if(gnode.parentId != null){
			for(int i=0; i<3; i++) for(int j=0; j<3; j++) System.out.print(gnode.parentId.nodeState[i][j]);
		}
		//System.out.print("} ");
		System.out.print("");
		/*
		for (int i = 0; i <gnode.nodeDegree(); i++) {
			printIdentifier(gnode.neighborNodes.get(i));
			System.out.print(" ");
		}
		*/
		System.out.println("} ");
	}
}
