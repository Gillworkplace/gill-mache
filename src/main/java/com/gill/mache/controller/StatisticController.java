package com.gill.mache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gill.mache.statistic.InnerStatistics;

/**
 * StatisticController
 *
 * @author gill
 * @version 2023/09/21
 **/
@RestController
@RequestMapping("/statistics")
public class StatisticController {

	@Autowired
	private InnerStatistics innerStatistics;

	@GetMapping("info")
	public String info() {
		return innerStatistics.println();
	}

}
