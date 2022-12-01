
package simulator.factories;

import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{

    public SetWeatherEventBuilder() {
        super("set_weather");
    }

    @Override
    protected Event createTheInstance(JSONObject data) throws JSONException {
        int time=data.getInt("time");
        List<HashMap> l=(List<HashMap>)(Object)data.getJSONArray("info").toList();
        List<Pair<String,Weather>> pares=new ArrayList();
        for(HashMap o:l){
            pares.add(new Pair<String,Weather>((String)o.get("road"),Weather.valueOf((String)o.get("weather"))));
        }
        return new SetWeatherEvent(time,pares);
    }
    
}
