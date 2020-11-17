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

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        this.id = pId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double pLongitude) {
        this.longitude = pLongitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double pLatitude) {
        this.latitude = pLatitude;
    }

    @Override
    public String toString() {
        return "Intersection{" + "id=" + id + ", longitude=" + longitude + ", latitude=" + latitude + '}';
    }
}