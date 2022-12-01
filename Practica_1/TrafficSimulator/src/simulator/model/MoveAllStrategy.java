
package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy{
    @Override
    public List<Vehicle> dequeue(List<Vehicle> q) {
        List q2=new ArrayList();
        if(q.size()>0){
            for(Vehicle v:q){
                q2.add(v);
            }
        }        
        return q2;
    }
}