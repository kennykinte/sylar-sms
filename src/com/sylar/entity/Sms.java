package com.sylar.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sms")
public class Sms {
	@Override
	public String toString() {
		return id + " - " + telephone + " - " + posttime + " - " + smstype + " - " + smscontent;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "posttime")
	private Timestamp posttime;

	@Column(name = "smstype")
	private short smstype;

	@Column(name = "fixed")
	private short fixed;

	@Column(name = "froms")
	private String froms;

	@Column(name = "smscontent")
	private String smscontent;

	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Timestamp getPosttime() {
		return posttime;
	}

	public void setPosttime(Timestamp posttime) {
		this.posttime = posttime;
	}

	public short getSmstype() {
		return smstype;
	}

	public void setSmstype(short smstype) {
		this.smstype = smstype;
	}

	public short getFixed() {
		return fixed;
	}

	public void setFixed(short fixed) {
		this.fixed = fixed;
	}

	public String getFroms() {
		return froms;
	}

	public void setFroms(String froms) {
		this.froms = froms;
	}

	public String getSmscontent() {
		return smscontent;
	}

	public void setSmscontent(String smscontent) {
		this.smscontent = smscontent;
	}
}
