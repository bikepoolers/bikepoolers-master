package com.timepass.app.bikepoolerz.dao;

import java.util.List;
import java.util.Map;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;



public interface CategoryDAO {

	@SqlQuery("Select category_Id,category_Name,FOOD_ID, FOOD_NAME, COMBO_ID from CATEGORY, FOOD_ITEMS where CATEGORY_ID=:categoryId")
	public List<Map<String, Object>> getCategory(@Bind("categoryId") long categoryId);

	
}
