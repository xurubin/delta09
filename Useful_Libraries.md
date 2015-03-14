# Useful Libraries #

## Graph Libraries ##

[JGraph](http://www.jgraph.com/jgraph.html): Provides a graph data model and (Swing) classes for a visual representation.

[JGraphT](http://jgrapht.sourceforge.net/): More mathematical graph data model which can be used in [combination with JGraph](http://jgrapht.sourceforge.net/visualizations.html).

## GUI Libraries ##
Since our client has special interest in the GUI, here are some libraries to make the GUI fancier:

[Substance](https://substance.dev.java.net/): Modifying the Swing look and feel. Various plugins are available, e.g. for [SwingX](https://substance-swingx.dev.java.net/) or [Flamingo](https://substance-flamingo.dev.java.net/).

[SwingX](https://swingx.dev.java.net): Additional Swing containers and components. For us the following components might be quite useful:
  * `JXTaskPane` and `JXCollapsiblePane`: As far as I can know, Swing has no container which layouts components vertically in collapsible groups. So we could use one of the two classes to represent the list of gates/components on the left-hand side of the GUI.
  * `JXStatusBar`, `JXErrorPane`, and others.

[Flamingo](https://flamingo.dev.java.net): Advanced Components for Swing. We probably do not have much use for the components this library provides but it does have classes for drawing resizable SVG icons (`SvgBatikResizableIcon`). This feature might be interesting for drawing gate/component pictures as this would look better when zooming in. Plus, it does not increase the code complexity since all the hard work is done by the library; it behaves exactly like an instance of the Swing Icon class.

I successfully tried loading SVG images using this library using the following code in one of the Gate Viewers (unfortunately, the default rendering class provided by JGraph does not redraw image when the user zooms in and out, and the image only appeared after I dragged the vertex...):
```
String iconPath = "org/delta/gui/diagram/images/and.svg";
URL iconUrl = AndGateView.class.getClassLoader().getResource(iconPath);
Icon icon = SvgBatikResizableIcon.getSvgIcon(iconUrl, new Dimension(500,500));
GraphConstants.setIcon(getAttributes(), icon);
```

## Testing ##
[EasyMock](http://www.easymock.org/) Mocking library.

[FEST](http://code.google.com/p/fest/): Swing GUI Testing