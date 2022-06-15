package com.supere77.inbox.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.supere77.inbox.model.Folder;

@Service
public class FoldeService {

	public List<Folder> getDefaultFolder(String userId) {
		return Arrays.asList(
				new Folder(userId, "Important", "green"), 
				new Folder(userId, "Inbox", "blue"),
				new Folder(userId, "Sent", "yellow"));
	}

}
