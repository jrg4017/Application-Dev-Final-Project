import java.io.*;		
import java.util.*;		
import javax.swing.*;

public class EdgeConvertFileParser {
/*********************************************************************/
/********* ATTRIBUTES ************************************************/
   private File parseFile;											 //
   private BufferedReader br;										 //
   private String currentLine;										 //
   private ArrayList alTables = new ArrayList(), alFields = new ArrayList(), alConnectors = new ArrayList();//when object is constructed, default
   private EdgeTable[] tables;										                  //
   private EdgeField[] fields;										 				  //
   private EdgeConnector[] connectors;												  //
   private int numFigure = 0, numConnector = 0;						  // all set to zero as default when object is constructed
   private int numNativeRelatedFields;    //not being used
/*************************************************************************************/
/********* CONSTANTS *****************************************************************/
   public static final String EDGE_ID = "EDGE Diagram File"; 						  //first line of .edg files should be this
   public static final String SAVE_ID = "EdgeConvert Save File"; 					  //first line of save files should be this
   public static final String DELIM = "|";										      //separator
/*********************************************************************/
/********* CONSTRUCTORS **********************************************/
   /**
   * accepts a file, sets it to parseFile and opens the file
     * @param constructorFile
   */
   public EdgeConvertFileParser(File constructorFile) {
      this.parseFile = constructorFile;
      this.openFile(parseFile);
   }//end EdgeConvertFileParser
/*********************************************************************/
/********* ACCESSORS *************************************************/
	/**
   * gets the Edge Tables
   * @return this.tables EdgeTable */
   public EdgeTable[] getEdgeTables() {  return this.tables;  }

   /**
   * gets the EdgeFields
   * @return this.field EdgeField */
   public EdgeField[] getEdgeFields() { return this.fields;  }

/*********************************************************************/
/********* METHODS ***************************************************/
   /**
   * parses through the Edge file and calls the correct function
    * depending on whether it's a connector or figure
    * @see isFigure()
    * @see isConnector()
   * @throw IOException
   */
   public void parseEdgeFile() throws IOException {
      while ((currentLine = br.readLine()) != null) {
         currentLine = currentLine.trim();
         final boolean isFigure = currentLine.startsWith("Figure ");
         final boolean isConnector = currentLine.startsWith("Connector ");

         //call the appropiate function if the boolean is true
         if(isFigure) isFigure();
         else if (isConnector) isConnector();

      } // while()
   } //end parseEdgeFile()

   /**
    * trimLines the last line of the current br.readLine()
    * @param numOfTimes int - number of times to trimLine the
    * @return rtnStr String
     */
   public String trimLine(int numOfTimes) throws IOException{
      String rtnStr="";
      for(int i = 0; i < numOfTimes; i++){
         rtnStr = br.readLine().trim();
      }
      return rtnStr;
   }//end trimLine

