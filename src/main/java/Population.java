import java.util.*;

public class Population {
    public  ArrayList<ArrayList<String>> generatepopulation(Set<String> RouteIdBusID,int combinations) {
        Random rand = new Random();
        ArrayList<String> buses=new ArrayList<>(RouteIdBusID);
        ArrayList<ArrayList<String>> totalCombinations=permute_recursion(buses,buses.size(),3);
        System.out.println("totalCombinations"+totalCombinations.size());
        ArrayList<ArrayList<String>> population= new ArrayList<>();
        int numberOfElements = 10;
        for (int i = 0; i < numberOfElements; i++) {
            int randomIndex = rand.nextInt(totalCombinations.size());
            ArrayList randomElement = totalCombinations.get(randomIndex);
            population.add(randomElement);
            // System.out.println(randomElement);
        }

        return population;
    }

    /*
    private ArrayList<ArrayList> permutate(Set<String> routeIdBusID) {
        List<String> list = new ArrayList<String>(routeIdBusID);
        int totalset=list.size();
        System.out.println("totalset "+totalset);
        ArrayList<ArrayList> allCombinations= new ArrayList<>();
        for(int num = 0;num < (1 << totalset);num++) {
            ArrayList<Object> combination = new ArrayList<>();
            for(int ndx = 0;ndx < totalset;ndx++) {

                if((num & (1 << ndx)) != 0) {

                    combination.add(list.get(ndx));
                }
            }
            int size=combination.size();
            if(size==3) {
                allCombinations.add(combination);
            }
        }
        return allCombinations;
    }
    */

    private ArrayList<ArrayList<String>> permute_recursion(ArrayList<String> buses,int n,int k) {
        if(n<k)
            return null;
        ArrayList<ArrayList<String>> combinations=new ArrayList<>();
        if(k==1){
            for(int i=1;i<=n;i++){
                ArrayList<String> list=new ArrayList<>();
                list.add(buses.get(i-1));
                combinations.add(list);
            }
            return combinations;
        }
        for(int i=n;i>=k;i--){

            for(ArrayList<String> aryList: Objects.requireNonNull(permute_recursion(buses,i - 1, k - 1))) {
                aryList.add(buses.get(i-1));
                combinations.add(aryList);
            }
        }
        return combinations;
    }

    public static void main(String[] args) {
        Population popObject= new Population();
        PreprocessData ppd= new PreprocessData();
        ppd.getBusesPerHour();
        UniqueBusPerRoute uniqueBusPerRoute=new UniqueBusPerRoute();
        uniqueBusPerRoute.getUniqueBusPerRoute();
        Set<String> RouteIdBusId=UniqueBusPerRoute.RouteIdBusId;
        int r = 3;
        ArrayList<ArrayList<String>> Population=popObject.generatepopulation(RouteIdBusId,r);
        Map<Integer,Map<String,ArrayList<Coordinate>>> data=PreprocessData.uniqueBusPerRoutePerHour;
        FitnessCalc fitnessclass=new FitnessCalc();
        Solution sol=fitnessclass.FitnessCalc(data,Population);
    }
}
