package com.anconet.JFindPwndHashes.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class Helpers {
	
	public static List<String> readFile(String fileName) {
		try {
			return FileUtils.readLines(new File(fileName), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public static Stream<String> readFileLineByLine(String fileName) {
		try {
			return Files.lines(Paths.get(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Stream.of(new String[] { "" });
	}
}
