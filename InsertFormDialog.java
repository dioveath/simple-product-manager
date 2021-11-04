

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class InsertFormDialog extends JDialog {

    public boolean DEBUG = true;
    public static int FIELD_WIDTH = 150;
    public static int RESULT_CANCEL = 1;
    public static int RESULT_OK = 2;

    String[] columns;
    int[] coltypes;
    ArrayList<JTextField> fields = new ArrayList<JTextField>();

    public static int m_result = -1;
    

    public InsertFormDialog(JFrame frame, String[] columns, int[] types){
	super(frame, true);
	setTitle("Insert Form");
	setPreferredSize(new Dimension((int)frame.getSize().getWidth()/2,
				       (int)frame.getSize().getHeight()/2));
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	this.columns = columns;
	this.coltypes = types;

	final JPanel mainPanel = new JPanel();
	mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

	for(int i = 0; i < columns.length; i++){
	    JPanel panel = new JPanel(new GridLayout(0, 1));
	    JLabel label = new JLabel(columns[i]);
	    JTextField f = new JTextField();
	    panel.setPreferredSize(new Dimension(FIELD_WIDTH, 40));
	    panel.add(label);
	    panel.add(f);
	    mainPanel.add(panel);

	    fields.add(f);
	}


	JButton addButton = new JButton("Add");
	addButton.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
		    addAction();
		}
	    });

	JButton cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e){
		    cancelAction();
		}
	    });

	JPanel panel = new JPanel(new GridLayout(0, 2));
	panel.add(addButton);
	panel.add(cancelButton);
	mainPanel.add(panel);


	add(mainPanel);
	addWindowListener(new WindowAdapter(){
		public void windowClosing(WindowEvent we){
		    if(DEBUG) { 
			System.out.println("Window Closing..!\n");
		    }
		}
	    });

    }


    public Object[] getData(){
	ArrayList<Object> data = new ArrayList<Object>();
	for(int i = 0; i < fields.size(); i++) {

	    switch(coltypes[i]){
	    case PCPartsDatabase.INT_TYPE:
		data.add(Integer.parseInt(fields.get(i).getText()));
		break;
	    case PCPartsDatabase.FLOAT_TYPE:
		data.add(Float.parseFloat(fields.get(i).getText()));
		break;
	    case PCPartsDatabase.VARCHAR_TYPE:		// VARCHAR strint like
		data.add(fields.get(i).getText());
		break;
	    default:
		{
		    if(DEBUG){
			System.out.println("getData(): DEFAULT TYPE["+i+"]: "+ coltypes[i]);
		    }
		    data.add(fields.get(i).getText());
		}
		break;
	    }

	    if(DEBUG){
		System.out.println(data.get(i));
	    }
	}

	return data.toArray(new Object[data.size()]);
    }

    
    public void addAction(){
	if(DEBUG){
	    System.out.println("Add action!");
	}

	if(isFieldsValid()) { 
	    m_result = RESULT_OK;
	    setVisible(false);
	} else {
	    JOptionPane.showMessageDialog(null,
					  "Invalid Data Type",
					  "Invalid Input",
					  JOptionPane.ERROR_MESSAGE);
	}

    }


    private boolean isFieldsValid(){
	boolean valid = true;

	for(int i = 0; i < fields.size(); i++) {
	    JTextField field = fields.get(i);
	    String value = field.getText();


	    if(value.isEmpty()) {
		field.setBackground(Color.RED);
		valid = false;
		continue;
	    }


	    field.setBackground(Color.WHITE);
	    switch(coltypes[i]){
	    case PCPartsDatabase.INT_TYPE:
		{
		    try {
			Integer.parseInt(value);
		    } catch(NumberFormatException nfe){
			field.setBackground(Color.RED);
			valid = false;
		    }
		}
		break;
	    case PCPartsDatabase.FLOAT_TYPE:
		{
		    try {
			Float.parseFloat(value);
		    } catch(NumberFormatException nfe){
			field.setBackground(Color.RED);
			valid = false;
		    }
		}
		break;
	    case PCPartsDatabase.VARCHAR_TYPE:
		break;
	    default:
		break;
	    }
	    
	}

	return valid;
    }



    public void cancelAction(){
	if(DEBUG){
	    System.out.println("Cancel Action!");
	}
	m_result = RESULT_CANCEL;
	setVisible(false);
    }


    public int getResult(){
	return m_result;
    }

};
