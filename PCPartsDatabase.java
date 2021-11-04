/*

  PCPartsDatabase.java

  Author: Saroj Rai @ Charicha 
  Created On: Wednesday,  3 October 2018.

  
  Class to interact with database, 
  Hard coded and implemented for PCParts 
  with tables

  We can go next level by making the creation
  of table dynamic or through a file.

*/




import java.sql.*;



public class PCPartsDatabase {

    public boolean DEBUG = true;

    public static final int INT_TYPE = 4;
    public static final int VARCHAR_TYPE = 12;
    public static final int FLOAT_TYPE = 7;

    // TODO: Half constructed design
    public static String[] cpu_columns = { "ID",
					   "Manufacturer",
					   "Model",
					   "ClockSpeed",
					   "Quantity",
					   "Price" };

    public static String CPU_TABLE_NAME = "CPU";
    public static String MOTHERBOARD_TABLE_NAME = "Motherboard";
    public static String MEMORY_TABLE_NAME = "Memory";
    public static String POWER_TABLE_NAME = "PowerSupplies";
    public static String STORAGE_TABLE_NAME = "Storage";
    public static String VIDEO_TABLE_NAME = "VideoGraphics";
    public static String COOLER_TABLE_NAME = "Cooler";
    public static String COMPUTERCASE_TABLE_NAME = "ComputerCases";

    public static String[] PC_TABLES = { CPU_TABLE_NAME,
					 MOTHERBOARD_TABLE_NAME,
					 MEMORY_TABLE_NAME,
					 POWER_TABLE_NAME,
					 STORAGE_TABLE_NAME,
					 VIDEO_TABLE_NAME
    };

    static PCPartsDatabase instance = null;
    Connection connection = null;
    Statement stmt = null;

 
    private PCPartsDatabase() throws SQLException, ClassNotFoundException {
	initDatabase();
    }


    public static PCPartsDatabase getInstance() throws SQLException, ClassNotFoundException{
	if(instance == null)
	    instance = new PCPartsDatabase();
	return instance;
    }


    // ADD AS MANY AS YOU WANT
    private void initDatabase() throws SQLException, ClassNotFoundException {
	Class.forName("com.mysql.jdbc.Driver");
	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306",
						 "root", "mysqlpassword");
	stmt = connection.createStatement();

	stmt.execute("CREATE DATABASE IF NOT EXISTS pc_parts");
	stmt.execute("USE pc_parts");
	    
	stmt.execute("CREATE TABLE IF NOT EXISTS " + CPU_TABLE_NAME + "( " +
		     cpu_columns[0] + " INT PRIMARY KEY AUTO_INCREMENT,"+
		     cpu_columns[1] + " VARCHAR(255), "+
		     cpu_columns[2] + " VARCHAR(255), "+
		     cpu_columns[3] + " FLOAT, "+
		     cpu_columns[4] + " INT, "+
		     cpu_columns[5] + " FLOAT)");
			 

	stmt.execute("CREATE TABLE IF NOT EXISTS " + MOTHERBOARD_TABLE_NAME +
		     "(ID INT PRIMARY KEY AUTO_INCREMENT, "+
		     "Manufacturer VARCHAR(255), "+
		     "Model VARCHAR(255),"+
		     "Quantity INT, " +
		     "Price FLOAT)");

	stmt.execute("CREATE TABLE IF NOT EXISTS "+ MEMORY_TABLE_NAME + 
		     "(ID INT PRIMARY KEY AUTO_INCREMENT, "+
		     "Manufacturer VARCHAR(255), "+
		     "RAMSize INT,"+
		     "Quantity INT, " +
		     "Price FLOAT)");


	stmt.execute("CREATE TABLE IF NOT EXISTS "+ POWER_TABLE_NAME + 
		     "(ID INT PRIMARY KEY AUTO_INCREMENT, "+
		     "Manufacturer VARCHAR(255), "+
		     "Power INT,"+
		     "Quantity INT, " + 
		     "Price FLOAT)");

	stmt.execute("CREATE TABLE IF NOT EXISTS " + STORAGE_TABLE_NAME + 
		     "(ID INT PRIMARY KEY AUTO_INCREMENT, "+
		     "Manufacturer VARCHAR(255), "+
		     "Size INT,"+
		     "Quantity INT, " + 
		     "Price FLOAT)");

	stmt.execute("CREATE TABLE IF NOT EXISTS " + VIDEO_TABLE_NAME +
		     "(ID INT PRIMARY KEY AUTO_INCREMENT, " +
		     "Manufacturer VARCHAR(255), " +
		     "VRAM INT, "+
		     "Quantity INT, " +
		     "Price FLOAT)");

