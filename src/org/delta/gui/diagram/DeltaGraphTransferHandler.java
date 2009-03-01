package org.delta.gui.diagram;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;

import org.delta.gui.ComponentPanel;
import org.delta.gui.MainWindow;
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
import org.jgraph.graph.ConnectionSet.Connection;

/**
 * @author Group Delta 2009
 * This is a subclass of GraphTransferHandler from the JGraph library that has been
 * specially adapted for our application. Specifically the
 * {@link #importData(JComponent, Transferable) importData()} and
 * {@link #handleExternalDrop(JGraph, Object[], Map, ConnectionSet, ParentMap, double, double)
 * handleExternalDrop()} methods have been overwritten to provide customised dropping
 * functionality, both from the {@link org.delta.gui.ComponentPanel ComponentPanel} and
 * from the clipboard.
 */
public class DeltaGraphTransferHandler extends GraphTransferHandler {
	/** Needed for correct serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Reference to this handler's model. The handler only deals with one
	 * graph model so that we can check LEDs are not being used more than once.
	 * @param model - the graph model to use for this handler.
	 */
	private DeltaGraphModel model;
	
	public DeltaGraphTransferHandler(DeltaGraphModel graphModel) {
		this.model = graphModel;
	}
	
	/**
	 * This overwritten version is very similar to the original. The main change
	 * is that for dropping components the insertion point is calculated so that
	 * the mouse release event is treated as the centre of the new component, rather
	 * than the top left corner.
	 * <br><br>
	 * It should also be noted that in our application the field AlwaysReceiveAsCopyAction
	 * is set to true, hence both drops from the ComponentPanel and from the clipboard
	 * are treated in the same way. This simplifies the use of the clipboard, but does
	 * require some changes in the HandleExternalDrop method as well.
	 * 
	 * @param comp - the JGraph we are importing to.
	 * @param t - the transferable object we are importing.
	 * @return boolean value signalling success or failure of the drop.
	 * @see org.jgraph.graph.GraphTransferHandler#importData(javax.swing.JComponent,
	 * 			java.awt.datatransfer.Transferable)
	 */
	// NOTE: 1. We abuse return value to signal removal to the sender.
	// 2. We always clone cells when transferred between two models
	// This is because they contain parts of the model's data.
	// 3. Transfer is passed to importDataImpl for unsupported
	// dataflavors (because method may return false, see 1.)
	@Override
	@SuppressWarnings("unchecked")
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
					Map<Object,Map<Object,Object>> nested = gt.getAttributeMap();
					
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
								nested = new Hashtable<Object,Map<Object,Object>>();
								Map<Object,Object> emptyMap = new Hashtable<Object,Object>();
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

