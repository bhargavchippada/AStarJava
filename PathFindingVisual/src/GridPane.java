import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;


public class GridPane extends JPanel {
    	int rows;
    	int columns;
        public GridPane(int r, int c) {
        	rows = r;
        	columns = c;
        	
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    gbc.gridx = col;
                    gbc.gridy = row;

                    CellPane cellPane = new CellPane();
                    cellPane.row = row;
                    cellPane.column = col;
                    Border border = null;
                    border = new MatteBorder(0, 0, 1, 1, Color.GRAY);
                    cellPane.setBorder(border);
                    add(cellPane, gbc);
                    GridVisual.cells[row][col] = cellPane;
                }
            }
        }
}
