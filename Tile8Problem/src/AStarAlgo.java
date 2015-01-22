import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;


public class AStarAlgo {
	public static boolean monotonicity = true;
	TreeSet<GraphNode> openList = new TreeSet<GraphNode>(Utils.comGraphNodeFVal);
	HashMap<Identifier,GraphNode> visitedList = new HashMap<Identifier,GraphNode>();
	
	boolean AStarFunction(Identifier startId, Identifier goalId){
		if(!checkReachability(startId, goalId)) return false;
		GraphNode startGraphNode = new GraphNode(startId);
		startGraphNode.gVal = 0;
		startGraphNode.hVal = heuristic_estimation(startId, goalId);
		startGraphNode.updateFVal();
		openList.add(startGraphNode);
		visitedList.put(startId, startGraphNode);
		int numOfNodesExpanded = 0;
		while(!openList.isEmpty()){
			//printOpenCloseList();
			GraphNode currentGraphNode = openList.pollFirst();
			currentGraphNode.expanded = true;
			//currentGraphNode.printNodeDetails();
			
			//check whether it is goal node
			int status = checkGoalState(currentGraphNode.id, goalId);
			if( status == 0) {
				System.out.println("number of expanded nodes = " + numOfNodesExpanded);
				traceback(currentGraphNode.id, 0);
				return true;
			}else if(status == -1) return false;
			
			//generate next node states
			Utils.generateNeighborNodes(currentGraphNode);
			numOfNodesExpanded++;
			Utils.localmonotonicity_check(currentGraphNode, goalId, visitedList);
			GraphNode neighbor;
			Identifier neighborId;
			for(int i=0; i<currentGraphNode.nodeDegree(); i++){
				neighborId = currentGraphNode.neighborNodes.get(i); 
				neighbor = visitedList.get(neighborId);
				//note neighbor may not be in G.graphNodes!!

				//not existing in both open nor closed list
				if(neighbor == null) {
					neighbor = new GraphNode(neighborId);
					float current_gVal = currentGraphNode.gVal+currentGraphNode.neighborEdgeCosts.get(i);
					neighbor.gVal = current_gVal;
					neighbor.hVal = heuristic_estimation(neighborId, goalId);
					neighbor.updateFVal();
					neighbor.parentId = currentGraphNode.id;
					openList.add(neighbor);
					visitedList.put(neighborId, neighbor);
					continue;
				}
				//check neighbor existing in closed List
				else if(neighbor.expanded) {
					//continue;
					
					float current_gVal = currentGraphNode.gVal+currentGraphNode.neighborEdgeCosts.get(i);
					if(neighbor.gVal >  current_gVal){
						neighbor.expanded = false;
						neighbor.gVal = current_gVal;
						neighbor.hVal = heuristic_estimation(neighborId, goalId);
						neighbor.updateFVal();
						neighbor.parentId = currentGraphNode.id;
						openList.add(neighbor);
					}
					continue;
					
				}
				//neighbor exists in open list
				else{
					float current_gVal = currentGraphNode.gVal+currentGraphNode.neighborEdgeCosts.get(i);
					if(neighbor.gVal > current_gVal){
						openList.remove(neighbor);
						neighbor.gVal = current_gVal;
						neighbor.hVal = heuristic_estimation(neighborId, goalId);
						neighbor.updateFVal();
						neighbor.parentId = currentGraphNode.id;
						openList.add(neighbor);
					}
					continue;
				}
				
			}
			
			
		}
		
		return false;
		
	}
	
