package timotei;

public class GeoPoint {
    private final double      longitude;
    private final double      latitude;
    
    public GeoPoint( double longitude, double latitude ){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLatitude(){ return latitude; }
    public double getLongitude(){ return longitude; }
}
