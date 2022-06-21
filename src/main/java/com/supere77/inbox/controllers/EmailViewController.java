package com.supere77.inbox.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.supere77.inbox.model.Email;
import com.supere77.inbox.model.Folder;
import com.supere77.inbox.repo.EmailRepository;
import com.supere77.inbox.repo.FolderRepository;
import com.supere77.inbox.service.FoldeService;

@Controller
public class EmailViewController {
	
	@Autowired
	private FolderRepository repo;
	
	@Autowired
	private FoldeService folderService;
	
	@Autowired
	private EmailRepository emailRepo;

	@GetMapping(value = "/emails/{id}")
	public ModelAndView emailView(@AuthenticationPrincipal OAuth2User principal, @PathVariable String id) {
		
		if(principal != null && StringUtils.hasText(principal.getAttribute("login"))) {
			
			ModelAndView modelAndView = new ModelAndView("email-page");
			
			// fetch username
			String user = principal.getAttribute("login");
			modelAndView.addObject("username", principal.getAttribute("name"));
			
			// fetch folder
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
			
		} 
		return new ModelAndView("index");
	}
}
