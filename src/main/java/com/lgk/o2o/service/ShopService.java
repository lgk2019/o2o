package com.lgk.o2o.service;

import java.io.File;

import com.lgk.o2o.dto.ShopExecution;
import com.lgk.o2o.entity.Shop;

public interface ShopService {

	ShopExecution addShop(Shop shop,File shopImg);
}
