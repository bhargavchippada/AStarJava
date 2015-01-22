import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;


public class AStarAlgo{
	
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
			//for graphics
			CellPane expandingCell = GridVisual.cells[currentGraphNode.id.nodeState[0]][currentGraphNode.id.nodeState[1]];
			expandingCell.setCellBackgroundColor(Color.PINK);
			expandingCell.revalidate();
			//System.out.println("hello");
			//currentGraphNode.printNodeDetails();
			
			//check whether it is goal node
			int status = checkGoalState(currentGraphNode.id, goalId);
			if( status == 0) {
				System.out.println("number of expanded nodes = " + numOfNodesExpanded);
				traceback(currentGraphNode.id, 0);
				System.out.println("Path Cost = " + currentGraphNode.fVal);
				pathNodes(currentGraphNode.id, new TreeSet<Identifier>(Utils.compareIdentifiers));
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
					
					//for graphics
					CellPane neighborCell = GridVisual.cells[neighborId.nodeState[0]][neighborId.nodeState[1]];
					neighborCell.setCellBackgroundColor(Color.YELLOW);
					neighborCell.revalidate();
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
						
						//for graphics
						CellPane neighborCell = GridVisual.cells[neighborId.nodeState[0]][neighborId.nodeState[1]];
						neighborCell.setCellBackgroundColor(Color.YELLOW);
						neighborCell.revalidate();
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
		//manhattan distance
		//manhattan heurisitc gives out not just the path with least cost but also least steps
		int rdiff = Math.abs(startId.nodeState[0]-goalId.nodeState[0]);
		int cdiff = Math.abs(startId.nodeState[1]-goalId.nodeState[1]);
		return (rdiff+cdiff);
		
	}
	
	boolean checkReachability(Identifier startId, Identifier goalId){
		return true;
	}
	
	
	//1 means it's not goal state; 0 means goal state found; -1 means goal state is unreachable;
	int checkGoalState(Identifier startId, Identifier goalId){
		if(Utils.compareId(startId, goalId)==0) return 0;
		return 1;
	}
	
	void traceback(Identifier gId, int count){
		if(gId == null) {
			System.out.println("No of steps: "+(count-1));
			return;
		}
		Utils.printIdentifier(gId);
		System.out.println();
		GraphNode gNode = visitedList.get(gId);
		traceback(gNode.parentId, count+1);
	}
	
	void pathNodes(Identifier gId, TreeSet<Identifier> path){
		if(gId == null) {
			for(int r=0;r<Utils.rows;r++){
				for(int c=0; c<Utils.columns; c++){
					int cost = Utils.costMap[r][c];
					Identifier temp = new Identifier();
					temp.nodeState[0] = r;
					temp.nodeState[1] = c;
					if(cost==-1) System.out.print("*\t");
					else if(path.contains(temp)) {
						System.out.print("#"+Utils.costMap[r][c]+"\t");
						
						//for graphics
						CellPane neighborCell = GridVisual.cells[temp.nodeState[0]][temp.nodeState[1]];
						neighborCell.setCellBackgroundColor(Color.CYAN);
						neighborCell.revalidate();
					}
					else System.out.print(Utils.costMap[r][c]+"\t");
				}
				System.out.println();
			}
			return;
		}
		GraphNode gNode = visitedList.get(gId);
		path.add(gNode.id);
		pathNodes(gNode.parentId, path);
	}
	
	void printOpenCloseList(){
		System.out.println("Lists:");
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
		/*
		long startTime = System.currentTimeMillis();
		File file = new File(System.getProperty("user.dir")+"/"+"input.txt");
		try {
			Scanner scanner = new Scanner(file);
			Utils.rows = Integer.parseInt(scanner.next());
			Utils.columns = Integer.parseInt(scanner.next());
			Utils.initCostMap();
			for(int r=0; r<Utils.rows; r++){
				for(int c=0; c<Utils.columns; c++){
					String inp = scanner.next();
					if(inp.equals("*")){
						Utils.costMap[r][c]=-1;
					}else{
						Utils.costMap[r][c]=Integer.parseInt(inp);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Doesn't exist!");
			e.printStackTrace();
		}
		
		Identifier nid = new Identifier();
		nid.nodeState[0] = 2;
		nid.nodeState[1] = 0;
		
		Identifier gid = new Identifier();
		gid.nodeState[0] = 2;
		gid.nodeState[1] = 5;
		
		AStarAlgo astar = new AStarAlgo();
		boolean b = astar.AStarFunction(nid, gid);
		System.out.println(b);
		long stopTime = System.currentTimeMillis();
	    long elapsedTime = stopTime - startTime;
	    System.out.println(elapsedTime+"ms");
	    */
	    /*
	    Identifier pid = new Identifier();
		pid.nodeState[0] = 2;
		pid.nodeState[1] = 1;
	    GraphNode g = new GraphNode(nid);
	    g.parentId = pid;
	    Utils.printNodeDetails(g);
	    */
	}
	
}
