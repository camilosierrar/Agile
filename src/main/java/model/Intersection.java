package model;

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

    public Intersection(long pId) {
        // Constructor used in XMLrequest, because we dont recieve longitude and latitude in the file
        this.id = pId;
    }

	public long getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }


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
