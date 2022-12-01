
package simulator.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{
    private String[] columnNames = { "Time","Desc" };
    List<Event> rowData;
    public EventsTableModel(Controller c) {
        rowData=new ArrayList();
        c.addObserver(this);
    }
    
    private void updateTable(List<Event> data){
        rowData=data;
        fireTableDataChanged();
    }
    
    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
        updateTable(events);
    }

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
        updateTable(events);
    }
 
    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        updateTable(events);
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
        Event e=rowData.get(rowIndex);
        switch(columnIndex){
            case 0:
                value=e.getTime();
                break;
            case 1:
                value=e.toString();
                break;
        }
        return value;
    }
    
    @Override
    public String getColumnName(int col) {
        return columnNames[col]; 
    }
    
}
