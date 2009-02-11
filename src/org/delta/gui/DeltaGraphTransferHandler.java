package org.delta.gui;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphTransferHandler;
import org.jgraph.graph.GraphTransferable;
import org.jgraph.graph.ParentMap;

public class DeltaGraphTransferHandler extends GraphTransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* importData is almost exactly the same as in GraphTransferHandler,
	 * but the insertion point calulation altered so the mouse click
	 * is now treated as the centre of the new component, rather than the
	 * top left (non-Javadoc)
	 * @see org.jgraph.graph.GraphTransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
	 */
	
	// NOTE: 1. We abuse return value to signal removal to the sender.
	// 2. We always clone cells when transferred between two models
	// This is because they contain parts of the model's data.
	// 3. Transfer is passed to importDataImpl for unsupported
	// dataflavors (becaue method may return false, see 1.)
	public boolean importData(JComponent comp, Transferable t) {
		try {
			if (comp instanceof JGraph) {
				JGraph graph = (JGraph) comp;
				GraphModel model = graph.getModel();
				GraphLayoutCache cache = graph.getGraphLayoutCache();
				if (t.isDataFlavorSupported(GraphTransferable.dataFlavor)
						&& graph.isEnabled()) {
					// May be null
					Point p = graph.getUI().getInsertionLocation();

					// Get Local Machine Flavor
					Object obj = t
							.getTransferData(GraphTransferable.dataFlavor);
					GraphTransferable gt = (GraphTransferable) obj;

					// Get Transferred Cells
					Object[] cells = gt.getCells();

					// Check if all cells are in the model
					boolean allInModel = true;
					for (int i = 0; i < cells.length && allInModel; i++)
						allInModel = allInModel && model.contains(cells[i]);

					// Count repetitive inserts
					if (in == cells)
						inCount++;
					else
						inCount = (allInModel) ? 1 : 0;
					in = cells;

					// Delegate to handle
					if (p != null && in == out
							&& graph.getUI().getHandle() != null) {
						int mod = (graph.getUI().getDropAction() == TransferHandler.COPY) ? InputEvent.CTRL_MASK
								: 0;
						graph.getUI().getHandle().mouseReleased(
								new MouseEvent(comp, 0, 0, mod, p.x, p.y, 1,
										false));
						return false;
					}

					// Get more Transfer Data
					Rectangle2D bounds = gt.getBounds();
					Map nested = gt.getAttributeMap();
					ConnectionSet cs = gt.getConnectionSet();
					ParentMap pm = gt.getParentMap();

					// Move across models or via clipboard always clones
					if (!allInModel
							|| p == null
							|| alwaysReceiveAsCopyAction
							|| graph.getUI().getDropAction() == TransferHandler.COPY) {

						// Translate cells
						double dx = 0, dy = 0;

						// Cloned via Drag and Drop
						if (nested != null) {
							if (p != null && bounds != null) {
								Point2D insert = graph.fromScreen(graph
										.snap((Point2D) p.clone()));
								// CHANGE MADE HERE - we now use getCenter not just get
								dx = insert.getX() - bounds.getCenterX();
								dy = insert.getY() - bounds.getCenterY();

								// Cloned via Clipboard
							} else {
								Point2D insertPoint = getInsertionOffset(graph,
										inCount, bounds);
								if (insertPoint != null) {
									dx = insertPoint.getX();
									dy = insertPoint.getY();
								}
							}
						}

						handleExternalDrop(graph, cells, nested, cs, pm, dx, dy);

						// Signal sender to remove only if moved between
						// different models
						return (graph.getUI().getDropAction() == TransferHandler.MOVE && !allInModel);
					}

					// We are dealing with a move across multiple views
					// of the same model
					else {

						// Moved via Drag and Drop
						if (p != null) {
							// Scale insertion location
							Point2D insert = graph.fromScreen(graph
									.snap(new Point(p)));

							// Compute translation vector and translate all
							// attribute maps.
							if (bounds != null && nested != null) {
								double dx = insert.getX() - bounds.getX();
								double dy = insert.getY() - bounds.getY();
								AttributeMap.translate(nested.values(), dx, dy);
							} else if (bounds == null) {

								// Prevents overwriting view-local
								// attributes
								// for known cells. Note: This is because
								// if bounds is null, the caller wants
								// to signal that the bounds were
								// not available, which is typically the
								// case if no graph layout cache
								// is at hand. To avoid overriding the
								// local attributes such as the bounds
								// with the default bounds from the model,
								// we remove all attributes that travel
								// along with the transferable. (Since
								// all cells are already in the model
								// no information is lost by doing this.)
								double gs2 = 2 * graph.getGridSize();
								nested = new Hashtable();
								Map emptyMap = new Hashtable();
								for (int i = 0; i < cells.length; i++) {

									// This also gives us the chance to
									// provide useful default location and
									// resize if there are no useful bounds
									// that travel along with the cells.
									if (!model.isEdge(cells[i])
											&& !model.isPort(cells[i])) {

										// Check if there are useful bounds
										// defined in the model, otherwise
										// resize,
										// because the view does not yet exist.
										Rectangle2D tmp = graph
												.getCellBounds(cells[i]);
										if (tmp == null)
											tmp = GraphConstants
													.getBounds(model
															.getAttributes(cells[i]));

										// Clone the rectangle to force a
										// repaint
										if (tmp != null)
											tmp = (Rectangle2D) tmp.clone();

										Hashtable attrs = new Hashtable();
										Object parent = model
												.getParent(cells[i]);
										if (tmp == null) {
											tmp = new Rectangle2D.Double(p
													.getX(), p.getY(), gs2 / 2,
													gs2);
											GraphConstants.setResize(attrs,
													true);

											// Shift
											p.setLocation(p.getX() + gs2, p
													.getY()
													+ gs2);
											graph.snap(p);
											// If parent processed then childs
											// are already located
										} else if (parent == null
												|| !nested
														.keySet()
														.contains(
																model
																		.getParent(cells[i]))) {
											CellView view = graph
													.getGraphLayoutCache()
													.getMapping(cells[i], false);
											if (view != null && !view.isLeaf()) {
												double dx = p.getX()
														- tmp.getX();
												double dy = p.getY()
														- tmp.getY();
												GraphLayoutCache
														.translateViews(
																new CellView[] { view },
																dx, dy);
											} else {
												tmp.setFrame(p.getX(),
														p.getY(), tmp
																.getWidth(),
														tmp.getHeight());
											}

											// Shift
											p.setLocation(p.getX() + gs2, p
													.getY()
													+ gs2);
											graph.snap(p);
										}
										GraphConstants.setBounds(attrs, tmp);
										nested.put(cells[i], attrs);
									} else {
										nested.put(cells[i], emptyMap);
									}
								}
							}

							// Edit cells (and make visible)
							cache.edit(nested, null, null, null);
						}

						// Select topmost cells in group-structure
						graph.setSelectionCells(DefaultGraphModel
								.getTopmostCells(model, cells));

						// Don't remove at sender
						return false;
					}
				} else
					return importDataImpl(comp, t);
			}
		} catch (Exception exception) {
			// System.err.println("Cannot import: " +
			// exception.getMessage());
			exception.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Overwritten version of the method from GraphTransferHandler that calls
	 * the cloneCells method from DeltaGraphModel in order to perform a deep
	 * clone of the cells (i.e. including children).
	 */
	protected void handleExternalDrop(JGraph graph, Object[] cells, Map nested,
			ConnectionSet cs, ParentMap pm, double dx, double dy) {

		// Removes all connections for which the port is neither
		// passed in the parent map nor already in the model.
		Iterator it = cs.connections();
		while (it.hasNext()) {
			ConnectionSet.Connection conn = (ConnectionSet.Connection) it
					.next();
			if (!pm.getChangedNodes().contains(conn.getPort())
					&& !graph.getModel().contains(conn.getPort())) {
				it.remove();
			}
		}
		DeltaGraphModel model = (DeltaGraphModel) graph.getModel();
		Map clones = model.cloneCells(graph.getModel(), cells);
		graph.getGraphLayoutCache().insertClones(cells, clones, nested, cs, pm,
				dx, dy);
	}
}
