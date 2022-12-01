
package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent{ 
    
    public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
        super(time,id,srcJunc,destJunc,length,co2Limit,maxSpeed,weather);
    }

    @Override
    void execute(RoadMap map) {
        InterCityRoad r2=new InterCityRoad(id,map.getJunction(source),map.getJunction(destination),maximum_speed,contamination_alarm_limit,length,weather_conditions);
        map.addRoad(r2);
    }

}
