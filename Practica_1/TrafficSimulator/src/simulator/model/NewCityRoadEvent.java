
package simulator.model;

public class NewCityRoadEvent extends NewRoadEvent{ 
    
    public NewCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
        super(time,id,srcJunc,destJunc,length,co2Limit,maxSpeed,weather);
    }

    @Override
    void execute(RoadMap map) {
        CityRoad r2=new CityRoad(id,map.getJunction(source),map.getJunction(destination),maximum_speed,contamination_alarm_limit,length,weather_conditions);
        map.addRoad(r2);
    }

}
