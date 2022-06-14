package com.supere77.inbox;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "datastax.astra")
public class DataStaxAstraProperties {

	private File secureConnectBundle;

	public File getSecureConnectBundle() {
		return secureConnectBundle;
	}

	public void setSecureConnectBundle(File secureConnectBundle) {
		this.secureConnectBundle = secureConnectBundle;
	}

}
