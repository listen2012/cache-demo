package com.listen.stock.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StockTaskExecutor {

	@Scheduled(fixedRate = 5000)
	public void doHighFre() {
		// something that should execute periodically
		System.out.println("sheng.li...test");
	}

	@Scheduled(cron = "0 0 15 * * ?")
	public void doDayly() {
		// something that should execute periodically
		System.out.println("sheng.li...test");
	}

}
