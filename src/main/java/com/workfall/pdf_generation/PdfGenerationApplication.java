package com.workfall.pdf_generation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PdfGenerationApplication {

	private static final Log log = LogFactory
			.getLog(PdfGenerationApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PdfGenerationApplication.class, args);
		log.info("Hello Abhishek...");
	}

}
