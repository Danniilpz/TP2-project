
package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject{
    protected Junction source;
    protected Junction destination;
    protected int length;
    protected int maximum_speed;
    protected int current_speed_limit;
    protected int contamination_alarm_limit;
    protected Weather weather_conditions;
    protected int total_contamination;
    protected List<Vehicle> vehicles;
    
    Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
        super(id);
        if(maxSpeed<0){
            throw new IllegalArgumentException("La velocidad máxima debe ser mayor que 0.");
        }
        else if(contLimit<0){
            throw new IllegalArgumentException("El límite de contaminación debe ser mayor que 0.");
        }
        else if(length<0){
            throw new IllegalArgumentException("La longitud debe ser mayor que 0.");
        }
        else if(srcJunc==null||destJunc==null||weather==null){
            throw new IllegalArgumentException("El cruce de origen, destino y el tiempo no pueden ser null.");
        }
        else{
            this.source=srcJunc;
            this.destination=destJunc;
            this.contamination_alarm_limit=contLimit;
            this.length=length;
            this.weather_conditions=weather;
            this.maximum_speed=maxSpeed;
            this.current_speed_limit=this.maximum_speed;
            this.total_contamination=0;
            this.vehicles=new SortedArrayList();
            srcJunc.addOutGoingRoad(this);
            destJunc.addIncommingRoad(this);
        }
    }

    void enter(Vehicle v){
        if(v.getLocation()!=0||v.getCurrentSpeed()!=0){
            throw new RuntimeException("La localización del vehículo o su velocidad debe ser 0.");
        }
        else{
            this.vehicles.add(v);
        }
    }
    
    void exit(Vehicle v){
        this.vehicles.remove(v);
    }

    void setWeather(Weather w) {
        if(w==null){
            throw new IllegalArgumentException("El tiempo no puede ser null.");
        }
        else{
            this.weather_conditions = w;
        }
    }
    
    void addContamination(int c){
        if(c<0){
            throw new IllegalArgumentException("La contaminación no puede ser menor que 0.");
        }
        else{
            this.total_contamination+=c;
        }
    }
    
    abstract void reduceTotalContamination();
    
    abstract void updateSpeedLimit();
    
    abstract int calculateVehicleSpeed(Vehicle v);
    
    @Override
    void advance(int time) {
        this.reduceTotalContamination();
        this.updateSpeedLimit();
        for(Vehicle v:this.vehicles){
            if(v.getStatus()==VehicleStatus.TRAVELING){
                v.setSpeed(this.calculateVehicleSpeed(v));
            }            
            v.advance(time);
        }
        Collections.sort(vehicles); //Ordenar lista de vehiculos por su localización
    }

    public List<Vehicle> getVehicles() {
        return Collections.unmodifiableList(this.vehicles);
    }

    public Junction getDestination() {
        return destination;
    }

    public Junction getSource() {
        return source;
    }

    public int getLength() {
        return length;
    }

    public int getTotalCO2() {
        return total_contamination;
    }

    public int getCO2Limit() {
        return contamination_alarm_limit;
    }    

    public Weather getWeather() {
        return weather_conditions;
    }

    public int getMaxSpeed() {
        return maximum_speed;
    }

    public int getSpeedLimit() {
        return current_speed_limit;
    }
        
    @Override
    public JSONObject report() {
        JSONObject jo = new JSONObject();
        jo.put("id", this._id);
        jo.put("speedlimit", this.current_speed_limit);
        jo.put("weather", this.weather_conditions);
        jo.put("co2", this.total_contamination);
        JSONArray ja=new JSONArray();
        for(Vehicle v:this.vehicles){
            ja.put(v.getId());
        }
        jo.put("vehicles", ja); 
        return jo;    
    }
    
}
