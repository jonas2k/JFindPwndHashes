package com.anconet.JFindPwndHashes.model;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class ResultsContainer {

	private List<Match> matches;
	private File outputFile;

	private Instant startTime;
	private Duration elapsedTime;

	private int adHashesCount;
	private long adAccountsCount;

	private static final String RESULTS_HEADER_STRING = "Username;NTLM-Hash;Pwn-Count%n";

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public void setStopTime(Instant stopTime) {
		this.elapsedTime = Duration.between(startTime, stopTime);
	}

	public String getElapsedTimeString() {
		return String.format("Elapsed time: %s days, %s hours, %s minutes and %s seconds.%n", elapsedTime.toDays(),
				elapsedTime.toHoursPart(), elapsedTime.toMinutesPart(), elapsedTime.toSecondsPart());
	}

	public String getAdHashesCountString() {
		return String.format("Checked AD hashes: %s%n", adHashesCount);
	}

	public int getAdHashesCount() {
		return adHashesCount;
	}

	public void setAdHashesCount(int adHashesCount) {
		this.adHashesCount = adHashesCount;
	}

	public String getAdAccountsCountString() {
		return String.format("Checked AD accounts: %s%n", adAccountsCount);
	}

	public void setAdAccountsCount(long adAccountsCount) {
		this.adAccountsCount = adAccountsCount;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public List<String> getHeaderStrings() {
		return List.of(getElapsedTimeString(), getAdAccountsCountString(), getAdHashesCountString(),
				String.format(RESULTS_HEADER_STRING));
	}
}
