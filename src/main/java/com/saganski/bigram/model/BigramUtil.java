package com.saganski.bigram.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class BigramUtil {

	public static List<String> parseFileLinesToStringList(String fileLocation) {
		final List<String> parsedFileLines = new ArrayList<>();

		try {
			final BufferedReader reader = Files.newBufferedReader(Paths.get(fileLocation));
			String line;
			while((line = reader.readLine()) != null) {
				line.trim();
				if(!line.isEmpty()) {
					parsedFileLines.add(line);
				}
			}
			
		}catch(IOException io) {
			io.printStackTrace();
			return parsedFileLines;
		}
		
		return parsedFileLines;
	}

	public static List<String> transformStringExtractList(String line) {
		final List<String> lineArray = Arrays.stream(line.split("\\s+"))
				.filter(a -> !a.isEmpty())
				.map(a -> a.replaceAll("[^a-zA-Z0-9]", ""))
				.filter(a -> !StringUtils.isBlank(a))
				.map(a -> a.toLowerCase())
				.collect(Collectors.toList());
		return lineArray;
	}

	public static Map<String, Integer> mapOfBigrams(List<String> lineList) {
		final Map<String, Integer> bigramResult = new HashMap<>();
		
		for(int i=0; i < lineList.size(); i++) {
			int indexOutOfBoundsCheck = i + 1;
			try {
				
				final String firstWord = lineList.get(i);
				final String secondWord = lineList.get(indexOutOfBoundsCheck);
				
				final String bigram = firstWord.concat(" ").concat(secondWord);
				
				if(bigramResult.containsKey(bigram)) {
					final Integer count = bigramResult.get(bigram) + 1;
					bigramResult.put(bigram, count);
				}else {
					bigramResult.put(bigram, 1);
				}
				
			}catch(IndexOutOfBoundsException e) {
				//end of the line
				continue;
			}
			
		}
		return bigramResult;
	}

	public static boolean argumentCheck(String[] args) {
		if(args.length != 1) {
			return false;
		}
		return true;
	}

	public static File writeFile(List<Map<String, Integer>> bigramResult) {
		
		try {
			final File file = new File("bigram-results.txt");
			if(!file.exists()) {
				file.createNewFile();
			}
			
			final FileWriter writer = new FileWriter(file.getAbsoluteFile());
			final BufferedWriter buffer = new BufferedWriter(writer);
			
			for(Map<String, Integer> resultLine : bigramResult) {
				buffer.write(resultLine.toString());
				buffer.newLine();
			}
			
			if(bigramResult.isEmpty()) {
				final String emptyMessage = "Unable to find Bigrams! Possible empty file given to parse";
				
				buffer.write(emptyMessage);
			}
			
			buffer.close();
			return file;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
