
package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{
    private int timeSlot;
    
    public MostCrowdedStrategy(int timeSlot) {
        this.timeSlot=timeSlot;
    }

    @Override
    public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime, int currTime) {
        if(roads.isEmpty()){
            return -1;
        } else if(currGreen==-1){
            int i=0,mayor=0;
            for(List<Vehicle> cola:qs){
                if(cola.size()>mayor) mayor=i;
                i++;
            }
            return mayor;
        }
        else if((currTime-lastSwitchingTime) <timeSlot){
            return currGreen;
        }
        else{
            int mayor=0;
            for(int i=((currGreen+1)%roads.size());(i%roads.size())<currGreen;i++){
                List<Vehicle> cola=qs.get((i%roads.size()));
                if(cola.size()>mayor) mayor=i;
                i++;
            }
            return mayor;
        }
    }
}