	public static float heuristic_estimation(Identifier startId, Identifier goalId){
		//return 0;
		//heuristic 1: manhattan
		
		float out=0;
		int startRow[] = new int[10];
		int startColumn[] = new int[10];
		int goalRow[] = new int[10];
		int goalColumn[] = new int[10];
		
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				startRow[startId.nodeState[i][j]] = i;
				startColumn[startId.nodeState[i][j]] = j;
				goalRow[goalId.nodeState[i][j]] = i;
				goalColumn[goalId.nodeState[i][j]] = j;
				
			}
		}
		for(int i=1; i<9; i++){
			float a = startRow[i] - goalRow[i];
			float b = startColumn[i] - goalColumn[i];
			
			if(a < 0){ out = out - a;}
			else out+=a;
			if(b < 0){ out = out - b;}
			else out+=b;
		}
		return out;
		
		
		//heuristic 2: number of displaced tiles
		/*
		float out=0;
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if (startId.nodeState[i][j] != goalId.nodeState[i][j] && startId.nodeState[i][j]!=9) out++;
			}
		}
		return out;
		*/
		
		//heuristic 3: linear conflicts heuristic
		/*
		float out=0;
		int startRow[] = new int[10];
		int startColumn[] = new int[10];
		int goalRow[] = new int[10];
		int goalColumn[] = new int[10];
		
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				startRow[startId.nodeState[i][j]] = i;
				startColumn[startId.nodeState[i][j]] = j;
				goalRow[goalId.nodeState[i][j]] = i;
				goalColumn[goalId.nodeState[i][j]] = j;
				
			}
		}
		for(int i=1; i<9; i++){
			float a = startRow[i] - goalRow[i];
			float b = startColumn[i] - goalColumn[i];
			
			if(a < 0){ out = out - a;}
			else out+=a;
			if(b < 0){ out = out - b;}
			else out+=b;
		}
		
		for(int i=0; i<3; i++){
			for(int j=0; j<2; j++){
				for(int k=j+1; k<3; k++){
					int val1 = startId.nodeState[i][j];
					int val2 = startId.nodeState[i][k];
					if(goalRow[val1] == i && goalRow[val2] == i && val1 != 9 && val2 != 9){
						if(goalColumn[val1] > goalColumn[val2]) out++; //linear conflict found
					}
				}
			}
		}
		
		return out;
		*/
		
		//heuristic 4: difference of inversion pairs; wrong heuristic probably
		/*
		int a[] = new int[8];
		int b[] = new int[8];
		int count_a = 0;
		int count_b = 0;
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(startId.nodeState[i][j] != 9) {
					a[count_a] = startId.nodeState[i][j];
					count_a++;
				}
				if(goalId.nodeState[i][j] != 9){
					b[count_b] = goalId.nodeState[i][j];
					count_b++;
				}
			}
		}
		int inv_count_a = 0;
		int inv_count_b = 0;
		int n=8;
		for(int i = 0; i < n - 1; i++)
		  for(int j = i+1; j < n; j++){
			  if(a[i] > a[j]) inv_count_a++;
			  if(b[i] > b[j]) inv_count_b++;
		  }		    
		
		return  Math.abs(inv_count_a - inv_count_b);
		*/
	}
	
	boolean checkReachability(Identifier startId, Identifier goalId){
		int a[] = new int[8];
		int b[] = new int[8];
		int count_a = 0;
		int count_b = 0;
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(startId.nodeState[i][j] != 9) {
					a[count_a] = startId.nodeState[i][j];
					count_a++;
				}
				if(goalId.nodeState[i][j] != 9){
					b[count_b] = goalId.nodeState[i][j];
					count_b++;
				}
			}
		}
		int inv_count_a = 0;
		int inv_count_b = 0;
		int n=8;
		for(int i = 0; i < n - 1; i++)
		  for(int j = i+1; j < n; j++){
			  if(a[i] > a[j]) inv_count_a++;
			  if(b[i] > b[j]) inv_count_b++;
		  }		    
		
		if((inv_count_a%2) == (inv_count_b%2)) return true;
		return false;
	}
	
	
	//1 means it's not goal state; 0 means goal state found; -1 means goal state is unreachable;
	int checkGoalState(Identifier startId, Identifier goalId){
		if(Utils.compareId(startId, goalId)==0) return 0;
		return 1;
		/*
		int a[] = new int[2];
		int pos[][] = new int[2][2];
		int count =0;
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(startId.nodeState[i][j] != goalId.nodeState[i][j]) {
					if(count<2) {
						a[count] = startId.nodeState[i][j];
						pos[count][0] = i;
						pos[count][1] = j;
					}
					count++;
				}
			}
		}
		int out;
		if(count == 0) out = 1;
		else if(count == 2) {
			if(a[0] != 9 && a[1] != 9) {
				if ((Math.abs(pos[0][0]-pos[0][1])+Math.abs(pos[1][0]-pos[1][1])<=2)){
					out = -1;
				}else{
					out = 0;
				}
			}
			else out = 0;
		}
		else out = 0;
		
		return out;
		*/
	}
	
	void traceback(Identifier gId, int count){
		if(gId == null) {
			System.out.println(count-1);
			return;
		}
		Utils.printIdentifier(gId);
		GraphNode gNode = visitedList.get(gId);
		traceback(gNode.parentId, count+1);
	}
	
	void printOpenCloseList(){
		System.out.println("Lists:");
		/*
		Iterator<GraphNode> it = openList.iterator();
		while (it.hasNext()) {
			GraphNode g = it.next();
			Utils.printNodeDetails(g);
		}
		System.out.println();
		*/
		Iterator it = visitedList.entrySet().iterator();
		GraphNode g;
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        g = (GraphNode) pairs.getValue();
	        Utils.printNodeDetails(g);
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public static void main(String args[]){
		long startTime = System.currentTimeMillis();
		
		Identifier nid = new Identifier();
		
		int[][] nstate = new int[][]{
				{8,6,7},
				{2,5,4},
				{3,9,1}
		};
		/*
		int[][] nstate = new int[][]{
				{8,7,6},
				{1,9,5},
				{2,3,4}
		};
		
		int[][] nstate = new int[][]{
				{9,8,7},
				{6,5,4},
				{3,2,1}
		};
		
		*/
		nid.nodeState = nstate;
		nid.zeroPos = new int[]{2,1};
		/*
		Identifier gid = new Identifier();
		int[][] gstate = new int[][]{
				{1,2,3},
				{8,9,4},
				{7,6,5}
		};
		gid.nodeState = gstate;
		gid.zeroPos = new int[]{1,1};
		*/
		Identifier gid = new Identifier();
		int[][] gstate = new int[][]{
				{1,2,3},
				{4,5,6},
				{7,8,9}
		};
		gid.nodeState = gstate;
		gid.zeroPos = new int[]{2,2};
		/*
		Identifier pid = new Identifier();
		int[][] pstate = new int[][]{
				{1,2,3},
				{4,5,6},
				{7,0,8}
		};
		pid.nodeState = pstate;
		pid.zeroPos = new int[]{2,1};
		
		g.parentId = pid;
		*/
		
		AStarAlgo astar = new AStarAlgo();
		System.out.println(astar.AStarFunction(nid, gid));
		System.out.println("monotonicity: "+AStarAlgo.monotonicity);
		//System.out.print(astar.heuristic_estimation(nid, gid));
		/*
		boolean mon = Utils.monotonicity_check(nid, gid);
		System.out.println(mon);
		*/
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime+"ms");
	}
	
}
