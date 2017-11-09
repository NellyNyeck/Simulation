package Output;

import environment.PEdge;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CSVWriter {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static FileWriter fileWriter;

    private static final String FILE_HEADER = "edge,count";

    public CSVWriter(String filename) {
        try {
            fileWriter=new FileWriter(filename, true);
            fileWriter.append(FILE_HEADER.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.flush();
        }
        catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        }

    }

    public static void writeCsvFile(ArrayList<PEdge> plat) {
        try {
            for(PEdge p : plat){
                if (p.getCounter()>1){
                    fileWriter.append(p.getAbout());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(String.valueOf(p.getCounter()));
                    fileWriter.append(NEW_LINE_SEPARATOR);
                    fileWriter.flush();
                }            }
        }
        catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        }
    }

    public static void writeNewLine(){
        try {
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void done(){
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
