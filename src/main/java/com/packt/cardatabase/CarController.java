package com.packt.cardatabase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.packt.cardatabase.domain.Car;
import com.packt.cardatabase.domain.CarRepository;

@RestController
public class CarController {
	private final CarRepository repository;

	public CarController(CarRepository repository) {
		this.repository = repository;
	}

	// Existing GET endpoint
	@GetMapping("/cars")
	public Iterable<Car> getCars() {
		return repository.findAll();
	}

	// POST endpoint to add a new car
	@PostMapping("/cars")
	public ResponseEntity<Car> addCar(@RequestBody Car car) {
		Car savedCar = repository.save(car);
		return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
	}

	// PUT endpoint to update an existing car
	@PutMapping("/cars/{id}")
	public ResponseEntity<Car> updateCar(@RequestBody Car newCar, @PathVariable Long id) {
		Car updatedCar = repository.findById(id)
				.map(car -> {
					car.setBrand(newCar.getBrand());
					car.setModel(newCar.getModel());
					car.setColor(newCar.getColor());
					car.setRegistrationNumber(newCar.getRegistrationNumber());
					car.setModelYear(newCar.getModelYear());
					car.setPrice(newCar.getPrice());
					return repository.save(car);
				})
				.orElseGet(() -> {
					newCar.setId(id);
					return repository.save(newCar);
				});
		return new ResponseEntity<>(updatedCar, HttpStatus.OK);
	}

	// DELETE endpoint to delete an existing car
	@DeleteMapping("/cars/{id}")
	public ResponseEntity<?> deleteCar(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