										Hashtable<Object,Object> attrs = new Hashtable<Object,Object>();
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
												|| !nested.keySet().contains(model.getParent(cells[i]))) {
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
	 * Overwritten version of the method from GraphTransferHandler. If the drop
	 * is genuinely external it uses the static cloneCells method from DeltaGraphModel
	 * in order to perform a deep clone of the cells (i.e. including ports).
	 * <br>
	 * If the drop is from the clipboard then it performs a shallow copy of the
	 * cells, as the passed parent map will contain copies of the ports.
	 * 
	 * @param graph - the JGraph we are dropping into.
	 * @param cells - an array of cells to insert.
	 * @param nested - nested AttributeMap containing attributes of the cells to insert.
	 * @param cs - ConnectionSet of the cells being dropped.
	 * @param pm - ParentMap of the cells being dropped.
	 * @param dx - x co-ordinate used to position the dropped cells.
	 * @param dy - y co-ordinate used to position the dropped cells.
	 * @see 
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void handleExternalDrop(JGraph graph, Object[] cells, Map nested,
			ConnectionSet cs, ParentMap pm, double dx, double dy) {

		// Removes all connections for which the port is neither
		// passed in the parent map nor already in the model.
		Iterator<Connection> it = cs.connections();
		while (it.hasNext()) {
			ConnectionSet.Connection conn = (ConnectionSet.Connection) it
					.next();
			if (!pm.getChangedNodes().contains(conn.getPort())
					&& !graph.getModel().contains(conn.getPort())) {
				it.remove();
			}
		}
		
		// Create Map of cloned cells
		Map<Object,Object> clones = null;
		DeltaGraphModel model = (DeltaGraphModel) graph.getModel();
		// If dragged from ComponentPanel, pm will be null so perform a deep copy (including ports)
		if (pm == null)
			clones = model.cloneCells(graph.getModel(), cells);
		// Else is from clipboard so pm will not be null, and we need only do a shallow copy
		else
			clones = model.cloneCells(cells);
		
		// If drop contains LEDs, ask user to choose the number, and tell the LED what model it's in
		Iterable clonedCells = clones.values();
		for (Object clone : clonedCells) {
			if (clone instanceof Ledr) {
				Ledr ledr = (Ledr)clone;
				ledr.setModel(model);
				int ledrNumber = getUserInput(ComponentPanel.LEDR);
				if (ledrNumber == -1) {
					JOptionPane.showMessageDialog ( MainWindow.get(),
													MainWindow.getTranslatorString ("NO_LED_NUM_LONG"),
													MainWindow.getTranslatorString ("NO_LED_NUM"),
													JOptionPane.ERROR_MESSAGE );
					return;
				}
				else if (ledrNumber == -2) {
					JOptionPane.showMessageDialog ( MainWindow.get(),
													MainWindow.getTranslatorString ("NO_MORE_COMP_1")
													+ MainWindow.getTranslatorString ("RED_LED")
													+ MainWindow.getTranslatorString ("NO_MORE_COMP_2"),
													MainWindow.getTranslatorString ("NO_MORE_COMP_TITLE")
													+ MainWindow.getTranslatorString("RED_LED"),
													JOptionPane.ERROR_MESSAGE );
					return;
				}
				ledr.setLedrNumber(ledrNumber);
			}
			else if (clone instanceof Ledg) {
				Ledg ledg = (Ledg)clone;
				ledg.setModel(model);
				int ledgNumber = getUserInput(ComponentPanel.LEDG);
				if (ledgNumber == -1) {
					JOptionPane.showMessageDialog ( MainWindow.get(),
													MainWindow.getTranslatorString ("NO_LED_NUM_LONG"),
													MainWindow.getTranslatorString ("NO_LED_NUM"),
													JOptionPane.ERROR_MESSAGE );
					return;
				}
				else if (ledgNumber == -2) {
					JOptionPane.showMessageDialog ( MainWindow.get(),
													MainWindow.getTranslatorString ("NO_MORE_COMP_1")
													+ MainWindow.getTranslatorString ("GREEN_LED")
													+ MainWindow.getTranslatorString ("NO_MORE_COMP_2"),
													MainWindow.getTranslatorString ("NO_MORE_COMP_TITLE")
													+ MainWindow.getTranslatorString("GREEN_LED"),
													JOptionPane.ERROR_MESSAGE );
					return;
				}
				ledg.setLedgNumber(ledgNumber);
			}
			else if (clone instanceof Switch) {
				Switch sw = (Switch)clone;
				int switchNumber = getUserInput(ComponentPanel.SWITCH);
				if (switchNumber == -1) {
					JOptionPane.showMessageDialog ( MainWindow.get(),
													MainWindow.getTranslatorString ("NO_COMP_NUM_LONG"),
													MainWindow.getTranslatorString ("NO_COMP_NUM"),
													JOptionPane.ERROR_MESSAGE );
					return;
				}
				sw.setSwitchNumber(switchNumber);
			}
			else if (clone instanceof PushButton) {
				PushButton key = (PushButton)clone;
				int pushButtonNumber = getUserInput(ComponentPanel.PUSHBUTTON);
				if (pushButtonNumber == -1) {
					JOptionPane.showMessageDialog ( MainWindow.get(),
													MainWindow.getTranslatorString ("NO_COMP_NUM_LONG"),
													MainWindow.getTranslatorString ("NO_COMP_NUM"),
													JOptionPane.ERROR_MESSAGE );
					return;
				}
				key.setPushButtonNumber(pushButtonNumber);
			}
			else if (clone instanceof SevenSegment) {
				SevenSegment sevenSegment = (SevenSegment)clone;
				sevenSegment.setModel(model);
				int sevenSegmentNumber = getUserInput(ComponentPanel.SEVENSEG);
				if (sevenSegmentNumber == -1) {
					JOptionPane.showMessageDialog ( MainWindow.get(),
													MainWindow.getTranslatorString ("NO_COMP_NUM_LONG"),
													MainWindow.getTranslatorString ("NO_COMP_NUM"),
													JOptionPane.ERROR_MESSAGE );
					return;
				}
				else if (sevenSegmentNumber == -2) {
					JOptionPane.showMessageDialog ( MainWindow.get(),
													MainWindow.getTranslatorString ("NO_MORE_COMP_1")
													+ MainWindow.getTranslatorString ("7SEG2")
													+ MainWindow.getTranslatorString ("NO_MORE_COMP_2"),
													MainWindow.getTranslatorString ("NO_MORE_COMP_TITLE")
													+ MainWindow.getTranslatorString("7SEG2"),
													JOptionPane.ERROR_MESSAGE );
					return;
				}
				sevenSegment.setSevenSegmentNumber(sevenSegmentNumber);
			}
			else if (clone instanceof ROM) {
				ROM rom = (ROM) clone;
				rom.setStore(getUserMemorySelection());
			}
		}
		
		// Insert cloned cells
		graph.getGraphLayoutCache().insertClones(cells, clones, nested, cs, pm, dx, dy);
	}
	
	/*
	 * TODO: get user input for intial memory value
	 */
	private List<Integer> getUserMemorySelection() {
		MemoryDialog dialog = new MemoryDialog(MainWindow.get(), MainWindow.getTranslatorString("SELECT_MEM"));
		dialog.showDialog();
		return dialog.getStoreValues();
	}
	
	private int getUserInput(int compType) {
		// Set up local variables based on the type of component
		int totalComps;
		String compPrefix;
		String compName;
		String compImage;
		switch (compType) {
		case ComponentPanel.LEDR:		totalComps = 18;
										compPrefix = "LEDR";
										compName = MainWindow.getTranslatorString("RED_LED");
										compImage = "ledr.png";
										break;
		case ComponentPanel.LEDG:		totalComps = 9;
										compPrefix = "LEDG";
										compName = MainWindow.getTranslatorString("GREEN_LED");
										compImage = "ledg.png";
										break;
		case ComponentPanel.SWITCH:		totalComps = 18;
										compPrefix = "SW";
										compName = MainWindow.getTranslatorString("SWITCH2");
										compImage = "switch.png";
										break;
		case ComponentPanel.PUSHBUTTON:	totalComps = 4;
										compPrefix = "KEY";
										compName = MainWindow.getTranslatorString("BUTTON2");
										compImage = "button.png";
										break;
		case ComponentPanel.SEVENSEG:	totalComps = 8;
										compPrefix = "HEX";
										compName = MainWindow.getTranslatorString("7SEG2");
										compImage = "7seg.png";
										break;
		default:						totalComps = 0;
										compPrefix = "UNKNOWN";
										compName = MainWindow.getTranslatorString("UNKNOWN");
										compImage = "";
		}
		
		// Create an array of unused components for the dialog box
		ArrayList<String> unusedComps = new ArrayList<String>();
		if ((compType == ComponentPanel.LEDR) || (compType == ComponentPanel.LEDG)) {
			for (int led=0; led<totalComps; led++) {
				if (!model.isLedUsed(led, compType))
					unusedComps.add(compPrefix+Integer.toString(led));
			}
		}
		else if (compType == ComponentPanel.SWITCH) {
			for (int sw=0; sw<totalComps; sw++) {
				unusedComps.add(compPrefix+Integer.toString(sw));
			}
		}
		else if (compType == ComponentPanel.PUSHBUTTON) {
			for (int key=0; key<totalComps; key++) {
				unusedComps.add(compPrefix+Integer.toString(key));
			}
		}
		else if (compType == ComponentPanel.SEVENSEG) {
			for (int hex=0; hex<totalComps; hex++) {
				if (!model.isSevenSegmentUsed(hex))
					unusedComps.add(compPrefix+Integer.toString(hex));
			}
		}
		// If there are no available components to paste, return -2 and let caller handle it
		if (unusedComps.isEmpty())
			return -2;
		
		Object[] possibilities = unusedComps.toArray();
		
		// Show the dialog and get the users choice
		ClassLoader cl = this.getClass().getClassLoader();
		String returnedString = (String)JOptionPane.showInputDialog(
				MainWindow.get(),
				MainWindow.getTranslatorString("CHOOSE1")
				+ compName +
				MainWindow.getTranslatorString("CHOOSE2"),
				MainWindow.getTranslatorString("CHOOSE3")
				+ compName +
				MainWindow.getTranslatorString("CHOOSE4"),
				JOptionPane.PLAIN_MESSAGE,
				new ImageIcon(cl.getResource("org/delta/gui/diagram/images/" + compImage)),
				possibilities,
				possibilities[0]);
		
		// If no string is returned (i.e. user clicks cancel), return -1 and let caller handle it
		if (returnedString == null)
			return -1;
		
		String number = returnedString.substring(compPrefix.length(),returnedString.length());
		int compNumber = Integer.valueOf(number);
		return compNumber;
	}
}
