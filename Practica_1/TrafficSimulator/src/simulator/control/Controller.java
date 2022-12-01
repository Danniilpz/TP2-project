package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.factories.*;
import simulator.model.*;

public class Controller {
    private TrafficSimulator sim;
    private Factory<Event> eventsFactory;
    public Controller(TrafficSimulator sim, Factory<Event> eventsFactory){
        if(sim==null||eventsFactory==null){
            throw new IllegalArgumentException("El simulador y la factoría de eventos no pueden ser null.");
        }
        else{
           this.sim=sim;
           this.eventsFactory=eventsFactory;
        }
    }
    public void loadEvents(InputStream in){
        JSONObject jo = new JSONObject(new JSONTokener(in));
        if(!jo.has("events")) {
            throw new RuntimeException("El archivo JSON tiene formato inválido.");
        }
        for(Object o:jo.getJSONArray("events")){
            Event e=eventsFactory.createInstance((JSONObject)o);
            sim.addEvent(e);
        }
    }
    public void run(int n, OutputStream out){ 
        PrintStream p=null;
        JSONArray ja=new JSONArray();
        JSONObject jo=new JSONObject();
        if(out!=null){
            p=new PrintStream(out); 
        }     
        for(int i=0;i<n;i++){
            sim.advance();
            ja.put(sim.report());             
        }
        jo.put("states", ja);
        if(out!=null&&p!=null) p.print(jo);
        else System.out.print(jo);
    }  
}
