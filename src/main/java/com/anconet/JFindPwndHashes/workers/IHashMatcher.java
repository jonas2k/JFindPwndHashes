package com.anconet.JFindPwndHashes.workers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.anconet.JFindPwndHashes.model.Match;

public interface IHashMatcher {
	public List<Match> matchWithPwnCount(ConcurrentMap<String, List<String>> adHashes, File pwndHashesFile)
			throws IOException;
}
