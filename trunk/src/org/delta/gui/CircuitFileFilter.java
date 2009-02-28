package org.delta.gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class CircuitFileFilter extends FileFilter
{

	public boolean accept (File f)
	{
		return ( f.isDirectory() || f.getName().toLowerCase().endsWith (".cir") );
	}

	public String getDescription()
	{
		return MainWindow.get().translator.getString ("CIR_FILES");
	}

}
