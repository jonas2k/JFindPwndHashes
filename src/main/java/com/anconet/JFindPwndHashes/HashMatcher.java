package com.anconet.JFindPwndHashes;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import com.anconet.JFindPwndHashes.model.Match;

public class HashMatcher implements IHashMatcher {

	@Override
	public List<Match> matchWithPwnCount(String adHashesString, String pwndHashesString) {

		final ConcurrentMap<String, Set<String>> adHashes = getAdHashesMap(adHashesString);

		printStatus(adHashes);

		List<Match> matches = readFileLineByLine(pwndHashesString).parallel()
				.filter(p -> adHashes.containsKey(p.split(":")[0]))
				.flatMap(p -> adHashes.entrySet().parallelStream()
						.filter(a -> p.split(":")[0].equals(a.getKey()))
						.peek(a -> printMatchInfo(a))
						.map(a -> new Match(a.getKey(), Integer.parseInt(p.split(":")[1]), a.getValue())))
				.collect(Collectors.toList());

		return matches;
	}

	@Override
	public List<Match> matchWithoutPwnCount(String adHashesString, String pwndHashesString) {

		final ConcurrentMap<String, Set<String>> adHashes = getAdHashesMap(adHashesString);

		printStatus(adHashes);

		List<Match> matches = adHashes.entrySet().parallelStream()
				.filter(a -> readFileLineByLine(pwndHashesString).parallel()
						.anyMatch(p -> p.split(":")[0].equals(a.getKey())))
				.peek(a -> printMatchInfo(a)).map(a -> new Match(a.getKey(), a.getValue()))
				.collect(Collectors.toList());

		return matches;
	}

	@Override
	public List<Match> matchWithPwnCountAlternative(String adHashesString, String pwndHashesString) {

		final ConcurrentMap<String, Set<String>> adHashes = getAdHashesMap(adHashesString);

		printStatus(adHashes);

		List<Match> matches = adHashes.entrySet().parallelStream()
				.flatMap(a -> readFileLineByLine(pwndHashesString).parallel()
						.filter(p -> p.split(":")[0].equals(a.getKey()))
						.peek(p -> printMatchInfo(a))
						.map(p -> new Match(a.getKey(), Integer.parseInt(p.split(":")[1]), a.getValue())))
				.collect(Collectors.toList());

		return matches;
	}

	private ConcurrentMap<String, Set<String>> getAdHashesMap(String adHashesString) {
		final ConcurrentMap<String, Set<String>> adHashes = readFile(adHashesString).parallelStream()
				.map(s -> s.split(":")).collect(Collectors.groupingByConcurrent(s -> s[3].toUpperCase(),
						Collectors.mapping(s -> s[0], Collectors.toSet())));
		return adHashes;
	}

	private PrintStream printMatchInfo(Entry<String, Set<String>> item) {
		return System.out.printf("Thread %s found hash %s for user(s) %s.%n", Thread.currentThread().getId(),
				item.getKey(), String.join(",", item.getValue()));
	}

	private List<String> readFile(String fileName) {
		try {
			return FileUtils.readLines(new File(fileName), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	private Stream<String> readFileLineByLine(String fileName) {
		try {
			return Files.lines(Paths.get(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Stream.of(new String[] { "" });
	}
	
	private void printStatus(final ConcurrentMap<String, Set<String>> adHashes) {
		System.out.printf("Processing %s hashes. This may take some time, go grab a cup of coffee.%s", adHashes.size(), System.lineSeparator());
		System.out.print("    (((("+System.lineSeparator() + 
				"   (((("+System.lineSeparator() + 
				"    ))))"+System.lineSeparator() + 
				" _ .---."+System.lineSeparator() + 
				"( |`---'|"+System.lineSeparator() + 
				" \\|     |"+System.lineSeparator() + 
				" : .___, :"+System.lineSeparator() + 
				"  `-----'"+System.lineSeparator());
	}
}
