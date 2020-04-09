package com.saganski.bigram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.saganski.bigram.model.BigramUtil;

@SpringBootApplication
public class BigramApplication {

	public static void main(String[] args) {
		SpringApplication.run(BigramApplication.class, args);
		
		if(BigramUtil.argumentCheck(args)) {
			final String fileLocation = args[0];
			
			final List<String> parsedFileLines = BigramUtil.parseFileLinesToStringList(fileLocation);
			
			final List<Map<String, Integer>> bigramResult = new ArrayList<>();
			
			parsedFileLines.forEach(l -> {
				final List<String> lineBigram = BigramUtil.transformStringExtractList(l);
				
				final Map<String, Integer> lineBigramResult = BigramUtil.mapOfBigrams(lineBigram);
				bigramResult.add(lineBigramResult);
			});
			
			System.out.println("results");
			bigramResult.forEach( b -> System.out.println(b));
			
			BigramUtil.writeFile(bigramResult);
			
			
		}else {
			System.out.println("*****************************************************************************************************");
			System.out.println("******************** ERROR ********************");
			final String exampleFileLocation = "C:\\Users\\ken.saganski\\Desktop\\test\\TestFile.txt";
			System.out.println("This application takes one one(1) argument run, and that is the file loction to parse. For ex: java Bigram " + exampleFileLocation +"\n");
			System.out.println("*****************************************************************************************************");
		}
			
	}
	
}
