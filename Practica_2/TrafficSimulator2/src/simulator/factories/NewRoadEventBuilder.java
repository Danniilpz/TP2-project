
package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.Event;
import simulator.model.Weather;

public class NewRoadEventBuilder extends Builder<Event>{
    protected int time,length,co2limit,maxspeed;
    protected String id,src,dest;
    protected Weather weather;
    public NewRoadEventBuilder(String type) {
        super(type);
    }
    
    protected void rellenaValores(JSONObject data) throws JSONException {
        time=data.getInt("time");
        id=data.getString("id");
        src=data.getString("src");
        dest=data.getString("dest");
        length=data.getInt("length");
        co2limit=data.getInt("co2limit");
        maxspeed=data.getInt("maxspeed");
        weather=data.getEnum(Weather.class, "weather");
    }

    @Override
    protected Event createTheInstance(JSONObject data) throws JSONException {
        return null;
    }
    
}
