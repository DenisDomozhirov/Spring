package com.example.SpringWebDemo.Service;

import com.example.SpringWebDemo.model.Pet;
import com.example.SpringWebDemo.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private Long idCounter;
    private final HashMap<Long, User> userHashMap;
    private PetService petService;

    public UserService(PetService petService) {
        this.idCounter = 0L;
        this.userHashMap = new HashMap<>();
        this.petService = petService;
    }

    public User createNewUser(
            User userToCreate
    ) {
        var newUserId = ++idCounter;
        var newUser = new User(
                newUserId,
                userToCreate.getName(),
                userToCreate.getEmail(),
                userToCreate.getAge(),
                new ArrayList<>()
        );

        userHashMap.put(idCounter, newUser);

        if(userToCreate.getPets() != null){
            for(Pet pet : userToCreate.getPets()){
                Pet createPet = petService.createPetForUser(newUserId, pet.getName());
                //newUser.getPets().add(createPet);
            }
        }
        return newUser;
    }

    public List<User> showAllUsers(
            String userName,
            Integer userAge
    ){
        return userHashMap.values()
                .stream()
                .filter(user -> userName == null || user.getName().equals(userName))
                .filter(user -> userAge == null || user.getAge() < userAge)
                .toList();
    }

    public User findUserById(
            Long id
    ){
        return Optional.ofNullable(userHashMap.get(id))
                .orElseThrow(() -> new NoSuchElementException(
                        "No such user with this id: %s".formatted(id)
                ));
    }

    public void deleteUserById(
            Long id
    ){
        var result = userHashMap.remove(id);
        if(result == null) throw new NoSuchElementException(
                "No such user with this id: %s".formatted(id)
        );
    }

    public User updateUsers(
            Long id,
            User userToUpdate
    ){

        User existingUser = userHashMap.get(id);

        if(existingUser == null) throw new NoSuchElementException(
                "No such user with this id: %s".formatted(id)
        );

        existingUser.setName(userToUpdate.getName());
        existingUser.setEmail(userToUpdate.getEmail());
        existingUser.setAge(userToUpdate.getAge());

        if(userToUpdate.getPets() != null){
            List<Pet> updatedPets = new ArrayList<>();
            for(Pet petFromRequest : userToUpdate.getPets()){
                if(petFromRequest.getId() == null){
                    Pet newPet = petService.createPetForUser(id, petFromRequest.getName());
                    updatedPets.add(newPet);
                } else {
                    Pet existingPet = petService.findById(petFromRequest.getId());
                    existingPet.setName(petFromRequest.getName());
                    if(!existingPet.getUserId().equals(id)) throw new IllegalArgumentException(
                            "Pet %s does not belong to user %s".formatted(petFromRequest.getId(), id)
                    );
                    updatedPets.add(existingPet);
                }
            }
            existingUser.setPets(updatedPets);
        }

        userHashMap.put(id, existingUser);
        return existingUser;
    }
}
