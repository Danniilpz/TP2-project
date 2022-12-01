
package simulator.model;

public class NewJunctionEvent extends Event{
    private String id;
    private LightSwitchingStrategy lsStrategy;
    private DequeuingStrategy dqStrategy;
    private int x;
    private int y;    
    public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
        super(time);
        this.id=id;
        this.lsStrategy=lsStrategy;
        this.dqStrategy=dqStrategy;
        this.x=xCoor;
        this.y=yCoor;
    }

    
    @Override
    void execute(RoadMap map) {
        map.addJunction(new Junction(id,lsStrategy,dqStrategy,x,y));
    }

    @Override
    public String toString() {
        return "New Junction '"+this.id+"'";
    }
    
}
