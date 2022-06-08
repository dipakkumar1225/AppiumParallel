package utilities.fileread;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.SneakyThrows;
import utilities.miscellanious.ConfigReader;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class CsvWriter {

    private final ConfigReader CONFIG_READER = new ConfigReader("config.properties");
    private CSVWriter writer;

    @SneakyThrows
    public CsvWriter() {
        String dir_Path = CONFIG_READER.getProperties().get("TEST_DATA_FILEPATH");
        writer = new CSVWriter(new FileWriter(dir_Path + File.separator + "Other_Bank_Beneficiaries_Details.csv"),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        String[] headerRecord = {"ACCOUNT_NUMBER", "PAYEE_NAME"};
        writer.writeNext(headerRecord);

    }

    @SneakyThrows
    public void createCsvDataSpecial(Map<String, String> csvMap) {
        String[] dataSet = csvMap.values().toArray(new String[0]);

        writer.writeAll(Collections.singleton(dataSet));


        writer.close();
    }
}
