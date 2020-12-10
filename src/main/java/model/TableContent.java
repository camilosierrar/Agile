package model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TableContent extends AbstractTableModel {
    private static final List<Step> data = new ArrayList<>();
    private final String[] header = {"Name", "Type", "Date"};
    private Date totalDuration;

    public TableContent(){

    }

    public TableContent(List<Segment> tableContent, Tour tour){
        data.clear();
        totalDuration = tour.getTimeDeparture();
        for(Segment s: tableContent){
            if(s.getOrigin().getId() == tour.getAddressDeparture().getId()){
                data.add(new Step(s.getOrigin().getId(), s.getName(), totalDuration, "Depot start", s.getOrigin().getId()));
            }
            else {
                for (Request request : tour.getRequests()) {
                    if (s.getOrigin().getId() == request.getPickupAddress().getId()) {
                        data.add(new Step(request.getPickupAddress().getId(),s.getName(), totalDuration, "PickUp",request.getDeliveryAddress().getId()));
                        totalDuration = addSecondsToDate(totalDuration, request.getPickupDuration());
                    } else if (s.getOrigin().getId() == request.getDeliveryAddress().getId()) {
                        data.add(new Step(request.getDeliveryAddress().getId(),s.getName(), totalDuration, "Delivery",request.getPickupAddress().getId()));
                        totalDuration = addSecondsToDate(totalDuration, request.getDeliveryDuration());
                    }
                }
            }
            totalDuration = addSecondsToDate(totalDuration, (int)(s.length));
        }
        data.add(new Step(tableContent.get(0).getOrigin().getId(),tableContent.get(0).getName(), totalDuration, "Depot arrival",tableContent.get(0).getOrigin().getId()));
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
                return data.get(rowIndex).getId();
            case 1:
                return data.get(rowIndex).getType();
            case 2:
                return data.get(rowIndex).getDate();
            default:
                return null;
        }
    }

    public static int getCoupleIndex(int index){
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