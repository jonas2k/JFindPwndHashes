package com.anconet.JFindPwndHashes;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public interface IAdHashCollector {

	public ConcurrentMap<String, List<String>> collectAdHashesAsMap(String adHashesString);

	public ConcurrentMap<String, List<String>> collectAdHashesAsMapByCustomCollector(String adHashesString);
}
