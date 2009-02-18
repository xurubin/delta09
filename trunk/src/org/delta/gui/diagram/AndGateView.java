package org.delta.gui.diagram;

import java.net.URL;
import java.net.URI;
import org.jgraph.graph.*;
//import java.awt.geom.*;
import javax.swing.ImageIcon;
import com.kitfox.svg.app.beans.SVGIcon;
public class AndGateView extends VertexView
{
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static DeltaComponentRenderer renderer = new DeltaComponentRenderer();
     
     public AndGateView()
     {
           super();
           String iconPath = "org/delta/gui/diagram/images/and.svg";
           SVGIcon icon = new SVGIcon();

           try {
        	   URI svgURI = new URI(AndGateView.class.getClassLoader().getResource(iconPath).toString());
        	   icon.setSvgURI(svgURI);
               icon.setScaleToFit(true);
           }catch(Exception e){}

           GraphConstants.setIcon(this.getAttributes(),icon);
           GraphConstants.setAutoSize(this.getAttributes(),true);
     }
     
     public AndGateView(Object arg0)
     {
           super(arg0);
           String iconPath = "org/delta/gui/diagram/images/and.svg";
           SVGIcon icon = new SVGIcon();

           try {
        	   URI svgURI = new URI(AndGateView.class.getClassLoader().getResource(iconPath).toString());
        	   icon.setSvgURI(svgURI);
               icon.setScaleToFit(true);
           }catch(Exception e){}

           GraphConstants.setIcon(this.getAttributes(),icon);
           GraphConstants.setAutoSize(this.getAttributes(),true);
     }
     
     public CellViewRenderer getRenderer()
     {
           return renderer;
     }
     
     /*public Point2D getPerimeterPoint(EdgeView edge,Point2D source,Point2D p)
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