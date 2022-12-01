package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;

	private Image _car;

	MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
                setPreferredSize (new Dimension (300, 200));
	}

	private void initGUI() {
		_car = loadImage("car.png");
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());
		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		drawRoads(g);
		drawVehicles(g);
	}

	private void drawRoads(Graphics g) {
            int i=0;
		for (Road r : _map.getRoads()) {
			// the road goes from (x1,y) to (x2,y)
			int x1 = 50;
			int x2 =getWidth()-100;
			int y = (i+1)*50;                        
			drawLine(g, x1, x2, y, Color.BLACK);
                        // a label with the road id
                        g.drawString(r.getId(), 20, y+2);
                        // weather icon
                        String weather="";
                        switch(r.getWeather()){
                            case SUNNY:
                                weather="sun";
                                break;
                            case CLOUDY:
                                weather="cloud";
                                break;
                            case STORM:
                                weather="storm";
                                break;
                            case WINDY:
                                weather="wind";
                                break;
                            case RAINY:
                                weather="rain";
                                break;
                        }
                        g.drawImage(loadImage(weather+".png"), getWidth()-90, y-(32/2), 32, 32, this);
                        // CO2 icon
                        int c = (int) Math.floor(Math.min((double) r.getTotalCO2()/(1.0 + (double) r.getCO2Limit()),1.0) / 0.19);
                        g.drawImage(loadImage("cont_"+c+".png"), getWidth()-50, y-(32/2), 32, 32, this);
                        
                        drawJunctions(g,r,y);
                        i++;
		}

	}

	private void drawVehicles(Graphics g) {
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				Road r = v.getRoad();
                                int i=0;
                                for (Road road : _map.getRoads()) {
                                    if(road.getId()==r.getId()){
                                        break;
                                    }
                                    i++;
                                }
				int x1 = 50;
				int x2 = getWidth()-100;
				int y = (i+1)*50;
				double roadLength = x2-x1;
				double x = roadLength * ((double) v.getLocation()) / ((double) r.getLength()); 
				int vX = x1 + ((int) x); // vehicle always goes to the right here
				// Choose a color for the vehcile's label and background, depending on its
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContaminationClass()));
				g.setColor(new Color(0, vLabelColor, 0));                                
				// draw an image of a car and it identifier
				g.drawImage(_car, vX, y-(2*16/3), 16, 16, this);
				g.drawString(v.getId(), vX, y-(2*16/3));
			}
		}
	}

	private void drawJunctions(Graphics g,Road r,int y) {
                // choose a color for the destination junction depending on the traffic light of the road
                Color destColor = _RED_LIGHT_COLOR;
                int idx = r.getDestination().getGreenLightIndex();
                if (idx != -1 && r.equals(r.getDestination().getInRoads().get(idx))) {
                        destColor = _GREEN_LIGHT_COLOR;
                }
               
                int x1 = 50;  //first junction
                int x2=getWidth()-100;  //second junction

                // draw a circle with center at (x,y) with radius _JRADIUS (first junction)
                g.setColor(_JUNCTION_COLOR);
                g.fillOval(x1-(_JRADIUS/2), y-(_JRADIUS/2), _JRADIUS, _JRADIUS);
                // draw a circle with center at (x,y) with radius _JRADIUS (second junction)
                g.setColor(destColor);
                g.fillOval(x2-(_JRADIUS/2), y-(_JRADIUS/2), _JRADIUS, _JRADIUS);

                // draw the junction's identifier at (x1,y-7)
                g.setColor(_JUNCTION_LABEL_COLOR);
                g.drawString(r.getSource().getId(), x1, y-7);
                // draw the junction's identifier at (x2,y-7)
                g.setColor(_JUNCTION_LABEL_COLOR);
                g.drawString(r.getDestination().getId(), x2, y-7);
	}

	// This method draws a line from (x1,y) to (x2,y).
	// The last argument is the color of the line
	private void drawLine(//
			Graphics g, //
			int x1, int x2, int y, //
			Color lineColor) {
		g.setColor(lineColor);
		g.drawLine(x1, y, x2, y);
	}

	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

}
