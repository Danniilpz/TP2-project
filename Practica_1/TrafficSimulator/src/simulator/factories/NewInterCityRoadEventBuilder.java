
package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder{

    public NewInterCityRoadEventBuilder() {
        super("new_inter_city_road");
    }
    
    @Override
    protected Event createTheInstance(JSONObject data) throws JSONException {
        super.createTheInstance(data);
        return new NewInterCityRoadEvent(time,id,src,dest,length,co2limit,maxspeed,weather);
    }
    
}
