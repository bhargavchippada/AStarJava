import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;


public class AStarAlgo {
	
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
	
	float heuristic_estimation(Identifier startId, Identifier goalId){
		//return 0;
		
		if(startId.nodeState[2]==0){
			return (startId.nodeState[0]+startId.nodeState[1]-1);
		}else{
			return (startId.nodeState[0]+startId.nodeState[1]);
		}
		
	}
	
	boolean checkReachability(Identifier startId, Identifier goalId){
		int N = Utils.N;
		if (startId.nodeState[0] < startId.nodeState[1] && startId.nodeState[0]!=0) return false;
		if ((N-startId.nodeState[0]) < (N-startId.nodeState[1]) && (N-startId.nodeState[0])!=0) return false;
		return true;
	}
	
	
	//0 means it's not goal state; 1 means goal state found; -1 means goal state is unreachable;
	int checkGoalState(Identifier startId, Identifier goalId){
		if(Utils.compareId(startId, goalId)==0) return 0;
		return 1;
	}
	
	void traceback(Identifier gId, int count){
		if(gId == null) {
			System.out.println(count-1);
			return;
		}
		Utils.printIdentifier(gId);
		System.out.println();
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
		Utils.N = 3;
		Utils.MaxAllowed = 2;
		int[] arr = new int[3];
		arr[0] = Utils.N;
		arr[1] = Utils.N;
		arr[2] = 0;
		nid.nodeState = arr;
		
		Identifier gid = new Identifier();
		gid.nodeState = new int[]{0,0,1};
		
		AStarAlgo astar = new AStarAlgo();
		boolean b = astar.AStarFunction(nid, gid);
		System.out.println(b);
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime+"ms");
	}
	
}
