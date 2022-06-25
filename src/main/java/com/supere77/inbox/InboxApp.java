package com.supere77.inbox;



import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

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

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.supere77.inbox.model.Email;
import com.supere77.inbox.model.EmailListItem;
import com.supere77.inbox.model.EmailListItemKey;
import com.supere77.inbox.model.Folder;
import com.supere77.inbox.repo.EmailListItemRepository;
import com.supere77.inbox.repo.EmailRepository;
import com.supere77.inbox.repo.FolderRepository;
import com.supere77.inbox.repo.UnreadEmailStatsRepository;

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
	private EmailListItemRepository emailListRepo;
	
	@Autowired
	private EmailRepository emailRepo;
	
	@Autowired
	private UnreadEmailStatsRepository unreadRepo;
	
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
		
		unreadRepo.incrementCounter("superernie77", "Inbox");
		unreadRepo.incrementCounter("superernie77", "Inbox");
		unreadRepo.incrementCounter("superernie77", "Inbox");
		
		
		for(int i = 0 ; i< 10 ; i++) {
			EmailListItemKey key = new EmailListItemKey();
			key.setId("superernie77");
			key.setLabel("Inbox");
			
			UUID createdAt = Uuids.timeBased();
			
			key.setTimeUUID(createdAt);
			
			EmailListItem item = new EmailListItem();
			item.setKey(key);
			item.setTo(Arrays.asList("superernie77"));
			item.setSubject("message no."+i);
			item.setUnread(true);
			emailListRepo.save(item);
			
			
			Email email = new Email();
			email.setFrom("superernie77");
			email.setId(createdAt);
			email.setSubject("message no."+i);
			email.setTo(Arrays.asList("superernie77"));
			email.setBody("New message body "+i);
			
			emailRepo.save(email);
		}
		
	}
	
	// Method to test OAuth Login with github
//	@RequestMapping("/user")
//	public String user(@AuthenticationPrincipal OAuth2User principal) {
//		System.out.println(principal);
//		return principal.getAttribute("name");
//	}
	

}
