package com.lgk.o2o.service;

import java.io.InputStream;

import com.lgk.o2o.dto.ShopExecution;
import com.lgk.o2o.entity.Shop;

public interface ShopService {

	ShopExecution addShop(Shop shop,InputStream shopImgInputStream,String fileName);
}
