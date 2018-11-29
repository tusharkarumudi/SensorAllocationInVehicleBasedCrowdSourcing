import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GeneticAlgorithm {
	Random rand = new Random();
	int total_mutations=0;
	static Solution sol;
	public void mutate(ArrayList<ArrayList<String>> population, Set<String> routeIdBusId,
			Map<Integer, Map<String, ArrayList<Coordinate>>> data) {
		List<String> list = new ArrayList<String>(routeIdBusId);
		for (int i = 0; i < population.size(); i++) {
			String randomElement = null;
			int randomlocation=rand.nextInt(population.get(i).size());
			if(randomlocation==3) {
				randomlocation=randomlocation-1;
			}
			boolean equals = true;
			while (equals) {
				equals = false;
				randomElement = getRandomElement(list);
				for (int j = 0; j < population.get(i).size(); j++) {
					if (population.get(i).get(j) == randomElement) {
						equals = true;
					}
				}
			}
			population.get(i).set(randomlocation, randomElement);
		}
		 FitnessCalc fitnessclass=new FitnessCalc();
	        Solution solution=fitnessclass.FitnessCalc(data,population);
	        //System.out.println("current Best Solution "+sol.fv.length);
	        //System.out.println("current Best Solution Buses Detaisl "+sol.busCombinations);
	        //System.out.println("Mutated Solution "+ solution.fv.length);
	       // System.out.println("Bus Details "+solution.busCombinations);
	        if(sol.fv.length < solution.fv.length) {
	        	sol=new Solution(solution.busCombinations, solution.fv);
	        }
		if (total_mutations < 2000) {
			total_mutations++;
			mutate(population, routeIdBusId, data);
		}
		else {
			System.out.println("Best Solution "+sol.busCombinations);
			System.out.println("Best Solution Buses Details"+sol.fv.length);
		}
	}
	public String getRandomElement(List<String> list) {
		String randomElement = list.get(rand.nextInt(list.size()));
		return randomElement;
	}
	public static void main(String ar[]) {
		GeneticAlgorithm ga= new GeneticAlgorithm();
		  Population popObject= new Population();
	        PreprocessData ppd= new PreprocessData();
	        UniqueBusPerRoute uniqueBusPerRoute=new UniqueBusPerRoute();
	        uniqueBusPerRoute.getUniqueBusPerRoute();
	        ppd.getBusesPerHour();
	        Set<String> RouteIdBusId=UniqueBusPerRoute.RouteIdBusId;
	        int r = 3;
	        ArrayList<ArrayList<String>> Population=popObject.generatepopulation(RouteIdBusId,r);
	        Map<Integer,Map<String,ArrayList<Coordinate>>> data=PreprocessData.uniqueBusPerRoutePerHour;
	        FitnessCalc fitnessclass=new FitnessCalc();
	        sol=fitnessclass.FitnessCalc(data,Population);
	        System.out.println("Best fitness value prior to CrossOver "+sol.fv.length);
	        System.out.println("Best route bus combination prior to CrossOver "+sol.busCombinations);
	        ga.mutate(Population,RouteIdBusId,data);
	}
}
