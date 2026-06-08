package com.example.SpringWebDemo.model;

import jakarta.validation.constraints.*;

import java.util.List;

public class User {
    @Null
    private Long id;
    @Size(max = 10)
    private String name;
    @Email
    private String email;
    @Min(0)
    @Max(value = 200)
    @NotNull
    private Integer age;
    private List<Pet> pets;

    public User(Long id, String name, String email, Integer age, List<Pet> pets) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.pets = pets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
