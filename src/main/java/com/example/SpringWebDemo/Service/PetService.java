package com.example.SpringWebDemo.Service;

import com.example.SpringWebDemo.model.Pet;
import com.example.SpringWebDemo.model.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PetService {

    private Long idCounter;
    private final HashMap<Long, Pet> petHashMap;
    private UserService userService;

    public PetService(@Lazy UserService userService) {
        this.petHashMap = new HashMap<>();
        this.idCounter = 0L;
        this.userService = userService;
    }

    public Pet createNewPet(
            Pet createPet
    ){

        User owner = userService.findUserById(createPet.getUserId());

        var newPetId = ++idCounter;
        var newPet = new Pet(
                idCounter,
                createPet.getName(),
                createPet.getUserId()
        );
        petHashMap.put(newPetId, newPet);
        if(owner.getPets() == null) owner.setPets(new ArrayList<>());
        owner.getPets().add(newPet);
        return newPet;
    }

    public Pet createPetForUser(Long userId, String petName) {
        Pet pet = new Pet(null, petName, userId);
        return createNewPet(pet);
    }

    public List<Pet> showAllPets(
            String petName
    ){
        return petHashMap.values()
                .stream()
                .filter(pet -> petName == null || pet.getName().equals(petName))
                .toList();
    }

    public Pet updateSomePets(
            Long id,
            Pet petToUpdate
    ){
        var updatePet = new Pet(
                id,
                petToUpdate.getName(),
                petToUpdate.getUserId()
        );
        petHashMap.put(id, updatePet);
        return updatePet;
    }

    public void deletePet(
            Long id
    ){
        Pet petToDelete = petHashMap.get(id);
        if(id == null) throw  new NoSuchElementException(
                "No such user with this id: %s".formatted(id)
        );

        Long userId = petToDelete.getUserId();

        var result = petHashMap.remove(id);

        User ownerPet = userService.findUserById(userId);
        if(ownerPet.getPets() != null){
            ownerPet.getPets().removeIf(pet -> pet.getId().equals(id));
        }

        if(result == null) throw new NoSuchElementException(
                "No such user with this id: %s".formatted(id)
        );
    }

    public Pet findById(
            Long id
    ){
        return Optional.ofNullable(petHashMap.get(id)).orElseThrow(() -> new NoSuchElementException(
                "No such user with this id: %s".formatted(id)
        ));
    }



}
