
package simulator.model;

import java.util.*;

public class NewVehicleEvent extends Event{
    private String id;
    private List<String> itinerary;
    private int maximum_speed;
    private int contamination_class;
    
    public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
        super(time);
        this.id=id;
        this.itinerary=itinerary;
        this.maximum_speed=maxSpeed;
        this.contamination_class=contClass;
    }

    @Override
    void execute(RoadMap map) {
        List<Junction> itineraryJ=new ArrayList();
        for(String s:itinerary){
            itineraryJ.add(map.getJunction(s));
        }
        map.addVehicle(new Vehicle(id,maximum_speed,contamination_class,itineraryJ));
    }
}
