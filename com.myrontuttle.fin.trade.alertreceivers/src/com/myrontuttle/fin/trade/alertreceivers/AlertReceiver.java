package com.myrontuttle.fin.trade.alertreceivers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;

/**
 * A record of the receiver used to receive alerts for a user
 * @author Myron Tuttle
 */
@Entity(name = "ALERT_RECEIVERS")
public class AlertReceiver implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "RECEIVER_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String receiverId;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "RECEIVER_TYPE")
	private String receiverType;
	
	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="NAME")
    @Column(name="VALUE")
    @CollectionTable(
    		name="RECEIVER_PARAMETERS", 
    		joinColumns=@JoinColumn(name="RECEIVER_ID"))
    private Map<String, String> parameters = new HashMap<String, String>();
	
	@Column(name = "Active")
	private boolean active;
	
	public AlertReceiver() {}
	
	public AlertReceiver(String userId, String receiverType) {
		this.userId = userId;
		this.receiverType = receiverType;
	}

	public void addParameter(String name, String value) {
		parameters.put(name, value);
	}
	
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	public void removeParameter(String name) {
		parameters.remove(name);
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
