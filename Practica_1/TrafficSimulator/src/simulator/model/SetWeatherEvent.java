
package simulator.model;

import java.util.List;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
    private List<Pair<String,Weather>> ws;
    public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
        super(time);
        if(ws==null){
            throw new IllegalArgumentException("ws no puede ser null.");
        }
        else{
            this.ws=ws;
        }
    }

    @Override
    void execute(RoadMap map) {
        for(Pair<String,Weather> w:ws){
            Road r=map.getRoad(w.getFirst());
            if(r==null){
                throw new RuntimeException("La carretera no existe.");
            }
            else{
                r.setWeather(w.getSecond());
            }
        }
    }
}
