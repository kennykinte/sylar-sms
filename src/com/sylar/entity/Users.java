package com.sylar.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "users")
public class Users {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "username")
	private String username;

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "groupid")
	private int groupid;

	@Column(name = "sex")
	private short sex;

	@Column(name = "note")
	private String note;
	
	// 内存外显
	@Transient
	private String groupText;
	
	@Transient
	private String sexText;

	// Getter,Setter----------------------
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setSex(short sex) {
		this.sex = sex;
	}

	public short getSex() {
		return sex;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return note;
	}
	
	public String getGroupText() {
		return groupText;
	}

	public void setGroupText(String groupText) {
		this.groupText = groupText;
	}

	public String getSexText() {
		return sexText;
	}

	public void setSexText(String sexText) {
		this.sexText = sexText;
	}

	// toString----------------------
	@Override
	public String toString() {
		return getId() + "-" + getUsername() + "-" + getTelephone() + "-"
				+ getSex() + "-" + getGroupid() + "-" + getNote();
	}
}
