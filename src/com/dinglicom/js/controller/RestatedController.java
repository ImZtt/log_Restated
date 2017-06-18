package com.dinglicom.js.controller;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dinglicom.js.service.RestatedService;

@Controller
public class RestatedController {
	private static final Log LOG = LogFactory.getLog(RestatedController.class);
	@Autowired
	private RestatedService service;

	@RequestMapping(value = "queryAll", method = {RequestMethod.POST,RequestMethod.GET})
	public void RequestLog(HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (request.getMethod() == null || !request.getMethod().equalsIgnoreCase("post")) {
			return;
		}
//		   BufferedReader br = null;
//			br = new BufferedReader(new InputStreamReader(
//					 request.getInputStream()));
//			String line = null;
//			StringBuilder sb = new StringBuilder();
//			while ((line = br.readLine()) != null) {
//				sb.append(line);
//			}
//			String message = sb.toString();
		try {
			ServletInputStream inputStream = request.getInputStream();
			if(inputStream == null ) return;
			String rtn = service.ResquestService(inputStream);
//			String rtn = service.ResquestService(message);
			LOG.error("response:" + rtn);
			response.getOutputStream().write(rtn.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

	
	
//	@RequestMapping(value="/index/{par1}/{par2}",method = {RequestMethod.POST,RequestMethod.GET})
//	public void getMessage(@PathVariable("par1") String par1,@PathVariable("par2") String par2){
//	     System.out.println("-------------》"+par1);
//	     System.out.println("------------》"+par2);
//	}

	}
}
