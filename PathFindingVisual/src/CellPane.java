

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class CellPane extends JPanel {
    	// -1 means can't pass, >=0 are the costs for the tile
		public int row;
		public int column;
        public int cost = 1;
        private JLabel label = new JLabel();
        public Color cellBackground = Color.WHITE;
        long startTime;
        public CellPane() {
        	setLayout(new GridBagLayout());
        	setBackground(cellBackground);
            this.add(label);
            addMouseListener(cellMouseAdapter);
        }
        
        public void resetPane(){
        	label.setText("");
        	setBackgroundColor(Color.WHITE);
        	cost = 1;
        }
        
        public void clearPane(){
        	if(row==GridVisual.startId.nodeState[0] && column==GridVisual.startId.nodeState[1]){
        		//start node
        		setBackgroundColor(Color.GREEN);
        	}else if(row==GridVisual.goalId.nodeState[0] && column==GridVisual.goalId.nodeState[1]){
        		//goal node
        		setBackgroundColor(Color.RED);
        	}else if(cost==1){
        		setBackgroundColor(Color.WHITE);
        	}else if(cost!=-1){
        		setBackgroundColor(Color.MAGENTA);
        	}
        }
        
        public void setBackgroundColor(Color color){
        	cellBackground = color;
        	setBackground(color);
        }
        
        public void setCellBackgroundColor(Color color){
        	if(row==GridVisual.startId.nodeState[0] && column==GridVisual.startId.nodeState[1]){
        		//start node
        		return;
        	}else if(row==GridVisual.goalId.nodeState[0] && column==GridVisual.goalId.nodeState[1]){
        		//goal node
        		return;
        	}
        	setBackgroundColor(color);
        }
        
        private MouseAdapter cellMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Color.GRAY);
                //System.out.println(cost);
            }

            @Override
            public void mouseExited(MouseEvent e) {
            	setBackground(cellBackground);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
            	startTime = new Date().getTime();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
            	long difference = new Date().getTime() - startTime;
            	if(GridVisual.lock) return; //take no action 
            	if(GridVisual.state == 0){
            		cellBackground = Color.GREEN;
            		setBackground(cellBackground);
            		GridVisual.startId.nodeState[0] = row;
            		GridVisual.startId.nodeState[1] = column;
            		GridVisual.state = 1;
            		return;
            	}else if(GridVisual.state == 1){
            		cellBackground = Color.RED;
            		setBackground(cellBackground);
            		GridVisual.goalId.nodeState[0] = row;
            		GridVisual.goalId.nodeState[1] = column;
            		GridVisual.state = 2;
            		return;
            	}
            	if(difference > 100){
            		System.out.println("long-press");
            		JPanel p = new JPanel();
            		JTextField costValue = new JTextField(10);
            		p.add(new JLabel("Insert Value :"));
            		p.add(costValue);
            		int choice = JOptionPane.showConfirmDialog(null, p, "Cost of the tile : ", JOptionPane.OK_CANCEL_OPTION);
                	if(choice == 0 && !costValue.getText().equals("")){
                		cost = Integer.parseInt(costValue.getText());
                	}else{
                		cost=2;
                	}
            		cellBackground = Color.MAGENTA;
                	label.setText(""+cost);
            	}else{
            		System.out.println("Single-click");
                	if(cost!=-1){
                		cellBackground = Color.BLACK;
                		label.setText("");
                		cost=-1;
                	}else{
                		cellBackground = Color.WHITE;
                		label.setText("");
                		cost=1;
                	}
            	}
            	Utils.costMap[row][column] = cost;
            	setBackground(cellBackground);
            }
        };
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(Utils.blockSize, Utils.blockSize);
        }
        
        @Override
        public void revalidate() {
        // TODO Auto-generated method stub
        super.revalidate();
        }
}