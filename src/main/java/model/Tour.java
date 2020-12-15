package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * The class Tour that will be modified when we use the algorithms to find the best tour
 */
public class Tour {
    /**
     * The departure address
     */
    private Intersection addressDeparture;
    /**
     * The time departure
     */
    private Date timeDeparture;
    /**
     * The list of requests
     */
    private List<Request> requests;

    public Tour(Intersection addressDeparture, Date timeDeparture, List<Request> requests) {
        this.addressDeparture = addressDeparture;
        this.timeDeparture = timeDeparture;
        this.requests = requests;
    }

    public Tour(Tour newTour) {
        this.addressDeparture = newTour.addressDeparture;
        this.timeDeparture = newTour.timeDeparture;
        this.requests = newTour.requests;
    }

    public Tour addRequest(Request req) {
        requests.add(req);
        return this;
    }

    public Tour removeRequest(long id) {
        for( Request r : requests) {
            if (r.getDeliveryAddress().getId() == id || r.getPickupAddress().getId() == id) {
                requests.remove(r);
            }
        }
        return this;
    }

    public Intersection getAddressDeparture() {
        return addressDeparture;
    }

    public Date getTimeDeparture() {
        return timeDeparture;
    }

    public List<Request> getRequests() {
        return requests;
    }

    /**
     * Find a request from its intersection
     * @param intersection
     * @return  request
     */
    public Request getRequestbyIntersection(Intersection intersection) {
        for (Request request: requests) {
            if (intersection.equals(request.getDeliveryAddress()) || intersection.equals(request.getPickupAddress())) {
                return request;
            }
        }
        return null;
    }

    /**
     * Removes a request
     * @param request
     * @return  the tour
     */
    public Tour removeRequest(Request request) {
        List<Request> newRequests = new ArrayList<>();
        for (Request req: requests) {
            if (!req.equals(request)) {
                newRequests.add(req);
            }
        }
        return new Tour(addressDeparture, timeDeparture, newRequests);
    }

    @Override
    public String toString() {
        return "Tour{" +
                "addressDeparture=" + addressDeparture +
                ", timeDeparture=" + timeDeparture +
                ", requests=" + requests +
                '}';
    }
}
