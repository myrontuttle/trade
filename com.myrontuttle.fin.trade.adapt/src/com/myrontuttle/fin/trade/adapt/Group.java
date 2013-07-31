package com.myrontuttle.fin.trade.adapt;

import java.util.ArrayList;

import javax.persistence.*;

/**
 * A group of trade strategy candidates which are generated with common settings
 * @author Myron Tuttle
 */
@Entity(name = "Groups")
public class Group {

	@Id
	@Column(name = "GroupId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String groupId;
	
	@OneToMany(mappedBy = "group", targetEntity = Candidate.class, fetch = FetchType.EAGER)
	private ArrayList<Candidate> candidates;
	
	@Column(name = "AlertAddress")
	private String alertAddress;
	

	public String getGroupId() {
		return groupId;
	}	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
    public String getAlertAddress() {
		return alertAddress;
	}
	public void setAlertAddress(String alertAddress) {
		this.alertAddress = alertAddress;
	}
}
