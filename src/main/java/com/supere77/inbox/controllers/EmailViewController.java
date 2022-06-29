package com.supere77.inbox.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.supere77.inbox.model.Email;
import com.supere77.inbox.model.EmailListItem;
import com.supere77.inbox.model.EmailListItemKey;
import com.supere77.inbox.model.Folder;
import com.supere77.inbox.repo.EmailListItemRepository;
import com.supere77.inbox.repo.EmailRepository;
import com.supere77.inbox.repo.FolderRepository;
import com.supere77.inbox.repo.UnreadEmailStatsRepository;
import com.supere77.inbox.service.FoldeService;

@Controller
public class EmailViewController {
	
	@Autowired
	private FolderRepository repo;
	
	@Autowired
	private FoldeService folderService;
	
	@Autowired
	private EmailRepository emailRepo;
	
	@Autowired
	private UnreadEmailStatsRepository unreadRepo;
	
	@Autowired
	private EmailListItemRepository emailListRepo;

	@GetMapping(value = "/emails/{id}")
	public ModelAndView emailView(
			@RequestParam String folder,
			@AuthenticationPrincipal OAuth2User principal, @PathVariable String id) {
		
		if(principal != null && StringUtils.hasText(principal.getAttribute("login"))) {
			
			ModelAndView modelAndView = new ModelAndView("email-page");
			
			// fetch username
			String user = principal.getAttribute("login");
			modelAndView.addObject("username", principal.getAttribute("name"));
			
			// Fetch folder
			List<Folder> defaultFolder = folderService.getDefaultFolder(user);
			modelAndView.addObject("defaultFolders",defaultFolder);
			
			List<Folder> userFolders = repo.findAllByUserId(user);
			modelAndView.addObject("userFolders",userFolders);
			
			// fetch email
			Email email =  emailRepo.findById(UUID.fromString(id)).orElse(null);
			
			if (email != null) {
				modelAndView.addObject("email", email);	
				return modelAndView;	
			}
			
			EmailListItemKey key = new EmailListItemKey();
			key.setLabel(folder);
			key.setId(user);
			key.setTimeUUID(email.getId());
			
			Optional<EmailListItem> optionalItem =	emailListRepo.findById(key);
			
			if (optionalItem.isPresent()) {
				EmailListItem item = optionalItem.get();
				if(item.isUnread()) {
					item.setUnread(false);
					emailListRepo.save(item);
					unreadRepo.decrementCounter(user, folder);
					
				}
				
			}
			
			
			// Fetch counter
			Map<String,Integer> counter = folderService.mapCountToLabels(user);
			modelAndView.addObject("stats", counter);
			
		} 
		return new ModelAndView("index");
	}
}
