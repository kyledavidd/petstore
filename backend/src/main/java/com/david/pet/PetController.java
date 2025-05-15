package com.david.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping(path = "/david")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    // Get all pets
    @GetMapping("/pets")
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    // Get pet by ID
    @GetMapping("/pets/{id}")
    public ResponseEntity<?> getPetById(@PathVariable Integer id) {
        Optional<Pet> pet = petRepository.findById(id);
        if (pet.isPresent()) {
            return ResponseEntity.ok(pet.get());
        } else {
            return ResponseEntity.ok(Collections.emptyMap());
        }
    }

    // Add new pet (JSON Body)
    @PostMapping("/pets")
    public Pet addNewPet(@RequestBody Pet pet) {
        return petRepository.save(pet);
    }

    // Update pet by ID
    @PutMapping("/pets/{id}")
    public ResponseEntity<?> updatePet(@PathVariable Integer id, @RequestBody Pet petDetails) {
        Optional<Pet> optionalPet = petRepository.findById(id);
        if (optionalPet.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet with ID " + id + " not found.");
        }

        Pet pet = optionalPet.get();
        pet.setName(petDetails.getName());
        pet.setSpecies(petDetails.getSpecies());
        pet.setBreed(petDetails.getBreed());
        pet.setGender(petDetails.getGender());
        pet.setImage(petDetails.getImage());
        pet.setDescription(petDetails.getDescription());
        pet.setPrice(petDetails.getPrice());

        petRepository.save(pet);
        return ResponseEntity.ok("Pet with id " + id + " updated.");
    }

    // Delete pet by ID
    @DeleteMapping("/pets/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Integer id) {
        if (!petRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet with ID " + id + " not found.");
        }
        petRepository.deleteById(id);
        return ResponseEntity.ok("Pet with id " + id + " deleted.");
    }

    // Search pets by keyword in name, species, breed, gender, and description
    @GetMapping("/pets/search/{key}")
    public List<Pet> searchPets(@PathVariable String key) {
        return petRepository.findAll().stream().filter(pet ->
                        (pet.getName() + pet.getSpecies() + pet.getBreed() + pet.getGender() + pet.getDescription())
                                .toLowerCase().contains(key.toLowerCase()))
                .toList();
    }

    // Retrieve all pets whose price is lower than or equal to the given price
    @GetMapping("/pets/search/price/{price}")
    public List<Pet> getPetsByPrice(@PathVariable Double price) {
        return petRepository.findByPriceLessThanEqual(price);
    }

    // Bulk Add Pets (JSON Body with List of Pets)
    @PostMapping("/pets/bulk")
    public ResponseEntity<?> addBulkPets(@RequestBody List<Pet> pets) {
        if (pets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No pets to add.");
        }
        List<Pet> savedPets = petRepository.saveAll(pets);
        return ResponseEntity.ok(savedPets);
    }
}