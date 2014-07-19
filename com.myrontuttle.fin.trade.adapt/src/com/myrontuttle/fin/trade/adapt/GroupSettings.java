package com.myrontuttle.fin.trade.adapt;

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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;

/**
 * Settings for a group
 * @author Myron Tuttle
 */
@Entity(name = "GROUP_SETTINGS")
public class GroupSettings implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SETTINGS_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long settingsId;

	@Column(name = "GROUP_ID")
	private long groupId;

	@OneToOne
	@JoinColumn(name = "GROUP_ID", referencedColumnName = "GROUP_ID")
	private Group group;

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="NAME")
    @Column(name="VALUE")
    @CollectionTable(
    		name="BOOLEAN_SETTINGS", 
    		joinColumns=@JoinColumn(name="SETTINGS_ID"))
    private Map<String, Boolean> booleanSettings = new HashMap<String, Boolean>();

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="NAME")
    @Column(name="VALUE")
    @CollectionTable(
    		name="INTEGER_SETTINGS", 
    		joinColumns=@JoinColumn(name="SETTINGS_ID"))
    private Map<String, Integer> integerSettings = new HashMap<String, Integer>();

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="NAME")
    @Column(name="VALUE")
    @CollectionTable(
    		name="STRING_SETTINGS", 
    		joinColumns=@JoinColumn(name="SETTINGS_ID"))
    private Map<String, Long> longSettings = new HashMap<String, Long>();

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="NAME")
    @Column(name="VALUE")
    @CollectionTable(
    		name="DOUBLE_SETTINGS", 
    		joinColumns=@JoinColumn(name="SETTINGS_ID"))
    private Map<String, Double> doubleSettings = new HashMap<String, Double>();

	@ElementCollection(fetch=FetchType.EAGER)
    @MapKeyColumn(name="NAME")
    @Column(name="VALUE")
    @CollectionTable(
    		name="STRING_SETTINGS", 
    		joinColumns=@JoinColumn(name="SETTINGS_ID"))
    private Map<String, String> stringSettings = new HashMap<String, String>();
	
	public GroupSettings() {}

	public long getSettingsId() {
		return settingsId;
	}

	public void setSettingsId(long settingsId) {
		this.settingsId = settingsId;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Map<String, Boolean> getBooleanSettings() {
		return booleanSettings;
	}

	public void setBooleanSettings(Map<String, Boolean> booleanSettings) {
		this.booleanSettings = booleanSettings;
	}
	
	public boolean getBooleanValue(String key) {
		return booleanSettings.get(key);
	}
	
	public void setBooleanValue(String key, boolean value) {
		booleanSettings.put(key, value);
	}
	
	public void removeBooleanValue(String key) {
		booleanSettings.remove(key);
	}

	public Map<String, Integer> getIntegerSettings() {
		return integerSettings;
	}

	public void setIntegerSettings(Map<String, Integer> integerSettings) {
		this.integerSettings = integerSettings;
	}

	public int getIntegerValue(String key) {
		return integerSettings.get(key);
	}
	
	public void setIntegerValue(String key, int value) {
		integerSettings.put(key, value);
	}
	
	public void removeIntegerValue(String key) {
		integerSettings.remove(key);
	}

	public Map<String, Long> getLongSettings() {
		return longSettings;
	}

	public void setLongSettings(Map<String, Long> longSettings) {
		this.longSettings = longSettings;
	}

	public long getLongValue(String key) {
		return longSettings.get(key);
	}
	
	public void setLongValue(String key, long value) {
		longSettings.put(key, value);
	}
	
	public void removeLongValue(String key) {
		longSettings.remove(key);
	}

	public Map<String, Double> getDoubleSettings() {
		return doubleSettings;
	}

	public void setDoubleSettings(Map<String, Double> doubleSettings) {
		this.doubleSettings = doubleSettings;
	}

	public double getDoubleValue(String key) {
		return doubleSettings.get(key);
	}
	
	public void setDoubleValue(String key, double value) {
		doubleSettings.put(key, value);
	}
	
	public void removeDoubleValue(String key) {
		doubleSettings.remove(key);
	}

	public Map<String, String> getStringSettings() {
		return stringSettings;
	}

	public void setStringSettings(Map<String, String> stringSettings) {
		this.stringSettings = stringSettings;
	}
	
	public String getStringValue(String key) {
		return stringSettings.get(key);
	}
	
	public void setStringValue(String key, String value) {
		stringSettings.put(key, value);
	}
	
	public void removeStringValue(String key) {
		stringSettings.remove(key);
	}
	
}
