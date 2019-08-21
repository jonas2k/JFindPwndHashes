package com.anconet.JFindPwndHashes.workers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public interface IAdHashCollector {
	public ConcurrentMap<String, List<String>> collectAdHashesAsMap(File adHashesFile) throws IOException;

	public ConcurrentMap<String, List<String>> collectAdHashesAsMapByCustomCollector(File adHashesFile)
			throws IOException;
}
