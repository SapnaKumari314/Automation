package main.java.com.proterra.APIPojo;

import java.util.List;

public class GetBusDetailsPojo {

	private String id;
	private List<String> busVins = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getBusVins() {
		return busVins;
	}

	public void setBusVins(List<String> busVins) {
		this.busVins = busVins;
	}

}
