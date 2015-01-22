import java.util.ArrayList;
import java.util.List;


public class GraphNode {
	public Identifier id;
	public Identifier parentId;
	public boolean expanded = false;
	public float gVal;
	public float fVal;
	public float hVal;
	//In case of 8 tile problem neighbor nodes implies descendant nodes (excluding parent node)
	public List<Identifier> neighborNodes = new ArrayList<>();
	public List<Float> neighborEdgeCosts = new ArrayList<>();
	
	public GraphNode(){};
	public GraphNode(Identifier nodeid) {
		id = nodeid;
	}
	
	void addNeighborNode(Identifier neighborNode, float cost){
		neighborNodes.add(neighborNode);
		neighborEdgeCosts.add(cost);
	}
	
	int nodeDegree(){
		return neighborNodes.size();
	}
	
	void updateFVal(){
		fVal = gVal + hVal;
	}
	
	public static void main(String args[]){
		Identifier nid = new Identifier();
		int[][] nstate = new int[][]{
				{1,2,3},
				{4,9,6},
				{7,5,8}
		};
		nid.nodeState = nstate;
		nid.zeroPos = new int[]{1,1};
		GraphNode g = new GraphNode(nid);
		
		Identifier pid = new Identifier();
		int[][] pstate = new int[][]{
				{1,2,3},
				{4,5,6},
				{7,9,8}
		};
		pid.nodeState = pstate;
		pid.zeroPos = new int[]{2,1};
		g.parentId = pid;
		Utils.generateNeighborNodes(g);
		Utils.printNodeDetails(g);
	}
	
}
