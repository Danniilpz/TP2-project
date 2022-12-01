
package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.*;

public class ControlPanel extends JPanel implements TrafficSimObserver,ActionListener{
    private Controller controller;
    private JFrame main;
    private JToolBar toolBar;
    private JButton open,co2class,weather,run,stop,exit;
    private JSpinner ticks;
    private boolean _stopped;
    private JFileChooser fc;
    private ChangeCO2ClassDialog dialog;
    private ChangeWeatherDialog dialog2;
    public ControlPanel(Controller c,JFrame main) {
        super(new BorderLayout());
        this.controller=c;
        this.main=main;
        this.controller.addObserver(this);
        fc=new JFileChooser();
        toolBar = new JToolBar(); 
        toolBar.setOpaque(false);
        this.add(toolBar);
        open = new JButton(); 
        open.setActionCommand("open"); 
        open.setToolTipText("Load a file"); 
        open.addActionListener(this); 
        open.setIcon(new ImageIcon("resources/icons/open.png") ); 
        toolBar.add(open);
        toolBar.addSeparator();
        co2class = new JButton(); 
        co2class.setActionCommand("co2class"); 
        co2class.setToolTipText("Change CO2 class of a Vehicle"); 
        co2class.addActionListener(this); 
        co2class.setIcon(new ImageIcon("resources/icons/co2class.png") ); 
        toolBar.add(co2class);
        weather = new JButton(); 
        weather.setActionCommand("weather"); 
        weather.setToolTipText("Change Weather of a Road"); 
        weather.addActionListener(this); 
        weather.setIcon(new ImageIcon("resources/icons/weather.png") ); 
        toolBar.add(weather);
        toolBar.addSeparator();
        run = new JButton(); 
        run.setActionCommand("run"); 
        run.setToolTipText("Run simulation"); 
        run.addActionListener(this); 
        run.setIcon(new ImageIcon("resources/icons/run.png") ); 
        toolBar.add(run);
        stop = new JButton(); 
        stop.setActionCommand("stop"); 
        stop.setToolTipText("Stop simulation"); 
        stop.addActionListener(this); 
        stop.setIcon(new ImageIcon("resources/icons/stop.png") ); 
        toolBar.add(stop);        
        SpinnerNumberModel model=new SpinnerNumberModel();
        model.setMinimum(0);
        JLabel ticks_label=new JLabel("Ticks:");
        JPanel spinnerPanel=new JPanel();
        ticks= new JSpinner(model);
        ticks.setValue(10);
        ticks.setPreferredSize(new Dimension(100,45));
        spinnerPanel.add(ticks);
        spinnerPanel.setMaximumSize(new Dimension(100,50));
        toolBar.add(ticks_label);
        toolBar.add(spinnerPanel);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.addSeparator();
        exit = new JButton(); 
        exit.setActionCommand("exit"); 
        exit.setToolTipText("Save a file"); 
        exit.addActionListener(this); 
        exit.setIcon(new ImageIcon("resources/icons/exit.png") ); 
        toolBar.add(exit);
        dialog=new ChangeCO2ClassDialog(main,controller);
        dialog2=new ChangeWeatherDialog(main,controller);
    }
    
    @Override
    public void onAdvanceStart(RoadMap map, List<Event> events, int time) {}

    @Override
    public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {}

    @Override
    public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {}

    @Override
    public void onReset(RoadMap map, List<Event> events, int time) {
        enableToolBar(true);
    }

    @Override
    public void onRegister(RoadMap map, List<Event> events, int time) {}

    @Override
    public void onError(String err) {
        JOptionPane.showMessageDialog(new JFrame(),err, "Error",JOptionPane.ERROR_MESSAGE);
        controller.reset();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== open){
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    controller.reset();
                try {
                    controller.loadEvents(new FileInputStream(file));
                } catch (Exception ex) {
                    System.err.println("Error");
                    JOptionPane.showMessageDialog(new JFrame(),
                    "An error occurred while loading file events.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if(e.getSource()==run){
            if(!(this.controller.getSim().getTime()==0&&this.controller.getSim().getListaEventos().size()==0)){
                _stopped=false;
                enableToolBar(false);
                this.run_sim((int)this.ticks.getValue()); 
            }
        }
        else if(e.getSource()==stop){
            enableToolBar(true);
            this.stop();
        }
        else if(e.getSource()==exit){
            int n = JOptionPane.showOptionDialog(new JFrame(),
				"Are sure you want to quit?", "Quit",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);
            if (n == 0) {
                System.exit(0);
            }
        }
        else if(e.getSource()==co2class){
            int status = dialog.open();
            if ( status == 1) {
                Pair<String,Integer> p=new Pair(dialog.getVehicle(),dialog.getCO2Class());
                List<Pair<String,Integer>> l=new ArrayList();
                l.add(p);
                this.controller.addEvent(new SetContClassEvent(this.controller.getSim().getTime()+dialog.getTicks(),l));
            }
        }
        else if(e.getSource()==weather){
            int status = dialog2.open();
            if ( status == 1) {
                Pair<String,Weather> p=new Pair(dialog2.getRoad(),Weather.valueOf(dialog2.getWeather()));
                List<Pair<String,Weather>> l=new ArrayList();
                l.add(p);
                this.controller.addEvent(new SetWeatherEvent(this.controller.getSim().getTime()+dialog2.getTicks(),l));
            }
        }
    }
    private void run_sim(int n) {
        if (n > 0 && !_stopped) {
            try {
                controller.run(1);
            } catch (Exception e) {
                _stopped = true;
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    run_sim(n - 1);
                }
                });
        } else {
            enableToolBar(true);
            _stopped = true;
        }
    }
    private void stop() {
        _stopped = true;
    }

    private void enableToolBar(boolean enable) {
        open.setEnabled(enable);
        co2class.setEnabled(enable);
        weather.setEnabled(enable);
        run.setEnabled(enable);
        exit.setEnabled(enable);
    }

    
}
