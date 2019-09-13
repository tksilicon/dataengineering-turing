package com.turing.dataengineeringengine.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turing.dataengineeringengine.kafta.Producer;
import com.turing.dataengineeringengine.model.GitHubAccounts;
import com.turing.dataengineeringengine.service.StorageService;

/**
 * The Github Analysis API Controller
 * 
 * @author thankgodukachukwu
 *
 */
@RestController
public class ProducerSparkKafkaController {

	private final Producer producer;
	private final StorageService storageService;
	@Value("${spark.driver.memory}")
	private String sparkDriverMemory;

	@Autowired
	ProducerSparkKafkaController(Producer producer, StorageService storageService) {
		this.producer = producer;
		this.storageService = storageService;
	}
	/**
	 * This method receive API call with an argument and uses Apache Spark to load (large) files into memory
	 * 
	 * @param filename
	 * @return
	 * @throws JsonProcessingException
	 */

	@PostMapping(path = "/api/getgithubanalysis")
	public ResponseEntity<String> enterFileForAnalysis(
			@RequestParam(name = "filename", required = true) String filename) throws JsonProcessingException {

		SparkConf sparkConf = new SparkConf().setAppName("JavaGitHubAnalysis").setMaster("local").set("spark.driver.memory", sparkDriverMemory);
		
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);

		JavaRDD<String> lines2 = ctx.textFile(System.getProperty("user.dir") + "/src/main/resources/" + filename)
				.repartition(3);

		int tracker = 0;

		List<String> stream = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		for (String word : lines2.collect()) {
			tracker++;
			if (word.trim().startsWith("http"))
				stream.add(word);
			if (tracker % 5 == 0) {

				this.producer.sendMessage(mapper.writeValueAsString(new GitHubAccounts(stream)));

				stream = new ArrayList<>();

			}

		}

		ctx.stop();

		return ResponseEntity.ok().body("Files Have Been Successfully processed");

	}
	
	/**
	 * This method receive API call without an argument and uses Apache Spark to load (large) files into memory
	 * @return
	 * @throws Exception
	 */

	@GetMapping(path = "/api/getgithubanalysis")
	public ResponseEntity<String> sendMessageToKafkaTopic2() throws Exception {
		JavaSparkContext ctx = null;

		SparkConf sparkConf = new SparkConf().setAppName("JavaGitHubAnalysis").setMaster("local")
				.set("spark.driver.allowMultipleContexts", "true");
	
		ctx = new JavaSparkContext(sparkConf);

		JavaRDD<String> lines2 = ctx.textFile(System.getProperty("user.dir") + "/src/main/resources/data/url_list1.csv")
				.repartition(3);

		int tracker = 0;

		List<String> stream = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		for (String word : lines2.collect()) {

			if (word.trim().startsWith("http")) {
				stream.add(word);
				tracker++;
			}
			if (tracker % 5 == 0) {

				this.producer.sendMessage(mapper.writeValueAsString(new GitHubAccounts(stream)));

				stream = new ArrayList<>();

			}

		}

		ctx.stop();

		return ResponseEntity.ok().body("Files Have Been Successfully processed");

	}

	/**
	 * 
	 * @param file
	 * @return
	 */

	@PostMapping(path = "/api/upload")
	public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {

		storageService.store(file);

		return ResponseEntity.ok().build();
	}
}
