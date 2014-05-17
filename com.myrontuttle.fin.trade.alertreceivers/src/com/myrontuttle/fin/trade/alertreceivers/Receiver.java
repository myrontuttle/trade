package com.myrontuttle.fin.trade.alertreceivers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;

/**
 * A record of the receiver used to receive alerts for a user
 * @author Myron Tuttle
 */
@Entity(name = "Receivers")
public class Receiver implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ReceiverId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String receiverId;

	@Column(name = "UserId")
	private String userId;

	@Column(name = "ReceiverType")
	private String receiverType;
	
	@ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(
    		name="receiver_parameters", 
    		joinColumns=@JoinColumn(name="ReceiverId"))
    private Map<String, String> parameters = new HashMap<String, String>();
	
	public Receiver() {}
	
	public Receiver(String userId, String receiverType) {
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
}
