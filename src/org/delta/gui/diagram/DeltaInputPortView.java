package org.delta.gui.diagram;

import org.jgraph.graph.*;
import java.awt.geom.*;
import java.awt.Color;

public class DeltaInputPortView extends PortView
{
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static PortRenderer renderer = new DeltaPortRenderer();
	protected static Color color = Color.red;
     
     public DeltaInputPortView()
     {
           super();
     }
     
     public DeltaInputPortView(Object arg0)
     {
           super(arg0);
     }
     
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
     
     public CellHandle getHandle(GraphContext c)
     {
    	 return null;
     }
}