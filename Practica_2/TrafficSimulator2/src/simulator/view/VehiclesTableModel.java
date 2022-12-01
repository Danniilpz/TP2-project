
package simulator.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{

    String[] columnNames={"Id","Location","Itinerary","CO2 Class","Max.Speed","Speed","Total CO2","Distance"};
    List<Vehicle> rowData;
    
    public VehiclesTableModel(Controller c) {        
        rowData=new ArrayList();
        c.addObserver(this);
    }
    
    private void updateTable(List<Vehicle> data){
        rowData=data;
        fireTableDataChanged();
    }

    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
        updateTable(map.getVehicles());
    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
        updateTable(map.getVehicles());
    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        updateTable(map.getVehicles());
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {
        updateTable(map.getVehicles());
    }

    @Override
    public void onError(String err) {}

    @Override
    public int getRowCount() {
        return rowData.size();
    }

    @Override
    public int getColumnCount() {
       return columnNames.length;
    } 

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value=null;
        Vehicle v=rowData.get(rowIndex);
        switch(columnIndex){
            case 0:
                value=v.getId();
                break;
            case 1:
                if(v.getStatus()==VehicleStatus.ARRIVED){
                    value="Arrived";
                }else if(v.getStatus()==VehicleStatus.WAITING){
                    value="Waiting:"+v.getRoad().getDestination();
                }else if(v.getStatus()==VehicleStatus.PENDING){
                    value="Pending";
                }
                else{
                    value=v.getRoad().getId()+":"+v.getLocation();
                }
                break;
            case 2:
                value=v.getItinerary();
                break;
            case 3:
                value=v.getContaminationClass();
                break;
            case 4:
                value=v.getMaximumSpeed();
                break;
            case 5:
                value=v.getCurrentSpeed();
                break;
            case 6:
                value=v.getTotalCO2();
                break;
            case 7:
                value=v.getTotalDistance();
                break;
        }
        return value;
    }
    
    public String getColumnName(int col) {
        return columnNames[col]; 
    }
    
}
