package com.listen.temp;

import java.util.LinkedHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TempTest {

	public String except() throws Exception {
		return "string";
	}

	@Test
	public void homePage() throws Exception {
		LinkedHashMap<?, ?> map = new LinkedHashMap<>();
		AbstractTempTest test = new DefaultTempTest(); 
		map.forEach((k, v) -> {
				try {
					test.except();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		});

	}
}
