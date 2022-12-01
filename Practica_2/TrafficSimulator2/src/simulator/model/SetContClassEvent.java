
package simulator.model;

import java.util.List;
import simulator.misc.Pair;

public class SetContClassEvent extends Event{
    private List<Pair<String,Integer>> cs;
    public SetContClassEvent(int time, List<Pair<String,Integer>> cs) {
        super(time);
        if(cs==null){
            throw new IllegalArgumentException("cs no puede ser null.");
        }
        else{
            this.cs=cs;
        }
    }

    @Override
    void execute(RoadMap map) {
        for(Pair<String,Integer> c:cs){
            Vehicle v=map.getVehicle(c.getFirst());
            if(c==null){
                throw new IllegalArgumentException("El vehículo no existe.");
            }
            else{
                v.setContaminationClass(c.getSecond());
            }
        }   
    }
    
    @Override
    public String toString() {
        String s= "Change CO2 class: [";
        for(Pair<String,Integer> c:cs){
            s+="("+c.getFirst()+","+c.getSecond()+"),";
        }
        s=s.substring(0, s.length()-1);
        s+="]";
        return s;
    }
}
