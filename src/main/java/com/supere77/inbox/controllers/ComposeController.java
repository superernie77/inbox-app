package com.supere77.inbox.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.supere77.inbox.model.Folder;
import com.supere77.inbox.repo.EmailRepository;
import com.supere77.inbox.repo.FolderRepository;
import com.supere77.inbox.service.FoldeService;

public class ComposeController {
	
	
	@Autowired
	private FolderRepository repo;
	
	@Autowired
	private FoldeService folderService;
	
	@Autowired
	private EmailRepository emailRepo;
	
	@GetMapping(value = "/emails/compose")
	public ModelAndView getComposePage(@AuthenticationPrincipal OAuth2User principal) {
		
		if(principal != null && StringUtils.hasText(principal.getAttribute("login"))) {
			
			ModelAndView modelAndView = new ModelAndView("compose-page");
			
			// fetch username
			String user = principal.getAttribute("login");
			modelAndView.addObject("username", principal.getAttribute("name"));
			
			// fetch folder
			List<Folder> defaultFolder = folderService.getDefaultFolder(user);
			modelAndView.addObject("defaultFolders",defaultFolder);
			
			List<Folder> userFolders = repo.findAllByUserId(user);
			modelAndView.addObject("userFolders",userFolders);
			return modelAndView;
		}
		return new ModelAndView("index");
	}
}
