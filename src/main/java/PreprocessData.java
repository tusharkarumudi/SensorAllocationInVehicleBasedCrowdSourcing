import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PreprocessData {


    private static final String CSV_FILE_PATH = "D:\\CourseWork\\Fall2018\\CSCI 8790\\buses.csv";
  //  private Map<Integer, Map<String, Set<Map<String, ArrayList<Coordinate>>>>> busesPerHour = new HashMap<>();
    public static Map<Integer, Map<String, ArrayList<Coordinate>>> uniqueBusPerRoutePerHour = new HashMap<>();
    private LocalDateTime[] hours = new LocalDateTime[25];

    public void getBusesPerHour() {
        timeRanges();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL);
            for (CSVRecord csvRecord : csvParser) {
                String timeStampString = csvRecord.get(9);
                String[] timestampArray = timeStampString.split(" ");
                String day = timestampArray[0];
                int speed = Integer.parseInt(csvRecord.get(6));
                if (day.startsWith("2018-07-03") && speed != 0) {
                    String routeIdStr = csvRecord.get(2);
                    String busIdStr = csvRecord.get(1);
                    String id = routeIdStr + "_" + busIdStr;
                    if (UniqueBusPerRoute.RouteIdBusId.contains(id)) {
                        float lat = Float.parseFloat(csvRecord.get(4));
                        float lon = Float.parseFloat(csvRecord.get(5));
                        LocalDateTime timestamp = parseDate(timeStampString);
                        int hour = getHourRange(timestamp);

                        Map<String,ArrayList<Coordinate>> busMap;
                        ArrayList<Coordinate> coordinates;
                        if (!uniqueBusPerRoutePerHour.containsKey(hour)) {
                            busMap = new HashMap<>();
                            coordinates = new ArrayList<>();
                            coordinates.add(new Coordinate(lat, lon));
                            busMap.put(id, coordinates);
                            uniqueBusPerRoutePerHour.put(hour,busMap);
                        } else {
                            busMap=uniqueBusPerRoutePerHour.get(hour);
                            if(!busMap.containsKey(id)){
                                coordinates = new ArrayList<>();
                                coordinates.add(new Coordinate(lat, lon));
                                busMap.put(id, coordinates);
                            } else{
                                coordinates=busMap.get(id);
                                coordinates.add(new Coordinate(lat,lon));
                                busMap.put(id,coordinates);
                            }
                        }

//                    System.out.println("hour is "+hour+" for timestamp "+timestamp);

//                        Map<String, Set<Map<String, ArrayList<Coordinate>>>> routeMap;
//                        Set<Map<String, ArrayList<Coordinate>>> busSet;
//                        Map<String, ArrayList<Coordinate>> busMap;
//                        ArrayList<Coordinate> coordinates;
//                        if (!busesPerHour.containsKey(hour)) {
//                            routeMap = new HashMap<>();
//                            busSet = new HashSet<>();
//                            busMap = new HashMap<>();
//                            coordinates = new ArrayList<>();
//                            coordinates.add(new Coordinate(lat, lon));
//                            busMap.put(busIdStr, coordinates);
//                            busSet.add(busMap);
//                            routeMap.put(routeIdStr, busSet);
//                            busesPerHour.put(hour, routeMap);
//                        } else {
//                            routeMap = busesPerHour.get(hour);
//                            if (!routeMap.containsKey(routeIdStr)) {
//                                busSet = new HashSet<>();
//                                busMap = new HashMap<>();
//                                coordinates = new ArrayList<>();
//                                coordinates.add(new Coordinate(lat, lon));
//                                busMap.put(busIdStr, coordinates);
//                                busSet.add(busMap);
//                                routeMap.put(routeIdStr, busSet);
//                            } else {
//                                for (Map<String, ArrayList<Coordinate>> map : routeMap.get(routeIdStr)) {
//                                    for (Map.Entry<String, ArrayList<Coordinate>> entry : map.entrySet()) {
//                                        if (entry.getKey().equals(busIdStr)) {
//                                            entry.getValue().add(new Coordinate(lat, lon));
//                                        }
//                                    }
//                                }
//                            }
//                        }

                    }//if
                }//if for day check
            }//for csv record iteration
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

/*
    private void generateUniqueBusPerRoute() {
        System.out.println("generating uniquebuses");
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        String routeId_BusId = "";
        int hour = 0;
        String routeId = "", busId = "";
        for (Map.Entry<Integer, Map<String, Set<Map<String, ArrayList<Coordinate>>>>> hourEntry : busesPerHour.entrySet()) {
            hour = hourEntry.getKey();
            Map<String, Set<Map<String, ArrayList<Coordinate>>>> routeEntryValue = hourEntry.getValue();
            for (Map.Entry<String, Set<Map<String, ArrayList<Coordinate>>>> routeEntry : routeEntryValue.entrySet()) {
                routeId = routeEntry.getKey();
                Set<Map<String, ArrayList<Coordinate>>> busSet = routeEntry.getValue();
                int maxSize = 0;
                for (Map<String, ArrayList<Coordinate>> busMap : busSet) {
                    for (Map.Entry<String, ArrayList<Coordinate>> buses : busMap.entrySet()) {
                        int size = buses.getValue().size();
                        if (maxSize < size) {
                            busId = buses.getKey();
                            coordinates = buses.getValue();
                            maxSize = size;
                        }
                    }
                }
                routeId_BusId = routeId + "_" + busId;
                RouteIdBusId.add(routeId_BusId);
            }
            Map<String, ArrayList<Coordinate>> uniqueBusesPerRouteWithCoordinates = new HashMap<>();
            uniqueBusesPerRouteWithCoordinates.put(routeId_BusId, coordinates);
            uniqueBusPerRoutePerHour.put(hour, uniqueBusesPerRouteWithCoordinates);
        }
        System.out.println("printing");
        for (Map.Entry<Integer, Map<String, ArrayList<Coordinate>>> busPerHour : uniqueBusPerRoutePerHour.entrySet()) {
            for (Map.Entry<String, ArrayList<Coordinate>> bus : busPerHour.getValue().entrySet()) {
                System.out.println(bus.getKey() + " " + bus.getValue());
            }
        }
    }
*/
    public int getHourRange(LocalDateTime timestamp) {
        int hour = -1;
        for (int i = 1; i < 25; i++) {
            if (timestamp.isAfter(hours[i - 1]) && timestamp.isBefore(hours[i])) {
                hour = i - 1;
            }
        }
        return hour;
    }

    public void timeRanges() {
        LocalDateTime startTime = parseDate("2018-07-02 23:59:59");
        hours[0] = startTime;
        for (int i = 1; i < 24; i++) {
            hours[i] = hours[i - 1].plusHours(1);
        }
        hours[24] = hours[23].plusHours(1);
    }

    public LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }


    public static void main(String[] args) {
        PreprocessData obj = new PreprocessData();
        obj.timeRanges();
        obj.getBusesPerHour();
    }
}
