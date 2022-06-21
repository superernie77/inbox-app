package com.supere77.inbox.model;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;

@Table( value = "messages_by_id")
public class Email {

	@Id @PrimaryKeyColumn(name = "id", ordinal = 1 , type= PrimaryKeyType.PARTITIONED)
	private UUID id;
	
	@CassandraType(type = Name.LIST , typeArguments = Name.TEXT)
	private List<String> to;
	
	@CassandraType(type = Name.TEXT)
	private String from;
	
	@CassandraType(type = Name.TEXT)
	private String subject;
	
	@CassandraType(type = Name.TEXT)
	private String body;

	public String getRecipientString() {
		return String.join(",", to);
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
}
