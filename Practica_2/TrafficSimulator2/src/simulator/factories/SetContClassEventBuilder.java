
package simulator.factories;

import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{

    public SetContClassEventBuilder() {
        super("set_cont_class");
    }

    @Override
    protected Event createTheInstance(JSONObject data) throws JSONException {
        int time=data.getInt("time");
        List<HashMap> l=(List<HashMap>)(Object)data.getJSONArray("info").toList();
        List<Pair<String,Integer>> pares=new ArrayList();
        for(HashMap o:l){
            pares.add(new Pair<String,Integer>((String)o.get("vehicle"),(int)o.get("class")));
        }
        return new SetContClassEvent(time,pares);
    }
    
}
