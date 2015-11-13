
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
}
