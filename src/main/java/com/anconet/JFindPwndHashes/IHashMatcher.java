package com.anconet.JFindPwndHashes;

import java.util.List;

import com.anconet.JFindPwndHashes.model.Match;

public interface IHashMatcher {

	public List<Match> matchWithPwnCount(String adHashesPath, String pwndHashesPath);
	public List<Match> matchWithoutPwnCount(String adHashesPath, String pwndHashesPath);
	public List<Match> matchWithPwnCountAlternative(String adHashesPath, String pwndHashesPath);
}
