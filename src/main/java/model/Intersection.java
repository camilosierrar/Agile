package model;
/**
 * The intersection class
 */
public class Intersection {
    /**
     * The id
     */
    private long id;
    /**
     * The longitude
     */
    private double longitude;
    /**
     * The latitude
     */
    private double latitude;

    public Intersection() {
    }

    public Intersection(long pId, double pLongitude, double pLatitude) {
        this.id = pId;
        this.longitude = pLongitude;
        this.latitude = pLatitude;
    }

    public Intersection(long pId) {
        // Constructor used in XMLrequest, because we dont recieve longitude and latitude in the file
        this.id = pId;
    }

    /**
     * the getId() function
     * @return the id of the intersection
     */
	public long getId() {
        return id;
    }
    /**
     * the getLongitude() function
     * @return the longitude of the intersection
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * the getLatitude() function
     * @return the latitude of the intersection
     */
    public double getLatitude() {
        return latitude;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }
    @Override
    public String toString() {
        return "Intersection{" + "id=" + id + ", longitude=" + longitude + ", latitude=" + latitude + '}';
    }


}
