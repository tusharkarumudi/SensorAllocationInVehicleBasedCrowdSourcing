import java.sql.Timestamp;

public class Bus {
    int id;
    int busId;
    int routeId;
    float latitude, longitude;
    int direction;
    Timestamp timestamp;

    Bus(int busId, int routeId, float latitude, float longitude, int direction, Timestamp timestamp) {
        this.busId = busId;
        this.routeId = routeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.timestamp = timestamp;
    }
}
