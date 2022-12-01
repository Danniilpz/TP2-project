
package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle>{
    private List<Junction> itinerary;
    private int maximum_speed;
    private int current_speed;
    private VehicleStatus status;
    private Road road;
    private int location;
    private int contamination_class;
    private int total_contamination;
    private int total_travelled_distance;
    private int last_junction;
    
    Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
        super(id);
        if(maxSpeed<0){
            throw new IllegalArgumentException("La velocidad debe ser mayor que 0.");
        }
        else if(contClass<0||contClass>10){
            throw new IllegalArgumentException("La clase de contaminación debe estar entre 0 y 10.");
        }
        else if(itinerary.size()<2){
            throw new IllegalArgumentException("El itineraría debe contener, como mínimo, dos cruces.");
        }
        else{
            this.itinerary=Collections.unmodifiableList(new ArrayList<>(itinerary));
            this.maximum_speed=maxSpeed;
            this.contamination_class=contClass;
            this.total_contamination=0;
            this.total_travelled_distance=0;
            this.current_speed=0;
            this.location=0;
            this.last_junction=-1;
            this.road=null;
            this.status=VehicleStatus.PENDING;
        }
    }

    void setSpeed(int s){
        if(s<0){
            throw new IllegalArgumentException("La velocidad debe ser mayor que 0.");
        }
        else if(s>=this.maximum_speed){
            this.current_speed=this.maximum_speed;
        }
        else{
            this.current_speed=s;
        }      
    }
    
    void setContaminationClass(int c){
        if(c<0||c>10){
            throw new IllegalArgumentException("La clase de contaminación debe estar entre 0 y 10.");
        }
        else{
            this.contamination_class=c;
        }      
    }
    
    void advance(int time){
        if(this.status==VehicleStatus.TRAVELING){
            int f=this.contamination_class; //factor de contaminación
            int l=this.location; //distancia recorrida
            if(this.location+this.current_speed<this.road.getLength()){
                this.location+=this.current_speed;
                this.total_travelled_distance+=this.current_speed;
            }
            else{
                int m=this.road.getLength()-this.location;
                this.total_travelled_distance+=m;
                this.location=this.road.getLength();
                this.road.getDestination().enter(this);
                this.status=VehicleStatus.WAITING;
                this.current_speed=0; //la velocidad del vehiculo debe ser 0 cuando su estado no es traveling
            }
            l=this.location-l;
            int c=l*f; //contaminacion producida
            this.total_contamination+=c;
            this.road.addContamination(c);
        }
    }
    
    void moveToNextRoad(){
        if(this.status==VehicleStatus.WAITING||this.status==VehicleStatus.PENDING){
            if(this.status==VehicleStatus.WAITING){
                this.road.exit(this);
            } 
            Junction jActual;
            Road nextRoad=null;
            this.location=0;
            if((this.last_junction+1)==this.itinerary.size()-1){
                this.status=VehicleStatus.ARRIVED;
            }
            else{
                this.last_junction++;
                jActual=this.itinerary.get(this.last_junction);
                nextRoad=jActual.roadTo(this.itinerary.get(this.last_junction+1));
                nextRoad.enter(this);
                this.road=nextRoad;
                this.status=VehicleStatus.TRAVELING;
            }   
        }
        else{
            throw new RuntimeException("El vehículo estado del vehículo no es WAITING o PENDING.");
        }
    }

    @Override
    public JSONObject report() {
        JSONObject jo = new JSONObject();
        jo.put("id", this._id);
        jo.put("speed", this.current_speed);
        jo.put("distance", this.total_travelled_distance);
        jo.put("co2", this.total_contamination);
        jo.put("class", this.contamination_class);
        jo.put("status", this.status);
        if(this.status!=VehicleStatus.ARRIVED&&this.status!=VehicleStatus.PENDING){
            jo.put("road", this.road.getId());
            jo.put("location", this.location);
        }
        return jo;
    }

    public int getCurrentSpeed() {
        return current_speed;
    }

    public int getLocation() {
        return location;
    }

    public Road getRoad() {
        return road;
    }

    public int getContaminationClass() {
        return contamination_class;
    }

    public List<Junction> getItinerary() {
        return itinerary;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public int getMaximumSpeed() {
        return maximum_speed;
    }

    public int getTotalCO2() {
        return total_contamination;
    }

    public int getTotalDistance() {
        return total_travelled_distance;
    }
    
    public void setRoad(Road road) {
        this.road = road;
    }

    @Override
    public int compareTo(Vehicle v) {
        if(this.location>v.getLocation()){
            return -1;
        } else if(this.location==v.getLocation()){
            return 0;
        } else{
            return 1;
        }
    }

}
