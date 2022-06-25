package com.supere77.inbox.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supere77.inbox.model.Folder;
import com.supere77.inbox.model.UnreadEmailStats;
import com.supere77.inbox.repo.UnreadEmailStatsRepository;

@Service
public class FoldeService {
	
	@Autowired
	private UnreadEmailStatsRepository unreadRepo;

	public List<Folder> getDefaultFolder(String userId) {
		return Arrays.asList(
				new Folder(userId, "Important", "green"), 
				new Folder(userId, "Inbox", "blue"),
				new Folder(userId, "Sent", "yellow"));
	}

	
	public Map<String,Integer> mapCountToLabels(String user ){
		List<UnreadEmailStats> stats = unreadRepo.findAllById(user);
		
		return stats.stream().collect(Collectors.toMap(UnreadEmailStats::getLabel, UnreadEmailStats::getUnreadCount));
		
	}
}
