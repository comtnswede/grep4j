package org.grep4j.core;

import static org.grep4j.core.Grep4j.extraLinesAfter;
import static org.grep4j.core.Grep4j.grep;
import static org.grep4j.core.fixtures.ProfileFixtures.localProfile;
import static org.grep4j.core.fixtures.ProfileFixtures.localProfileWithWildecard;
import static org.grep4j.core.fluent.Dictionary.on;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.grep4j.core.result.GrepResult;
import org.grep4j.core.result.GrepResultsSet;
import org.testng.annotations.Test;

@Test
public class WhenExtractingWithRegEx {

	public void aGrepResultsSetWithSingleFile() {
		GrepResultsSet results = grep("ERROR 1", on(localProfile()), extraLinesAfter(20));
		for (GrepResult result : results.extractWithRegEx("Marco(.*)has been")) {
			assertThat(StringUtils.contains(result.getText(), "customer Marco(id=12345) has been updated successfully"), is(true));
		}
	}

	public void aGrepResultsSetWithMultipleFiles() {
		GrepResultsSet results = grep("ER", on(Arrays.asList(localProfileWithWildecard("*"))), extraLinesAfter(20));
		for (GrepResult result : results.extractWithRegEx("ER(.*)OR")) {
			if (result.getFileName().endsWith("gz")) {
				assertThat(StringUtils.contains(result.getText(), "GZ ERROR 1"), is(true));
				assertThat(StringUtils.contains(result.getText(), "GZ ERROR 2"), is(true));
			} else {
				assertThat(StringUtils.contains(result.getText(), "ERROR 1"), is(true));
				assertThat(StringUtils.contains(result.getText(), "ERROR 2"), is(true));
			}
		}
	}

	public void aGrepResultsWithSingleFile() {
		GrepResultsSet results = grep("ERROR 1", on(localProfile()), extraLinesAfter(20));
		for (GrepResult result : results) {
			assertThat(StringUtils.contains(result.getText(), "customer Marco(id=12345) has been updated successfully"), is(true));
		}
	}

	public void aGrepResultsWithMultipleFiles() {
		GrepResultsSet results = grep("ER", on(Arrays.asList(localProfileWithWildecard("*"))), extraLinesAfter(20));
		for (GrepResult result : results) {
			if (result.getFileName().endsWith("gz")) {
				assertThat(StringUtils.contains(result.extractWithRegEx("E(.*)OR").getText(), "GZ ERROR 1"), is(true));
				assertThat(StringUtils.contains(result.extractWithRegEx("E(.*)OR").getText(), "GZ ERROR 2"), is(true));
			} else {
				assertThat(StringUtils.contains(result.extractWithRegEx("E(.*)OR").getText(), "ERROR 1"), is(true));
				assertThat(StringUtils.contains(result.extractWithRegEx("E(.*)OR").getText(), "ERROR 2"), is(true));
			}
		}
	}

	public void aGrepResultsSetWithSingleFileMultipleExtracts() {
		GrepResultsSet results = grep("ERROR 1", on(localProfile()), extraLinesAfter(20));
		for (GrepResult result : results.extractWithRegEx("f(.*)e").extractWithRegEx("ext(.*)ct")) {
			assertThat(StringUtils.contains(result.getText(), "fine extract"), is(true));
		}
	}

	public void aGrepResultsWithMultipleFilesMultipleExtracts() {
		GrepResultsSet results = grep("ER", on(Arrays.asList(localProfileWithWildecard("*"))), extraLinesAfter(20));
		for (GrepResult result : results) {
			if (result.getFileName().endsWith("gz")) {
				assertThat(StringUtils.contains(result.extractWithRegEx("OR").extractWithRegEx("2").getText(), "GZ ERROR 1"), is(false));
				assertThat(StringUtils.contains(result.extractWithRegEx("OR").extractWithRegEx("1").getText(), "GZ ERROR 2"), is(false));
			} else {
				assertThat(StringUtils.contains(result.extractWithRegEx("fine").extractWithRegEx("extract").extractWithRegEx("(.*)ub(.*)").getText(),
						"fine double extract"), is(true));
			}
		}
	}

}
