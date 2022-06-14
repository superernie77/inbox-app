package com.supere77.inbox.repo;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.supere77.inbox.model.Folder;

@Repository
public interface FolderRepository extends CassandraRepository<Folder, String> {

}
