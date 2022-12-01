
package simulator.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{
    private Controller controller;
    private JLabel time,info;
    public StatusBar(Controller c) {        
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        this.controller=c;
        this.controller.addObserver(this);
        time=new JLabel("Time: 0");        
        info=new JLabel("Welcome!");
        this.add(createPanel(time,180,30));
        this.add(createPanel(info,500,30));
    }
    private JPanel createPanel(JLabel label, int x, int y) {
        JPanel panel;
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(label);
        panel.setMaximumSize(new Dimension(x, y));
        return panel; 
    }
    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
        this.time.setText("Time: "+time);
        this.info.setText("");
    }

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {}

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
        this.info.setText("Event added ("+e.toString()+")");
    }

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        this.time.setText("Time: 0");        
        this.info.setText("Welcome!");
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {}

    @Override
    public void onError(String err) {}
}
