package com.anconet.JFindPwndHashes.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class Helpers {
	
	private static MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();

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

	public static long getLineCount(String fileName) {

		long lineCount = 0;
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			lineCount = stream.count();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineCount;
	}

	public static String getProjectName() {
		String projectName = Helpers.class.getPackage().getImplementationTitle();
		if (projectName == null) {
			projectName = getMavenModel().getName();
		}
		return projectName;
	}

	public static String getProjectVersion() {
		String projectVersion = Helpers.class.getPackage().getImplementationVersion();
		if (projectVersion == null) {
			projectVersion = getMavenModel().getVersion();
		}
		return projectVersion;
	}

	private static Model getMavenModel() {
		try {
			if ((new File("pom.xml")).exists()) {
				return mavenXpp3Reader.read(new FileReader("pom.xml"));
			} else {
				return mavenXpp3Reader.read(new InputStreamReader(
						Helpers.class.getResourceAsStream(Constants.pomPackagePath)));
			}

		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}
	}
}
