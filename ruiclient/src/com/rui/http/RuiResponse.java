package com.rui.http;

import java.io.Serializable;

public class RuiResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236850440771706488L;

	public static final int SUCCESS = 200;

	public int code;
	public String body;

	public RuiResponse(int code, String body) {
		this.code = code;
		this.body = body;
	}

	public RuiResponse(String body) {
		this.code = 200;
		this.body = body;
	}

	public RuiResponse() {

	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isSucces() {
		return code == 200;
	}

	public String toString() {
		return this.body;
	}

	public boolean notFound() {
		return code == 404;
	}

}
