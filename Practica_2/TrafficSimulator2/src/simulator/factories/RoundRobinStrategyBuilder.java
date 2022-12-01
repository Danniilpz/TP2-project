
package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy>{

    public RoundRobinStrategyBuilder() {
        super("round_robin_lss");
    }    
    
    @Override
    protected LightSwitchingStrategy createTheInstance(JSONObject data) throws JSONException {
        int t=data.has("timeslot") ? (int)data.get("timeslot") : 1;
        return new RoundRobinStrategy(t);
    }
    
}
