
package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy{
    @Override
    public List<Vehicle> dequeue(List<Vehicle> q) {
        List q2=new ArrayList();
        if(q.size()>0) q2.add(q.get(0));
        return q2;
    }
}
