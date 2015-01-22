
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class OptionPane extends JPanel {
	private JButton startButton = new JButton("start");
	private JButton clearButton = new JButton("clear");
	private JButton resetButton = new JButton("reset");
	private JLabel label = new JLabel("<html>Hello World!<br>blahblahblah</html>", SwingConstants.CENTER);
	
    public OptionPane() {
    	//setBackground(Color.WHITE);
    	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	//GridBagConstraints gbc = new GridBagConstraints();
    	Dimension buttonSize = new Dimension(80,30);
    	startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    	label.setAlignmentX(Component.CENTER_ALIGNMENT);
    	startButton.setPreferredSize(buttonSize);
    	clearButton.setPreferredSize(buttonSize);
    	resetButton.setPreferredSize(buttonSize);
    	startButton.setMaximumSize(buttonSize);
    	clearButton.setMaximumSize(buttonSize);
    	resetButton.setMaximumSize(buttonSize);
    	add(startButton);
    	add(Box.createRigidArea(new Dimension(0,10)));
        add(clearButton);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(resetButton);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(label);
        
        startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long startTime = System.currentTimeMillis();
				System.out.println("Button Clicked");
				AStarAlgo astar = new AStarAlgo();
				boolean b;
				b = astar.AStarFunction(GridVisual.startId, GridVisual.goalId);
				System.out.println(b);
				long stopTime = System.currentTimeMillis();
			    long elapsedTime = stopTime - startTime;
			    System.out.println(elapsedTime+"ms");
				
			}
		});
        
        resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Utils.initCostMap();
				GridVisual.resetCellPanes();
				GridVisual.state = 0;
				GridVisual.startId = new Identifier();
				GridVisual.goalId = new Identifier();
			}
		});
        
        clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GridVisual.clearCellPanes();
			}
		});
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 200);
    }
}
