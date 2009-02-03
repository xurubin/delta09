package org.delta.gui.diagram;

import java.net.URL;
import org.jgraph.graph.*;
//import java.awt.geom.*;
import javax.swing.ImageIcon;

public class AndGateView extends VertexView
{
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private static VertexRenderer renderer = new VertexRenderer();
     
     /*public AndGateView()
     {
           super();
           GraphConstants.setIcon(this.getAttributes(),new ImageIcon("../../images/and.png"));
     }*/
     
     public AndGateView(Object arg0)
     {
           super(arg0);
           String iconPath = "org/delta/gui/diagram/images/and.png";
           URL iconUrl = AndGateView.class.getClassLoader().getResource(iconPath);
           GraphConstants.setIcon(this.getAttributes(),new ImageIcon(iconUrl));
           GraphConstants.setAutoSize(this.getAttributes(),true);
     }
     
     /*public CellViewRenderer getRenderer()
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
     }*/
}