	// stmt.execute("CREATE TABLE IF NOT EXISTS " + COOLER_TABLE_NAME + 

    }
  

    // Generic
    public boolean insertTo(String tableName, Object[] data) throws SQLException {
	String iQuery = "INSERT INTO " + tableName + " VALUES ( ?";
	for(int i = 1; i < data.length; i++)
	    iQuery += ", ?";
	iQuery += ")";

	if(DEBUG){
	    System.out.println("iQuery: " + iQuery);
	}

	PreparedStatement pstmt = connection.prepareStatement(iQuery);
	int[] types = getColumnTypes(tableName);

	for(int i = 0; i < data.length; i++){
	    switch(types[i]){
	    case INT_TYPE: 	// INT
		pstmt.setInt(i+1, (Integer)data[i]);
		break;
	    case VARCHAR_TYPE:	// VARCHAR
		pstmt.setString(i+1, (String) data[i]);
		break;
	    case FLOAT_TYPE: 	// FLOAT
		pstmt.setFloat(i+1, (Float) data[i]);
		break;
	    }
	}

	return pstmt.executeUpdate() > 0 ? true : false;
    }


    // row[0] is id and it is not writable in jTable, we
    // use that to identify our row at database
    // TODO: Better to update just the updated column data
    public boolean updateAt(String tableName, Object[] row) throws SQLException{
	String[] columns = getColumnNames(tableName);

	String iQuery = "UPDATE " + tableName + " SET ";

	iQuery += columns[1] + " = ? ";
	for(int i = 2; i < columns.length; i++) // skip i = 0 (id)
	    iQuery += ", " + columns[i] + " = ? ";
	iQuery += "WHERE " + columns[0] + " = ? ";

	if(DEBUG){
	    System.out.println(iQuery);
	}

	PreparedStatement pstmt = connection.prepareStatement(iQuery);
	int[] types = getColumnTypes(tableName);

	for(int i = 1; i < row.length; i++){
	    if(DEBUG){
		System.out.println("" + i + ": " + i);
	    }

	    switch(types[i]){
	    case INT_TYPE: 	// INT
		{
		    int value = Integer.parseInt((String)row[i]);
		    pstmt.setInt(i, value);
		}
		break;
	    case VARCHAR_TYPE:	// VARCHAR
		pstmt.setString(i, (String) row[i]);
		break;
	    case FLOAT_TYPE: 	// FLOAT
		{
		    float value = Float.parseFloat((String) row[i]);
		    pstmt.setFloat(i, value);
		}
		break;
	    }
	}

	int idValue = Integer.parseInt((String) row[0]);
	pstmt.setInt(row.length, idValue); // where id = ?

	return pstmt.executeUpdate() > 0 ? true : false;
    }


    // this also works based on id
    // NOTE: to be safe we can check all data in where clause
    public boolean deleteFrom(String tableName, Object[] row) throws SQLException {
	String[] columnNames = getColumnNames(tableName);
	String iQuery = "DELETE FROM " + tableName + " WHERE ";

	iQuery += columnNames[0] + " = ? ";

	PreparedStatement pstmt = connection.prepareStatement(iQuery);
	int[] types = getColumnTypes(tableName);

	switch(types[0]){
	case INT_TYPE: 	// INT
	    {
		int value = Integer.parseInt((String)row[0]);
		pstmt.setInt(1, value);
	    }
	    break;
	case VARCHAR_TYPE:	// VARCHAR
	    pstmt.setString(1, (String) row[0]);
	    break;
	case FLOAT_TYPE: 	// FLOAT
	    {
		float value = Float.parseFloat((String) row[0]);
		pstmt.setFloat(1, value);
	    }
	    break;
	}

	
	return pstmt.executeUpdate() > 0 ? true : false;
    }


    int[] getColumnTypes(String tableName){
	int[] types = null;
	try { 
	    ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int ncol = rsmd.getColumnCount();

	    types = new int[ncol];
	    for(int i = 0; i < ncol; i++){
		types[i] = rsmd.getColumnType(i+1);
	    }

	    if(DEBUG){
		for(int i = 0; i < types.length; i++)
		    System.out.println("types[" + i + "]: " + types[i]);
	    }

	} catch(Exception e){
	    e.printStackTrace();
	}

	return types;
    }


    String[] getColumnNames(String tableName){
	String[] c = null;

	try { 
	    ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
	    if(rs == null) return null;


	    ResultSetMetaData rsmd = rs.getMetaData();
	    int ncol = rsmd.getColumnCount();
	    c = new String[ncol];

	    for(int i = 0; i < ncol; i++)
		c[i] = rsmd.getColumnName(i + 1);

	} catch(SQLException e){
	    System.out.println(e);
	}

	if(c == null) { 
	    c = new String[1];
	    c[0] = "UNKNOWN ERROR!";
	}

	return c;
    }


    Object[][] getRowData(String tableName){
	Object[][] data = null;

	try { 
	    ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

	    ResultSetMetaData rsmd = rs.getMetaData();
	    int ncol = rsmd.getColumnCount();
	    int nrow = getRowsCount(rs);
	    data = new Object[nrow][ncol];


	    int i = 0;
	    while(rs.next()){
		for(int j = 0; j < ncol; j++)
		    data[i][j] = (Object) rs.getString(j + 1);
		i++;
	    }

	} catch (SQLException e){
	    e.printStackTrace();
	}

	if(data == null) { 
	    data = new Object[1][1];
	    data[0][0] = (Object) "Error!";
	}

	return data;
    }


    int getRowsCount(ResultSet rs){
	int totalRows = 0;
	try {
	    rs.last();
	    totalRows = rs.getRow();
	    rs.beforeFirst();
	} catch(Exception e){
	    e.printStackTrace();
	}
	return totalRows;
    }

    // DEBUG
    public void execute(String query){
	try {
	    stmt.execute(query);
	} catch(Exception e){
	    e.printStackTrace();
	}
    }

}
