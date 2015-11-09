import java.awt.*;
import java.awt.event.*;
import javax.swing.*;   
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class CreateDDLMySQL extends EdgeConvertCreateDDL {
/*********************************************************************/
/********* ATTRIBUTES ************************************************/
   protected String databaseName;                                            //the name of databases
   protected String[] strDataType = {"VARCHAR", "BOOL", "INT", "DOUBLE"};    //this array is for determining how MySQL refers to datatypes
   protected StringBuffer sb;											     //
/*********************************************************************/
/********* CONSTRUCTORS **********************************************/
   /**
   * default constructor with empty arg list for to allow output dir to be set before there are table and field objectss
   */
   public CreateDDLMySQL() { } //end default constructor
   
   /**
   * constructor 
   * @param inputTables EdgeTable[]
   * @param inputFields EdgeTable[]
   */
   public CreateDDLMySQL(EdgeTable[] inputTables, EdgeField[] inputFields) {
      super(inputTables, inputFields); //call EdgeConvert supers
      this.sb = new StringBuffer();
   } //end CreateDDLMySQL
   
/*********************************************************************/
/********* ACCESSORS *************************************************/   
   /**
   * gets the database name
   * @return databaseName
   */
   public String getDatabaseName() {
      return databaseName;
   }//end getDatabaseName
   
   /**
   * gets the product name
   * @return string
   */
   public String getProductName() {
      return "MySQL"; 
   }//end getProductName

   /**
   * creates the DDL & then get the SQL string generated
   * @return string
   */
   public String getSQLString() {
      createDDL();
      return this.sb.toString();
   }//end getSQLString
 
/*********************************************************************/
/********* METHODS ***************************************************/

   /**
   * creates the DDL (data description language) for the program
   */
   public void createDDL() {
      EdgeConvertGUI.setReadSuccess(true);
      databaseName = generateDatabaseName();
      this.sb.append("CREATE DATABASE " + databaseName + ";\r\n");
      this.sb.append("USE " + databaseName + ";\r\n");
      for (int boundCount = 0; boundCount <= maxBound; boundCount++) { //process tables in order from least dependent (least number of bound tables) to most dependent
         for (int tableCount = 0; tableCount < numBoundTables.length; tableCount++) { //step through list of tables
            if (numBoundTables[tableCount] == boundCount) { //
               this.sb.append("CREATE TABLE " + tables[tableCount].getName() + " (\r\n");
               int[] nativeFields = tables[tableCount].getNativeFieldsArray();
               int[] relatedFields = tables[tableCount].getRelatedFieldsArray();
               boolean[] primaryKey = new boolean[nativeFields.length];
               int numPrimaryKey = 0;
               int numForeignKey = 0;
               for (int nativeFieldCount = 0; nativeFieldCount < nativeFields.length; nativeFieldCount++) { //print out the fields
                  EdgeField currentField = getField(nativeFields[nativeFieldCount]);
                  this.sb.append("\t" + currentField.getName() + " " + strDataType[currentField.getDataType()]);
                  if (currentField.getDataType() == 0) { //varchar
                     this.sb.append("(" + currentField.getVarcharValue() + ")"); //append varchar length in () if data type is varchar
                  }
                  if (currentField.getDisallowNull()) {
                     this.sb.append(" NOT NULL");
                  }
                  if (!currentField.getDefaultValue().equals("")) {
                     if (currentField.getDataType() == 1) { //boolean data type
                        this.sb.append(" DEFAULT " + convertStrBooleanToInt(currentField.getDefaultValue()));
                     } else { //any other data type
                        this.sb.append(" DEFAULT " + currentField.getDefaultValue());
                     }
                  }
                  if (currentField.getIsPrimaryKey()) {
                     primaryKey[nativeFieldCount] = true;
                     numPrimaryKey++;
                  } else {
                     primaryKey[nativeFieldCount] = false;
                  }
                  if (currentField.getFieldBound() != 0) {
                     numForeignKey++;
                  }
                  this.sb.append(",\r\n"); //end of field
               }
               if (numPrimaryKey > 0) { //table has primary key(s)
                  this.sb.append("CONSTRAINT " + tables[tableCount].getName() + "_PK PRIMARY KEY (");
                  for (int i = 0; i < primaryKey.length; i++) {
                     if (primaryKey[i]) {
                        this.sb.append(getField(nativeFields[i]).getName());
                        numPrimaryKey--;
                        if (numPrimaryKey > 0) {
                           this.sb.append(", ");
                        }
                     }
                  }
                  this.sb.append(")");
                  if (numForeignKey > 0) {
                     this.sb.append(",");
                  }
                  this.sb.append("\r\n");
               }
               if (numForeignKey > 0) { //table has foreign keys
                  int currentFK = 1;
                  for (int i = 0; i < relatedFields.length; i++) {
                     if (relatedFields[i] != 0) {
                        this.sb.append("CONSTRAINT " + tables[tableCount].getName() + "_FK" + currentFK +
                                  " FOREIGN KEY(" + getField(nativeFields[i]).getName() + ") REFERENCES " +
                                  getTable(getField(nativeFields[i]).getTableBound()).getName() + "(" + getField(relatedFields[i]).getName() + ")");
                        if (currentFK < numForeignKey) {
                           this.sb.append(",\r\n");
                        }
                        currentFK++;
                     }
                  }
                  this.sb.append("\r\n");
               }
               this.sb.append(");\r\n\r\n"); //end of table
            }
         }
      }
   }//end createDDL
   
	/**
	* converts a String boolean used in MySQL to an int
	* MySQL uses '1' and '0' for boolean types
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
   * @return databaseName String 
   */
   public String generateDatabaseName() { 
      String dbNameDefault = "MySQLDB"; //default for databaseName
	  
      do { //asks for the database name
         databaseName = (String)JOptionPane.showInputDialog(
                       null,
                       "Enter the database name:",
                       "Database Name",
                       JOptionPane.PLAIN_MESSAGE,
                       null,
                       null,
                       dbNameDefault);
         if (databaseName == null) {
            EdgeConvertGUI.setReadSuccess(false);
            return "";
         }
         if (databaseName.equals("")) {
            JOptionPane.showMessageDialog(null, "You must select a name for your database.");
         }
      } while (databaseName.equals(""));
      return databaseName;
   }//end generateDatabaseName
   
   
}//end EdgeConvertCreateDDL
