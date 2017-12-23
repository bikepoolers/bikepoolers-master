package com.timepass.app.bikepoolerz.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Embeddable
public class Combo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9193086529358721200L;
	
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "COMBO_ID")
	private Long comboId;
	
	@Column(name = "COMBO_NAME")
	private String comboName;

	@JoinColumn(name = "CATEGORY_ID")
	private Category category;
	
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getComboId() {
		return comboId;
	}

	public void setFoodId(Long comboId) {
		this.comboId = comboId;
	}

	public String getComboName() {
		return comboName;
	}

	public void setComboName(String comboName) {
		this.comboName = comboName;
	}


		
	
}
