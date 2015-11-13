
import javax.swing.JOptionPane;


public class CreateDDLSqlServer extends EdgeConvertCreateDDL {

    protected String databaseName;                                            //the name of databases
    protected String[] strDataType = {"VARCHAR", "BOOL", "INT", "DOUBLE"};    //this array is for determining how MySQL refers to datatypes
    protected StringBuffer sb;

    public CreateDDLSqlServer() {
    }

    public CreateDDLSqlServer(EdgeTable[] inputTables, EdgeField[] inputFields) {
        super(inputTables, inputFields); //call EdgeConvert supers
        this.sb = new StringBuffer();
    }

    @Override
    public String getDatabaseName() {
        return this.databaseName;
    }

    @Override
    public String getProductName() {
        return productNames.SqlServer.toString();
    }

    @Override
    public String getSQLString() {
        createDDL();
        return this.sb.toString();
    }

    @Override
    public void createDDL() {

    }
    
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
}
