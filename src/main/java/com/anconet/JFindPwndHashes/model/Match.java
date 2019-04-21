package com.anconet.JFindPwndHashes.model;

import java.util.Set;

public class Match {

	private String ntlmHash;
	private int pwnCount;
	private Set<String> userNames;
	
	public Match(String ntlmHash, Set<String> userNames) {
		super();
		this.ntlmHash = ntlmHash;
		this.pwnCount = 0;
		this.userNames = userNames;
	}

	public Match(String ntlmHash, int pwnCount, Set<String> userNames) {
		super();
		this.ntlmHash = ntlmHash;
		this.pwnCount = pwnCount;
		this.userNames = userNames;
	}

	public String getNtlmHash() {
		return ntlmHash;
	}

	public int getPwnCount() {
		return pwnCount;
	}

	public Set<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(Set<String> userNames) {
		this.userNames = userNames;
	}
}