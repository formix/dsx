package org.formix.dsx.builders;

import org.formix.dsx.builders.XmlExplicitNull;

public class Department {

	private String name;
	private String description;
	private Object tag;
	
	public Department() {
		this.name = "";
		this.description = null;
		this.tag = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlExplicitNull
	public String getDescription() {
		return description;
	}

	@XmlExplicitNull
	public void setDescription(String description) {
		this.description = description;
	}

	@XmlExplicitNull
	public Object getTag() {
		return tag;
	}

	@XmlExplicitNull
	public void setTag(Object tag) {
		this.tag = tag;
	}

}
