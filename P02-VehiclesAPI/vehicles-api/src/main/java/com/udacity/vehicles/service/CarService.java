package com.udacity.vehicles.service;

import com.udacity.vehicles.client.prices.Price;
import com.udacity.vehicles.entity.Car;
import com.udacity.vehicles.entity.Location;
import com.udacity.vehicles.exception.CarNotFoundException;
import com.udacity.vehicles.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    RestTemplate restTemplate = new RestTemplate();
    private final CarRepository repository;

    public CarService(CarRepository repository) {
        /**
         * TODO: Add the Maps and Pricing Web Clients you create
         *   in `VehiclesApiApplication` as arguments and set them here.
         */
        this.repository = repository;
    }

    /**
     * Gathers a list of all vehicles
     *
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        List<Car> all = repository.findAll();
        all.stream().forEach(car -> {
            //get price
            getCarPrice(car);
            //get location
            getCarLocation(car);
        });

        return all;
    }

    private void getCarPrice(Car car) {
        Price price = restTemplate.getForObject("http://localhost:8082/prices/" + car.getId(), Price.class);
        car.setPrice(price.getFullPrice());
    }

    private void getCarLocation(Car car) {
        Double lat = car.getLocation().getLat();
        Double lon = car.getLocation().getLon();
        Location location = restTemplate.getForObject("http://localhost:9191/maps?lat=" + lat + "&lon=" + lon, Location.class);
        location.setLat(lat);
        location.setLon(lon);
        car.setLocation(location);
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     *
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */
        Optional<Car> byId = repository.findById(id);
        if (byId.isEmpty()) {
            throw new CarNotFoundException("Car ID is not found.");
        }
        /**
         * TODO: Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the `id` input'
         * TODO: Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
        Car car = byId.get();
        getCarPrice(car);

        /**
         * TODO: Use the Maps Web client you create in `VehiclesApiApplication`
         *   to get the address for the vehicle. You should access the location
         *   from the car object and feed it to the Maps service.
         * TODO: Set the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */
        getCarLocation(car);

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     *
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        //Update car
        if (car.getId() != null) {
            repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        return repository.save(carToBeUpdated);
                    });
        }

        //Create car
        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     *
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */
        Optional<Car> byId = repository.findById(id);
        if (byId.isEmpty()) {
            throw new CarNotFoundException("Car ID is not found.");
        }

        /**
         * TODO: Delete the car from the repository.
         */
        repository.deleteById(id);

    }
}
