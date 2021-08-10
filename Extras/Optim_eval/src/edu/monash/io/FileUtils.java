package edu.monash.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static void writeStringToFile(String filename, String contents) {
		try (FileWriter writer = new FileWriter(filename)) {
			writer.write(contents);
		} catch (IOException ex) {
			throw new RuntimeException("Unhandled IOException.", ex);
		}
	}

	public static List<Double> readPriceCSV(String pathname) {
		
		List<Double> prices = new ArrayList<>();

		String contents = readFileAsString(pathname);
		String[] lines = contents.split("[\r\n]+");
		for (int i = 1; i < lines.length; i++) {
			
			String[] parts = lines[i].split(",");
			double price = Double.valueOf(parts[3]);
			
			// Twice because prices in CSV are every 30min and we schedule every 15min.
			prices.add(price);
			prices.add(price);
		}

		return prices;
	}

	/**
	 * @see readFileAsString(String)
	 */
	public static String readFileAsString(File file) {
		return readFileAsString(file.getPath());
	}

	/**
	 * Convenience function to read the file (indicated by pathname), in its entirety, into
	 * one String (assuming the default encoding, UTF-8). The String probably contains multiple
	 * lines.
	 * 
	 * Not recommended for files 100+ MBs large.
	 */
	public static String readFileAsString(String pathname) {
		try {
			return tryReadFileaAsString(pathname);
		} catch (IOException ex) {
			throw new RuntimeException(String.format("Unhandled IOException when reading file %s.", pathname), ex);
		}
	}

	private static String tryReadFileaAsString(String pathname) throws IOException {
		return new String(Files.readAllBytes(FileSystems.getDefault().getPath(pathname)));
	}
}
