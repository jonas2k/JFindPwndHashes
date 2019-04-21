package com.anconet.JFindPwndHashes;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import com.anconet.JFindPwndHashes.model.Match;

public interface IHashMatcher {

	public List<Match> matchWithPwnCount(ConcurrentMap<String, Set<String>> adHashes, String pwndHashesPath);
	public List<Match> matchWithoutPwnCount(ConcurrentMap<String, Set<String>> adHashes, String pwndHashesPath);
	public List<Match> matchWithPwnCountAlternative(ConcurrentMap<String, Set<String>> adHashes, String pwndHashesPath);
}
