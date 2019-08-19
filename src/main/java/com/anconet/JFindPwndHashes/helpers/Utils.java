package com.anconet.JFindPwndHashes.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class Utils {

	public static List<String> readFile(File file) throws IOException {
		return FileUtils.readLines(file, StandardCharsets.UTF_8);
	}

	public static Stream<String> readFileLineByLine(File file) throws IOException {
		return Files.lines(file.toPath());
	}

	public static long getLineCount(File file) {

		long lineCount = 0;
		try (Stream<String> stream = Files.lines(file.toPath())) {
			lineCount = stream.count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineCount;
	}

	public static void writeLineToFile(String line, File outputFile) throws IOException {
		FileUtils.write(outputFile, line, StandardCharsets.UTF_8, true);
	}
}
