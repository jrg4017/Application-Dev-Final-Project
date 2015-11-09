import java.util.StringTokenizer;

public class EdgeField {
/*********************************************************************/
/********* CONSTANT **************************************************/  
   public static final int VARCHAR_DEFAULT_LENGTH = 1;
/*********************************************************************/
/********* ATTRIBUTES ************************************************/     
   private int numFigure, tableID = 0, tableBound = 0, fieldBound = 0, dataType = 0, varcharValue = VARCHAR_DEFAULT_LENGTH;
   private String name, defaultValue = "";
   private boolean disallowNull = false, isPrimaryKey = false;
   private static String[] strDataType = {"Varchar", "Boolean", "Integer", "Double"};
/*********************************************************************/
/********* CONSTRUCTOR ***********************************************/    
   /**
   * receive the input string and set the appropiate variables
   */
   public EdgeField(String inputString) {
      StringTokenizer st = new StringTokenizer(inputString, EdgeConvertFileParser.DELIM);
      this.numFigure = Integer.parseInt(st.nextToken());
      this.name = st.nextToken();
   }//end EdgeField
/*********************************************************************/
/********* ACCESSORS *************************************************/   
   /**
   * get the numFigure
   * @return this.numFigure */
   public int getNumFigure() { return this.numFigure; }//end getNumFigure
   
   /**
   * get the names
   * @return this.name */
   public String getName() { return this.name; }//end getName
 
   /**
   * gets the table id
   * @return this.tableID */
   public int getTableID() { return this.tableID; }//end getTableID
 
 /**
   * get table bound 
   * @return this.tableBound int */
   public int getTableBound() { return this.tableBound; }//end getTableBound

  /**
   * get the field bound
   * @return fieldBound int */
   public int getFieldBound() { return fieldBound; }//end getFieldBound
   
  /**
   * gets the disallow null
   * @return this.disallowNull boolean */
   public boolean getDisallowNull() { return this.disallowNull; }//end disallowNull
   
   /**
   * gets whether the key is a primary key
   * @return this.isPrimaryKey boolean */
   public boolean getIsPrimaryKey() { return this.isPrimaryKey; }//end getIsPrimaryKey
   
    /**
   *gets the default value
   * @return this.default String */
   public String getDefaultValue() { return this.defaultValue; }//end getDefaultValue
   
   /**
   * get varchar value 
   * @return this.varcharValue int */
   public int getVarcharValue() {  return this.varcharValue; }//end getVarcharValue
   
   /**
   * get data type set
   * @return this.dataType int */
   public int getDataType() { return this.dataType; }//end getDataType
   
   /**
   * get the data type in string
   * @return this.strDataType String[]
   */
   public static String[] getStrDataType() {
      return this.strDataType;
   }//end getStrDataType
/*********************************************************************/
/********* MUTATORS **************************************************/   
   /**
   * set table ID 
   * @param value int */
   public void setTableID(int value) { this.tableID = value; }//end setTableID
   
   /**
   * set the table bound 
   * @param value int */
   public void setTableBound(int value) { this.tableBound = value; }//end setTableBound
   
   /**
	* Set field bound
    * @param value int */
   public void setFieldBound(int value) { this.fieldBound = value; }//end setFieldBound

   /**
   * set the disallow null 
   * @param value boolean */
   public void setDisallowNull(boolean value) { this.disallowNull = value; }//end setDisallowNull
  
  /**
  * set if the key is a primary key
  * @param value boolean */
   public void setIsPrimaryKey(boolean value) { this.isPrimaryKey = value; }//end setIsPrimaryKey 
   
   /**
   * set the default value
   * @param value String */
   public void setDefaultValue(String value) { this.defaultValue = value; }//end setDefaultValue
   /**
   * set varchar value 
   * @param value int */
   public void setVarcharValue(int value) {
      if (value > 0) {
         this.varcharValue = value;
      }
   }//end setVarcharValue
   
   /**
   * set data type in obj 
   * @param value int */
   public void setDataType(int value) {
      if (value >= 0 && value < strDataType.length) {
         this.dataType = value;
      }
   }//end setDataType
   
/*********************************************************************/
/********* METHODS ***************************************************/  
   /**
   * return the variables in the form of a string with a | separator
   * @return String
   */
   public String toString() {
      return this.numFigure + EdgeConvertFileParser.DELIM +
      this.name + EdgeConvertFileParser.DELIM +
      this.tableID + EdgeConvertFileParser.DELIM +
      this.tableBound + EdgeConvertFileParser.DELIM +
      this.fieldBound + EdgeConvertFileParser.DELIM +
      this.dataType + EdgeConvertFileParser.DELIM +
      this.varcharValue + EdgeConvertFileParser.DELIM +
      this.isPrimaryKey + EdgeConvertFileParser.DELIM +
      this.disallowNull + EdgeConvertFileParser.DELIM +
      this.defaultValue;
   }//end toString
}//end EdgeField
