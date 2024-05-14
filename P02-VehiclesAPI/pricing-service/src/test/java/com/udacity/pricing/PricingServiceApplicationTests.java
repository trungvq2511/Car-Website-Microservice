package com.udacity.pricing;

import com.udacity.pricing.entity.Price;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testGetPriceById() {
		int existId = 1;
		ResponseEntity<Price> response =
				restTemplate.getForEntity("http://localhost:8082/prices/" + existId, Price.class);
		MatcherAssert.assertThat(response.getStatusCode(), CoreMatchers.equalTo(HttpStatus.OK));

		int notExistId = 999;
		ResponseEntity<Price> response2 =
				restTemplate.getForEntity("http://localhost:8082/prices/" + notExistId, Price.class);
		MatcherAssert.assertThat(response2.getStatusCode(), CoreMatchers.equalTo(HttpStatus.NOT_FOUND));

	}

}
