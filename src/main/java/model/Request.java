package model;


public class Request {
    private Intersection pickupAddress;
    private Intersection deliveryAddress;
    private int pickupDuration;
    private int deliveryDuration;
<<<<<<< HEAD

    public Request(Intersection pickupAddress, Intersection deliveryAddress, int pickupDuration, int deliveryDuration) {
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.pickupDuration = pickupDuration;
        this.deliveryDuration = deliveryDuration;
    }

    public Intersection getPickupAddress() {
        return pickupAddress;
    }

    public Intersection getDeliveryAddress() {
        return deliveryAddress;
    }

    public int getPickupDuration() {
        return pickupDuration;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    @Override
    public String toString() {
        return "Request{" +
                "pickupAddress=" + pickupAddress +
                ", deliveryAddress=" + deliveryAddress +
                ", pickupDuration=" + pickupDuration +
                ", deliveryDuration=" + deliveryDuration +
                '}';
    }
||||||| parent of da05bdb... Changes in Intersection, Request and XMLrequest
=======

    public Request(Intersection pickupAddress, Intersection deliveryAddress, int pickupDuration, int deliveryDuration) {
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.pickupDuration = pickupDuration;
        this.deliveryDuration = deliveryDuration;
    }
>>>>>>> da05bdb... Changes in Intersection, Request and XMLrequest
}
