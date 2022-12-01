
package simulator.model;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject{
    private List<Road> carreteras_entrantes;
    private Map<Junction,Road> carreteras_salientes;
    private List<List<Vehicle>> colas;
    private Map<Road,List<Vehicle>> mapa_colas;
    private int currentGreen;
    private int lastSwitch;
    private LightSwitchingStrategy lsStrategy;
    private DequeuingStrategy dqStrategy;
    private int x;
    private int y;
    Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
        super(id);
        if (lsStrategy==null||dqStrategy==null){
            throw new IllegalArgumentException("Las estrategias no pueden ser null.");
        }
        else if(xCoor<0||yCoor<0){
            throw new IllegalArgumentException("Las coordenadas deben ser mayores que 0.");
        }    
        else{
            this.lsStrategy=lsStrategy;
            this.dqStrategy=dqStrategy;
            this.x=xCoor;
            this.y=yCoor;
            this.currentGreen=-1; 
            this.lastSwitch=0;
            this.carreteras_entrantes=new ArrayList();
            this.carreteras_salientes=new HashMap();
            this.colas=new ArrayList();
            this.mapa_colas=new HashMap();
        }        
    }

    void addIncommingRoad(Road r){
        if(r.getDestination()!=this){
            throw new RuntimeException("La carretera no llega al cruce actual.");
        }    
        else{
            this.carreteras_entrantes.add(r);
            this.colas.add(new ArrayList());
            this.mapa_colas.put(r, new ArrayList());
        }
    }
    
    void addOutGoingRoad(Road r){
        Set<Map.Entry<Junction, Road>> salientes=this.carreteras_salientes.entrySet();
        boolean repeated=false;
        for(Map.Entry<Junction, Road> saliente:salientes){
            if(saliente.getKey()==r.getDestination()) repeated=true; 
        }
        if(repeated){
            throw new RuntimeException("La carretera ya existe.");
        }    
        else if(r.getSource()!=this){
            throw new IllegalArgumentException("La carretera no sale del cruce actual.");
        }    
        else{
            this.carreteras_salientes.put(r.getDestination(), r);
        }
    }
    
    void enter(Vehicle v){
        if(this.carreteras_entrantes.contains(v.getRoad())){
            this.colas.get(this.carreteras_entrantes.indexOf(v.getRoad())).add(v);
            this.mapa_colas.get(v.getRoad()).add(v);
        }        
    }
    
    Road roadTo(Junction j){
        return this.carreteras_salientes.get(j);
    }
    
    @Override
    void advance(int time) {
        if(this.carreteras_entrantes.size()>0){
            if(this.currentGreen!=-1){
                List<Vehicle> vehicles=this.dqStrategy.dequeue(this.colas.get(this.currentGreen));
                for(Vehicle vehicle:vehicles){
                    vehicle.moveToNextRoad();
                    this.mapa_colas.get(this.carreteras_entrantes.get(currentGreen)).remove(vehicle);                    
                    this.colas.get(this.currentGreen).remove(vehicle);
                }
            }     
            int newGreen=this.lsStrategy.chooseNextGreen(carreteras_entrantes, colas, currentGreen, lastSwitch, time);
            if(newGreen!=this.currentGreen){
                this.currentGreen=newGreen;
                this.lastSwitch=time;
            }
        }          
    }

    @Override
    public JSONObject report() {
        JSONObject jo = new JSONObject();
        jo.put("id", this._id);
        if(currentGreen==-1){
            jo.put("green", "none");
        }
        else{
            jo.put("green", this.carreteras_entrantes.get(this.currentGreen).getId());
        }
        JSONArray ja = new JSONArray();
        Set<Map.Entry<Road,List<Vehicle>>> entry_colas=this.mapa_colas.entrySet();
        for(Map.Entry<Road,List<Vehicle>> cola:entry_colas){
            JSONObject jo2 = new JSONObject();
            jo2.put("road", cola.getKey()._id);
            JSONArray ja2 = new JSONArray();
            for(Vehicle vehicle:cola.getValue()){                
                ja2.put(vehicle.getId());
            }
            jo2.put("vehicles", ja2);
            ja.put(jo2);
        }
        jo.put("queues", ja);
        return jo;  
    }
    
}
