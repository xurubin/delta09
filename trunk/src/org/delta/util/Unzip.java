package org.delta.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Unzip {

	public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	public static void unzip(String pathToZipFile, String pathToExportFolder) {
		Enumeration<? extends ZipEntry> entries;
		ZipFile zipFile;

		try {
			File f = new File(Unzip.class.getClassLoader().getResource(pathToZipFile).toURI());
			zipFile = new ZipFile(f);

			entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();

				if(entry.getName().contains("__MACOSX")) continue;
				if(entry.getName().contains(".svn")) continue;
				
				if (entry.isDirectory()) {
					(new File(pathToExportFolder + entry.getName())).mkdir();
					continue;
				}
				

				copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(
						new FileOutputStream(pathToExportFolder + entry.getName())));
			}

			zipFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}