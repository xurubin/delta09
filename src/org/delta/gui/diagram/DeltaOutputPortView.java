package org.delta.gui.diagram;

import org.jgraph.graph.*;

import java.awt.geom.*;
import java.awt.Color;

public class DeltaOutputPortView extends PortView
{
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Color normalColor = new Color(99,108,137);
	private static final Color highlightColor = new Color(166,72,190);
	private static DeltaPortRenderer renderer = new DeltaPortRenderer();
     
     public DeltaOutputPortView()
     {
           super();
           GraphConstants.setForeground(this.getAttributes(), normalColor);
           GraphConstants.setBackground(this.getAttributes(), highlightColor);
     }
     
     public DeltaOutputPortView(Object arg0)
     {
           super(arg0);
           GraphConstants.setForeground(this.getAttributes(), normalColor);
           GraphConstants.setBackground(this.getAttributes(), highlightColor);
     }
     
     /**
      * This method is overwritten so we can use our custom DeltaPortRenderer.
      * @return reference to the port renderer
      */
     public CellViewRenderer getRenderer()
     {
           return renderer;
     }
     
     public Point2D getPerimeterPoint(EdgeView edge,Point2D source,Point2D p)
     {
         // No special implementation of this required as all edges
    	 // will terminate at ports.
    	 return super.getPerimeterPoint(edge,source,p);
     }
     
     /**
      * Returns the bounds of the port as a rectangle.
      * @return the bounds of the port
      */
     public Rectangle2D getBounds() {
 		Point2D loc = getLocation();
 		double x = 0;
 		double y = 0;
 		if (loc != null) {
 			x = loc.getX();
 			y = loc.getY();
 		}
 		Rectangle2D bounds = new Rectangle2D.Double(x-5,y-5,10,10);
 		return bounds;
 	}
}