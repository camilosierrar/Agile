package model;

/**
 * The class representing a request
 */
public class Request {
    /**
     * The pickup address
     */
    private Intersection pickupAddress;
    /**
     * The delivery address
     */
    private Intersection deliveryAddress;
    /**
     * The pickup duration
     */
    private int pickupDuration;
    /**
     * The delivery duration
     */
    private int deliveryDuration;

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
}
