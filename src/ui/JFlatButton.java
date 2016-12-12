package ui;

import java.awt.Cursor;
import javax.swing.JButton;

public class JFlatButton extends JButton {

	private static final long serialVersionUID = 1L;


	public JFlatButton(String caption){
		super(caption);
		applyStyle();
	}

	public JFlatButton() {
		applyStyle();
	}
	
	
	private void applyStyle() {
//		this.setBorderPainted(false);
//		this.setFocusPainted(false);
//		this.setContentAreaFilled(false);		
//		this.setBackground(Colors.LIGHT_GRAY);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//		this.setOpaque(true);
//		this.setMinimumSize(new Dimension(100, 100));

	}	
}
