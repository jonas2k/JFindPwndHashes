package com.anconet.JFindPwndHashes;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class AdHashCollector implements IAdHashCollector {
	
	@Override
	public ConcurrentMap<String, List<String>> collectAdHashesAsMap(String adHashesString) {
		final ConcurrentMap<String, List<String>> adHashes = Helpers.readFile(adHashesString).parallelStream()
				.map(s -> s.split(":")).collect(Collectors.groupingByConcurrent(s -> s[3].toUpperCase(),
						Collectors.mapping(s -> s[0], Collectors.toList())));
		return adHashes;
	}

	@Override
	public ConcurrentMap<String, List<String>> collectAdHashesAsMapByCustomCollector(String adHashesString) {
		final ConcurrentMap<String, List<String>> adHashes = Helpers.readFile(adHashesString).parallelStream()
				.collect(new ConcurrentMapCollector());
		return adHashes;
	}
}
