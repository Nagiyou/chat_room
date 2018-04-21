package com.information;

import java.io.Serializable;

public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String password;
	
	public Account(){}
	public Account(String id, String password){
			this.id = id;
			this.password = password;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPW(){
		return password;
	}
}
