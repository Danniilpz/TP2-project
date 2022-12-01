
package simulator.model;

public class CityRoad extends Road{

    public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
        super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
    }

    @Override
    public void reduceTotalContamination() {
        int x=0,contamination;
        if(this.weather_conditions==Weather.WINDY||
            this.weather_conditions==Weather.STORM) x=10;
        else x=2;
        contamination=this.total_contamination-x;
        if(contamination>=0) this.total_contamination=contamination;
        else this.total_contamination=0;
    }

    @Override
    public void updateSpeedLimit() {
        this.current_speed_limit=this.maximum_speed;
    }

    @Override
    public int calculateVehicleSpeed(Vehicle v) {
        return (int)(((11.0-v.getContaminationClass())/11.0)*this.maximum_speed);
    }
    
}