package model;

import config.Config;
import config.Variable;
import controller.Controller;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * The class managing the storage of requests and segments, that will be
 * modified by the remove, undo, redo etc functions
 */
public class TableContent extends AbstractTableModel {
    private static final List<Step> data = new ArrayList<>();
    private final String[] header = {"Name", "Type", "Date"};
    private Date totalDuration;

    public TableContent(){
        fireTableDataChanged ();
    }

    public TableContent(List<Segment> tableContent, Tour tour, Controller controller){
        data.clear();
        totalDuration = tour.getTimeDeparture();
        for(Segment s: tableContent){
            if(s.getOrigin().getId() == tour.getAddressDeparture().getId()){
                data.add(new Step(s.getOrigin().getId(), controller.getAddress(s.getOrigin().getLatitude(),s.getOrigin().getLongitude())
                        , totalDuration, "Depot start", s.getOrigin().getId()));
            }
            else {
                for (Request request : tour.getRequests()) {
                    if (s.getOrigin().getId() == request.getPickupAddress().getId()) {
                        data.add(new Step(request.getPickupAddress().getId(),controller.getAddress(s.getOrigin().getLatitude(),s.getOrigin().getLongitude()),
                                totalDuration, "PickUp",request.getDeliveryAddress().getId()));
                        totalDuration = addSecondsToDate(totalDuration, request.getPickupDuration());
                    } else if (s.getOrigin().getId() == request.getDeliveryAddress().getId()) {
                        data.add(new Step(request.getDeliveryAddress().getId(),controller.getAddress(s.getOrigin().getLatitude(),s.getOrigin().getLongitude()),
                                totalDuration, "Delivery",request.getPickupAddress().getId()));
                        totalDuration = addSecondsToDate(totalDuration, request.getDeliveryDuration());
                    }
                }
            }
            totalDuration = addSecondsToDate(totalDuration, (int)(s.length*3.6)/Config.SPEED);
        }
        data.add(new Step(tableContent.get(0).getOrigin().getId(),controller.getAddress(tableContent.get(0).getOrigin().getLatitude(),tableContent.get(0).getOrigin().getLongitude()), totalDuration, "Depot arrival",tableContent.get(0).getOrigin().getId()));

        //data.add(new Step(tableContent.get(0).getOrigin().getId(),tableContent.get(0).getName(), totalDuration, "Depot arrival",tableContent.get(0).getOrigin().getId()));
        fireTableDataChanged ();
    }

    private Date addSecondsToDate(Date date, int secondsToAdd){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, secondsToAdd);
        return calendar.getTime();
    }
    public int getColumnCount(){
        return header.length;
    }
    public int getRowCount() {
        return data.size();
    }
    public String getColumnName(int col){
        return header[col];
    }

    public void removeCouple(int lastIndex, int firstIndex) {
        data.remove(lastIndex);
        data.remove(firstIndex);
        fireTableRowsDeleted(lastIndex, lastIndex);
        fireTableRowsDeleted(firstIndex, firstIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return data.get(rowIndex).getName();
            case 1:
                return data.get(rowIndex).getType();
            case 2:

                String res = data.get(rowIndex).getDate().getHours() + ":";
                int x = data.get(rowIndex).getDate().getMinutes();
                if (x<10) {
                    res += "0"+x;
                } else {
                    res += x;
                }
                return res;
            default:
                return null;
        }
    }

    public long getIDbyIndex(int index) {
        return data.get(index).getId();
    }

    public int getIndexbyId(long id) {
        int index = -1;
        for(Step step: data){
            if(step.getId() == id){
                index = data.indexOf(step);
            }
        }
        return index;
    }

    public static int getCoupleIndex(int index){
        System.out.println("Index getCoupleIndex : " +index);
        long coupleId = data.get(index).getCouple();
        int coupleIndex = index;
        for(Step step: data){
            if(step.getId() == coupleId && index != data.indexOf(step)){
                coupleIndex = data.indexOf(step);
            }
        }
        return coupleIndex;
    }

}