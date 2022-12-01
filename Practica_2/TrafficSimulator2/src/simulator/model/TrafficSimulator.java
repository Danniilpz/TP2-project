
package simulator.model;

import java.util.*;
import org.json.JSONObject;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
    private RoadMap mapa_carreteras;
    private List<Event> lista_eventos;
    private List<TrafficSimObserver> lista_observadores;
    private int time;

    public TrafficSimulator() {
        this.mapa_carreteras=new RoadMap();
        this.lista_eventos=new SortedArrayList();
        this.lista_observadores=new ArrayList();
        this.time=0;
    }
    public void addEvent(Event e){
        this.lista_eventos.add(e);
        for(TrafficSimObserver ob: this.lista_observadores){
            ob.onEventAdded(mapa_carreteras, lista_eventos, e, time);
        }
    }
    public void advance(){
        try{
            time++;
            for(TrafficSimObserver ob: this.lista_observadores){
                ob.onAdvanceStart(mapa_carreteras, lista_eventos, time);
            }
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
            for(Junction j:this.mapa_carreteras.getJunctions()){
                j.advance(time);
            }
            for(Road r:this.mapa_carreteras.getRoads()){
                r.advance(time);
            }
            for(TrafficSimObserver ob: this.lista_observadores){
                ob.onAdvanceEnd(mapa_carreteras, lista_eventos, time);
            }
        }catch(RuntimeException e){
            for(TrafficSimObserver ob: this.lista_observadores){
                ob.onError(e.getMessage());
            }
            throw e;
        }
    }
    public void reset(){
        this.mapa_carreteras.reset();
        this.lista_eventos.clear();
        time=0;
        for(TrafficSimObserver ob: this.lista_observadores){
            ob.onReset(mapa_carreteras, lista_eventos, time);
        }
    }
    public JSONObject report(){
        JSONObject jo = new JSONObject();
        jo.put("time", this.time);
        jo.put("state", this.mapa_carreteras.report());
        return jo;  
    }

    @Override
    public void addObserver(TrafficSimObserver o) {
        this.lista_observadores.add(o);
        for(TrafficSimObserver ob: this.lista_observadores){
            ob.onRegister(mapa_carreteras, lista_eventos, time);
        }
    }

    @Override
    public void removeObserver(TrafficSimObserver o) {
        this.lista_observadores.remove(o);
    }

    public RoadMap getRoadMap() {
        return mapa_carreteras;
    }

    public int getTime() {
        return time;
    }

    public List<Event> getListaEventos() {
        return lista_eventos;
    }
    
    
}
