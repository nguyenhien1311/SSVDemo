package com.example.demo.controller;

import com.example.demo.constant.ResponseCode;
import com.example.demo.response.ResponseBody;
import com.example.demo.response.RootResponse;

public class AppController {

	protected RootResponse sucess(ResponseBody body) {
		return RootResponse.builder().code(ResponseCode.OK).body(body).build();
	}
	
}
