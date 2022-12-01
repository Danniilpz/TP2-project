
package simulator.model;

import java.util.*;
import org.json.JSONObject;
import simulator.misc.SortedArrayList;

public class TrafficSimulator {
    private RoadMap mapa_carreteras;
    private List<Event> lista_eventos;
    private int time;

    public TrafficSimulator() {
        this.mapa_carreteras=new RoadMap();
        this.lista_eventos=new SortedArrayList();
        this.time=0;
    }
    public void addEvent(Event e){
        this.lista_eventos.add(e);
    }
    public void advance(){
        time++;
        List<Event> eventsToExecute=new ArrayList();
        for(Event e:this.lista_eventos){
            if(e.getTime()==time){
                eventsToExecute.add(e);
            }
        }
        for(Event e:eventsToExecute){
            this.lista_eventos.remove(e);
            e.execute(mapa_carreteras);
        }
        for(Vehicle v:this.mapa_carreteras.getPendingVehicles()){
            v.moveToNextRoad();
        }
        for(Junction j:this.mapa_carreteras.getJunctions()){
            j.advance(time);
        }
        for(Road r:this.mapa_carreteras.getRoads()){
            r.advance(time);
        }
    }
    public void reset(){
        this.mapa_carreteras.reset();
        this.lista_eventos.clear();
        time=0;
    }
    public JSONObject report(){
        JSONObject jo = new JSONObject();
        jo.put("time", this.time);
        jo.put("state", this.mapa_carreteras.report());
        return jo;  
    }
}
