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
		nid.nodeState = new int[]{3,3,0};
		GraphNode node = new GraphNode(nid);
		Utils.generateNeighborNodes(node);
		Utils.printNodeDetails(node);
	}
	
}
