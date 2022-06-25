package com.supere77.inbox.repo;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import com.supere77.inbox.model.UnreadEmailStats;

@Repository
public interface UnreadEmailStatsRepository extends CassandraRepository<UnreadEmailStats, String> {
	
	List<UnreadEmailStats> findAllById(String id);
	
	@Query("update unread_email_stats set unreadcount = unreadcount +1 where user_id = ?0 and label = ?1")
	void incrementCounter(String userId, String folder);
	
	@Query("update unread_email_stats set unreadcount = unreadcount +1 where user_id = ?0 and label = ?1")
	void decrementCounter(String userId, String folder);

}
