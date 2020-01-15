package com.lgk.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lgk.o2o.entity.ShopCategory;

//public interface ShopCategoryDao {
//
//	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
//}


public interface ShopCategoryDao {
	/**
	 * 根据传入的查询条件返回店铺类别列表
	 * 
	 * @param shopCategoryCondition
	 * @return
	 */
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition")ShopCategory shopCategoryCondition);


}