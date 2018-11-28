import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UniqueBusPerRoute {
    public static Map<String,String> busPerRoute=new HashMap<>();
    private static final String CSV_FILE_PATH = "D:\\CourseWork\\Fall2018\\CSCI 8790\\buses.csv";
    private Map<String,Map<String, Set<Integer>>> busEveryHour=new HashMap<>();
    private LocalDateTime[] hours=new LocalDateTime[24];
    PreprocessData preprocessData=new PreprocessData();
    public static Set<String> RouteIdBusId=new HashSet<>();

    public void getUniqueBusPerRoute(){
        try {
            Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL);
            preprocessData.timeRanges();

            for (CSVRecord csvRecord : csvParser) {
                String busId=csvRecord.get(1);
                String routeId=csvRecord.get(2);
                String timeStampString = csvRecord.get(9);
                LocalDateTime timestamp=preprocessData.parseDate(timeStampString);
                int hour=preprocessData.getHourRange(timestamp);
                Map<String,Set<Integer>> hourlyBuses;
                Set<Integer> busRunningHours;
                if(!busEveryHour.containsKey(routeId)){
                    hourlyBuses=new HashMap<>();
                    busRunningHours=new HashSet<>();
                    busRunningHours.add(hour);
                    hourlyBuses.put(busId,busRunningHours);
                    busEveryHour.put(routeId,hourlyBuses);
                }else{
                    hourlyBuses=busEveryHour.get(routeId);
                    if(!hourlyBuses.containsKey(busId)){
                        busRunningHours=new HashSet<>();
                        busRunningHours.add(hour);
                        hourlyBuses.put(busId,busRunningHours);
                    }else{
                        busRunningHours=hourlyBuses.get(busId);
                        busRunningHours.add(hour);
                        hourlyBuses.put(busId,busRunningHours);
                    }
                }
            }

            for(Map.Entry<String,Map<String,Set<Integer>>> routes:busEveryHour.entrySet()){
                Map<String,Set<Integer>> buses=routes.getValue();
                String routeId=routes.getKey();
                String busId="";
                int maxSize=0;
                for(Map.Entry<String,Set<Integer>> busesEntry:buses.entrySet()){
                    String busIdKey=busesEntry.getKey();
                    int size=busesEntry.getValue().size();
                    if(maxSize<size){
                        maxSize=size;
                        busId=busIdKey;
                    }
                }
                String id=routeId+"_"+busId;
                RouteIdBusId.add(id);
            }
            }catch (IOException e){
            e.printStackTrace();
        }
    }
}
