import java.util.Comparator;
import java.util.Date;


public class Utils {
	public static int blockSize = 30;
	public static int rows = 20;
	public static int columns = 20;
	public static int costMap[][];
	
	public static void initCostMap(){
		costMap = new int[rows][columns];
		for(int r=0;r<rows;r++){
			for(int c=0; c<columns; c++){
				costMap[r][c] = 1;
			}
		}
	}
	
	public static void printCostMap(){
		for(int r=0;r<rows;r++){
			for(int c=0; c<columns; c++){
				System.out.print(costMap[r][c]+"\t");
			}
			System.out.println();
		}
	}
	
	public static int compareId(Identifier arg1, Identifier arg2){
		for(int i=0; i<2; i++){
			if(arg1.nodeState[i] < arg2.nodeState[i]) return -1;
			else if(arg1.nodeState[i] > arg2.nodeState[i]) return 1;
		}
		return 0;
	}
	
	public static Comparator<Identifier> compareIdentifiers =  new Comparator<Identifier>() {

		@Override
		public int compare(Identifier o1, Identifier o2) {
			return compareId(o1, o2);
		}
	};
	
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
		int r = g.id.nodeState[0]; //row
		int c = g.id.nodeState[1]; //column
		int temp1,temp2;
		int diag_cost;
		if((r-1)>=0){
			generateNeighborNodeHelper(g, r-1, c,0);
			if((c-1)>=0){
				temp1 = Utils.costMap[r][c-1];
				temp2 = Utils.costMap[r-1][c];
				diag_cost = (temp1>=temp2)? temp2:temp1;
				generateNeighborNodeHelper(g, r-1, c-1,diag_cost);
			}
			if((c+1)<columns){
				temp1 = Utils.costMap[r][c+1];
				temp2 = Utils.costMap[r-1][c];
				diag_cost = (temp1>=temp2)? temp2:temp1;
				generateNeighborNodeHelper(g, r-1, c+1,diag_cost);
			}
		}
		if((r+1)<rows){
				generateNeighborNodeHelper(g, r+1, c,0);
			if((c-1)>=0){
				temp1 = Utils.costMap[r][c-1];
				temp2 = Utils.costMap[r+1][c];
				diag_cost = (temp1>=temp2)? temp2:temp1;
				generateNeighborNodeHelper(g, r+1, c-1,1);
			}
			if((c+1)<columns){
				temp1 = Utils.costMap[r][c+1];
				temp2 = Utils.costMap[r+1][c];
				diag_cost = (temp1>=temp2)? temp2:temp1;
				generateNeighborNodeHelper(g, r+1, c+1,1);
			}
		}
		if((c-1)>=0){
			generateNeighborNodeHelper(g, r, c-1,0);
		}
		if((c+1)<columns){
			generateNeighborNodeHelper(g, r, c+1,0);
		}
	}
	
	public static void generateNeighborNodeHelper(GraphNode gnode, int R, int C, int add_cost){
		int cost = costMap[R][C];
		if(cost==-1) return;
		float float_cost = (float) Math.sqrt(add_cost*add_cost+cost*cost);;
		Identifier temp = new Identifier();
		temp.nodeState[0] = R;
		temp.nodeState[1] = C;
		if(gnode.parentId!=null && compareId(temp, gnode.parentId)==0) return;
		gnode.addNeighborNode(temp, float_cost);
	}
	
	public static void printIdentifier(Identifier id){
		System.out.print("<"+id.nodeState[0]+","+id.nodeState[1]+","+costMap[id.nodeState[0]][id.nodeState[1]]+">");
		//System.out.println();
	}
	
	public static void printNodeDetails(GraphNode gnode){
		System.out.print(" {");
		printIdentifier(gnode.id);
		//System.out.print(","+zeroPos[0]+zeroPos[1]+",");
		System.out.print(","+gnode.fVal+","+gnode.gVal+","+gnode.expanded+",");
		//System.out.print(" ");
		if(gnode.parentId != null) printIdentifier(gnode.parentId);
		System.out.print("} ");
		
		System.out.print(",{");
		generateNeighborNodes(gnode);
		for (int i = 0; i <gnode.neighborNodes.size(); i++) {
			printIdentifier(gnode.neighborNodes.get(i));
			System.out.print(",");
		}
		System.out.print("}");
		System.out.println("} ");
		
	}
	
	public static void sleepFor(int msecs){
		long startTime = new Date().getTime();
		int count = 0;
		while((new Date().getTime() - startTime)<msecs){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("waiting");
			count++;
		}
		System.out.println(count);
	}
}
