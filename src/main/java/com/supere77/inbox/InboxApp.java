package com.supere77.inbox;

import java.nio.file.Path;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import com.supere77.inbox.repo.FolderRepository;
import com.supere77.inbox.service.EmailService;

@SpringBootApplication
@RestController
public class InboxApp {

	public static void main(String[] args) {
		SpringApplication.run(InboxApp.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilder(DataStaxAstraProperties props) {
		Path bundle = props.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@Autowired
	private FolderRepository repo;

	@Autowired
	private EmailService emailService;

	@PostConstruct
	public void init() {
		for (int i = 0; i < 10; i++) {
			emailService.sendEmail("superernie77", Arrays.asList("superernie77", "eddy"), "hello " + i, "Subject " + i);
		}

	}

	// Method to test OAuth Login with github
//	@RequestMapping("/user")
//	public String user(@AuthenticationPrincipal OAuth2User principal) {
//		System.out.println(principal);
//		return principal.getAttribute("name");
//	}

}
