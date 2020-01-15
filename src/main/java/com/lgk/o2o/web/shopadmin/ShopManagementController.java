package com.lgk.o2o.web.shopadmin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.portable.OutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgk.o2o.dto.ShopExecution;
import com.lgk.o2o.entity.Area;
import com.lgk.o2o.entity.PersonInfo;
import com.lgk.o2o.entity.Shop;
import com.lgk.o2o.entity.ShopCategory;
import com.lgk.o2o.enums.ShopStateEnum;
import com.lgk.o2o.exceptions.ShopOperationException;
import com.lgk.o2o.service.AreaService;
import com.lgk.o2o.service.ShopCategoryService;
import com.lgk.o2o.service.ShopService;
import com.lgk.o2o.util.HttpServletRequestUtil;
import com.lgk.o2o.util.ImageUtil;
import com.lgk.o2o.util.PathUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="/getshopinitinfo",method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		Map<String, Object> modelMap = new HashMap<String,Object>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		List<Area> areaList = new ArrayList<Area>();
		try{
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
		
	}

	@PostMapping("/registershop")
	@ResponseBody
	private Map<String,Object> registerShop(HttpServletRequest request){
		
		Map<String,Object> modelMap = new HashMap<String,Object>();
		
		//1.接收并转化为相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try{
			shop = mapper.readValue(shopStr,Shop.class);
		}catch(Exception e){
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(commonsMultipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
			shopImg = (CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		//2.注册店铺
		if(shop != null && shopImg != null){
			PersonInfo owner = new PersonInfo();
			// Session TODO
			owner.setUserId(1L);
			shop.setOwner(owner);
			ShopExecution se;
			try {
				se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
				if(se.getState() == ShopStateEnum.CHECK.getState()){
					modelMap.put("success", true);
				}else{
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			}catch(ShopOperationException e){
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}		
			return modelMap;
		}else{
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	
	/**
	 * 将 CommonsMultipartFile 类型转换为 File 类型
	 * @param ins
	 * @param file
	 */
//	private static void inputStreamToFile(InputStream ins,File file){
//		FileOutputStream os = null;
//		try{
//			os = new FileOutputStream(file);
//			int bytesRead = 0;
//			byte[] buffer = new byte[1024];
//			while((bytesRead = ins.read(buffer)) != -1){
//				os.write(buffer,0,bytesRead);
//			}
//		}catch(Exception e){
//			throw new RuntimeException("调用 inputStreamToFile 产生异常：" + e.getMessage());
//		}finally{
//			try{
//				if(os != null){
//					os.close();
//				}
//				if(ins != null){
//					ins.close();
//				}
//			}catch(IOException e){
//				throw new RuntimeException(" inputStreamToFile 关闭io产生异常：" + e.getMessage());
//			}
//		}
//	}
}
