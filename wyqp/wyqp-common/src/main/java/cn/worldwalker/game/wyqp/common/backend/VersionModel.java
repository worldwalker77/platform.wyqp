package cn.worldwalker.game.wyqp.common.backend;

import java.util.Date;

public class VersionModel {
	
	private Integer id;
	
	private String updateUrl;
	
	private String codeUrl;
	
	private String newFeature;
	
	private String clientVersion;
	
	private Date createTime;
	
	private Date updateTime;
	
	private int start = 0;
	
	private int limit = 10;

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}

	public String getNewFeature() {
		return newFeature;
	}

	public void setNewFeature(String newFeature) {
		this.newFeature = newFeature;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
	
}
