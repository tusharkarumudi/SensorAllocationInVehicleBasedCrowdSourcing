public class SensorPlacement {
    public static void main(String[] args) {
        PreprocessData preprocessData=new PreprocessData();
        GenerateGrid grid=new GenerateGrid();
        preprocessData.getBusesPerHour();
        grid.generateGridCoordinates();
    }
}
