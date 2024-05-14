package com.udacity.pricing;

import com.udacity.pricing.entity.Price;
import com.udacity.pricing.repository.PriceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Creates a Spring Boot Application to run the Pricing Service.
 * TODO: Convert the application from a REST API to a microservice.
 */
@SpringBootApplication
@EnableEurekaClient
public class PricingServiceApplication {
private PriceRepository priceRepository;

    public PricingServiceApplication(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(PricingServiceApplication.class, args);
    }
    @Bean
    public CommandLineRunner run() throws Exception {
        return args -> {
            //add Price data to DB
            List<Price> priceList = new ArrayList<>();
            for (long i = 1; i <= 20; i++) {
                Price price = new Price("USD", randomPrice(), i);
                priceList.add(price);
            }
            priceRepository.saveAll(priceList);
        };
    }

    /**
     * Holds {ID: Price} pairings (current implementation allows for 20 vehicles)
     */
    private static final Map<Long, Price> PRICES = LongStream
            .range(1, 20)
            .mapToObj(i -> new Price("USD", randomPrice(), i))
            .collect(Collectors.toMap(Price::getVehicleId, p -> p));

    /**
     * Gets a random price to fill in for a given vehicle ID.
     * @return random price for a vehicle
     */
    private static BigDecimal randomPrice() {
        return new BigDecimal(ThreadLocalRandom.current().nextDouble(1, 5))
                .multiply(new BigDecimal(5000d)).setScale(2, RoundingMode.HALF_UP);
    }
}
