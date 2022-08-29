package main.java.com.proterra.APIPojo;

import java.util.List;

public class GetM5BusStatusPojo {

	private String id;
	private List<String> busUnitIds = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getBusUnitIds() {
		return busUnitIds;
	}

	public void setBusUnitIds(List<String> busUnitIds) {
		this.busUnitIds = busUnitIds;
	}

}
