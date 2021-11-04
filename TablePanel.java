
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;



public class TablePanel extends JPanel {

    
    public boolean DEBUG = true;

    PCPartsDatabase m_database;
    final JTable m_table;

    final JFrame m_frame;


    public TablePanel(JFrame frame, PCPartsDatabase database, String tableName){
	this.m_database = database;
	this.m_frame = frame;

	setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

	final PCPartsTableModel tModel = new PCPartsTableModel(database,
							       tableName);
	m_table = new JTable(tModel);
	m_table.getTableHeader().setReorderingAllowed(false);
	JScrollPane pane = new JScrollPane(m_table);
	pane.setPreferredSize(new Dimension(700, 200));


	JButton addButton = new JButton("Add Item");
	JButton removeButton = new JButton("Remove Item");

	addButton.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
		    InsertFormDialog dialog = new
			InsertFormDialog(m_frame,
					 database.getColumnNames(tableName),
					 database.getColumnTypes(tableName));

		    dialog.pack();
		    dialog.setLocationRelativeTo(m_frame);
		    dialog.setVisible(true);

		    if(DEBUG){
			System.out.println("Dialog returned!");
			Object[] data = dialog.getData();
			for(int i = 0; i < data.length; i++){
			    System.out.println("" + i + ": " + data[i]);
			}
		    }

		    if(dialog.getResult() == InsertFormDialog.RESULT_OK){
			try { 
			    tModel.addRow(dialog.getData());
			} catch(SQLException se){
			    JOptionPane.showMessageDialog(null,
							  "" + se,
							  "Error!",
							  JOptionPane.ERROR_MESSAGE);
			    se.printStackTrace();
			}
		    }
		}
	    });


	removeButton.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
		    int[] selection = m_table.getSelectedRows();
		    for(int i = 0; i < selection.length; i++)
			selection[i] = m_table.convertRowIndexToModel(selection[i]);

		    if(DEBUG) {
			System.out.println("Remove Button:");
			for(int i = 0; i < selection.length; i++)
			    System.out.println("Selection: " + selection[i]);
		    }

		    if(selection.length > 0) {
			try { 
			    tModel.deleteRow(selection[0]);
			} catch(SQLException se){
			    JOptionPane.showMessageDialog(null,
							  "" + se,
							  "Error!",
							  JOptionPane.ERROR_MESSAGE);
			    se.printStackTrace();
			}
		    }
		}
	    });

	JLabel label = new JLabel(tableName + " Table ");

	add(label);
	add(pane);
	add(addButton);
	add(removeButton);
    }



}


    

