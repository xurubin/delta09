package org.delta.gui.diagram;

import java.net.URI;
import java.net.URL;
import org.jgraph.graph.*;

import com.kitfox.svg.app.beans.SVGIcon;
//import java.awt.geom.*;
import javax.swing.ImageIcon;

public class OrGateView extends VertexView
{
     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static DeltaComponentRenderer renderer = new DeltaComponentRenderer();
     
     /*public AndGateView()
     {
           super();
           GraphConstants.setIcon(this.getAttributes(),new ImageIcon("../../images/and.png"));
     }*/
     
     public OrGateView(Object arg0)
     {
         super(arg0);
         String iconPath = "org/delta/gui/diagram/images/or.svg";
         SVGIcon icon = new SVGIcon();

         try {
      	   URI svgURI = new URI(OrGateView.class.getClassLoader().getResource(iconPath).toString());
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