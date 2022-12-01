
package simulator.factories;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{
    Factory<LightSwitchingStrategy> lsStrategy;
    Factory<DequeuingStrategy> dqStrategy;
    public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lsStrategy, Factory<DequeuingStrategy> dqStrategy){
        super("new_junction");
        this.dqStrategy=dqStrategy;
        this.lsStrategy=lsStrategy;
    }
    
    @Override
    protected Event createTheInstance(JSONObject data) throws JSONException{
        int time=data.getInt("time");
        String id=data.getString("id");
        List<Integer> coors=(List<Integer>)(Object)data.getJSONArray("coor").toList();
        int x=coors.get(0);
        int y=coors.get(1);
        JSONObject j=data.getJSONObject("ls_strategy");
        LightSwitchingStrategy ls_strategy=lsStrategy.createInstance(j);
        DequeuingStrategy dq_strategy=dqStrategy.createInstance(data.getJSONObject("dq_strategy"));
        return new NewJunctionEvent(time,id,ls_strategy,dq_strategy,x,y);
    }
    
}
