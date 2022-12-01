
package simulator.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{

    String[] columnNames={"Id","Green","Queues"};
    List<Junction> rowData;
    
    public JunctionsTableModel(Controller c) {        
        rowData=new ArrayList();
        c.addObserver(this);
    }

    private void updateTable(List<Junction> data){
        rowData=data;
        fireTableDataChanged();
    }
    
    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
        updateTable(map.getJunctions());
    }
    
    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
        updateTable(map.getJunctions());
    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
        updateTable(map.getJunctions());
    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        updateTable(map.getJunctions());
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {}

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
        Junction j=rowData.get(rowIndex);
        switch(columnIndex){
            case 0:
                value=j.getId();
                break;
            case 1:
                value=j.getGreenLightRoad();
                break;
            case 2:
                value=j.getMapaColasString();
                break;
        }
        return value;
    }
    
    @Override
    public String getColumnName(int col) {
        return columnNames[col]; 
    }
    
}
