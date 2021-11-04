/*

  PCParts.java

  Author: Saroj Rai @ Charicha 
  Created On: Wednesday,  3 October 2018.
  

*/


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;


public class PCParts extends JFrame {

    public static boolean DEBUG = false;
    PCPartsDatabase pcdatabase = null;

    JPanel mainPanel;

    final JPanel homePanel;
    final JPanel aboutPanel;
    JPanel currentPanel;

    JFrame self;

    ArrayList<TablePanel> tablePanels;


    public PCParts(){
	super("HOLY ****");
	self = this;

	try { 
	    pcdatabase = PCPartsDatabase.getInstance();
	} catch (Exception e){
	    JOptionPane.showMessageDialog(null,
					  "" + e,
					  "Error!",
					  JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	}

	// setLayout();
	setSize(800, 600);
	// setResizable(false);
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);


	JMenuBar menuBar = new JMenuBar();
	JMenu menu = new JMenu("Home");


	JMenuItem homeitem = new JMenuItem("Home");
	homePanel = new HomePanel();
	homeitem.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    switchPanel(new HomePanel());
		}
	    });

	JMenuItem aboutitem = new JMenuItem("About");
	aboutPanel = new AboutPanel();
	aboutitem.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    switchPanel(new AboutPanel());
		}
	    });
		       

	JMenuItem exititem = new JMenuItem("Exit");
	exititem.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    dispose();
		}
	    });


	// Sub Menu for all tables
	JMenu subMenu = new JMenu("Warehouse");
	tablePanels = new ArrayList<TablePanel>();
	if(pcdatabase != null){
	    String[] allTables = pcdatabase.PC_TABLES;

	    for(int i = 0; i < allTables.length; i++){
		JMenuItem item = new JMenuItem("Show " + allTables[i]);
		final TablePanel tPanel = new TablePanel(self, pcdatabase, allTables[i]);
		item.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
			    switchPanel(tPanel);
			}
		    });


		subMenu.add(item);
		tablePanels.add(tPanel);
	    }

	}

	menu.add(homeitem);
	menu.add(subMenu);
	menu.add(aboutitem);
	menu.add(exititem);
	menuBar.add(menu);
	setJMenuBar(menuBar);


	mainPanel = new JPanel(new GridLayout());
	switchPanel(new HomePanel());
	add(mainPanel);

	if(DEBUG) {
	    JPanel debugPanel = new JPanel(new GridLayout(0, 1));


	    JTextField command = new JTextField(50);
	    command.setSize(new Dimension(600, 20));
	    final JLabel label = new JLabel("REPORTS: ");
	    label.setPreferredSize(new Dimension(600, 20));

	    command.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e){
			JTextField field = (JTextField) e.getSource();

			field.setText("");
			label.setText("ENTERED!");
			
		    }
		});

	    JPanel panel = new JPanel();
	    panel.add(command);
	    panel.add(label);
	    debugPanel.add(panel);
	    add(debugPanel);
	}

    }

    public void switchPanel(JPanel panel){
	if(panel == null || currentPanel == panel) {
	    revalidate();
	    return;
	}

	mainPanel.add(panel, BorderLayout.CENTER);
	if(currentPanel != null)
	    mainPanel.remove(currentPanel);
	currentPanel = panel;
	revalidate();
    }

};
