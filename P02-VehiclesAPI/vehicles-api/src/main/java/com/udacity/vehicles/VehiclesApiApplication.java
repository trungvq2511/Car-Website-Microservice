package com.udacity.vehicles;

import com.udacity.vehicles.entity.Condition;
import com.udacity.vehicles.entity.Location;
import com.udacity.vehicles.entity.Car;
import com.udacity.vehicles.repository.CarRepository;
import com.udacity.vehicles.entity.Details;
import com.udacity.vehicles.entity.Manufacturer;
import com.udacity.vehicles.repository.ManufacturerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

/**
 * Launches a Spring Boot application for the Vehicles API,
 * initializes the car manufacturers in the database,
 * and launches web clients to communicate with maps and pricing.
 */
@SpringBootApplication
@EnableEurekaServer
@EnableJpaAuditing
public class VehiclesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehiclesApiApplication.class, args);
    }

    /**
     * Initializes the car manufacturers available to the Vehicle API.
     *
     * @param manufacturerRepository where the manufacturer information persists.
     * @return the car manufacturers to add to the related manufacturerRepository
     */
    @Bean
    CommandLineRunner initDatabase(ManufacturerRepository manufacturerRepository, CarRepository carRepository) {
        return args -> {
            //init Manufacture database
            manufacturerRepository.save(new Manufacturer(100, "Audi"));
            manufacturerRepository.save(new Manufacturer(101, "Chevrolet"));
            manufacturerRepository.save(new Manufacturer(102, "Ford"));
            manufacturerRepository.save(new Manufacturer(103, "BMW"));
            manufacturerRepository.save(new Manufacturer(104, "Dodge"));

            //init Car database
            carRepository.save(new Car(LocalDateTime.now(), LocalDateTime.now(), Condition.NEW,
                    new Details("Body 1", "Model 1", manufacturerRepository.findById(100).get(), 4, "Fuel Type 1", "Engine 1", 1000, 2021, 2021, "Black"),
                    new Location(1.0, 1.0), "1000 USD"));
            carRepository.save(new Car(LocalDateTime.now(), LocalDateTime.now(), Condition.USED,
                    new Details("Body 2", "Model 2", manufacturerRepository.findById(101).get(), 5, "Fuel Type 2", "Engine 2", 2000, 2022, 2022, "Red"),
                    new Location(2.0, 2.0), "2000 USD"));
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Web Client for the maps (location) API
     *
     * @param endpoint where to communicate for the maps API
     * @return created maps endpoint
     */
    @Bean(name = "maps")
    public WebClient webClientMaps(@Value("${maps.endpoint}") String endpoint) {
        return WebClient.create(endpoint);
    }

    /**
     * Web Client for the pricing API
     *
     * @param endpoint where to communicate for the pricing API
     * @return created pricing endpoint
     */
    @Bean(name = "pricing")
    public WebClient webClientPricing(@Value("${pricing.endpoint}") String endpoint) {
        return WebClient.create(endpoint);
    }

}
