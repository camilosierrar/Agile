public class Intersection {
    private long id;
    private double longitude;
    private double latitude;

    public Intersection() {
    }

    public Intersection(long pId, double pLongitude, double pLatitude) {
        this.id = pId;
        this.longitude = pLongitude;
        this.latitude = pLatitude;
    }

    public String getId() {
        return id;
    }

    public void setId(long pId) {
        this.id = pId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(double pLongitude) {
        this.longitude = pLongitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(double pLatitude) {
        this.latitude = pLatitude;
    }
}