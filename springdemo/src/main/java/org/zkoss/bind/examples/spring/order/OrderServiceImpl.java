/* FakeSearchService.java

	Purpose:
		
	Description:
		
	History:
		2011/10/25 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.bind.examples.spring.order;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.lang.Strings;

/**
 * @author Hawk
 * 
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderDao orderDao;

	public List<Order> list() {
		return orderDao.findAll();
	}

	public void delete(Order order) {
			orderDao.remove(order);
	}

	public void save(Order order) {
		if (order.getId()==null){
			orderDao.newOrder(order);
		}else{
			orderDao.save(order);
		}
	}

}
