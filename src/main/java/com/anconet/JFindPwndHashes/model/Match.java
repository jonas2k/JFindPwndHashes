package com.anconet.JFindPwndHashes.model;

import java.util.List;

public class Match {

	private String ntlmHash;
	private int pwnCount;
	private List<String> userNames;
	
	public Match(String ntlmHash, List<String> userNames) {
		super();
		this.ntlmHash = ntlmHash;
		this.pwnCount = 0;
		this.userNames = userNames;
	}

	public Match(String ntlmHash, int pwnCount, List<String> userNames) {
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

	public List<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}
}