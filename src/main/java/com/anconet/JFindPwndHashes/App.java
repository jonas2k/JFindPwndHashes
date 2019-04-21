package com.anconet.JFindPwndHashes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import com.anconet.JFindPwndHashes.model.Match;

public class App {

	public static void main(String[] args) {

		Options options = prepareOptions();
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine commandLine = parser.parse(options, args);

			if (inputIsValid(commandLine)) {

				String adHashesParam = commandLine.getOptionValue("a");
				String pwndHashesParam = commandLine.getOptionValue("p");
				String outputFileParam = commandLine.getOptionValue("o");

				IHashMatcher hashMatcher = new HashMatcher();

				Instant startTime = Instant.now();

				List<Match> matches = hashMatcher.matchWithPwnCount(adHashesParam, pwndHashesParam);

				Instant finishedTime = Instant.now();
				Duration elapsedTime = Duration.between(startTime, finishedTime);

				File outputFile = new File(System.getProperty("user.home"), getOutputFileName(outputFileParam));
				String elapsedTimeString = String.format("Elapsed time: %s days, %s hours, %s minutes and %s seconds.%s",
						elapsedTime.toDays(), elapsedTime.toHoursPart(), elapsedTime.toMinutesPart(),
						elapsedTime.toSecondsPart(), System.lineSeparator());

				writeDataToFile(matches, elapsedTimeString, outputFile);

				System.out.println(elapsedTimeString);
				System.out.printf("Wrote output file to \"%s\".", outputFile);

			} else {
				printHelp(options);
			}

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeDataToFile(List<Match> matches, String elapsedTimeString, File outputFile)
			throws IOException {
		FileUtils.write(outputFile, elapsedTimeString, StandardCharsets.UTF_8, true);
		String headerLine = "Username;NTLM-Hash;Pwn-Count" + System.lineSeparator();
		FileUtils.write(outputFile, headerLine, StandardCharsets.UTF_8, true);

		for (Match match : matches) {

			for (String user : match.getUserNames()) {
				String line = user + ";" + match.getNtlmHash() + ";" + match.getPwnCount() + System.lineSeparator();
				FileUtils.write(outputFile, line, StandardCharsets.UTF_8, true);
			}
		}
	}

	private static String getOutputFileName(String outputFileParam) {

		String outputFileName = "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")) + ".csv";

		if (outputFileParam != null && !outputFileParam.isEmpty() && !outputFileName.isBlank()) {
			outputFileName = outputFileParam + outputFileName;
		} else {
			outputFileName = "jFindPwndHashes" + outputFileName;
		}

		return outputFileName;
	}

	private static boolean inputIsValid(CommandLine commandLine) {
		return (commandLine.hasOption("p") && commandLine.hasOption("a") && allFilesAreReadable(commandLine));
	}

	private static boolean allFilesAreReadable(CommandLine commandLine) {
		return Arrays.asList(commandLine.getOptionValue("a"), commandLine.getOptionValue("p")).stream()
				.allMatch(s -> (Files.exists(Paths.get(s)) && Files.isReadable(Paths.get(s))));
	}

	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("JFindPwndHashes", options);
	}

	private static Options prepareOptions() {
		Option help = new Option("h", "help", false, "print this message");

		Option hibpFile = Option.builder("p").longOpt("pwndhashes").desc("path to hibp ntlm hash file, required")
				.hasArg().argName("FILE").build();

		Option adHashesFile = Option.builder("a").longOpt("adhashes").desc("path to ntds.dit hash file, required")
				.hasArg().argName("FILE").build();

		Option outputFile = Option.builder("o").longOpt("outputfilename")
				.desc("name of the output file (will be written to user profile directory)").hasArg().argName("FILE")
				.build();

		Options options = new Options();

		options.addOption(help);
		options.addOption(hibpFile);
		options.addOption(adHashesFile);
		options.addOption(outputFile);

		return options;
	}
}
