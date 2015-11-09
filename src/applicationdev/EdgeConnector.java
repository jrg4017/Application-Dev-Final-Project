import java.util.StringTokenizer;

public class EdgeConnector {
/*********************************************************************/
/********* ATTRIBUTES ************************************************/
   private int numConnector, endPoint1, endPoint2;					 //
   private String endStyle1, endStyle2;                              //
   private boolean isEP1Field, isEP2Field, isEP1Table, isEP2Table;   //

/*********************************************************************/
/********* CONSTRUCTORS **********************************************/
	/**
	* reads the input string and separates it into the neccessary attributes
	* @param inputString String
	*/     
   public EdgeConnector(String inputString) {
	  //tokenize the input string and parse
      StringTokenizer st = new StringTokenizer(inputString, EdgeConvertFileParser.DELIM);
	  //set the attributes from the string token
      this.numConnector = Integer.parseInt(st.nextToken());
      this.endPoint1 = Integer.parseInt(st.nextToken());
      this.endPoint2 = Integer.parseInt(st.nextToken());
      this.endStyle1 = st.nextToken();
      this.endStyle2 = st.nextToken();
      this.isEP1Field = false;
      this.isEP2Field = false;
      this.isEP1Table = false;
      this.isEP2Table = false;
   }//end EdgeConnector
   
/*********************************************************************/
/********* ACCESSORS *************************************************/      
   /**
   * gets the connector number
   * @return this.numConnector int */
   public int getNumConnector() { return this.numConnector; }//end getNumConnector
   
   /**
   * gets the 1st end point
   * @return this.endPoint1 int */
   public int getEndPoint1() {  return this.endPoint1; }//end getEndPoint1
   
   /**
   * gets the 2nd end point
   * @return this.endPoint2 int  */
   public int getEndPoint2() { return endPoint2; } //end getEndPoint2
   
   /**
   * gets the 1st end style
   * @return this.endStyle1 String */
   public String getEndStyle1() { return this.endStyle1; }//end getEndStyle1
   
   /**
   * gets the 2nd end style
   * @return this.endStyle2 String */
   public String getEndStyle2() { return this.endStyle2; }//end getEndStyle2
   
   /**
   * gets the isEP1Field
   * @return this.isEP1Field boolean */
   public boolean getIsEP1Field() { return this.isEP1Field;  }//end getIsEP1Field
   
   /**
   * gets the isEP2Field
   * @return this.isEP2Field boolean */
   public boolean getIsEP2Field() { return this.isEP2Field;}//end getIsEP2Field
	
   /**
   * gets the isEP1Table
   * @return this.isEP1Table boolean */
   public boolean getIsEP1Table() {  return isEP1Table;  }//end getIsEP1Table

    /**
   * gets the isEP2Table
   * @return this.isEP2Table boolean */
   public boolean getIsEP2Table() {  return isEP2Table;  }//end getIsEP2Table
   
/*********************************************************************/
/********* MUTATORS **************************************************/ 
   /**
   * sets the isEP1Field
   * @param value boolean */
   public void setIsEP1Field(boolean value) { this.isEP1Field = value;  }//end setIsEP1Field
   
   /**
   * sets the isEP2Field
   * @param value boolean */
   public void setIsEP2Field(boolean value) { this.isEP2Field = value;  }//end setIsEP2Field

    /**
   * sets the isEP1Table
   * @param value boolean */
   public void setIsEP1Table(boolean value) { this.isEP1Table = value; }//end setIsEP1Table
	
   /**
   * sets the isEP2Table
   * @param value boolean */
   public void setIsEP2Table(boolean value) {  this.isEP2Table = value;  }//end setIsEP2Table
   
}//end EdgeConnector 
