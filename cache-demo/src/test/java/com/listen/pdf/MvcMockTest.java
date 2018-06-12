package com.listen.pdf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.listen.SpringBootDemo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemo.class)
@WebAppConfiguration
public class MvcMockTest {

	@Autowired
	private WebApplicationContext webContext;

	private MockMvc mockMvc;

	@Before
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}

	@Test
	public void homePage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/listen/mock"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Hello World"));
//				.andExpect(
//						MockMvcResultMatchers.model().attributeExists("books"))
//				.andExpect(
//						MockMvcResultMatchers.model().attribute("books",
//								Matchers.is(Matchers.empty())));
	}
}
