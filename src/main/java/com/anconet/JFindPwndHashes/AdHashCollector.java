package com.anconet.JFindPwndHashes;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class AdHashCollector {
	
	public ConcurrentMap<String, Set<String>> collectAdHashesAsMap(String adHashesString) {
		final ConcurrentMap<String, Set<String>> adHashes = Helpers.readFile(adHashesString).parallelStream()
				.map(s -> s.split(":")).collect(Collectors.groupingByConcurrent(s -> s[3].toUpperCase(),
						Collectors.mapping(s -> s[0], Collectors.toSet())));
		return adHashes;
	}
}
