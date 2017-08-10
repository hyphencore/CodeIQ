package GuiTest1;
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MyGuiTest1 extends JFrame{
	public JFrame frame;

	// constructor
	MyGuiTest1() {
		super("GuiTest1");
	}

	public void showWindow() {
		JButton btn = new JButton("button");
		Container contentPane = getContentPane();

		setBounds(100, 100, 300, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane.add(btn, BorderLayout.CENTER);

		setVisible(true);
	}
}
