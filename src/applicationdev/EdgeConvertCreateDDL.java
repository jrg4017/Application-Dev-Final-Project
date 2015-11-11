import java.awt.*;		
import java.awt.event.*;		
import javax.swing.*;		
import javax.swing.event.*;		
import java.io.*;		
import java.util.*; 

public abstract class EdgeConvertCreateDDL {
/*********************************************************************/
/********* ATTRIBUTES ************************************************/
   static String[] products = {"MySQL"};							  //	
   protected EdgeTable[] tables; 									  //master copy of EdgeTable objects
   protected EdgeField[] fields; 									  //master copy of EdgeField objects
   protected int[] numBoundTables;									  //
   protected int maxBound;											  //
   protected StringBuffer sb;										  //
   protected int selected;											  //
/*********************************************************************/
/********* CONSTRUCTORS **********************************************/  
   /**
   * sets the tables and fields of database to convert in class
   * @param tables EdgeTable
   * @param fields EdgeField
   */
   public EdgeConvertCreateDDL(EdgeTable[] tables, EdgeField[] fields) {
      this.tables = tables;
      this.fields = fields;
      initialize();
   } //end EdgeConvertCreateDDL
   
   /**
   * default constructor with empty arg list for to allow output dir 
   * to be set before there are table and field objects
   */
   public EdgeConvertCreateDDL() { } //EdgeConvertCreateDDL()
/*********************************************************************/
/********* ACCESSORS *************************************************/
  /**
   * gets the table at the requested numFigure
   * @param numFigure int
   * @return EdgeTable
   */
   protected EdgeTable getTable(int numFigure) {
      for (int tIndex = 0; tIndex < this.tables.length; tIndex++) {
         final boolean isEqualNFt = numFigure == this.tables[tIndex].getNumFigure();
         if (isEqualNFt) {
            return this.tables[tIndex];
         }
      }
      return null;
   }//end getTable
   /**
   * gets the field at the requested numFigure
   * @param numFigure int
   * @return EdgeField
   */
   protected EdgeField getField(int numFigure) {
      for (int fIndex = 0; fIndex < this.fields.length; fIndex++) {
         final boolean isEqualNFf = numFigure == this.fields[fIndex].getNumFigure();
         if (isEqualNFf) {
            return this.fields[fIndex];
         }
      }
      return null;
   }//end getField
/*********************************************************************/
/********* METHODS ***************************************************/
   /**
   * initalizes the 
   */
   public void initialize() {
      this.numBoundTables = new int[this.tables.length];
      this.maxBound = 0;
      this.sb = new StringBuffer();

      for (int i = 0; i < this.tables.length; i++) { //step through list of tables
         int numBound = 0; //initialize counter for number of bound tables
         int[] relatedFields = this.tables[i].getRelatedFieldsArray();
         for (int j = 0; j < relatedFields.length; j++) { //step through related fields list
            final boolean isNonZeroRF = relatedFields[j] != 0;
            if (isNonZeroRF) {
               numBound++; //count the number of non-zero related fields
            }
         }
         final boolean numBoundHigher = numBound > this.maxBound;
         this.numBoundTables[i] = numBound;
         if (numBoundHigher) {
            this.maxBound = numBound;
         }
      }
   }//end initialize 
/*********************************************************************/
/********* ABSTRACT METHODS ******************************************/

   public abstract String getDatabaseName();

   public abstract String getProductName();
   
   public abstract String getSQLString();
   
   public abstract void createDDL();
   
}//EdgeConvertCreateDDL
