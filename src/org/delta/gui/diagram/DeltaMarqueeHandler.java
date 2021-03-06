package org.delta.gui.diagram;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.delta.gui.MainWindow;

import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.Port;
import org.jgraph.graph.PortView;

// MarqueeHandler that Connects Vertices and Displays PopupMenus
public class DeltaMarqueeHandler extends BasicMarqueeHandler implements Serializable {
	
	/** Needed for correct serializaiton. */
	private static final long serialVersionUID = 1L;

	// Holds a reference to the graph
	private JGraph graph;
	
	// Holds the Start and the Current Point
	protected Point2D start, current;

	// Holds the First and the Current Port
	protected PortView port, firstPort;

	/**
	 * Creates a new DeltaMarqueeHandler for the given graph.
	 * 
	 * @param graph - Graph that the DeltaMarqueeHandler will operate with.
	 */
	public DeltaMarqueeHandler(JGraph graph) {
		super();
		this.graph = graph;
	}

	// Override to Gain Control (for PopupMenu and ConnectMode)
	public boolean isForceMarqueeEvent(MouseEvent e) {
		if (e.isShiftDown())
			return false;
		// If Right Mouse Button we want to Display the PopupMenu
		if (SwingUtilities.isRightMouseButton(e))
			return true;
		// Find and remember port
		port = getSourcePortAt(e.getPoint());
		// If port found, ports are visible and port not already connected
		if (port != null && graph.isPortsVisible()
				&& !((Port)port.getCell()).edges().hasNext())
			return true;
		// Else call superclass
		return super.isForceMarqueeEvent(e);
	}

	// Display PopupMenu or Remember Start Location and First Port
	public void mousePressed(final MouseEvent e) {
		// If Right Mouse Button
		if (SwingUtilities.isRightMouseButton(e)) {
			// Find Cell in Model Coordinates
			Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
			// Create PopupMenu for the Cell
			JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
			// Display PopupMenu
			menu.show(graph, e.getX(), e.getY());
			// Else if in ConnectMode and Remembered Port is Valid
		} else if (port != null && graph.isPortsVisible()) {
			// Remember Start Location
			start = graph.toScreen(port.getLocation());
			// Remember First Port
			firstPort = port;
		} else {
			// Call Superclass
			super.mousePressed(e);
		}
	}
    
