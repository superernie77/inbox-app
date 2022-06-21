package com.supere77.inbox.repo;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.supere77.inbox.model.Email;

public interface EmailRepository extends CassandraRepository<Email, UUID>{

}
