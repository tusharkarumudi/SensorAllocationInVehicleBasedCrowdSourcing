import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GenerateGrid {
    private float minLat=Integer.MAX_VALUE;
    private float maxLat=Integer.MIN_VALUE;
    private float minLong=Integer.MAX_VALUE;
    private float maxLong=Integer.MIN_VALUE;
    private static final String CSV_FILE_PATH = "D:\\CourseWork\\Fall2018\\CSCI 8790\\buses.csv";
    public ArrayList<ArrayList<Coordinate>> coordinates=new ArrayList<ArrayList<Coordinate>>();


    public static void main(String[] args) {

       GenerateGrid gridSystem = new GenerateGrid();
       gridSystem.getMinMaxCoordinates();
       float stepSize=0.0001f;
    }

    private void getMinMaxCoordinates(){
        try {
            Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL);

            for (CSVRecord csvRecord : csvParser) {
                // Accessing Values by Column Index
                Float lat = Float.parseFloat(csvRecord.get(4));
                Float lon = Float.parseFloat(csvRecord.get(5));

                if(lat<minLat){
                    minLat=lat;
                }
                if(lat>maxLat){
                    maxLat=lat;
                }
                if(lon<minLong){
                    minLong=lon;
                }
                if(lon>maxLong){
                    maxLong=lon;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(minLat);
        System.out.println(maxLat);
        System.out.println(minLong);
        System.out.println(maxLong);
    }

    public void generateGridCoordinates() {
        getMinMaxCoordinates();
        float stepSize=0.0001f;
        float lon=minLong;
        while(lon < maxLong) {
            float lat = minLat;
            ArrayList<Coordinate> coordinatesList = new ArrayList<Coordinate>();
            while(lat<maxLat){
                coordinatesList.add(new Coordinate(lat,lon));
                lat += stepSize;
                System.out.println(lat+","+lon);
            }
            coordinates.add(coordinatesList);
            lon += stepSize;
        }
    }

}


