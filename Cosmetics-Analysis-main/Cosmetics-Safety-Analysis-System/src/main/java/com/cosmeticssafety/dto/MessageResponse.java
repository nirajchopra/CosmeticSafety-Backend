package com.cosmeticssafety.dto;

public class MessageResponse {

	private String message;
	private String token;

	public MessageResponse() {
	}

	public MessageResponse(String message) {
		this.message = message;
	}

	public MessageResponse(String message, String token) {
		this.message = message;
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
