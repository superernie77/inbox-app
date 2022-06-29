package com.supere77.inbox.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.supere77.inbox.model.Folder;
import com.supere77.inbox.repo.FolderRepository;
import com.supere77.inbox.service.EmailService;
import com.supere77.inbox.service.FoldeService;

@Controller
public class ComposeController {

	@Autowired
	private FolderRepository repo;

	@Autowired
	private FoldeService folderService;

	@Autowired
	private EmailService emailService;
	
	@GetMapping(value = "/compose")
	public ModelAndView getComposePage(@RequestParam(required = false) String to,
			@AuthenticationPrincipal OAuth2User principal) {

		if (principal != null && StringUtils.hasText(principal.getAttribute("login"))) {

			ModelAndView modelAndView = new ModelAndView("compose-page");

			// fetch username
			String user = principal.getAttribute("login");
			modelAndView.addObject("username", principal.getAttribute("name"));

			// fetch folder
			List<Folder> defaultFolder = folderService.getDefaultFolder(user);
			modelAndView.addObject("defaultFolders", defaultFolder);
			
			// fetch counter
			Map<String,Integer> counter = folderService.mapCountToLabels(user);
			modelAndView.addObject("stats", counter);

			List<Folder> userFolders = repo.findAllByUserId(user);
			modelAndView.addObject("userFolders", userFolders);

			List<String> uniqueIds = cleanIds(to);
			modelAndView.addObject("toIds", String.join(", ", uniqueIds));
			
			return modelAndView;
		}
		return new ModelAndView("index");
	}


	private List<String> cleanIds(String to) {
		if (StringUtils.hasText(to)) {
		String[] splitIds = to.split(",");

		List<String> uniqueIds = Arrays.asList(splitIds).stream().map(id -> StringUtils.trimWhitespace(id))
				.filter(id -> StringUtils.hasText(id)).distinct().collect(Collectors.toList());
		return uniqueIds;
		}
		return new ArrayList<>();
	}
	
	
	@PostMapping("/send")
	public ModelAndView sentEmails(
			@RequestBody MultiValueMap<String,String> params,
			@AuthenticationPrincipal OAuth2User principal) {
		ModelAndView modelAndView = new ModelAndView("redirect:/");
		
		String subject = params.getFirst("subject");
		String to = params.getFirst("to");
		String body = params.getFirst("body");
		
		List<String> toIds = cleanIds(to);
		emailService.sendEmail(principal.getAttribute("login"), toIds, body, subject);
		
		return modelAndView;
	}
}
