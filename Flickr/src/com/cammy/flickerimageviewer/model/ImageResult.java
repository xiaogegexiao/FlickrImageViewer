package com.cammy.flickerimageviewer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for every image
 * @author Xiao
 *
 */
public class ImageResult {
	@SerializedName("title")
	private String title;

	@SerializedName("link")
	private String link;

	@SerializedName("media")
	private Media media;

	@SerializedName("date_taken")
	private String takendateline;

	@SerializedName("description")
	private String description;

	@SerializedName("published")
	private String publisheddateline;

	@SerializedName("author")
	private String author;

	@SerializedName("author_id")
	private String authorId;

	@SerializedName("tags")
	private String Tags;

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

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public String getTakendateline() {
		return takendateline;
	}

	public void setTakendateline(String takendateline) {
		this.takendateline = takendateline;
	}

	public String getPublisheddateline() {
		return publisheddateline;
	}

	public void setPublisheddateline(String publisheddateline) {
		this.publisheddateline = publisheddateline;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getTags() {
		return Tags;
	}

	public void setTags(String tags) {
		Tags = tags;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		ImageResult.sdf = sdf;
	}

	/**
	 * the format of date used in the json result
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");

	/**
	 * convert time from format string to long
	 * 
	 * @param dateline
	 * @return
	 */
	private static long getDateTimeLongValue(String dateline) {
		try {
			Date date = sdf.parse(dateline);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * convert time from long to format string
	 * 
	 * @param dateline
	 * @return
	 */
	private static String getDateTimeStringValue(long dateline) {
		try {
			Date date = new Date(dateline);
			String datestr = sdf.format(date);
			return datestr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public class Media {
		@SerializedName("m")
		private String media_url;

		public String getMedia_url() {
			return media_url;
		}

		public void setMedia_url(String media_url) {
			this.media_url = media_url;
		}
	}
}
