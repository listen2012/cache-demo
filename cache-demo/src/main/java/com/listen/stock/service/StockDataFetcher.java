package com.listen.stock.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.listen.pub.JacksonUtils;
import com.listen.stock.menu.input.StockDataInput;
import com.listen.stock.menu.input.StockMenuDetailInput;

@SpringBootApplication
// @RequestMapping("/listen")
public class StockDataFetcher {
	final static Logger logger = LoggerFactory
			.getLogger(StockDataFetcher.class);
	private String url;
	private RestTemplate rtemplate = new RestTemplate();

	public void loadCurrentData() {
		ResponseEntity<String> responseEntity = rtemplate.getForEntity(url,
				String.class);
		logger.info("stock menu response --------> {}",
				responseEntity.getBody());
		StockDataInput stockData = (StockDataInput) JacksonUtils
				.getObjectFromJson(responseEntity.getBody(),
						StockDataInput.class);
		List<StockMenuDetailInput> stocks = stockData.getResult().getData();
		//TODO call email service
	}

}
