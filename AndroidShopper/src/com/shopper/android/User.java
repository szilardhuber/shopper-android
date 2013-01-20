package com.shopper.android;

public class User {

	private String email;
	private String password;
	
	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public boolean isValidEmail() {
		if (email == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	    }
	}
	
	public boolean isValidPassword() {
		if (password == null) {
	        return false;
	    }
		return true; // TODO: implement this
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + "]";
	}
	
	
}
