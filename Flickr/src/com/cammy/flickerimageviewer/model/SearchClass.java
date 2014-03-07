package com.cammy.flickerimageviewer.model;

import com.google.gson.annotations.SerializedName;
/**
 * Model class for json result
 * @author Xiao
 *
 */
public class SearchClass {
	@SerializedName("title")
	private String title;

	@SerializedName("link")
	private String link;

	@SerializedName("description")
	private String description;

	@SerializedName("modified")
	private String modified;

	@SerializedName("generator")
	private String generator;

	@SerializedName("items")
	private ImageResult[] items;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public ImageResult[] getItems() {
		return items;
	}

	public void setItems(ImageResult[] items) {
		this.items = items;
	}

}