    /**
     * Creates right-click context menu for the graph. At the moment is only
     * used to change the contents of a ROM after it has been added.
     * @param pt - point where the right-click occurred.
     * @param cell - cell at that point.
     * @return the popup menu relevant for this context.
     */
    public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
        JPopupMenu menu = new JPopupMenu();
        if (cell instanceof ROM) {
            menu.add(new AbstractAction(MainWindow.getTranslatorString("EDIT_POPUP")) {
                // Needed for correct serialization.
                private static final long serialVersionUID = 1L;
                // Change the ROM contents by calling the TransferHandler method
                public void actionPerformed(ActionEvent e) {
                    DeltaGraphTransferHandler handler =
                        (DeltaGraphTransferHandler) graph.getTransferHandler();
                    ROM rom = (ROM) cell;
                    rom.setStore(handler.getUserMemorySelection(rom));
                }
            });
        }
        /*if (cell != null) {
            // Edit
            menu.add(new AbstractAction("Edit") {
                public void actionPerformed(ActionEvent e) {
                    graph.startEditingAtCell(cell);
                }
            });
        }
        // Remove
        if (!graph.isSelectionEmpty()) {
            menu.addSeparator();
            menu.add(new AbstractAction("Remove") {
                public void actionPerformed(ActionEvent e) {
                    remove.actionPerformed(e);
                }
            });
        }
        menu.addSeparator();
        // Insert
        menu.add(new AbstractAction("Insert") {
            public void actionPerformed(ActionEvent ev) {
                insert(pt);
            }
        });*/
        return menu;
    }

	// Find Port under Mouse and Repaint Connector
	public void mouseDragged(MouseEvent e) {
		// If remembered Start Point is Valid
		if (start != null) {
			// Fetch Graphics from Graph
			Graphics g = graph.getGraphics();
			// Reset Remembered Port
			PortView newPort = getTargetPortAt(e.getPoint());
			// Do not flicker (repaint only on real changes)
			if (newPort == null || newPort != port) {
				// Xor-Paint the old Connector (Hide old Connector)
				paintConnector(Color.black, graph.getBackground(), g);
				// If Port was found then Point to Port Location
				port = newPort;
				if (port != null)
					current = graph.toScreen(port.getLocation());
				// Else If no Port was found then Point to Mouse Location
				else
					current = graph.snap(e.getPoint());
				// Xor-Paint the new Connector
				paintConnector(graph.getBackground(), Color.black, g);
			}
		}
		// Call Superclass
		super.mouseDragged(e);
	}

	public PortView getSourcePortAt(Point2D point) {
		// Disable jumping
		graph.setJumpToDefaultPort(false);
		PortView result;
		try {
			// Find a Port View in Model Coordinates and Remember
			result = graph.getPortViewAt(point.getX(), point.getY());
		} finally {
			graph.setJumpToDefaultPort(true);
		}
		return result;
	}

	// Find a Cell at point and Return its first Port as a PortView
	protected PortView getTargetPortAt(Point2D point) {
		// Find a Port View in Model Coordinates and Remember
		return graph.getPortViewAt(point.getX(), point.getY());
	}
	
	// Connect the First Port and the Current Port in the Graph or Repaint
	public void mouseReleased(MouseEvent e) {
		// If Valid Event, Current and First Port, then check proposed Edge is valid
		if (e != null && port != null && firstPort != null && firstPort != port) {
			DeltaEdge edge = new DeltaEdge();
			boolean samePortType = false;
			// Check other end of edge is not the same type of port
			if ((port.getCell() instanceof DeltaInputPort)
				&& (firstPort.getCell() instanceof DeltaInputPort))
					samePortType = true;
			else if ((port.getCell() instanceof DeltaOutputPort)
				&& (firstPort.getCell() instanceof DeltaOutputPort))
					samePortType = true;
			// If valid edge, add it to the graph
			if (graph.getModel().acceptsSource(edge, firstPort.getCell())
					&& graph.getModel().acceptsTarget(edge, port.getCell())
						&& !samePortType) {
					graph.getGraphLayoutCache().insertEdge(edge, firstPort.getCell(), port.getCell());
			}
			// If not then repaint
			else
				graph.repaint();
			e.consume();
		// Else Repaint the Graph
		} else
			graph.repaint();
		// Reset Global Vars
		firstPort = port = null;
		start = current = null;
		// Call Superclass
		super.mouseReleased(e);
	}

	// Show Special Cursor if Over Port
	/*public void mouseMoved(MouseEvent e) {
		// Check Mode and Find Port
		if (e != null && getSourcePortAt(e.getPoint()) != null
				&& graph.isPortsVisible()) {
			// Set Cusor on Graph (Automatically Reset)
			graph.setCursor(new Cursor(Cursor.HAND_CURSOR));
			// Consume Event
			// Note: This is to signal the BasicGraphUI's
			// MouseHandle to stop further event processing.
			e.consume();
		} else
			// Call Superclass
			super.mouseMoved(e);
	}*/

	// Use Xor-Mode on Graphics to Paint Connector
	protected void paintConnector(Color fg, Color bg, Graphics g) {
		// Set Foreground
		g.setColor(fg);
		// Set Xor-Mode Color
		g.setXORMode(bg);
		// Highlight the Current Port
		paintPort(graph.getGraphics());
		// If Valid First Port, Start and Current Point
		if (firstPort != null && start != null && current != null)
			// Then Draw A Line From Start to Current Point
			g.drawLine((int) start.getX(), (int) start.getY(),
					(int) current.getX(), (int) current.getY());
	}

	// Use the Preview Flag to Draw a Highlighted Port
	protected void paintPort(Graphics g) {
		// If Current Port is Valid
		if (port != null) {
			// If Not Floating Port...
			boolean o = (GraphConstants.getOffset(port.getAllAttributes()) != null);
			// ...Then use Parent's Bounds
			Rectangle2D r = (o) ? port.getBounds() : port.getParentView()
					.getBounds();
			// Scale from Model to Screen
			r = graph.toScreen((Rectangle2D) r.clone());
			// Add Space For the Highlight Border
			r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r
					.getHeight() + 6);
			// Paint Port in Preview (=Highlight) Mode
			graph.getUI().paintCell(g, port, r, true);
		}
	}

}