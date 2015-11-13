
import javax.swing.JOptionPane;


public class CreateDDLSqlServer extends EdgeConvertCreateDDL {
/*********************************************************************/
/********* ATTRIBUTES ************************************************/
    protected String databaseName;                                            //the name of databases
    protected String[] strDataType = {"VARCHAR", "BOOL", "INT", "DOUBLE"};    //this array is for determining how SQL refers to datatypes
    protected StringBuffer sb;
/*********************************************************************/
/********* CONSTRUCTORS **********************************************/
   /**
   * default constructor with empty arg list for to allow output dir to be set before there are table and field objects
   */
    public CreateDDLSqlServer() { } //end default constructor
    
   /**
   * constructor 
   * @param inputTables EdgeTable[]
   * @param inputFields EdgeTable[]
   */
    public CreateDDLSqlServer(EdgeTable[] inputTables, EdgeField[] inputFields) {
        super(inputTables, inputFields); //call EdgeConvert supers
        this.sb = new StringBuffer();
    } //end CreateDDLSqlServer

/*********************************************************************/
/********* ACCESSORS *************************************************/   
   /**
   * gets the database name
   * @return this.databaseName
   */
    @Override
    public String getDatabaseName() { return this.databaseName; } //end getDatabaseName()

   /**
   * gets the product name
   * @return string
   */
    @Override
    public String getProductName() { return productNames.SqlServer.toString(); } //end getProductName()
    
 /**
   * creates the DDL & then get the SQL string generated
   * @return string
   */
    @Override
    public String getSQLString() {
        createDDL();
        return this.sb.toString();
    } //end getSQLString()

/*********************************************************************/
/********* METHODS ***************************************************/

   /**
   * creates the DDL (data description language) for the program
   */
    @Override
    public void createDDL() {
       EdgeConvertGUI.setReadSuccess(true);
       this.databaseName = generateDatabaseName();
       this.databaseName = generateDatabaseName();
       this.sb.append("CREATE DATABASE " + this.databaseName + ";\r\n");
       this.sb.append("USE " + this.databaseName + ";\r\n");
    } //end createDDL
    
    /**
     * converts a String boolean used in SqlServer to an int SqlServer uses '1' and '0'
     * for boolean types
     *
     * @return int
     */
    protected int convertStrBooleanToInt(String input) {
        if (input.equals("true")) {
            return 1;
        } else {
            return 0;
        }
    }//end convertStrBooleanToInt
   
   /**
   * generates the database name by prompting user for the name
   * @return this.databaseName String 
   */
   public String generateDatabaseName() { 
      String dbNameDefault = "SqlServerDB"; //default for this.databaseName
	  
      do { //asks for the database name
         this.databaseName = (String)JOptionPane.showInputDialog(
                       null,
                       "Enter the database name:",
                       "Database Name",
                       JOptionPane.PLAIN_MESSAGE,
                       null,
                       null,
                       dbNameDefault);
         if (this.databaseName == null) {
            EdgeConvertGUI.setReadSuccess(false);
            return "";
         }
         if (this.databaseName.equals("")) {
            JOptionPane.showMessageDialog(null, "You must select a name for your database.");
         }
      } while (this.databaseName.equals(""));
      return this.databaseName;
   }//end generateDatabaseName
}///end CreateDDLSqlServer
