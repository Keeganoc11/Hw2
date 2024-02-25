package com.packt.cardatabase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.packt.cardatabase.domain.Owner;
import com.packt.cardatabase.domain.OwnerRepository;

@RestController
public class OwnerController {
    private final OwnerRepository repository;

    public OwnerController(OwnerRepository repository) {
        this.repository = repository;
    }

    // GET endpoint to list all owners
    @GetMapping("/owners")
    public Iterable<Owner> getOwners() {
        return repository.findAll();
    }

    // POST endpoint to add a new owner
    @PostMapping("/owners")
    public ResponseEntity<Owner> addOwner(@RequestBody Owner owner) {
        Owner savedOwner = repository.save(owner);
        return new ResponseEntity<>(savedOwner, HttpStatus.CREATED);
    }

    // PUT endpoint to update an existing owner
    @PutMapping("/owners/{ownerid}")
    public ResponseEntity<Owner> updateOwner(@RequestBody Owner newOwner, @PathVariable Long ownerid) {
        Owner updatedOwner = repository.findById(ownerid)
                .map(owner -> {
                    owner.setFirstname(newOwner.getFirstname());
                    owner.setLastname(newOwner.getLastname());
                    return repository.save(owner);
                })
                .orElseGet(() -> {
                    newOwner.setOwnerid(ownerid);
                    return repository.save(newOwner);
                });
        return new ResponseEntity<>(updatedOwner, HttpStatus.OK);
    }

    // DELETE endpoint to delete an existing owner
    @DeleteMapping("/owners/{ownerid}")
    public ResponseEntity<?> deleteOwner(@PathVariable Long ownerid) {
        repository.deleteById(ownerid);
        return ResponseEntity.noContent().build();
    }
}

