package com.test.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;


	@Test
	void OkStatusMaxPrice()throws Exception {
		this.mockMvc.perform(get("http://localhost:8082/cryptocurrencies/maxprice?name=BTC")).andDo(print()).andExpect(status().isOk());
	}
	@Test
	void OkStatusMinPrice()throws Exception {
		this.mockMvc.perform(get("http://localhost:8082/cryptocurrencies/minprice?name=BTC")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void NotFoundMaxPrice()throws Exception {
		this.mockMvc.perform(get("http://localhost:8082/cryptocurrencies/maxprice?name=NoCurrency")).andDo(print()).andExpect(status().isNotFound()).andExpect(result -> assertEquals("There is no such currency", result.getResponse().getErrorMessage()));
	}
	@Test
	void NotFoundMinPrice()throws Exception {
		this.mockMvc.perform(get("http://localhost:8082/cryptocurrencies/minprice?name=NoCurrency")).andDo(print()).andExpect(status().isNotFound()).andExpect(result -> assertEquals("There is no such currency", result.getResponse().getErrorMessage()));
	}


	@Test
	void OkPages()throws Exception {
		this.mockMvc.perform(get("http://localhost:8082/cryptocurrencies?name=BTC&page=2&size=10")).andDo(print()).andExpect(status().isOk());
	}
	@Test
	void OkPagesOneQParam()throws Exception {
		this.mockMvc.perform(get("http://localhost:8082/cryptocurrencies?name=BTC")).andDo(print()).andExpect(status().isOk());
	}
	@Test
	void NotFoundPages()throws Exception {
		this.mockMvc.perform(get("http://localhost:8082/cryptocurrencies?name=BTC&page=-2&size=10")).andDo(print()).andExpect(status().isNotFound()).andExpect(result -> assertEquals("There is no such page", result.getResponse().getErrorMessage()));
		this.mockMvc.perform(get("http://localhost:8082/cryptocurrencies?name=BTC&page=4000&size=4000")).andDo(print()).andExpect(status().isNotFound()).andExpect(result -> assertEquals("There is no such page", result.getResponse().getErrorMessage()));
	}
	@Test
	void NotFoundPagesCurrency()throws Exception {
		this.mockMvc.perform(get("http://localhost:8082/cryptocurrencies?name=NoCurrency")).andDo(print()).andExpect(status().isNotFound()).andExpect(result -> assertEquals("There is no such currency", result.getResponse().getErrorMessage()));
	}

	@Test
	void CSVCreated()throws Exception {
		this.mockMvc.perform(get("http://localhost:8082/cryptocurrencies/csv")).andDo(print()).andExpect(status().isCreated());
	}

	@Test
	void currencyTest() {
		Currency test = new Currency("test", 2.3);

		String rewrite = "secondTest";
		Double rewriteInt = 11.3;

		test.setCurrencyType(rewrite);
		test.setValue(rewriteInt);

		assertEquals(rewriteInt, test.getValue());
		assertEquals(rewrite, test.getCurrencyType());
	}

}
