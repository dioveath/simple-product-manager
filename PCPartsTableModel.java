

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;
import java.sql.*;



class PCPartsTableModel extends AbstractTableModel {


    public boolean DEBUG = true;

    PCPartsDatabase database;
    String tableName;

    Vector<String> columnNames;
    Vector<Object[]> data;


    public PCPartsTableModel(PCPartsDatabase db, String tn){
	this.database = db;
	this.tableName = tn;

	this.columnNames = new Vector<String>();
	this.data = new Vector<Object[]>();

	loadValues();
    }


    private void loadValues(){
	columnNames.clear();
	data.clear();
	columnNames.addAll(Arrays.asList(database.getColumnNames(tableName)));
	data.addAll(Arrays.asList(database.getRowData(tableName)));
    }
    

    @Override
    public int getColumnCount(){
	return columnNames.size();
    }
    @Override
    public int getRowCount(){
	return data.size();
    }

    @Override
    public String getColumnName(int col){
	return columnNames.get(col);
    }

    @Override
    public Object getValueAt(int row, int col){
	return data.get(row)[col];
    }

    public Object[] getRowData(int row){
	return data.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int col){
	if(col == 0)
	    return false;
	else
	    return true;
    }


    // just return and show some dialog, if the format was not valid
    @Override
    public void setValueAt(Object value, int row, int col) {
	if(DEBUG) {
	    System.out.println("Setting value (" + value.toString() +
			       ") at (" + row + ", " + col + ")");
	}

	switch(database.getColumnTypes(tableName)[col]){
	case PCPartsDatabase.INT_TYPE:
	    try {
		Integer.parseInt((String) value);
	    } catch (NumberFormatException nfe){
		JOptionPane.showMessageDialog(null,
					      "" + nfe,
					      "Error!",
					      JOptionPane.ERROR_MESSAGE);
		nfe.printStackTrace();

		return;

	    }
	    break;
	case PCPartsDatabase.FLOAT_TYPE:
	    try {
		Float.parseFloat((String) value);
	    } catch (NumberFormatException nfe){
		JOptionPane.showMessageDialog(null,
					      "" + nfe,
					      "Error!",
					      JOptionPane.ERROR_MESSAGE);
		nfe.printStackTrace();

		return;
	    }
	    break;
	case PCPartsDatabase.VARCHAR_TYPE: // we can accept anythin as string
	    break;
	}

	data.get(row)[col] = value;

	try { 
	    database.updateAt(tableName, data.get(row));
	    loadValues();
	    fireTableDataChanged();
	} catch(SQLException se){
	    se.printStackTrace();
	}

    }


    public void deleteRow(int row) throws SQLException{
	if(DEBUG) {
	    System.out.println("Deletting row, " + row);
	}

	if(database.deleteFrom(tableName, data.get(row))) { 
	    data.removeElementAt(row); 
	    fireTableRowsDeleted(row, row);
	}
    }
    

    public void addRow(Object[] row) throws SQLException{
	if(DEBUG) {
	    System.out.println("Adding row: ");
	    for(int i = 0; i < row.length; i++)
		System.out.println("" + i + ": " + row[i]);
	}

	if(database.insertTo(tableName, row)){
	    loadValues();
	    fireTableDataChanged();
	}

	// int ridx = data.size() -1;
	// fireTableRowsInserted(ridx, ridx);
    }
}


