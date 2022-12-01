package simulator.model;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
    private List<Junction> lista_cruces;
    private List<Road> lista_carreteras;
    private List<Vehicle> lista_vehiculos;
    private Map<String,Junction> mapa_cruces;
    private Map<String,Road> mapa_carreteras;
    private Map<String,Vehicle> mapa_vehiculos;
    
    public RoadMap() {
        this.lista_cruces = new ArrayList();
        this.lista_carreteras = new ArrayList();
        this.lista_vehiculos = new ArrayList();
        this.mapa_cruces = new HashMap();
        this.mapa_carreteras = new HashMap();
        this.mapa_vehiculos = new HashMap();
    }
    
    void addJunction(Junction j){
        if(this.mapa_cruces.containsKey(j.getId())){
            throw new RuntimeException("El cruce ya existe.");
        }
        this.lista_cruces.add(j);
        this.mapa_cruces.put(j.getId(), j);
    }
    
    void addRoad(Road r){
        if(this.mapa_carreteras.containsKey(r.getId())){
            throw new RuntimeException("La carretera ya existe.");
        }
        if(!this.mapa_cruces.containsKey(r.getSource().getId())||!this.mapa_cruces.containsKey(r.getDestination().getId())) {} //throw Excepction;
        this.lista_carreteras.add(r);
        this.mapa_carreteras.put(r.getId(), r);                
    }
    void addVehicle(Vehicle v){
        if(this.mapa_vehiculos.containsKey(v.getId())){
            throw new RuntimeException("El vehículo ya existe.");
        }
        List<Junction> itinerary=v.getItinerary();
        boolean correct=true;
        for(int i=0;(i+1)<itinerary.size()&&correct==true;i++){
            Junction j1=itinerary.get(i);
            Junction j2=itinerary.get(i+1);
            boolean existsRoad=false;
            for(Road r:this.lista_carreteras){
                if(r.getSource()==j1&&r.getDestination()==j2) existsRoad=true;
            }
            if(!existsRoad) correct=false;
        }
        if(!correct){
            throw new RuntimeException("El itinerario del vehículo no es correcto.");
        }
        this.lista_vehiculos.add(v);
        this.mapa_vehiculos.put(v.getId(), v); 
    }
    public Junction getJunction(String id){
        Junction a=this.mapa_cruces.get(id);
        return a;
    }
    public Road getRoad(String id){
        return this.mapa_carreteras.get(id);
    }
    public Vehicle getVehicle(String id){
        return this.mapa_vehiculos.get(id);
    }
    public List<Junction> getJunctions(){
        return Collections.unmodifiableList(this.lista_cruces);
    }
    public List<Road> getRoads(){
        return Collections.unmodifiableList(this.lista_carreteras);
    }
    public List<Vehicle> getVehicles(){
        return Collections.unmodifiableList(this.lista_vehiculos);
    }
    public List<Vehicle> getPendingVehicles(){
        List<Vehicle> vs=new ArrayList();
        for(Vehicle v:this.lista_vehiculos){
            if(v.getStatus()==VehicleStatus.PENDING){
                vs.add(v);
            }
        }
        return Collections.unmodifiableList(vs);
    }
    void reset(){
        this.lista_carreteras.clear();
        this.lista_cruces.clear();
        this.lista_vehiculos.clear();
        this.mapa_carreteras.clear();
        this.mapa_cruces.clear();
        this.mapa_vehiculos.clear();
    }
    public JSONObject report(){
        JSONObject jo = new JSONObject();        
        JSONArray ja1 = new JSONArray();
        for(Junction j:this.lista_cruces){
            ja1.put(j.report());
        }
        jo.put("junctions", ja1);
        JSONArray ja2 = new JSONArray();
        for(Road r:this.lista_carreteras){
            ja2.put(r.report());
        }
        jo.put("roads", ja2);
        JSONArray ja3 = new JSONArray();
        for(Vehicle v:this.lista_vehiculos){
            ja3.put(v.report());
        }
        jo.put("vehicles", ja3);        
        return jo;
    }
}
