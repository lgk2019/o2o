package com.lgk.o2o.service;

import java.util.List;

import com.lgk.o2o.entity.ShopCategory;

public interface ShopCategoryService {

	/**
	 * 
	 * @param ShopCategoryCondition
	 * @return
	 */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
