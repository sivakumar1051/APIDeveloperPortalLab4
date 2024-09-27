package com.siva.apidevportal.Models;

public class ApiKeys {
	
	private String api_Key;
    private String status;
    private String createdAt;
    
	public ApiKeys(String api_Key, String status, String createdAt) {
		super();
		this.api_Key = api_Key;
		this.status = status;
		this.createdAt = createdAt;
	}
	
	public String getApi_Key() {
		return api_Key;
	}
	public void setApi_Key(String api_Key) {
		this.api_Key = api_Key;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

}
