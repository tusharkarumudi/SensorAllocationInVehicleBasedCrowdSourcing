import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FitnessCalc {

    Map<String, FitnessValue> fitnessValuesMap = new HashMap<>();

    public Solution FitnessCalc(Map<Integer, Map<String, ArrayList<Coordinate>>> hourlyBusData, ArrayList<ArrayList<String>> population) {
        StringBuilder busCombination;
        int totalLength = 0;
        int totalDataPoints = 0;
        for (ArrayList<String> populationAL : population) {
            busCombination = new StringBuilder();
            for (String populationSample : populationAL) {
                busCombination.append(populationSample + " ");
                int totalDataPointsInTheRoute = 1, maxTotalDataPointsInTheRoute = Integer.MIN_VALUE;
                int length = 0;
                for (Map.Entry<Integer, Map<String, ArrayList<Coordinate>>> hourly : hourlyBusData.entrySet()) {
                    Map<String, ArrayList<Coordinate>> busMap = hourly.getValue();
                    if (busMap.containsKey(populationSample)) {
                        ArrayList<Coordinate> coordinates = busMap.get(populationSample);
                        Coordinate initial = coordinates.get(0);
                        for (int i = 1; i < coordinates.size(); i++) {
                            Coordinate coordinate1 = coordinates.get(i);
                            totalDataPointsInTheRoute++;
                            if (length == 0 && initial.latitude == coordinate1.latitude && initial.longitude == coordinate1.longitude) {
                                length = totalDataPointsInTheRoute;
                            }
                        }
                    }
                    if (maxTotalDataPointsInTheRoute < totalDataPointsInTheRoute) {
                        maxTotalDataPointsInTheRoute = totalDataPointsInTheRoute;
                    }
                }
                totalDataPoints += maxTotalDataPointsInTheRoute;
                totalLength += length;
            }
            fitnessValuesMap.put(busCombination.toString().trim(), new FitnessValue(totalLength, totalDataPoints));
        }
        return getBestSolution();
    }

    public Solution getBestSolution(){
        int bestLength=Integer.MIN_VALUE;
        String bestCombination="";
        FitnessValue val=new FitnessValue(0,0);
        for(Map.Entry<String,FitnessValue> entry:fitnessValuesMap.entrySet()){
            FitnessValue fv=entry.getValue();
            if(fv.length>bestLength){
                bestLength=fv.length;
                val=fv;
                bestCombination=entry.getKey();
            }
        }
        return new Solution(bestCombination,val);
    }

/*
    public void FitnessCalc1(Map<Integer, Map<String, ArrayList<Coordinate>>> data, ArrayList<ArrayList<String>> population) {
        for (int key : data.keySet()) {
            //System.out.println("current hour"+key);
            Map<String, ArrayList<Coordinate>> perhour = data.get(key);
            for (int j = 0; j < population.size(); j++) {
                for (int k = 0; k < population.get(j).size(); k++) {
                    //System.out.println("population bus name each time "+population.get(j).get(k).toString());
                    for (String busnamekey : perhour.keySet()) {
                        //	System.out.println("busnameeachtime "+busnamekey);
                        if (busnamekey == population.get(j).get(k).toString()) {
                            //System.out.println("populationkey "+population.get(j).get(k).toString());
                            ArrayList<Coordinate> cordinates = perhour.get(busnamekey);
                            Coordinate intial = cordinates.get(0);
                            int totalnumberoftimes = 0;
                            int length = 0;
                            //System.out.println("busname"+busnamekey);
                            //	System.out.println("number of coordinates for this bus"+cordinates.size());
                            for (int total = 1; total < cordinates.size(); total++) {
                                if ((cordinates.get(total).latitude == intial.latitude)
                                        && (cordinates.get(total).longitude == intial.longitude)) {
                                    totalnumberoftimes++;
                                    if (totalnumberoftimes == 1) {
                                        length = total;
                                    }
                                }
                            }
                            if (length == 0) {
                                length = cordinates.size();
                            }
                            //System.out.println("length"+length);
                            //System.out.println("totaltimes"+totalnumberoftimes);
                        }

                    }
                }
            }
        }
    }
*/

    public void calculatefitness() {

    }
}
