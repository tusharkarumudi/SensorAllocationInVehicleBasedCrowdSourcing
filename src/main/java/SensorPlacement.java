public class SensorPlacement {
    public static void main(String[] args) {
        PreprocessData preprocessData=new PreprocessData();
        UniqueBusPerRoute uniqueBusPerRoute=new UniqueBusPerRoute();
        uniqueBusPerRoute.getUniqueBusPerRoute();
        GenerateGrid grid=new GenerateGrid();
        preprocessData.getBusesPerHour();
        grid.generateGridCoordinates();

    }
}
