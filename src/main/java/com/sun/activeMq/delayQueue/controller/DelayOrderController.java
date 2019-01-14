package com.sun.activeMq.delayQueue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.activeMq.delayQueue.service.SaveDelayOrder;

@Controller
public class DelayOrderController {

	@Autowired
	private SaveDelayOrder saveDelayOrder;

    @RequestMapping("/submitOrder")
    public String saveUser(){
    	saveDelayOrder.saveDelayOrder(3);
    	return null;
    }
}
