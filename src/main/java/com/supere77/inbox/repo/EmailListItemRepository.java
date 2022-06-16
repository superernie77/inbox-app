package com.supere77.inbox.repo;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.supere77.inbox.model.EmailListItem;
import com.supere77.inbox.model.EmailListItemKey;

public interface  EmailListItemRepository extends CassandraRepository<EmailListItem, EmailListItemKey> {
	
	List<EmailListItem> findByKey_IdAndKey_Label(String id, String label);

}
