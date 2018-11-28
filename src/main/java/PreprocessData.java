import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PreprocessData {


    private static final String CSV_FILE_PATH = "D:\\CourseWork\\Fall2018\\CSCI 8790\\buses.csv";
    private Map<Integer,Map<String,Set<Map<String,Set<Coordinate>>>>> busesPerHour = new HashMap<>();
    private Map<Integer,Map<String,Set<Coordinate>>> uniqueBusPerRoutePerHour=new HashMap<>();
    private LocalDateTime[] hours=new LocalDateTime[24];


    private void getBusesPerHour() {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL);
            for (CSVRecord csvRecord : csvParser) {
                String timeStampString = csvRecord.get(9);
                String[] timestampArray =timeStampString.split(" ");
                String day = timestampArray[0];
                int speed=Integer.parseInt(csvRecord.get(6));
                if (day.startsWith("2018-07-03") && speed!=0) {
                    String routeIdStr = csvRecord.get(2);
                    String busIdStr = csvRecord.get(1);
                    float lat = Float.parseFloat(csvRecord.get(4));
                    float lon = Float.parseFloat(csvRecord.get(5));
                    LocalDateTime timestamp=parseDate(timeStampString);
                    int direction = Integer.parseInt(csvRecord.get(3));
                    int hour=getHourRange(timestamp);
//                    System.out.println("hour is "+hour+" for timestamp "+timestamp);
                    Map<String,Set<Map<String,Set<Coordinate>>>> routeMap;
                    Set<Map<String,Set<Coordinate>>> busSet;
                    Map<String,Set<Coordinate>> busMap;
                    Set<Coordinate> coordinates;
                    if(!busesPerHour.containsKey(hour)){
                       routeMap=new HashMap<>();
                        busSet=new HashSet<>();
                        busMap=new HashMap<>();
                        coordinates=new HashSet<>();
                        coordinates.add(new Coordinate(lat,lon));
                        busMap.put(busIdStr,coordinates);
                        busSet.add(busMap);
                        routeMap.put(routeIdStr,busSet);
                        busesPerHour.put(hour,routeMap);
                    } else {
                        routeMap=busesPerHour.get(hour);
                        if(!routeMap.containsKey(routeIdStr)){
                            busSet=new HashSet<>();
                            busMap=new HashMap<>();
                            coordinates=new HashSet<>();
                            coordinates.add(new Coordinate(lat,lon));
                            busMap.put(busIdStr,coordinates);
                            busSet.add(busMap);
                            routeMap.put(routeIdStr,busSet);
                        } else {
                            for(Map<String,Set<Coordinate>> map:routeMap.get(routeIdStr)){
                                for(Map.Entry<String,Set<Coordinate>> entry:map.entrySet()){
                                    if(entry.getKey().equals(busIdStr)){
                                        entry.getValue().add(new Coordinate(lat,lon));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            generateUniqueBusPerRoute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void generateUniqueBusPerRoute(){
        System.out.println("generating uniquebuses");
        Set<Coordinate> coordinates=new HashSet<>();
        String routeId_BusId="";
        int hour=0;
        String  routeId="",busId="";
        for(Map.Entry<Integer,Map<String,Set<Map<String,Set<Coordinate>>>>> hourEntry:busesPerHour.entrySet()){
            hour=hourEntry.getKey();
            Map<String,Set<Map<String,Set<Coordinate>>>> routeEntryValue=hourEntry.getValue();
            for(Map.Entry<String,Set<Map<String,Set<Coordinate>>>> routeEntry:routeEntryValue.entrySet()){
                routeId=routeEntry.getKey();
                Set<Map<String,Set<Coordinate>>> busSet=routeEntry.getValue();
                int maxSize=0;
                for(Map<String,Set<Coordinate>> busMap:busSet){
                    for(Map.Entry<String,Set<Coordinate>> buses:busMap.entrySet()){
                        int size=buses.getValue().size();
                        if(maxSize<size){
                            busId=buses.getKey();
                            coordinates=buses.getValue();
                            maxSize=size;
                        }
                    }
                }
            }
            Map<String,Set<Coordinate>> uniqueBusesPerRouteWithCoordinates=new HashMap<>();
            routeId_BusId=routeId+"_"+busId;
            uniqueBusesPerRouteWithCoordinates.put(routeId_BusId,coordinates);
            uniqueBusPerRoutePerHour.put(hour,uniqueBusesPerRouteWithCoordinates);
        }
        System.out.println("printing");
        for(Map.Entry<Integer,Map<String,Set<Coordinate>>> busPerHour:uniqueBusPerRoutePerHour.entrySet()){
            for(Map.Entry<String,Set<Coordinate>> bus:busPerHour.getValue().entrySet()){
                System.out.println(bus.getKey()+" "+bus.getValue());
            }
        }
    }

    private int getHourRange(LocalDateTime timestamp){
        if(timestamp.isAfter(hours[23])){
            return 23;
        }
       for(int i=1;i<24;i++){
           if(timestamp.isAfter(hours[i-1]) && timestamp.isBefore(hours[i])){
               System.out.println("in if");
               return i-1;
           }
       }
        System.err.println("Error in getting hour range "+timestamp);
       return -1;
    }

    private void timeRanges() {
        LocalDateTime startTime=parseDate("2018-07-03 00:00:01");
        hours[0]=startTime;
        System.out.println(hours[0]);
        for(int i=1;i<24;i++){
            hours[i]=hours[i-1].plusHours(1);
            System.out.println(hours[i]);
        }
        System.out.println("done printing time ranges");
    }

    private LocalDateTime parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date,formatter);
    }


    public static void main(String[] args) {
        PreprocessData obj=new PreprocessData();
        obj.timeRanges();
        obj.getBusesPerHour();
    }
}
