import java.awt.EventQueue;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



public class GridVisual {
	 public static JFrame jframe;
	 public static boolean lock = false;
	 public static CellPane cells[][];
	 public static Identifier startId = new Identifier();
	 public static Identifier goalId = new Identifier();
	 public static int state=0;
	 
	 public static void initCellPanes(){
		 cells = new CellPane[Utils.rows][Utils.columns];
	 }
	 
	 public static void resetCellPanes(){
		 for(int r=0;r<Utils.rows;r++){
				for(int c=0; c<Utils.columns; c++){
					cells[r][c].resetPane();
				}
			}

	 }
	 
	 public static void clearCellPanes(){
		 for(int r=0;r<Utils.rows;r++){
				for(int c=0; c<Utils.columns; c++){
					cells[r][c].clearPane();
				}
			}
	 }

	 public GridVisual(){
	        EventQueue.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                try {
	                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
	                }

	                JFrame frame = new JFrame("Testing");
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                frame.setLayout(new GridBagLayout());
	                frame.add(new GridPane(Utils.rows,Utils.columns));
	                frame.add(new OptionPane());
	                frame.pack();
	                frame.setLocationRelativeTo(null);
	                frame.setVisible(true);
	                jframe = frame;
	            }
	        });
	 }
	 
	 public static void main(String[] args) {
		 	Utils.rows = 20;
			Utils.columns = 20;
			Utils.initCostMap();
		 	GridVisual.initCellPanes();
	        new GridVisual();			
	 }
}
