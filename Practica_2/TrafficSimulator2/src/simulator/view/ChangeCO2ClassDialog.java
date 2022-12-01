
package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import simulator.control.Controller;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog{
    private Controller controller;
    private JPanel mainPanel,secondary,buttonsPanel;
    private JLabel description,label1,label2,label3;
    private JComboBox<String> list1;
    private JComboBox<Integer> list2;
    private JSpinner ticks;
    private JButton cancelButton, OKButton;
    private int _status=1;
    public ChangeCO2ClassDialog(Frame parent,Controller c) {
        super(parent, true); 
        this.controller=c;
        initGUI();
    }

    private void initGUI() {
            setTitle("Change CO2 Class");
            mainPanel = new JPanel(new BorderLayout());
            description=new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now");
            mainPanel.add(description,BorderLayout.NORTH);
            secondary=new JPanel();
            label1=new JLabel("Vehicle: ");
            label2=new JLabel("CO2 Class: ");
            label3=new JLabel("Ticks:");
            list1=new JComboBox();            
            list1.setPreferredSize(new Dimension(80,25));
            String[] co2class={"0","1","2","3","4","5","6","7","8","9","10"};
            list2=new JComboBox(co2class);
            list2.setPreferredSize(new Dimension(80,25));
            SpinnerNumberModel model=new SpinnerNumberModel();
            model.setMinimum(0);
            JPanel spinnerPanel=new JPanel();
            ticks= new JSpinner(model);
            ticks.setValue(1);
            ticks.setPreferredSize(new Dimension(80,25));
            spinnerPanel.add(ticks);
            spinnerPanel.setMaximumSize(new Dimension(80,25));
            secondary.add(label1);
            secondary.add(list1);
            secondary.add(label2);
            secondary.add(list2);
            secondary.add(label3);
            secondary.add(spinnerPanel);
            mainPanel.add(secondary, BorderLayout.CENTER);
            buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);
            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        _status = 0;
                        ChangeCO2ClassDialog.this.setVisible(false);
                    }
            });
            buttonsPanel.add(cancelButton);
            OKButton = new JButton("OK");
            OKButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        _status = 1;
                        ChangeCO2ClassDialog.this.setVisible(false);
                    }
            });
            buttonsPanel.add(OKButton);
            mainPanel.add(buttonsPanel, BorderLayout.PAGE_END);
            this.addWindowListener(new WindowListener() {
                @Override
                public void windowClosing(WindowEvent e) {_status = 0;}
                @Override
                public void windowOpened(WindowEvent e) {}
                @Override
                public void windowClosed(WindowEvent e) {}
                @Override
                public void windowIconified(WindowEvent e) {}
                @Override
                public void windowDeiconified(WindowEvent e) {}
                @Override
                public void windowActivated(WindowEvent e) {}
                @Override
                public void windowDeactivated(WindowEvent e) {}
            });
            setContentPane(mainPanel);
            setMinimumSize(new Dimension(100, 100));
            setVisible(false);
    }
    public int open() {
        list1.setModel(new DefaultComboBoxModel(controller.getSim().getRoadMap().getVehicleIds())); //update list of vehicles
        if(list1.getItemCount()==0) this.OKButton.setEnabled(false);
        else this.OKButton.setEnabled(true);
        setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
        pack();
        setVisible(true);
        return _status;
    }

    public String getVehicle() {
        return list1.getSelectedItem().toString();
    }
    public int getCO2Class() {
        return Integer.parseInt(list2.getSelectedItem().toString());
    }
    public int getTicks() {
        return (int)ticks.getValue();
    }
}
