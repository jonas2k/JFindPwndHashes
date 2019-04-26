package com.anconet.JFindPwndHashes;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.anconet.JFindPwndHashes.model.Match;

public interface IHashMatcher {

	public List<Match> matchWithPwnCount(ConcurrentMap<String, List<String>> adHashes, String pwndHashesPath);
}
