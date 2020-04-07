package com.saganski.bigram;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.saganski.bigram.model.BigramUtil;

@SpringBootTest
class BigramApplicationTests {

	/*
	 * 
	 * Command Line Arguments
	 * 
	 */
	
	@Test
	void correctNumberOfArguments() {
		final String[] givenArgs = {"fileLocation"};
		final Boolean expected = true;
		final Boolean actual = BigramUtil.argumentCheck(givenArgs);
		
		assertEquals(expected, actual, "Should return true if correct number of arguments are given (1)");
		
	}
	
	@Test
	void tooManyNumberOfArguments() {
		final String[] givenArgs = {"fileLocation", "secondaryArg"};
		final Boolean expected = false;
		final Boolean actual = BigramUtil.argumentCheck(givenArgs);
		
		assertEquals(expected, actual, "Should return false if incorrect number of arguments are given (anything other then 1)");
		
	}
	
	@Test
	void noNumberOfArguments() {
		final String[] givenArgs = {};
		final Boolean expected = false;
		final Boolean actual = BigramUtil.argumentCheck(givenArgs);
		
		assertEquals(expected, actual, "Should return false if incorrect number of arguments are given (anything other then 1)");
		
	}
	
	/*
	 * 
	 * File Parsing 
	 * 
	 */
	
	@Test
	void parseFileToListOfLines() {
		try {
			final String firstLine = "The quick brown        fox and the quick   blue    hare.";
			final String secondLine = "\nThe quick brown        fox and the quick   blue    hare.";
			
			final File file = File.createTempFile("temp-file", ".txt");
			final Path path = Paths.get(file.getAbsolutePath());
			
			Files.write(path, firstLine.getBytes(), StandardOpenOption.APPEND);
			Files.write(path, secondLine.getBytes(), StandardOpenOption.APPEND);
			
			final List<String> expected = Stream.of("The quick brown        fox and the quick   blue    hare.","The quick brown        fox and the quick   blue    hare.")
					.collect(Collectors.toList());
			final List<String> actual = BigramUtil.parseFileLinesToStringList(file.getAbsolutePath());
			
			file.delete();
			
			assertEquals(expected, actual, "Should return a list of string lines from given a file path name");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	void returnsEmptyListOfLines() {
		final List<String> expected = Collections.emptyList();
		final List<String> actual = BigramUtil.parseFileLinesToStringList("non-sense/path");
		assertEquals(expected, actual, "Should return a an empty list of string lines");
		
	}
	
	/*
	 * STRING TRANSFORMATION TEST
	 */
	@Test
	void transformStringIntoList() {
		 final List<String> expected = Stream.of("the", "quick", "brown", "fox", "and", "the", "quick", "blue", "hare")
			      .collect(Collectors.toList());
		 final List<String> actual = BigramUtil.transformStringExtractList("The quick brown        fox and the quick   blue    hare.");
		 
		 assertEquals(expected, actual, "Should parse string into list of strings");
	}
	
	@Test
	void allCharsShouldBeLowerCase() {
		 final List<String> expected = Stream.of("the", "quick", "brown", "fox", "and", "the", "quick", "blue", "hare")
			      .collect(Collectors.toList());
		 final List<String> actual = BigramUtil.transformStringExtractList("THE QUICK BROWN FOX AND THE QUICK BLUE HARE.");
		 
		 assertEquals(expected, actual, "Should transform chars to lower case");
	}
	
	@Test
	void removesEmptySpace() {
		final List<String> expected = Stream.of("the", "quick", "brown", "fox", "and", "the", "quick", "blue", "hare")
			      .collect(Collectors.toList());
		final List<String> actual = BigramUtil.transformStringExtractList("The quick brown     \t   fox and the quick   blue  \n  hare.");
		 
		assertEquals(expected, actual, "Should transform chars to lower case");
	}
	
	@Test
	void removesPunctuationAndSpecialChar() {
		 final List<String> expected = Stream.of("the", "quick", "brown", "fox", "and", "the", "quick", "blue", "hare")
			      .collect(Collectors.toList());
		 final List<String> actual = BigramUtil.transformStringExtractList("The; quick: brown. fox and the quick   blue!  hare.");
		 
		 assertEquals(expected, actual, "Should transform chars to lower case");
	}
	
	@Test
	void removesBlankLines() {
		 final List<String> expected = Collections.emptyList();
		 final List<String> actual = BigramUtil.transformStringExtractList("   ");
		 
		 assertEquals(expected, actual, "Should be empty");
	}
	
	/*
	 * Bigrams
	 */
	@Test
	void transformLineToMapOfBigrams() {
		final List<String> line = Stream.of("the", "quick", "brown", "fox", "and", "the", "quick", "blue", "hare")
			      .collect(Collectors.toList());
		@SuppressWarnings("serial")
		final Map<String, Integer> expected = new HashMap<String, Integer>() {{
			put("quick blue", 1);
			put("the quick", 2);
			put("blue hare", 1);
			put("fox and", 1);
			put("quick brown", 1);
			put("and the", 1);
			put("brown fox", 1);
		}};
		final Map<String, Integer> actual = BigramUtil.mapOfBigrams(line);
		
		assertEquals(expected,actual,"Should create a map with word count");
		
	}
	
	/*
	 *File writing test 
	 */

	@Test
	void wrtiesNewFile() {
		final List<Map<String, Integer>> bigramResults = new ArrayList<>();
		@SuppressWarnings("serial")
		final Map<String, Integer> bigramHash = new HashMap<String, Integer>() {{
			put("quick blue", 1);
			put("the quick", 2);
			put("blue hare", 1);
			put("fox and", 1);
			put("quick brown", 1);
			put("and the", 1);
			put("brown fox", 1);
		}};
		
		bigramResults.add(bigramHash);
		
		final File file = BigramUtil.writeFile(bigramResults);
		final Boolean fileExists = true;
		final Boolean actual = file.exists();
		
		assertEquals(fileExists,actual,"Should create a file");
		
		file.delete();
		
	}

}
