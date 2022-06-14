package com.supere77.inbox;



import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.supere77.inbox.model.Folder;
import com.supere77.inbox.repo.FolderRepository;

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
	
	@PostConstruct
	public void init() {
		
		Folder folder = new Folder();
		folder.setLabel("Inbox");
		folder.setUserId("superernie77");
		folder.setColor("blue");
		
		Folder folder2 = new Folder();
		folder2.setLabel("Sent");
		folder2.setUserId("superernie77");
		folder2.setColor("gree");
		
		Folder folder3 = new Folder();
		folder3.setLabel("Important");
		folder3.setUserId("superernie77");
		folder3.setColor("yellow");
		
		repo.save(folder);
		repo.save(folder2);
		repo.save(folder3);
		
	}
	
	// Method to test OAuth Login with github
//	@RequestMapping("/user")
//	public String user(@AuthenticationPrincipal OAuth2User principal) {
//		System.out.println(principal);
//		return principal.getAttribute("name");
//	}
	

}
