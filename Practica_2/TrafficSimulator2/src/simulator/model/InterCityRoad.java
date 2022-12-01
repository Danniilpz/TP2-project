
package simulator.model;

public class InterCityRoad extends Road{

    public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
        super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
    }

    @Override
    public void reduceTotalContamination() {
        int x=0;
        if(this.weather_conditions==Weather.SUNNY) x=2;
        else if(this.weather_conditions==Weather.CLOUDY) x=3;
        else if(this.weather_conditions==Weather.RAINY) x=10;
        else if(this.weather_conditions==Weather.WINDY) x=15;
        else if(this.weather_conditions==Weather.STORM) x=20;
        this.total_contamination=((int)(((100.0-x)/100.0)*this.total_contamination));
    }

    @Override
    public void updateSpeedLimit() {
        if(this.total_contamination>this.contamination_alarm_limit){
            this.current_speed_limit=((int)(this.maximum_speed*0.5));
        }
        else{
            this.current_speed_limit=this.maximum_speed;
        }
    }

    @Override
    public int calculateVehicleSpeed(Vehicle v) {
        if(this.weather_conditions==Weather.STORM) return ((int)(this.maximum_speed*0.8));
        else return this.current_speed_limit;
    }
    
}
