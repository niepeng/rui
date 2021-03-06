package com.rui.android_client.model;

import java.io.Serializable;

public class BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2089347400398454795L;
	private long id;
	private long created;
	private long updated;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getUpdated() {
		return updated;
	}

	public void setUpdated(long updated) {
		this.updated = updated;
	}

}
