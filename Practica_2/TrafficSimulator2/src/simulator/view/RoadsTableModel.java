
package simulator.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{

    String[] columnNames={"Id","Length","Weather","Max.Speed","Speed Limit","Total CO2","CO2 Limit"};
    List<Road> rowData;
    
    public RoadsTableModel(Controller c) {  
        rowData=new ArrayList();
        c.addObserver(this);
    }

    private void updateTable(List<Road> data){
        rowData=data;
        fireTableDataChanged();
    }
    
    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}
    
    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
        updateTable(map.getRoads());
    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
        updateTable(map.getRoads());
    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        updateTable(map.getRoads());
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {
        updateTable(map.getRoads());
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
        Road r=rowData.get(rowIndex);
        switch(columnIndex){
            case 0:
                value=r.getId();
                break;
            case 1:
                value=r.getLength();
                break;
            case 2:
                value=r.getWeather();
                break;
            case 3:
                value=r.getMaxSpeed();
                break;
            case 4:
                value=r.getSpeedLimit();
                break;
            case 5:
                value=r.getTotalCO2();
                break;
            case 6:
                value=r.getCO2Limit();
                break;
        }
        return value;
    }
    
    public String getColumnName(int col) {
        return columnNames[col]; 
    }

    
}
