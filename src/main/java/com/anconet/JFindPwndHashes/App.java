package com.anconet.JFindPwndHashes;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentMap;

import com.anconet.JFindPwndHashes.helpers.Constants;
import com.anconet.JFindPwndHashes.helpers.Utils;
import com.anconet.JFindPwndHashes.model.Match;
import com.anconet.JFindPwndHashes.model.ResultsContainer;
import com.anconet.JFindPwndHashes.workers.AdHashCollector;
import com.anconet.JFindPwndHashes.workers.HashMatcher;
import com.anconet.JFindPwndHashes.workers.IAdHashCollector;
import com.anconet.JFindPwndHashes.workers.IHashMatcher;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;

@Command(name = Constants.APP_NAME, mixinStandardHelpOptions = true, version = Constants.APP_NAME + " "
		+ Constants.APP_VERSION, sortOptions = false, description = "Matches hashes extracted from NTDS.dit against HIBP NTLM hash collection", footer = {
				"", "Check github page for information on how to extract and prepare hashes." }, header = {
						"     _ ___ _         _ ___                _ _  _         _           ",
						"  _ | | __(_)_ _  __| | _ \\_ __ ___ _  __| | || |__ _ __| |_  ___ ___",
						" | || | _|| | ' \\/ _` |  _| V  V / ' \\/ _` | __ / _` (_-< ' \\/ -_|_-<",
						"  \\__/|_| |_|_||_\\__,_|_|  \\_/\\_/|_||_\\__,_|_||_\\__,_/__/_||_\\___/__/", "",
						Constants.APP_VERSION + " by " + Constants.APP_AUTHOR, "" })
public class App implements Runnable {

	@Option(names = { "-p", "--pwned-hashes" }, required = true, description = "path to hibp ntlm hash file, required.")
	File pwndHashesFile;

	@Option(names = { "-a", "--ad-hashes" }, required = true, description = "path to prepared ntlm hash file, required.")
	File adHashesFile;

	@Spec
	CommandSpec spec;

	public static void main(String[] args) {
		new CommandLine(new App()).execute(args);
	}

	@Override
	public void run() {
		Arrays.stream(this.spec.usageMessage().header())
				.forEach(l -> System.out.println(CommandLine.Help.Ansi.AUTO.string(l)));

		try {
			IAdHashCollector adHashCollector = new AdHashCollector();
			IHashMatcher hashMatcher = new HashMatcher();

			final ConcurrentMap<String, List<String>> adHashes = adHashCollector
					.collectAdHashesAsMapByCustomCollector(adHashesFile);

			ResultsContainer resultsContainer = new ResultsContainer();
			resultsContainer.setAdHashesCount(adHashes.size());
			resultsContainer.setAdAccountsCount(Utils.getLineCount(adHashesFile));

			printConfirmationRequest(resultsContainer.getAdHashesCount());

			resultsContainer.setStartTime(Instant.now());
			resultsContainer.setMatches(hashMatcher.matchWithPwnCount(adHashes, pwndHashesFile));
			resultsContainer.setStopTime(Instant.now());

			resultsContainer.setOutputFile(getOutputFile());

			writeResultsToFile(resultsContainer);

			System.out.println(resultsContainer.getElapsedTimeString());
			System.out.printf("Wrote output file to \"%s\".%n", resultsContainer.getOutputFile());

		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (UncheckedIOException e) {
			System.err.println("Malformed input detected.");
		}
	}

	private void writeResultsToFile(ResultsContainer resultsContainer) throws IOException {
		for (String line : resultsContainer.getHeaderStrings()) {
			Utils.writeLineToFile(line, resultsContainer.getOutputFile());
		}

		for (Match match : resultsContainer.getMatches()) {

			for (String user : match.getUserNames()) {
				String line = String.format("%s;%s;%s%n", user, match.getNtlmHash(), match.getPwnCount());
				Utils.writeLineToFile(line, resultsContainer.getOutputFile());
			}
		}
	}

	private File getOutputFile() {
		return new File(System.getProperty("user.home"), getOutputFileName());
	}

	private String getOutputFileName() {
		return String.format("%s-%s.csv", Constants.APP_NAME,
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")));
	}

	private void printConfirmationRequest(int adHashesSize) {
		System.out.printf("I'll process %s hashes. This may take some time, go grab a cup of coffee.%n", adHashesSize);
		System.out.printf("    ((((%n   ((((%n    ))))%n _ .---.%n( |`---'|%n \\|     |%n : .___, :%n  `-----'%n");
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Press Enter to continue.");
			scanner.nextLine();
		}
	}
}
