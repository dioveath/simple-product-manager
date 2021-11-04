



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class SwingTest implements Runnable {


    public static void main(String[] args){
	SwingUtilities.invokeLater(new SwingTest());
    }


    public PCParts panel; 


    public void run(){

	new PCParts();

    // 	final JFrame frame = new JFrame("BOOM..!");
    // 	frame.setSize(800, 600);
    // 	frame.setResizable(false);
    // 	frame.setLocationRelativeTo(null);
    // 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    // 	JMenuBar menuBar = new JMenuBar();
    // 	// menu

    // 	JMenu m = new JMenu("Operation");
    // 	JMenuItem item = new JMenuItem("SHOW CPU", KeyEvent.VK_A);
    // 	item.addActionListener(new ActionListener() {
    // 		public void actionPerformed(ActionEvent e){
    // 		    frame.switchPanel(new MotherboardPanel(pcdatabase));
    // 		    System.out.println("ITEM PRESSED");
    // 		}
    // 	    });
	
    // 	m.add(item);
    // 	menuBar.add(m);

    // 	frame.setJMenuBar(menuBar);

    // 	panel = new PCParts();
    // 	frame.add(panel, BorderLayout.CENTER);

    // 	frame.setVisible(true);
    }


};
