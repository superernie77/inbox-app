package com.supere77.inbox.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.supere77.inbox.model.EmailListItem;
import com.supere77.inbox.model.Folder;
import com.supere77.inbox.model.UnreadEmailStats;
import com.supere77.inbox.repo.EmailListItemRepository;
import com.supere77.inbox.repo.FolderRepository;
import com.supere77.inbox.repo.UnreadEmailStatsRepository;
import com.supere77.inbox.service.FoldeService;

@Controller
public class InboxController {
	
	@Autowired
	private FolderRepository repo;
	
	@Autowired
	private FoldeService folderService;
	
	@Autowired
	private EmailListItemRepository emailRepo;
	
	
	
	@GetMapping(value = "/")
	public ModelAndView homePage(
			@RequestParam(required = false) String folder,
			@AuthenticationPrincipal OAuth2User principal) {
		
		if(principal != null && StringUtils.hasText(principal.getAttribute("login"))) {
			
			ModelAndView modelAndView = new ModelAndView("inbox-page");
			String user = principal.getAttribute("login");
			modelAndView.addObject("username", principal.getAttribute("name"));
			
			// fetch folder
			List<Folder> defaultFolder = folderService.getDefaultFolder(user);
			modelAndView.addObject("defaultFolders",defaultFolder);
			
			// fetch counter
			Map<String,Integer> counter = folderService.mapCountToLabels(user);
			modelAndView.addObject("stats", counter);
			
			List<Folder> userFolders = repo.findAllByUserId(user);
			modelAndView.addObject("userFolders",userFolders);
			
			// fetch messages
			if(!StringUtils.hasText(folder)) {
				folder = "Inbox";
			}
			
			List<EmailListItem> emails =  emailRepo.findByKey_IdAndKey_Label(user, folder );
			modelAndView.addObject("emaillist", emails);
			modelAndView.addObject("folder", folder);
			
			
			PrettyTime p = new PrettyTime();
			emails.forEach( i -> {
				UUID id = i.getKey().getTimeUUID();
				Date emailDate = new Date(Uuids.unixTimestamp(id));
				String s = p.format(emailDate);
				i.setDateString(s);
				
			});
			
			return modelAndView;
		}
		return new ModelAndView("index");
	}
}
