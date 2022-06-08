package utilities.fileread;
import org.relique.jdbc.csv.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CSVRead {

    public CSVRead() {
        try {
            Class.forName("org.relique.jdbc.csv.CsvDriver");
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public int getRecordCount(String csvFileDirectory, String strQuery, String csvFileName, String whereClause) {
        int totalRecordCount = 0;
        try {
            Connection csvConnection1 = DriverManager.getConnection("jdbc:relique:csv:" + csvFileDirectory);
            CsvStatement csvStatement1 = (CsvStatement) csvConnection1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            CsvResultSet csvResultSet1 = (CsvResultSet) csvStatement1.executeQuery(strQuery + csvFileName + " where " + whereClause);
            csvResultSet1.last();
            totalRecordCount = csvResultSet1.getRow();
            csvResultSet1.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRecordCount;
    }

    public Object[][] getDataFromCSVFile(String csvFileDirectory, String csvFileName, String strCSVScreenName) {
        Object csvData1[][] = null;
        try {
            Connection csvConnection = DriverManager.getConnection("jdbc:relique:csv:" + csvFileDirectory);
            CsvStatement csvStatement = (CsvStatement) csvConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            CsvResultSet csvResultSet = (CsvResultSet) csvStatement.executeQuery("select * from " + csvFileName + " where ScreenName ='" + strCSVScreenName + "'");

            csvResultSet.last();
            int totalRecordCount = csvResultSet.getRow();
            csvResultSet.beforeFirst();

            System.out.println("record count : " + totalRecordCount);

            CsvResultSetMetaData csvResultSetMetaData = (CsvResultSetMetaData) csvResultSet.getMetaData();
            int totalColumnCount = csvResultSetMetaData.getColumnCount();

            ArrayList<String> columnNameList = new ArrayList<>();
            for (int i = 1; i <= totalColumnCount; i++) {
                columnNameList.add(csvResultSetMetaData.getColumnName(i));
            }

            Object csvData[][] = new Object[totalRecordCount][1];
            Map<String, String> csvHashtable = new Hashtable<>();
            int rowIndex = 0;
            while (csvResultSet.next()) {
                for (String columnName : columnNameList) {
//                    if (columnName.equalsIgnoreCase("RunToTest")) {
//                        continue;
//                    }
                    System.out.println("column name: " + columnName);
                    csvHashtable.put(columnName, csvResultSet.getString(columnName));
                }

                System.out.println("csv value : " + Collections.singletonList(csvHashtable));
                csvData[rowIndex][0] = csvHashtable;
                rowIndex++;
            }
            csvData1 = csvData;

            csvResultSet.close();
            csvConnection.close();
        } catch (SQLException sqlexception) {
            sqlexception.printStackTrace();
        }
        return csvData1;
    }

    public Object[][] getDataFromCSVFile(String csvFileDirectory, String csvFileName) {
        Object csvData1[][] = null;
        try {
            Connection csvConnection = DriverManager.getConnection("jdbc:relique:csv:" + csvFileDirectory);
            CsvStatement csvStatement = (CsvStatement) csvConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            CsvResultSet csvResultSet = (CsvResultSet) csvStatement.executeQuery("select * from " + csvFileName);

            csvResultSet.last();
            int totalRecordCount = csvResultSet.getRow();
            csvResultSet.beforeFirst();

            System.out.println("record count : " + totalRecordCount);

            CsvResultSetMetaData csvResultSetMetaData = (CsvResultSetMetaData) csvResultSet.getMetaData();
            int totalColumnCount = csvResultSetMetaData.getColumnCount();

            ArrayList<String> columnNameList = new ArrayList<>();
            for (int i = 1; i <= totalColumnCount; i++) {
                columnNameList.add(csvResultSetMetaData.getColumnName(i));
            }

            Object csvData[][] = new Object[totalRecordCount][1];
            Map<String, String> csvHashtable = new Hashtable<>();
            int rowIndex = 0;
            while (csvResultSet.next()) {
                for (String columnName : columnNameList) {
//                    if (columnName.equalsIgnoreCase("RunToTest")) {
//                        continue;
//                    }
                    System.out.println("column name: " + columnName);
                    csvHashtable.put(columnName, csvResultSet.getString(columnName));
                }

                System.out.println("csv value : " + Collections.singletonList(csvHashtable));
                csvData[rowIndex][0] = csvHashtable;
                rowIndex++;
            }
            csvData1 = csvData;

            csvResultSet.close();
            csvConnection.close();
        } catch (SQLException sqlexception) {
            sqlexception.printStackTrace();
        }
        return csvData1;
    }

    public void printRecords(String csvFileDirectory, String csvFileName, String screenName) {
        String data = null;
        try {
            Connection csvConnection2 = DriverManager.getConnection("jdbc:relique:csv:" + csvFileDirectory);
            CsvStatement csvStatement2 = (CsvStatement) csvConnection2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            CsvResultSet csvResultSet2 = (CsvResultSet) csvStatement2.executeQuery("select * from " + csvFileName + " where ScreenName='" + screenName + "'");

            csvResultSet2.last();

            int totalRecordCount = csvResultSet2.getRow();
            csvResultSet2.beforeFirst();

            CsvResultSetMetaData csvResultSetMetaData = (CsvResultSetMetaData) csvResultSet2.getMetaData();
            int totalColumnCount = csvResultSetMetaData.getColumnCount();

            //print Header
            for (int i = 1; i <= totalColumnCount; i++) {
                System.out.print(csvResultSetMetaData.getColumnLabel(i) + ",");
            }
            System.out.println();

            // print records
            while (csvResultSet2.next()) {
                for (int j = 1; j <= totalColumnCount; j++) {
                    System.out.print(csvResultSet2.getString(j) + ",");
                }
            }
        } catch (SQLException sqlexception) {
            sqlexception.printStackTrace();
        }
    }
}
