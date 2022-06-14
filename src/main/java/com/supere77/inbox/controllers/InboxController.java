package com.supere77.inbox.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.supere77.inbox.model.Folder;
import com.supere77.inbox.repo.FolderRepository;

@Controller
public class InboxController {
	
	@Autowired
	private FolderRepository repo;
	
	
	@GetMapping(value = "/")
	public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model) {
		
		if(principal != null && StringUtils.hasText(principal.getAttribute("login"))) {
			
			String user = principal.getAttribute("login");
			List<Folder> userFolders = repo.findAllByUserId(user);
			model.addAttribute("userFolders",userFolders);
			
			return "inbox-page";
		}
		return "index";
	}

}
