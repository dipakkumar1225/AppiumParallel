package utilities.fileread;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;

import java.util.List;

public class ExcelWrite {

    private Fillo fillo;
    private Connection connection = null;
    private String connectionString = null;

    public ExcelWrite(String filePath) throws FilloException {
        fillo = new Fillo();
        connection = fillo.getConnection(filePath);
    }

    public void writeData(String sheetName, String strQuery) throws FilloException {
//        System.setProperty("ROW", "1");
//        System.setProperty("COLUMN", "1");

        String str = "INSERT INTO " + sheetName + "(RunToTest,NewMpin) VALUES('Y', " + strQuery +")";
        connectionString = str;
        connection.executeUpdate(connectionString);
        connection.close();
    }

    public List<String> getSheetNames() {
        List<String> sheetNameList = connection.getMetaData().getTableNames();
        System.out.println(connection.getMetaData().getTableNames());
        connection.close();
        return sheetNameList;
    }
}
