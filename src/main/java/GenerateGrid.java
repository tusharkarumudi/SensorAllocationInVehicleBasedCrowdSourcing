import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GenerateGrid {
    private float minLatLat = Float.MAX_VALUE, minLatLong = Float.MAX_VALUE;
    private float maxLatLat = Float.MIN_VALUE, maxLatLong = Float.MIN_VALUE;
    private float minLongLat = Float.MAX_VALUE, minLongLong = Float.MAX_VALUE;
    private float maxLongLat = Float.MIN_VALUE, maxLongLong = Float.MIN_VALUE;
    private static final String CSV_FILE_PATH = "D:\\CourseWork\\Fall2018\\CSCI 8790\\buses.csv";
    private ArrayList<ArrayList<Coordinate>> coordinates=new ArrayList<ArrayList<Coordinate>>();

    class Coordinate {
        float latitude;
        float longitude;

        Coordinate(float latitude, float longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

    }

    public static void main(String[] args) {

       GenerateGrid gridSystem = new GenerateGrid();
       gridSystem.getMinMaxCoordinates();
       float stepSize=0.0001f;
       gridSystem.generateGrid(stepSize);
    }

    private void getMinMaxCoordinates(){
        try {
            Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL);

            for (CSVRecord csvRecord : csvParser) {
                // Accessing Values by Column Index
                Float lat = Float.parseFloat(csvRecord.get(4));
                Float lon = Float.parseFloat(csvRecord.get(5));

                if (lat < minLatLat) {
                    minLatLat = lat;
                    minLatLong = lon;
                }

                if (lat > maxLatLat) {
                    maxLatLat = lat;
                    maxLatLong = lon;
                }

                if (lon < minLongLong) {
                    minLongLat = lat;
                    minLongLong = lon;
                }

                if (lon > maxLongLong) {
                    maxLongLat = lat;
                    maxLongLong = lon;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(minLatLat + " " + minLatLong);
        System.out.println(maxLatLat + " " + maxLatLong);
        System.out.println(minLongLat + " " + minLongLong);
        System.out.println(maxLongLat + " " + maxLongLong);
    }

    private void generateGrid(float stepSize) {
        float lon=minLongLong;
        while(lon < maxLongLong) {
            float lat = minLatLat;
            ArrayList<Coordinate> coordinatesList = new ArrayList<Coordinate>();
            while(lat<maxLatLat){
                coordinatesList.add(new Coordinate(lat,lon));
                lat += stepSize;
                System.out.println(lat+","+lon);
            }
            coordinates.add(coordinatesList);
            lon += stepSize;
        }
    }

}


