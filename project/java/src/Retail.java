/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.lang.Math;
import java.sql.Timestamp;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Retail {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));
	
	//THE USER VARIABLE***********************************************************
	private List<String> authorisedUser = null;
	private List<String> mStores = null;

   /**
    * Creates a new instance of Retail shop
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public Retail(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end Retail

   // Method to calculate euclidean distance between two latitude, longitude pairs. 
   public double calculateDistance (double lat1, double long1, double lat2, double long2){
      double t1 = (lat1 - lat2) * (lat1 - lat2);
      double t2 = (long1 - long2) * (long1 - long2);
      return Math.sqrt(t1 + t2); 
   }
   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            Retail.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      Retail esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the Retail object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new Retail (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: esql.authorisedUser = LogIn(esql); System.out.println(esql.authorisedUser.get(0)); break;
               case 9: keepon = false; 
		esql.authorisedUser = null;
		break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (esql.authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. View Stores within 30 miles");
                System.out.println("2. View Product List");
                System.out.println("3. Place a Order");
                System.out.println("4. View 5 recent orders");

                //the following functionalities basically used by managers
                System.out.println("5. Update Product");
                System.out.println("6. View 5 recent Product Updates Info");
                System.out.println("7. View 5 Popular Items");
                System.out.println("8. View 5 Popular Customers");
                System.out.println("9. Place Product Supply Request to Warehouse");
		System.out.println("10. See User Info");

                System.out.println(".........................");
                System.out.println("20. Log out");
                switch (readChoice()){
                   case 1: viewStores(esql); break;
                   case 2: viewProducts(esql); break;
                   case 3: placeOrder(esql); break;
                   case 4: viewRecentOrders(esql); break;
                   case 5: updateProduct(esql); break;
                   case 6: viewRecentUpdates(esql); break;
                   case 7: viewPopularProducts(esql); break;
                   case 8: viewPopularCustomers(esql); break;
                   case 9: placeProductSupplyRequests(esql); break;
					//CUSTOM FUNCTION TO CHECK YOUR USER ****************************************
		   		   case 10: userInfo(esql); break;

                   case 20: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();
         System.out.print("\tEnter latitude: ");   
         String latitude = in.readLine();       //enter lat value between [0.0, 100.0]
         System.out.print("\tEnter longitude: ");  //enter long value between [0.0, 100.0]
         String longitude = in.readLine();
         
         String type="Customer";

	String query = String.format("INSERT INTO USERS (name, password, latitude, longitude, type) VALUES ('%s','%s', %s, %s,'%s')", name, password, latitude, longitude, type);

         esql.executeUpdate(query);
         System.out.println ("User successfully created!");		 
		 
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static List<String> LogIn(Retail esql) throws SQLException { 
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE name = '%s' AND password = '%s'", name, password);
         List<List<String>> user = esql.executeQueryAndReturnResult(query);

		 if (user.size() > 0){
			 
			if (!(user.get(0).get(5).replaceAll("\\s", "") == "manager")){
				
				System.out.println("you are a manager!!");
				
				query = String.format("select storeID from store where storeid in (select storeid from store where managerID = %s)", user.get(0).get(0));	
				List<List<String>> res = esql.executeQueryAndReturnResult(query);
				
				esql.mStores = new ArrayList<String>();
				
				for(List<String> x : res){
					esql.mStores.add(x.get(0));
				}

			}
			return user.get(0);
		 }
		  
		 System.out.println("User does not exist!!");
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end
	
  public static void viewStores(Retail esql) {
	try{
   	    System.out.print("\tFinding the closest stores to you...\n \n");
		
		int cnt = 0;
		
		String query = String.format("SELECT * FROM Store");
		List<List<String>> re = esql.executeQueryAndReturnResult(query);
		
		System.out.println("-------------------Results---------------------\n");
		
		for(List<String> store : re){
			if( esql.calculateDistance(Double.parseDouble(esql.authorisedUser.get(3)), Double.parseDouble(esql.authorisedUser.get(4)), Double.parseDouble(store.get(2)), Double.parseDouble(store.get(3))) <= 30 ){
				System.out.println("Store ID: " + store.get(0) + " Store Name: " + store.get(1));
			}
		}
	 	   
	}catch(Exception e){
		System.err.println(e.getMessage());
	}
   }
	
   public static void viewProducts(Retail esql) {
   	try{
		System.out.print("Enter your desired Store ID: ");
		String storeID = in.readLine();
		
		String query = String.format("SELECT * FROM Product WHERE storeID = %s", storeID);
		int re = esql.executeQueryAndPrintResult(query);
	
	}catch(Exception e){
		System.err.println(e.getMessage());
	}
   }

public static void placeOrder(Retail esql) {
	try{
		System.out.print("Enter the desired Store ID: ");
		String id = in.readLine();
		
		System.out.print("Enter the product name exactly: ");
		String product = in.readLine();
		
		System.out.print("Enter the number of units desired: ");
		String unitno = in.readLine();
		
		String query = String.format("SELECT * FROM Product WHERE storeID = %s and numberOfUnits >= %d", id, Integer.parseInt(unitno));
		List<List<String>> re = esql.executeQueryAndReturnResult(query);
		
		if (re.size() > 1){
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			query = String.format("INSERT INTO Orders (customerID, storeID, productName, unitsOrdered) VALUES ('%s', '%s', '%s', '%s', '%s')", esql.authorisedUser.get(0), id, product, unitno, ts.toString());
			esql.executeUpdate(query);
			
			query = String.format("update product set numberOfUnits = %d where productName = %s and storeid = %s", Integer.parseInt(re.get(1).get(2)) - Integer.parseInt(unitno), product, id);
			
			System.out.println("Order succefully processed!");
		}
		else{
			System.out.println("Order Invalid!");
		}
	}
	catch(Exception e){
		System.err.println(e.getMessage());
	}
}

public static void viewRecentOrders(Retail esql) {
    try{
		if (!(esql.authorisedUser.get(5).replaceAll("\\s", "") == "customer")){
			String query = String.format("SELECT * FROM Orders WHERE customerID = %s LIMIT 5", esql.authorisedUser.get(0));
			int res = esql.executeQueryAndPrintResult(query);
		}
		else if(!(esql.authorisedUser.get(5).replaceAll("\\s", "") == "manager")){
			String query = String.format("SELECT * from orders where storeid in (select storeid from store where managerUserID = %s)", esql.authorisedUser.get(0));
			int res = esql.executeQueryAndPrintResult(query);
		}
		else{
			int res = 0;
		}
	}
	catch(Exception e){
		System.err.println(e.getMessage());
	}
}
	
public static void updateProduct(Retail esql) {
	try{
		if (!(esql.authorisedUser.get(5) == "manager")){
			System.out.print("Enter the desired Store ID: ");
			String id = in.readLine();
			
			if (esql.mStores.contains(id)){
				System.out.print("Enter the product you desire exactly: ");
				String product = in.readLine();
				
				String query = String.format("select productName from product where storeID = %s", id);
				int res = esql.executeQuery(query);
				
				if(res > 1){
					System.out.print("What would you like to change?\n" + 
									"1.) Price\n2.) Inventory\n");
									
					switch(readChoice()){
						case 1: System.out.print("Enter new price: ");
								String price = in.readLine();
								
								query = String.format("update product set pricePerUnit = %s where storeid = %s and productName = %s", price, id, product);
								esql.executeUpdate(query);
								
								System.out.println("Price changed successfully!!");
								break;
								
						case 2: System.out.print("Enter new number of units: ");
								String units = in.readLine();
								
								query = String.format("update product set numberOfUnits = %s where storeid = %s and productName = %s", units, id, product);
								esql.executeUpdate(query);
								
								System.out.println("Inventory changed successfully!!");
								break;
								
						default: 
								System.out.println("Option Invalid!!");
								break;
					}
				}
				else{
					System.out.print("Product not available at this store!!");
				}
				
			}
			else{
				System.out.print("Permission Denied!!");
			}
		}
		else{
			System.out.print("Permission Denied!!");
		}
	}
	catch(Exception e){
		System.err.println(e.getMessage());
	}
}
   public static void viewRecentUpdates(Retail esql) {}
	
public static void viewPopularProducts(Retail esql) {
   try{
	if (!(esql.authorisedUser.get(5) == "manager")){
		String query = String.format("select product.productName, product.storeID, count(orders.orderNumber) as numOrders from product" + 
						" inner join orders on product.productName = orders.productName" +
					    	" having product.storeID in (select storeid from store where managerUserID = %s)" +
						" group by product.productName, product.storeID, numOrders order by numOrders desc limit 5", esql.authorisedUser.get(0));
		int re = esql.executeQueryAndPrintResult(query);
	}
	else{
		System.out.println("Permission Denied!!!");
	}
   }
   catch(Exception e){
	System.err.println(e.getMessage());
   }
}
	
public static void viewPopularCustomers(Retail esql) {
	try{
		if (!(esql.authorisedUser.get(5) == "manager")){
			String query = String.format("select users.userID, users.name, count(orders.orderNumber) as numOrders from users" + 
										" inner join orders on users.userID = orders.customerID" + 
						    				" having product.storeID in (select storeid from store where managerUserID = %s)" +
										" group by users.name, users.userID, numOrders order by numOrders desc limit 5", esql.authorisedUser.get(0));
			int re = esql.executeQueryAndPrintResult(query);
		}
		else{
			System.out.println("Permission Denied!!!");
		}
	}
	catch(Exception e){
		System.err.println(e.getMessage());
	}
}
	
public static void placeProductSupplyRequests(Retail esql) {}
	
public static void userInfo(Retail esql) {
   	System.out.printf("User Info: %s, %s", esql.authorisedUser.get(1), esql.authorisedUser.get(5));
}

}//end Retail

