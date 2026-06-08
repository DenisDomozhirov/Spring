package com.example.SpringWebDemo.controller;

import com.example.SpringWebDemo.Service.PetService;
import com.example.SpringWebDemo.model.Pet;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    private PetService petService;

    public PetController(PetService petService){this.petService = petService;}


    @PostMapping("/createPet")
    public ResponseEntity<Pet> createPet(
            @RequestBody @Valid Pet petToCreate
    ){
      log.info("Post request is create = {}", petToCreate);
      var petCreate = petService.createNewPet(petToCreate);
      return ResponseEntity
              .status(HttpStatus.CREATED)
              .body(petCreate);
    }

    @GetMapping("/showAllPets")
    public List<Pet> showAllPets(
            @RequestParam(value = "pet-name", required = false) String petName
    ){
        log.info("Get request for pet");
        return petService.showAllPets(petName);
    }
    @GetMapping("/showPetsByid/{id}")
    public ResponseEntity<Pet> showPetsById(
            @PathVariable("id") Long id
    ){
        log.info("Get request is create");
        petService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping("/updatePets/{id}")
    public ResponseEntity<Pet> updateSomePet(
            @PathVariable Long id,
            @RequestBody @Valid Pet updatePet
    ){
        log.info("Put request is create");
        var petUpdate = petService.updateSomePets(id, updatePet);
        return ResponseEntity
                .status(HttpStatus.UPGRADE_REQUIRED)
                .body(petUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSomePet(
            @PathVariable Long id
    ){
        log.info("Delete request is done");
        petService.deletePet(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


}
