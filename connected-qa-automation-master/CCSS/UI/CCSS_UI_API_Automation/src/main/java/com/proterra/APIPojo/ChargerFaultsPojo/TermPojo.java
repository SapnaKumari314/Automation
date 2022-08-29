package main.java.com.proterra.APIPojo.ChargerFaultsPojo;

import org.json.JSONPropertyName;

public class TermPojo {

	
	private String requestStatus;

	@JSONPropertyName("request.status")
	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

}
