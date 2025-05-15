package com.david.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findByNameIgnoreCaseContainingOrSpeciesIgnoreCaseContainingOrBreedIgnoreCaseContaining(
            String name, String species, String breed);

    List<Pet> findByPriceLessThanEqual(Double price);
}