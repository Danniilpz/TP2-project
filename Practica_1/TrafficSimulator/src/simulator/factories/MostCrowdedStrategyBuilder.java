
package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.DequeuingStrategy;
import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;
import simulator.model.MoveFirstStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{

    public MostCrowdedStrategyBuilder() {
        super("most_crowded_lss");
    }    
    
    @Override
    protected LightSwitchingStrategy createTheInstance(JSONObject data) throws JSONException {
        int t=data.has("timeslot") ? (int)data.get("timeslot") : 1;
        return new MostCrowdedStrategy(t);
    }
    
}
    

