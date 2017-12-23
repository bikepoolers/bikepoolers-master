package com.timepass.app.bikepoolerz.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "FOOD_ITEMS")
public class FoodItems implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9193086529358721200L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "FOOD_ID")
	private Long foodId;
	
	@Column(name = "FOOD_NAME")
	private String foodName;

	@JoinColumn(name = "CATEGORY_ID")
	private Category category;
	
	@EmbeddedId
	private Combo combo;
	
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getFoodId() {
		return foodId;
	}

	public void setFoodId(Long foodId) {
		this.foodId = foodId;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}


		
	
}
