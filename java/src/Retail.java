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
import java.lang.Math;
import java.util.Arrays;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class Retail {

   // reference to physical database connection.
   private Connection _connection = null;

   // Member variable for referencing logged in users
   private String loggedInUserName;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

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

   public String getLoggedInUser() {
      return loggedInUserName;
   }

   public void setLoggedInUser(String user) {
      loggedInUserName = user;
   }

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
            System.out.println("\nMAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              while(usermenu) {
                System.out.println("\nMAIN MENU");
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
   public static String LogIn(Retail esql){
      try{
         System.out.print("\tEnter name: ");
         String name = in.readLine();
         System.out.print("\tEnter password: ");
         String password = in.readLine();

         String query = String.format("SELECT * FROM USERS WHERE name = '%s' AND password = '%s'", name, password);
         int userNum = esql.executeQuery(query);
	      if (userNum > 0){
            esql.setLoggedInUser(name);
		      return name;
         }
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end

// Rest of the functions definitioa go in here

   public static void viewStores(Retail esql) {
      try{
         //getting logged-in user's location
         String username = esql.getLoggedInUser();
         String getUser = "SELECT * FROM Users U WHERE U.name = \'";
         List<List<String>> users = esql.executeQueryAndReturnResult(getUser + username + "\';"); //grab users w username equal to current user (should return 1)
         List<String> user = users.get(0); //get first (should be only) user
         double userLat = Double.parseDouble(user.get(3));
         double userLong = Double.parseDouble(user.get(4));

         //get list of stores to parse each location's distance from user
         String getStores = "SELECT * FROM Store S;";
         List<List<String>> stores = esql.executeQueryAndReturnResult(getStores);
         List<String> store = new ArrayList<String>();
         double euclideanDist = 0.0;
         double cartesianDistMiles = 0.0;
         double storeLat = 0.0;
         double storeLong = 0.0;
         List<Double> distances = Arrays.asList(new Double[stores.size()]); //store distances of stores in separate array to reference in tandem w store
         System.out.print("Total # of stores: " + stores.size());
         for (int i = 0; i < stores.size(); i++){
            store = stores.get(i);
            storeLat = Double.parseDouble(store.get(2)); //reference column 3(lat) in sql results
            storeLong = Double.parseDouble(store.get(3)); //reference column 4(long) in sql results
            euclideanDist = esql.calculateDistance(userLat,userLong,storeLat,storeLong);
            // removes from stores list any stores > 30 miles from user
            distances.set(i,euclideanDist);
            if (euclideanDist > 30.0){
               //System.out.print("\nOver 30 miles: " + store.get(1) + " " + euclideanDist);
               stores.remove(i);
               i = i-1;
            }
         }
         System.out.print("\n# of stores after filtering: " + stores.size());
         //output list of stores < 30 miles from user
         System.out.print("\n|#|\t|Store name|\t\t\t|Store ID|\t|Latitude|\t|Longitude|\t|Distance|");
         for (int j = 0; j < stores.size(); j++){
            store = stores.get(j);
            System.out.printf("\n %d\t  %s   %s\t\t%s\t%s\t  %.2f", j+1, store.get(1), store.get(0), store.get(2), store.get(3), distances.get(j));
         }
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   public static void viewProducts(Retail esql) {   
      try{

         //initialize
         List<List<String>> stores = new ArrayList<List<String>>();
         List<String> store = new ArrayList<String>();
         List<List<String>> products = new ArrayList<List<String>>();
         List<String> product = new ArrayList<String>();
         boolean viewingProducts = true;
         String storeID = "";
         String input = "";

         while(viewingProducts){
            //output list of stores that user can request to

            String getStores = "SELECT * FROM Store;";
            stores = esql.executeQueryAndReturnResult(getStores);
            System.out.print("\n|#|\t|Store name|\t\t\t|Latitude|\t|Longitude|");
            for(int i = 0; i < stores.size(); i++){
               store = stores.get(i);
               System.out.printf("\n%d\t%s\t%s\t%s", (i+1), store.get(1), store.get(2), store.get(3));
            }

            //select store, pass input to stores array index
            System.out.printf("\nPlease select a store to view its products (%d - %d): ", 1, stores.size());
            input = in.readLine();
            store = stores.get(Integer.parseInt(input) - 1);
            storeID = store.get(0);
            System.out.printf("\nYou selected store: %s\n", store.get(1));

            //get list of products from selected store
         
            //get current product listing
            String getProduct = "SELECT * FROM Product WHERE storeID = \'" + storeID + "\';";
            System.out.print("\n|#|\t|Product|\t\t\t|Price|\t|Quantity|");
            products = esql.executeQueryAndReturnResult(getProduct);
            for(int j = 0; j < products.size(); j++){
               product = products.get(j);
               System.out.printf("\n %d\t%s\t$%.2f\t%d", (j+1), product.get(1), Double.parseDouble(product.get(3)), Integer.parseInt(product.get(2)));
            }

            System.out.print("\n\nView another store's products?\n\t1. Yes\n\t2. No\n\t");
            switch(readChoice()){
               case 1: break;
               case 2: viewingProducts = false; break;
               default: System.out.println("Unrecognized choice! Exiting..."); viewingProducts = false; break;
            }

            if(!viewingProducts){
               return;
            }


         }




      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }

   public static List<List<String>> returnStores(Retail esql){
      //only called from within other methods
      List<List<String>> stores = Arrays.asList(Arrays.asList(new String[20]));
      try{
         //getting logged-in user's location
         String username = esql.getLoggedInUser();
         String getUser = "SELECT * FROM Users U WHERE U.name = \'";
         List<List<String>> users = esql.executeQueryAndReturnResult(getUser + username + "\';"); //grab users w username equal to current user (should return 1)
         List<String> user = users.get(0); //get first (should be only) user
         double userLat = Double.parseDouble(user.get(3));
         double userLong = Double.parseDouble(user.get(4));

         //get list of stores to parse each location's distance from user
         String getStores = "SELECT * FROM Store S;";
         stores = esql.executeQueryAndReturnResult(getStores);
         List<String> store;
         double euclideanDist = 0.0;
         double cartesianDistMiles = 0.0;
         double storeLat = 0.0;
         double storeLong = 0.0;
         List<Double> distances = Arrays.asList(new Double[stores.size()]); //store distances of stores in separate array to reference in tandem w store
         for (int i = 0; i < stores.size(); i++){
            store = stores.get(i);
            storeLat = Double.parseDouble(store.get(2)); //reference column 3(lat) in sql results
            storeLong = Double.parseDouble(store.get(3)); //reference column 4(long) in sql results
            euclideanDist = esql.calculateDistance(userLat,userLong,storeLat,storeLong);
            // removes from stores list any stores > 30 miles from user
            distances.set(i,euclideanDist);
            if (euclideanDist > 30.0){
               //System.out.print("\nOver 30 miles: " + store.get(1) + " " + euclideanDist);
               stores.remove(i);
               i = i-1;
            }
         }
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
      return stores;
   }

   public static void placeOrder(Retail esql) {
      try{
         String query1 = ""; //for general query usage
         String query2 = ""; //for general query usage
         //Find stores within 30 miles radius of user and print list of stores
         viewStores(esql);
         List<List<String>> stores = returnStores(esql);

         //Grab userID from logged in user
         String username = esql.getLoggedInUser();
         String getUser = "SELECT * FROM Users U WHERE U.name = \'";
         List<List<String>> users = esql.executeQueryAndReturnResult(getUser + username + "\';"); //grab users w username equal to current user (should return 1)
         List<String> user = users.get(0); //get first (should be only) user
         String userID =  user.get(0); //get userID from current user

         //User select store from list
         System.out.printf("\nPlease select a store (%d - %d): ", 1, stores.size());
         String input = in.readLine();
         int selection = Integer.parseInt(input);
         List<String> store = stores.get(selection-1);
         System.out.printf("\nYou selected store: %s\n", store.get(1));
         String storeID = store.get(0);

         //init queries
         String productRequest = "";
         String productStockUpdate = "";
         List<List<String>> products = esql.executeQueryAndReturnResult("SELECT * FROM Product;");
         List<String> product = new ArrayList<String>();

         //start order flow
         boolean orderFlow = true;
         boolean startover = false;
         while(orderFlow){
            startover = false;

            //list products at store
            System.out.print("\n\n\t========Available Products========\n\n");
            String getProduct = "SELECT * FROM Product WHERE storeID = \'" + storeID + "\';";
            System.out.print("\n|#|\t|Product|\t\t\t|Price|\t|Quantity|");
            products = esql.executeQueryAndReturnResult(getProduct);
            for(int j = 0; j < products.size(); j++){
               product = products.get(j);
               System.out.printf("\n %d\t%s\t$%.2f\t%d", (j+1), product.get(1), Double.parseDouble(product.get(3)), Integer.parseInt(product.get(2)));
            }
            System.out.printf("\nWhat product would you like to order? (%d - %d): ", 1, products.size());
            input = in.readLine();
            product = products.get(Integer.parseInt(input) - 1);
            System.out.printf("\nYou selected: %s  $%.2f, %s\n", product.get(1), Double.parseDouble(product.get(3)), product.get(2));
            
            System.out.printf("\nHow many would you like to order? (price: $%d, in stock: %d): ", Integer.parseInt(product.get(3)), Integer.parseInt(product.get(2)));
            input = in.readLine();
            int requestedAmount = Integer.parseInt(input);
            if(requestedAmount < (Integer.parseInt(product.get(2)) + 1)){ //check if num requested < num in stock at user's selected store
               //first, to create new order, determine new order number to be used (NEXTVAL on orderNumber sequence)
               //order format for reference: (orderNumber,customerID,storeID,productName,unitsOrdered,orderTime)
               //product listing format for reference: (storeID,productName,numberOfUnits,pricePerUnit)
               
               //get current date and time
               String timeQuery = "SELECT NOW()";
               List<List<String>> times = esql.executeQueryAndReturnResult(timeQuery);
               List<String> time = times.get(0);
               String timestamp = time.get(0);
               timestamp = timestamp.substring(0, timestamp.length() - 10);
               //System.out.print(timestamp);
               
               //confirm order
               System.out.printf("\nPlease confirm order:\n\tItem: %s\n\tQuantity: %d\n\nPlace order?\n1. Yes\n2. No, exit to main menu\n", product.get(1), requestedAmount);
               switch(readChoice()){
                  case 1: startover = false; break;
                  case 2: startover = true; break;
                  default : System.out.println("Unrecognized choice!"); break;
               }
               if(startover){
                  break;
               }
               //assemble and execute query
               productRequest = "INSERT INTO Orders VALUES (NEXTVAL(\'orders_orderNumber_seq\'),\'" + userID + "\',\'" + product.get(0) + "\',\'" + product.get(1) + "\',\'" + requestedAmount + "\',\'" + timestamp + "\');"; //create product request query
               esql.executeUpdate(productRequest);

               //update product listing's stock
               productStockUpdate = "UPDATE Product SET numberOfUnits = \'";
               productStockUpdate = productStockUpdate + (Integer.parseInt(product.get(2)) - requestedAmount) + "\' WHERE storeID = \'" + product.get(0) + "\' AND productName = \'" + product.get(1) + "\';";
               esql.executeUpdate(productStockUpdate);

               //print order confirmation
               System.out.print("\n\n\t\t\t=====Order Confirmation=====\n");
               query1 = "SELECT * FROM Orders O WHERE O.customerID = \'"; 
               query2 = "\' GROUP BY 1 ORDER BY 1 DESC LIMIT 1;";
               esql.executeQueryAndPrintResult(query1 + user.get(0) + query2);
               System.out.printf("\n\nPlace another order from this store?\nYour current store: %s\n1. Yes\n2. No, please exit to main menu\n", store.get(1));
               switch(readChoice()){
                  case 1: orderFlow = true; break;
                  case 2: orderFlow = false; break;
                  default : System.out.println("Unrecognized choice!"); break;
               }
            }
            else{
               System.out.printf("Cannot request more than %s units of %s \n\t(you entered: %d) \nPlease reenter product selection.\n", product.get(2), product.get(1), requestedAmount);
            }
         }
         
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }

   public static void viewRecentOrders(Retail esql) {
      try{
         String username = esql.getLoggedInUser();
         System.out.print("\n\n\t\t\t\t\t========Recent Orders========\n");
         String query1 = "SELECT O.storeID, S.name, O.productName, O.unitsOrdered, O.orderTime FROM Orders O, Store S, Users U WHERE O.storeID = S.storeID AND U.userID = O.customerID AND U.name = \'";
         String query2 = "\' GROUP BY O.storeID, S.name, O.productName, O.unitsOrdered, O.orderTime ORDER BY 5 DESC LIMIT 5;";
         String query = query1 + username + query2;
         //int rowCount = esql.executeQueryAndPrintResult(query);
         List<List<String>> recentOrders = esql.executeQueryAndReturnResult(query);
         List<String> order = new ArrayList<String>();

         //print orders
         System.out.print("\n|Product|\t\t\t|Amount|   |Store ID| |Store Name|\t\t\t|Date Ordered|");
         for(int i = 0; i < recentOrders.size(); i++){
            order = recentOrders.get(i);
            System.out.printf("\n%s\t %s\t\t%s\t%s %s", order.get(2), order.get(3), order.get(0), order.get(1), order.get(4));
         }
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   // ----------manager functions (always check if manager before executing)----------

   public static void updateProduct(Retail esql) {
      try{
         //Grab userID from logged in user
         String username = esql.getLoggedInUser();
         String getUser = "SELECT * FROM Users U WHERE U.name = \'";
         List<List<String>> users = esql.executeQueryAndReturnResult(getUser + username + "\';"); //grab users w username equal to current user (should return 1)
         List<String> user = users.get(0); //get first (should be only) user
         String userID =  user.get(0); //get userID from current user

         //initialize
         String input = "";
         String storeID = "";
         List<String> store = new ArrayList<String>();
         List<List<String>> stores = new ArrayList<List<String>>();
         List<String> product = new ArrayList<String>();
         List<List<String>> products = new ArrayList<List<String>>();
         String getStores = "";
         String prodName = "";
         String warehouseID = "";
         String numUnits = "";
         boolean isAdmin = false;
         int newQuantity = 0;
         Double newPrice = 0.0;
         boolean startover = false;

         //check if logged-in user is a manager or admin
         int storemanager = esql.executeQuery("SELECT * FROM Users U WHERE U.userID = \'" + userID + "\' AND (U.type = \'manager\' OR U.type = \'admin\');");
         if (storemanager < 1){
            System.out.print("\nERROR: Must be logged in as a manager or administrator to use this function. Exiting...\n\n");
            return;
         }
         if(Integer.parseInt(user.get(0)) == 1){
            //only the admin will have userID = 1
            isAdmin = true;
         }

         //managers must choose store from list of stores they manage, admin can choose any store
         getStores = "SELECT * FROM Store S WHERE S.managerID = \'" + userID + "\';";

         if(isAdmin){
            System.out.print("\n========Admin View========\n");
            getStores = "SELECT * FROM Store;";
         }
         
         //output list of stores that user can request to
         stores = esql.executeQueryAndReturnResult(getStores);
         System.out.print("\n|#|\t|Store name|\t\t\t|Latitude|\t|Longitude|");
         for(int i = 0; i < stores.size(); i++){
            store = stores.get(i);
            System.out.printf("\n%d\t%s\t%s\t%s", (i+1), store.get(1), store.get(2), store.get(3));
         }

         //select store, pass input to stores array index
         System.out.printf("\nPlease select a store (%d - %d): ", 1, stores.size());
         input = in.readLine();
         store = stores.get(Integer.parseInt(input) - 1);
         storeID = store.get(0);
         System.out.printf("\nYou selected store: %s\n", store.get(1));

         //get list of products from selected store
         
         //get current product listing
         String getProduct = "SELECT * FROM Product WHERE storeID = \'" + storeID + "\';";
         System.out.print("\n|#|\t|Product|\t\t\t|Price|\t|Quantity|");
         products = esql.executeQueryAndReturnResult(getProduct);
         for(int j = 0; j < products.size(); j++){
            product = products.get(j);
            System.out.printf("\n %d\t%s\t$%.2f\t%d", (j+1), product.get(1), Double.parseDouble(product.get(3)), Integer.parseInt(product.get(2)));
         }

         //product selection
         System.out.printf("\nPlease select a product (%d - %d): ", 1, products.size());
         input = in.readLine();
         product = products.get(Integer.parseInt(input) - 1);
         System.out.printf("\nYou selected: %s  $%.2f, %s", product.get(1), Double.parseDouble(product.get(3)), product.get(2));

         //update price or quantity
         System.out.print("\nUpdate quantity or price?\n\n\t1. Quantity\n\t2. Price\n\t3. Both Quantity and Price\n\t");
         switch(readChoice()){
            case 1: 
               System.out.print("\nEnter new quantity: ");
               newQuantity = Integer.parseInt(in.readLine());
               newPrice = Double.parseDouble(product.get(3));
               break;
            case 2:
               System.out.print("\nEnter new price ($#.##): $");
               newPrice = Double.parseDouble(in.readLine());
               newQuantity = Integer.parseInt(product.get(2));
               break;
            case 3:
               System.out.print("\nEnter new quantity: ");
               newQuantity = Integer.parseInt(in.readLine());
               System.out.print("\n\tEnter new price ($#.##): $");
               newPrice = Double.parseDouble(in.readLine());
               break;
            default: System.out.println("\nUnrecognized choice! Exiting to main menu..."); startover = true; break;
         }

         if(startover){
            return;
         }

         //display old and new entries side-by-side
         System.out.print("\nPlease confirm changes: \n");
         System.out.printf("Old: %s  $%.2f, %s\n", product.get(1), Double.parseDouble(product.get(3)), product.get(2));
         System.out.printf("New: %s  $%.2f, %s\n", product.get(1), newPrice, newQuantity);

         System.out.print("\n\t1. Confirm and exit\n\t2. Discard and exit\n\n\t");
         switch(readChoice()){
            case 1: break;
            case 2: startover = true; break;
            default: System.out.println("\nUnrecognized choice! Exiting to main menu..."); startover = true; break;
         }

         if(startover){
            return;
         }

         //confirm update
         String productUpdate = "UPDATE Product SET numberOfUnits = \'" + newQuantity + "\', pricePerUnit = \'" + newPrice + "\' WHERE storeID = \'" + product.get(0) + "\' AND productName = \'" + product.get(1) + "\';";
         esql.executeUpdate(productUpdate);
         System.out.print("\n\n========Update Confirmation========\n\n");
         String updatedEntry  = "SELECT * FROM Product P WHERE storeID = \'" + storeID + "\' AND productName = \'" + product.get(1) + "\';";
         esql.executeQueryAndPrintResult(updatedEntry);

         //log update if user is a manager
         if(!isAdmin){
            //get current date and time
            String timeQuery = "SELECT NOW()";
            List<List<String>> times = esql.executeQueryAndReturnResult(timeQuery);
            List<String> time = times.get(0);
            String timestamp = time.get(0);
            timestamp = timestamp.substring(0, timestamp.length() - 10);
            String newUpdate = "INSERT INTO ProductUpdates VALUES(NEXTVAL(\'productupdates_updateNumber_seq\'),\'" + userID + "\',\'" + product.get(0) + "\',\'" + product.get(1) + "\',\'" + timestamp + "\');";
            System.out.println(newUpdate);
            esql.executeUpdate(newUpdate);

            //confirm log update
            System.out.print("\n\n======Update Log Confirmation======\n\n");
            String updateLogEntry = "SELECT * FROM ProductUpdates ORDER BY 1 DESC LIMIT 1";
            esql.executeQueryAndPrintResult(updateLogEntry);
         }

      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   public static void viewRecentUpdates(Retail esql) {
      try{
         String username = esql.getLoggedInUser();
         //check if logged-in user is a manager or admin
         int storemanager = esql.executeQuery("SELECT * FROM Users U WHERE U.name = \'" + username + "\' AND (U.type = \'manager\' OR U.type = \'admin\');");
         if (storemanager < 1){
            System.out.print("\nERROR: Must be logged in as a manager or administrator to use this function. Exiting...\n\n");
            return;
         }
         String query1 = "SELECT * FROM ProductUpdates P WHERE EXISTS (SELECT * FROM Users U, Store S WHERE (U.type = \'admin\' OR U.userID = S.managerID) AND U.name = \'";
         String query2 = "\') GROUP BY P.updateNumber, P.managerID, P.storeID, P.productName, P.updatedOn ORDER BY 5 DESC LIMIT 5";
         String query = query1 + username + query2;
         //int rowCount = esql.executeQueryAndPrintResult(query);
         List<List<String>> recentUpdates = esql.executeQueryAndReturnResult(query);
         List<String> update = new ArrayList<String>();

         //print updates
         System.out.print("\n\n\t\t\t\t========Recent Updates========\n");
         System.out.print("\n|Update #| |Manager ID| |Store ID| |Product Name|\t\t   |Date Updated|");
         for(int i = 0; i < recentUpdates.size(); i++){
            update = recentUpdates.get(i);
            System.out.printf("\n    %s\t\t%s\t    %s\t    %s %s", update.get(0), update.get(1), update.get(2), update.get(3), update.get(4));
         }

      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   public static void viewPopularProducts(Retail esql) {
      try{
         String username = esql.getLoggedInUser();
         //check if logged-in user is a manager or admin
         int storemanager = esql.executeQuery("SELECT * FROM Users U WHERE U.name = \'" + username + "\' AND (U.type = \'manager\' OR U.type = \'admin\');");
         if (storemanager < 1){
            System.out.print("\nERROR: Must be logged in as a manager or administrator to use this function. Exiting...\n\n");
            return;
         }
         String query1 = "SELECT * FROM (SELECT P.productName, COUNT(O.productName) AS total_orders FROM Product P, Orders O, Users U, Store S WHERE (U.userID = S.managerID OR U.type = \'admin\') AND P.storeID = S.storeID AND O.storeID = S.storeID AND P.productName = O.productName AND U.name = \'";
         String query2 = "\' GROUP BY P.productName) AS popular_products ORDER BY 2 DESC LIMIT 5;";
         String query = query1 + username + query2;
         //int rowCount = esql.executeQueryAndPrintResult(query);
         List<List<String>> popularProducts = esql.executeQueryAndReturnResult(query);
         List<String> product = new ArrayList<String>();

         //print products
         System.out.print("\n\t=====Popular Products=====\n");
         System.out.print("\n|Product|\t\t |Total Orders|");
         for(int i = 0; i < popularProducts.size(); i++){
            product = popularProducts.get(i);
            System.out.printf("\n%s %s", product.get(0), product.get(1));
         }
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   public static void viewPopularCustomers(Retail esql) {
      try{
         String username = esql.getLoggedInUser();
         //check if logged-in user is a manager
         int storemanager = esql.executeQuery("SELECT * FROM Users U WHERE U.name = \'" + username + "\' AND (U.type = \'manager\' OR U.type = \'admin\');");
         if (storemanager < 1){
            System.out.print("\nERROR: Must be logged in as a manager or administrator to use this function. Exiting...\n\n");
            return;
         }
         String query1 = "SELECT * FROM (SELECT O.customerID, SUM(O.unitsOrdered) AS total_units_ordered FROM Product P, Orders O, Users U, Store S WHERE (U.userID = S.managerID OR U.type = \'admin\') AND P.storeID = S.storeID AND O.storeID = S.storeID AND P.productName = O.productName AND U.name = \'";
         String query2 = "\' GROUP BY O.customerID) AS popular_customers ORDER BY 2 DESC LIMIT 5;";
         String query = query1 + username + query2;
         List<List<String>> popularCustomers = esql.executeQueryAndReturnResult(query);
         List<String> customer = new ArrayList<String>();
         System.out.print("\n\t=====Popular Customers=====\n");
         System.out.print("\n|Customer ID|\t\t |# Products Ordered|");
         for(int i = 0; i < popularCustomers.size(); i++){
            customer = popularCustomers.get(i);
            System.out.printf("\n   %s \t\t\t\t%s", customer.get(0), customer.get(1));
         }
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

   public static void placeProductSupplyRequests(Retail esql) {
      try{
         //Grab userID from logged in user
         String username = esql.getLoggedInUser();
         String getUser = "SELECT * FROM Users U WHERE U.name = \'";
         List<List<String>> users = esql.executeQueryAndReturnResult(getUser + username + "\';"); //grab users w username equal to current user (should return 1)
         List<String> user = users.get(0); //get first (should be only) user
         String userID =  user.get(0); //get userID from current user

         //initialize
         String input = "";
         String storeID = "";
         List<String> store = new ArrayList<String>();
         List<List<String>> stores = new ArrayList<List<String>>();
         String getStores = "";
         String prodName = "";
         String warehouseID = "";
         String numUnits = "";
         boolean isAdmin = false;

         //check if logged-in user is a manager or admin
         int storemanager = esql.executeQuery("SELECT * FROM Users U WHERE U.userID = \'" + userID + "\' AND (U.type = \'manager\' OR U.type = \'admin\');");
         if (storemanager < 1){
            System.out.print("\nERROR: Must be logged in as a manager or administrator to use this function. Exiting...\n\n");
            return;
         }
         if(Integer.parseInt(user.get(0)) == 1){
            //only the admin will have userID = 1
            isAdmin = true;
         }

         //managers must choose store from list of stores they manage, admin can choose any store
         getStores = "SELECT * FROM Store S WHERE S.managerID = \'" + userID + "\';";

         if(isAdmin){
            System.out.print("\n========Admin View========\n");
            getStores = "SELECT * FROM Store;";
         }
         
         //output list of stores that user can request to
         stores = esql.executeQueryAndReturnResult(getStores);
         System.out.print("\n|#|\t|Store name|\t\t\t|Latitude|\t|Longitude|");
         for(int i = 0; i < stores.size(); i++){
            store = stores.get(i);
            System.out.printf("\n%d\t%s\t%s\t%s", i+1, store.get(1), store.get(2), store.get(3));
         }

         //select store, pass input to stores array index
         System.out.printf("\nPlease select a store (%d - %d): ", 1, stores.size());
         input = in.readLine();
         store = stores.get(Integer.parseInt(input) - 1);
         storeID = store.get(0);
         System.out.printf("\nYou selected store: %s\n", store.get(1));

         //product entry

         //list products at store
         String getProduct = "SELECT * FROM Product WHERE storeID = \'" + storeID + "\';";
         List<List<String>> products = esql.executeQueryAndReturnResult(getProduct);
         List<String> product = new ArrayList<String>();
         System.out.print("\n\n\t========Available Products========\n\n");
         System.out.print("\n|#|\t|Product|\t\t\t|Price|\t|Quantity|");
         for(int j = 0; j < products.size(); j++){
            product = products.get(j);
            System.out.printf("\n %d\t%s\t$%.2f\t%d", (j+1), product.get(1), Double.parseDouble(product.get(3)), Integer.parseInt(product.get(2)));
         }
         System.out.printf("\nWhich product would you like to resupply? (%d - %d): ", 1, products.size());
         input = in.readLine();
         product = products.get(Integer.parseInt(input) - 1);
         System.out.printf("\nYou selected: %s  $%.2f, %s\n", product.get(1), Double.parseDouble(product.get(3)), product.get(2));

         System.out.print("\n\tPlease enter desired # of units: ");
         numUnits = in.readLine();
         System.out.print("\nEnter warehouse ID to confirm request: ");
         warehouseID = in.readLine();

         //insert new request
         String supplyRequest = "INSERT INTO ProductSupplyRequests VALUES(NEXTVAL(\'productsupplyrequests_requestNumber_seq\'),\'" + userID + "\',\'" + warehouseID + "\',\'" + store.get(0) + "\',\'" + product.get(1) + "\',\'" + numUnits + "\');";
         //System.out.println(supplyRequest);
         esql.executeUpdate(supplyRequest);

         
         //update product listing's stock
         String productStockUpdate = "UPDATE Product SET numberOfUnits = \'";
         int newStockAmount = Integer.parseInt(product.get(2)) + Integer.parseInt(numUnits);
         productStockUpdate = productStockUpdate + newStockAmount + "\' WHERE storeID = \'" + product.get(0) + "\' AND productName = \'" + product.get(1) + "\';";
         esql.executeUpdate(productStockUpdate);
         
         //print request and update confirmation notices
         System.out.print("\n\n\t\t\t========Request Confirmation=======\n");
         String query1 = "SELECT * FROM ProductSupplyRequests GROUP BY 1 ORDER BY 1 DESC LIMIT 1;";
         esql.executeQueryAndPrintResult(query1);
         System.out.print("\n\n\t\t\t=====Stock Update Confirmation=====\n");
         esql.executeQueryAndPrintResult("SELECT * FROM Product WHERE storeID = \'" + product.get(0) + "\' AND productName = \'" + product.get(1) + "\';");
      }catch(Exception e){
         System.err.println(e.getMessage());
      }
   }

}//end Retail