    /**
     * If it's a relation, print error to get user to normalize it
     * @param style
     */
    public void isRelation(String style){
        if(style.startsWith("Relation")) {
            JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile + "\ncontains relations.  Please resolve them and try again.");
            EdgeConvertGUI.setReadSuccess(false);
        }
    }//end isRelations

    /**
     * checks to see if text is blank and print out error
     * @param text
     */
    public void isBlank(String text) {
        if (text.equals("")) {
            JOptionPane.showMessageDialog(null, "There are entities or attributes with blank names in this diagram.\nPlease provide names for them and try again.");
            EdgeConvertGUI.setReadSuccess(false);
        }
    }//end isBlank

    public boolean lookForUnderlined() throws IOException {
        do { //advance to end of record, look for whether the text is underlined
            currentLine = trimLine(1);
            final boolean isAUnderlined = currentLine.startsWith("TypeUnderl");
            if (isAUnderlined) {
                return true;
            }
        } while (!currentLine.equals("}")); // this is the end of a Figure entry

        return false;
    }

    /**
     * if a duplicate, print error
     * if an entity, create an EdgeTable object and add to alTables
     * @param text
     */
    public void isEntity(String text){
        if (isTableDup(text)) {
            JOptionPane.showMessageDialog(null, "There are multiple tables called " + text + " in this diagram.\nPlease rename all but one of them and try again.");
            EdgeConvertGUI.setReadSuccess(false);
        }
        alTables.add(new EdgeTable(numFigure + DELIM + text));
    }//end isEntity

    /**
     * if an attribute, create a EdgeField object and add to alField
     * @param text
     * @param isUnderlined
     */
    public void isAttribute(String text, boolean isUnderlined){
        EdgeField tempField = new EdgeField(numFigure + DELIM + text);
        tempField.setIsPrimaryKey(isUnderlined);
        alFields.add(tempField);
    }//end isAttribute

    /**
     * looks to see if the escape is greater than zero
     * @param text
     * @param escape
     * @return String
     */
    public String isEAboveZero(String text, int escape){
        if (escape > 0) {
            return text.substring(0, escape); //Edge denotes a line break as "\line", disregard anything after a backslash
        }
        return text;
    }//end isEAboveZero
    
   
    /**
    * if it is a figure, check to see what is entity, relation, attribute, etc
    */
   public void isFigure() throws IOException{
       boolean isEntity = false, isAttribute = false,  isUnderlined = false;       // all set to false when object is created

       numFigure = Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1)); //get the Figure number
       currentLine = trimLine(2); // this should be "{

       final boolean isNStyle = !currentLine.startsWith("Style");

       if (!isNStyle) { // this is to weed out other Figures, like Labels, if it's isNStyle (therefore !N
            String style = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")); //get the Style parameter

           final boolean isAEntity = style.startsWith("Entity");
           final boolean isAnAttribute = style.startsWith("Attribute");

           isRelation(style); //presence of Relations implies lack of normalization
           if (isAEntity) isEntity = true;
           if (isAnAttribute) isAttribute = true;

           currentLine = trimLine(1); //this should be Text
           String text = currentLine.substring(currentLine.indexOf("\"") + 1, currentLine.lastIndexOf("\"")).replaceAll(" ", ""); //get the Text parameter

           isBlank(text);

           int escape = text.indexOf("\\");
           text = isEAboveZero(text, escape);

           isUnderlined = lookForUnderlined();

           if (isEntity) isEntity(text); //create a new EdgeTable object and add it to the alTables ArrayList
           if (isAttribute) isAttribute(text, isUnderlined); //create a new EdgeField object and add it to the alFields ArrayList
         }
   }//end isFigure

    /**
     * if it's a connector, grab necessary information
     * @throws IOException
     */
   public void isConnector() throws IOException{
      //this is the start of a Connector entry
         String endStyle1, endStyle2;
         int endPoint1, endPoint2;
         numConnector = parseInt(); //get the Connector number
         currentLine = trimLine(3); // this should be "{"; not interested in Style; Figure1
         endPoint1 = parseInt();
         currentLine = trimLine(1); // Figure2
         endPoint2 = parseInt();
         currentLine = trimLine(5); // not interested in EndPoint1,EndPoint2,SuppressEnd1,SuppressEnd2, and End1
         endStyle1 = subString("\"","\""); //get the End1 parameter
         currentLine = trimLine(1); // End2
         endStyle2 = subString("\"","\""); //get the End2 parameter

         do { //advance to end of record
            currentLine = trimLine(1);
         } while (!currentLine.equals("}")); // this is the end of a Connector entry

         alConnectors.add(new EdgeConnector(numConnector + DELIM + endPoint1 + DELIM + endPoint2 + DELIM + endStyle1 + DELIM + endStyle2));
     // if("Connector")
   }//end is Connector

    /**
     * returns the int of the substring that is grabbed
     * @return int
     */
   public int parseInt(){
       return Integer.parseInt(currentLine.substring(currentLine.indexOf(" ") + 1));
   }//end parseInt

    public String subString(String start, String end){
        if(end.equals("")) return currentLine.substring(currentLine.indexOf(start));
        return currentLine.substring(currentLine.indexOf(start) + 1, currentLine.lastIndexOf(end));
    }//end subString
   
   /**
   * identify nature of Connector endpoints
   */

   private void resolveConnectors() {
      int endPoint1, endPoint2;
      int fieldIndex = 0, table1Index = 0, table2Index = 0;
      for (int cIndex = 0; cIndex < connectors.length; cIndex++) {
         endPoint1 = connectors[cIndex].getEndPoint1();
         endPoint2 = connectors[cIndex].getEndPoint2();
         fieldIndex = -1;
         for (int fIndex = 0; fIndex < fields.length; fIndex++) { //search fields array for endpoints
          //Simplifying a complex expression
            if (endPoint1 == fields[fIndex].getNumFigure()) { //found endPoint1 in fields array
               connectors[cIndex].setIsEP1Field(true); //set appropriate flag
               fieldIndex = fIndex; //identify which element of the fields array that endPoint1 was found in
            }
            if (endPoint2 == fields[fIndex].getNumFigure()) { //found endPoint2 in fields array
               connectors[cIndex].setIsEP2Field(true); //set appropriate flag
               fieldIndex = fIndex; //identify which element of the fields array that endPoint2 was found in
            }
         }
         for (int tIndex = 0; tIndex < tables.length; tIndex++) { //search tables array for endpoints
            if (endPoint1 == tables[tIndex].getNumFigure()) { //found endPoint1 in tables array
               connectors[cIndex].setIsEP1Table(true); //set appropriate flag
               table1Index = tIndex; //identify which element of the tables array that endPoint1 was found in
            }
            if (endPoint2 == tables[tIndex].getNumFigure()) { //found endPoint1 in tables array
               connectors[cIndex].setIsEP2Table(true); //set appropriate flag
               table2Index = tIndex; //identify which element of the tables array that endPoint2 was found in
            }
         }

         final boolean isField = connectors[cIndex].getIsEP1Field() && connectors[cIndex].getIsEP2Field();
         final boolean isTable = connectors[cIndex].getIsEP1Table() && connectors[cIndex].getIsEP2Table();
         
         if (isField) { //both endpoints are fields, implies lack of normalization
            JOptionPane.showMessageDialog(null, "The Edge Diagrammer file\n" + parseFile + "\ncontains composite attributes. Please resolve them and try again.");
            EdgeConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
            break; //stop processing list of Connectors
         }

         if (isTable) { //both endpoints are tables
            final boolean isManyR = (connectors[cIndex].getEndStyle1().indexOf("many") >= 0) && (connectors[cIndex].getEndStyle2().indexOf("many") >= 0);
            if (isManyR) { //the connector represents a many-many relationship, implies lack of normalization
               JOptionPane.showMessageDialog(null, "There is a many-many relationship between tables\n\"" + tables[table1Index].getName() + "\" and \"" + tables[table2Index].getName() + "\"" + "\nPlease resolve this and try again.");
               EdgeConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
               break; //stop processing list of Connectors
            } else { //add Figure number to each table's list of related tables
               tables[table1Index].addRelatedTable(tables[table2Index].getNumFigure());
               tables[table2Index].addRelatedTable(tables[table1Index].getNumFigure());
               continue; //next Connector
            }
         }

         final boolean nTableAssigned = fieldIndex >=0 && fields[fieldIndex].getTableID() == 0;
         final boolean isETable1 = connectors[cIndex].getIsEP1Table();
         final boolean isTableAssigned = fieldIndex >=0;
         if (nTableAssigned) { //field has not been assigned to a table yet
            if (isETable1) { //endpoint1 is the table
               tables[table1Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
               fields[fieldIndex].setTableID(tables[table1Index].getNumFigure()); //tell the field what table it belongs to
            } else { //endpoint2 is the table
               tables[table2Index].addNativeField(fields[fieldIndex].getNumFigure()); //add to the appropriate table's field list
               fields[fieldIndex].setTableID(tables[table2Index].getNumFigure()); //tell the field what table it belongs to
            }
         } else if (isTableAssigned) { //field has already been assigned to a table
            JOptionPane.showMessageDialog(null, "The attribute " + fields[fieldIndex].getName() + " is connected to multiple tables.\nPlease resolve this and try again.");
            EdgeConvertGUI.setReadSuccess(false); //this tells GUI not to populate JList components
            break; //stop processing list of Connectors
         }
      } // connectors for() loop
   } //end resolveConnectors

   /**
   * parse the save file of the edge
   * @throws IOException
   */

   public void parseSaveFile() throws IOException {
      StringTokenizer stTables, stNatFields, stRelFields, stNatRelFields;
      EdgeTable tempTable;
      currentLine = br.readLine();
      currentLine = br.readLine(); 
      while (currentLine.startsWith("Table: ")) {
         numFigure = parseInt(); //get the Table number
         currentLine = br.readLine(); 
         currentLine = br.readLine(); 
         String tableName = subString(" ","");
         tempTable = new EdgeTable(numFigure + DELIM + tableName);

         currentLine = br.readLine(); 
         stNatFields = new StringTokenizer(subString(" ", ""), DELIM);
         int numFields = stNatFields.countTokens();
         for (int i = 0; i < numFields; i++) {
            tempTable.addNativeField(Integer.parseInt(stNatFields.nextToken()));
         }

         currentLine = br.readLine(); 
         stTables = new StringTokenizer(subString(" ",""), DELIM);
         int numTables = stTables.countTokens();
         for (int i = 0; i < numTables; i++) {
            tempTable.addRelatedTable(Integer.parseInt(stTables.nextToken()));
         }
         tempTable.makeArrays();

         currentLine = br.readLine(); //this should be the RelatedFields list
         stRelFields = new StringTokenizer(subString(" ",""), DELIM);
         numFields = stRelFields.countTokens();

         for (int i = 0; i < numFields; i++) {
            tempTable.setRelatedField(i, Integer.parseInt(stRelFields.nextToken()));
         }

         alTables.add(tempTable);
         currentLine = br.readLine();
         currentLine = br.readLine(); 
         currentLine = br.readLine();
      }
      addFieldAttributes((currentLine = br.readLine()) != null);
   } // end parseSaveFile
      
   /**
   * Adds field attributes to field 
   * Adds field to ArrayList alFields
   */
   private void addFieldAttributes(boolean isNotNull) {
        EdgeField tempField;
        StringTokenizer stField;
        while (isNotNull) {
         stField = new StringTokenizer(currentLine, DELIM);
         numFigure = Integer.parseInt(stField.nextToken());
         String fieldName = stField.nextToken();
         tempField = new EdgeField(numFigure + DELIM + fieldName);
         tempField.setTableID(Integer.parseInt(stField.nextToken()));
         tempField.setTableBound(Integer.parseInt(stField.nextToken()));
         tempField.setFieldBound(Integer.parseInt(stField.nextToken()));
         tempField.setDataType(Integer.parseInt(stField.nextToken()));
         tempField.setVarcharValue(Integer.parseInt(stField.nextToken()));
         tempField.setIsPrimaryKey(Boolean.valueOf(stField.nextToken()).booleanValue());
         tempField.setDisallowNull(Boolean.valueOf(stField.nextToken()).booleanValue());
         if (stField.hasMoreTokens()) { //Default Value may not be defined
            tempField.setDefaultValue(stField.nextToken());
         }
         alFields.add(tempField);
      }
   }

   /**
   * convert ArrayList objects into arrays of the appropriate Class type
   */
   private void makeArrays() {
	//TODO clean the finals up!! messy and improper code
      if (alTables != null) tables = (EdgeTable[])alTables.toArray(new EdgeTable[alTables.size()]);
      if (alFields != null) fields = (EdgeField[])alFields.toArray(new EdgeField[alFields.size()]);
      if (alConnectors != null) connectors = (EdgeConnector[])alConnectors.toArray(new EdgeConnector[alConnectors.size()]);

   }//end makeArray

   /**
   * see if this is a duplicate table
   * @param testTableName String - name of table to test
   * @return boolean
   */
   private boolean isTableDup(String testTableName) {
      for (int i = 0; i < alTables.size(); i++) {
         EdgeTable tempTable = (EdgeTable)alTables.get(i);
         final boolean isDuplicate = tempTable.getName().equals(testTableName);
         if (isDuplicate) {
            return true;
         }
      }
      return false;
   }//end isTableDup

   /**
   * opens the file passed into the constructor
   * @param inputFile File
   */
   public void openFile(File inputFile) {
     int numLine = 0;
      try {
         FileReader fr = new FileReader(inputFile);
         br = new BufferedReader(fr);
         //test for what kind of file we have
         currentLine = trimLine(1);
         numLine++;
        final boolean isEdgeID = currentLine.startsWith(EDGE_ID);
        final boolean isSaveID = currentLine.startsWith(SAVE_ID);
         if (isEdgeID) { //the file chosen is an Edge Diagrammer file
            this.parseEdgeFile(); //parse the file
            br.close();
            this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
            this.resolveConnectors(); //Identify nature of Connector endpoints
         } else {
            if (isSaveID) { //the file chosen is a Save file created by this application
               this.parseSaveFile(); //parse the file
               br.close();
               this.makeArrays(); //convert ArrayList objects into arrays of the appropriate Class type
            } else { //the file chosen is something else
               JOptionPane.showMessageDialog(null, "Unrecognized file format");
            }
         }
      } // try
      catch (FileNotFoundException fnfe) {
         System.out.println("Cannot find \"" + inputFile.getName() + "\".");
         System.exit(0);
      } // catch FileNotFoundException
      catch (IOException ioe) {
         System.out.println(ioe);
         System.exit(0);
      } // catch IOException
   } //end openFile
   
} // EdgeConvertFileHandler
