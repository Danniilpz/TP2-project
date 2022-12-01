package simulator.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import simulator.control.Controller;

public class MainWindow extends JFrame {

    private Controller _ctrl;

    public MainWindow(Controller ctrl) {
        super("Traffic Simulator");
        _ctrl = ctrl;
        initGUI();
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.setContentPane(mainPanel);
        mainPanel.add(new ControlPanel(_ctrl,this), BorderLayout.PAGE_START);
        mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);
        JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(viewsPanel, BorderLayout.CENTER);
        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
        viewsPanel.add(tablesPanel);
        JPanel mapsPanel = new JPanel();
        mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
        viewsPanel.add(mapsPanel);
        // tables
        JTable events=new JTable(new EventsTableModel(_ctrl));
        events.setFillsViewportHeight(true);
        events.setShowGrid(false);
        JPanel eventsView = createViewPanel(events, "Events");
        eventsView.setPreferredSize(new Dimension(500, 200));
        tablesPanel.add(eventsView);
        JTable vehicles=new JTable(new VehiclesTableModel(_ctrl));
        vehicles.setFillsViewportHeight(true);
        vehicles.setShowGrid(false);
        JPanel vehiclesView = createViewPanel(vehicles, "Vehicles");
        vehiclesView.setPreferredSize(new Dimension(500, 200));
        tablesPanel.add(vehiclesView);
        JTable roads=new JTable(new RoadsTableModel(_ctrl));
        roads.setFillsViewportHeight(true);
        roads.setShowGrid(false);
        JPanel roadsView = createViewPanel(roads, "Roads");
        roadsView.setPreferredSize(new Dimension(500, 200));
        tablesPanel.add(roadsView);
        JTable junctions=new JTable(new JunctionsTableModel(_ctrl));
        junctions.setFillsViewportHeight(true);
        junctions.setShowGrid(false);
        JPanel junctionsView = createViewPanel(junctions, "Junctions");
        junctionsView.setPreferredSize(new Dimension(500, 200));
        tablesPanel.add(junctionsView);
        // maps
        JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
        mapView.setPreferredSize(new Dimension(500, 400));
        mapsPanel.add(mapView);
        JPanel mapView2 = createViewPanel(new MapByRoadComponent(_ctrl), "Map by Road");
        mapView2.setPreferredSize(new Dimension(500, 400));
        mapsPanel.add(mapView2);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private JPanel createViewPanel(JComponent c, String title) {
        JPanel p = new JPanel(new BorderLayout());
        Border b = BorderFactory.createLineBorder(Color.black, 2);
        p.setBorder(BorderFactory.createTitledBorder(b, title));
        p.add(new JScrollPane(c));
        return p;
    }
}
