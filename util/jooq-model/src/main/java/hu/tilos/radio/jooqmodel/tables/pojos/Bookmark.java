/**
 * This class is generated by jOOQ
 */
package hu.tilos.radio.jooqmodel.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.2" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Bookmark implements java.io.Serializable {

	private static final long serialVersionUID = -1027552049;

	private java.lang.Integer  id;
	private java.lang.Integer  radioshowId;
	private java.lang.Integer  episodeId;
	private java.lang.Integer  userId;
	private java.sql.Timestamp from;
	private java.sql.Timestamp to;
	private java.sql.Timestamp modified;
	private java.lang.String   title;
	private java.lang.String   content;

	public Bookmark() {}

	public Bookmark(
		java.lang.Integer  id,
		java.lang.Integer  radioshowId,
		java.lang.Integer  episodeId,
		java.lang.Integer  userId,
		java.sql.Timestamp from,
		java.sql.Timestamp to,
		java.sql.Timestamp modified,
		java.lang.String   title,
		java.lang.String   content
	) {
		this.id = id;
		this.radioshowId = radioshowId;
		this.episodeId = episodeId;
		this.userId = userId;
		this.from = from;
		this.to = to;
		this.modified = modified;
		this.title = title;
		this.content = content;
	}

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getRadioshowId() {
		return this.radioshowId;
	}

	public void setRadioshowId(java.lang.Integer radioshowId) {
		this.radioshowId = radioshowId;
	}

	public java.lang.Integer getEpisodeId() {
		return this.episodeId;
	}

	public void setEpisodeId(java.lang.Integer episodeId) {
		this.episodeId = episodeId;
	}

	public java.lang.Integer getUserId() {
		return this.userId;
	}

	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}

	public java.sql.Timestamp getFrom() {
		return this.from;
	}

	public void setFrom(java.sql.Timestamp from) {
		this.from = from;
	}

	public java.sql.Timestamp getTo() {
		return this.to;
	}

	public void setTo(java.sql.Timestamp to) {
		this.to = to;
	}

	public java.sql.Timestamp getModified() {
		return this.modified;
	}

	public void setModified(java.sql.Timestamp modified) {
		this.modified = modified;
	}

	public java.lang.String getTitle() {
		return this.title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	public java.lang.String getContent() {
		return this.content;
	}

	public void setContent(java.lang.String content) {
		this.content = content;
	}
}