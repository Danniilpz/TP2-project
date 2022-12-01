
package simulator.model;


public abstract class NewRoadEvent extends Event{ 
    protected String id;
    protected String source;
    protected String destination;
    protected int length;
    protected int maximum_speed;
    protected int contamination_alarm_limit;
    protected Weather weather_conditions;
    
    public NewRoadEvent(int time, String id, String srcJunc, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
        super(time);
        this.id=id;
        this.source=srcJunc;
        this.destination=destJunc;
        this.contamination_alarm_limit=co2Limit;
        this.length=length;
        this.weather_conditions=weather;
        this.maximum_speed=maxSpeed;
    }

    @Override
    abstract void execute(RoadMap map);

}
