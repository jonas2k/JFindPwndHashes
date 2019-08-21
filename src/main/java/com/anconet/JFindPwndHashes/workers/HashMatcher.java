package com.anconet.JFindPwndHashes.workers;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.anconet.JFindPwndHashes.helpers.Utils;
import com.anconet.JFindPwndHashes.model.Match;

public class HashMatcher implements IHashMatcher {

	@Override
	public List<Match> matchWithPwnCount(ConcurrentMap<String, List<String>> adHashes, File pwndHashesFile)
			throws IOException {

		List<Match> matches = Utils.readFileLineByLine(pwndHashesFile).parallel()
				.filter(p -> adHashes.containsKey(p.split(":")[0]))
				.flatMap(p -> adHashes.entrySet().stream().filter(a -> p.split(":")[0].equals(a.getKey()))
						.peek(a -> printMatchInfo(a))
						.map(a -> new Match(a.getKey(), Integer.parseInt(p.split(":")[1]), a.getValue())))
				.collect(Collectors.toList());

		return matches;
	}

	private PrintStream printMatchInfo(Entry<String, List<String>> item) {
		return System.out.printf("Thread %s found hash %s for user(s) %s.%n", Thread.currentThread().getId(),
				item.getKey(), String.join(",", item.getValue()));
	}
}
