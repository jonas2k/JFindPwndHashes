package com.anconet.JFindPwndHashes.workers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.anconet.JFindPwndHashes.helpers.Utils;

public class AdHashCollector implements IAdHashCollector {

	@Override
	public ConcurrentMap<String, List<String>> collectAdHashesAsMap(File adHashesFile) throws IOException {
		final ConcurrentMap<String, List<String>> adHashes = Utils.readFile(adHashesFile).parallelStream()
				.map(s -> s.split(":")).collect(Collectors.groupingByConcurrent(s -> s[3].toUpperCase(),
						Collectors.mapping(s -> s[0], Collectors.toList())));
		return adHashes;
	}

	@Override
	public ConcurrentMap<String, List<String>> collectAdHashesAsMapByCustomCollector(File adHashesFile)
			throws IOException {
		final ConcurrentMap<String, List<String>> adHashes = Utils.readFile(adHashesFile).parallelStream()
				.collect(new ConcurrentMapCollector());
		return adHashes;
	}
}
