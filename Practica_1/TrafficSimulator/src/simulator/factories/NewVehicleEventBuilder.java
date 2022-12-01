
package simulator.factories;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{

    public NewVehicleEventBuilder() {
        super("new_vehicle");
    }
    
    @Override
    protected Event createTheInstance(JSONObject data) throws JSONException {
        int time=data.getInt("time");
        String id=data.getString("id");
        int maxspeed=data.getInt("maxspeed");
        int clas=data.getInt("class");
        List<String> itinerary=(List<String>)(Object)data.getJSONArray("itinerary").toList();
        return new NewVehicleEvent(time,id,maxspeed,clas,itinerary);
    }
    
}
