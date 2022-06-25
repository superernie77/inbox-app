package com.supere77.inbox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.supere77.inbox.model.Email;
import com.supere77.inbox.model.EmailListItem;
import com.supere77.inbox.model.EmailListItemKey;
import com.supere77.inbox.repo.EmailListItemRepository;
import com.supere77.inbox.repo.EmailRepository;
import com.supere77.inbox.repo.UnreadEmailStatsRepository;

@Service
public class EmailService {

	@Autowired
	private EmailRepository emailRepo;
	
	@Autowired
	private EmailListItemRepository listItemRepo;
	
	@Autowired
	private UnreadEmailStatsRepository unreadRepo;
	
	
	public void sendEmail(String from, List<String> to, String body, String subject) {
		
		Email mail = new Email();
		
		mail.setBody(body);
		mail.setFrom(from);
		mail.setSubject(subject);
		mail.setTo(to);
		mail.setId(Uuids.timeBased());
		emailRepo.save(mail);
		
		to.forEach( toId -> {
			EmailListItem item = extracted(to, subject, mail, toId, "Inbox");
			listItemRepo.save(item);
			unreadRepo.incrementCounter(toId, "Inbox");
		});
		
		EmailListItem itemOwner = extracted(to, subject, mail, mail.getFrom(), "Sent");
		itemOwner.setUnread(false);
		listItemRepo.save(itemOwner);
		
	}


	private EmailListItem extracted(List<String> to, String subject, Email mail, String owner, String folder) {
		EmailListItem item = new EmailListItem();
		EmailListItemKey key = new EmailListItemKey();
		key.setTimeUUID(mail.getId());
		key.setId(owner);
		key.setLabel(folder);
		
		item.setKey(key);
		item.setSubject(subject);
		item.setTo(to);
		item.setUnread(true);
		return item;
	}
}
