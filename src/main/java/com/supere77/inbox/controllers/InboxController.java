package com.supere77.inbox.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.supere77.inbox.model.Folder;
import com.supere77.inbox.repo.FolderRepository;

@Controller
public class InboxController {
	
	@Autowired
	private FolderRepository repo;
	
	
	@GetMapping(value = "/")
	public ModelAndView homePage(@AuthenticationPrincipal OAuth2User principal) {
		
		if(principal != null && StringUtils.hasText(principal.getAttribute("login"))) {
			
			ModelAndView modelAndView = new ModelAndView("inbox-page");
			String user = principal.getAttribute("login");
			List<Folder> userFolders = repo.findAllByUserId(user);
			modelAndView.addObject("userFolders",userFolders);
			
			return modelAndView;
		}
		return new ModelAndView("index");
	}

}
