package com.cesul.barber.services;

import com.cesul.barber.dtos.UserCreationDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {

    private final List<UserCreationDTO> users = new ArrayList<>();

    public UserCreationDTO create(UserCreationDTO user) {
        user.setId(UUID.randomUUID());
        this.users.add(user);
        return user;
    }


    public List<UserCreationDTO> findAll() {
        return this.users;
    }


    public Optional<UserCreationDTO> findById(UUID id) {
        return this.users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }


    public Optional<UserCreationDTO> update(UUID id, UserCreationDTO updatedUser) {

        Optional<UserCreationDTO> userOptional = findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        UserCreationDTO existingUser = userOptional.get();

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        return Optional.of(existingUser);
    }


    public Optional<UserCreationDTO> updateAvatar(UUID id, Map<String, Object> request) {

        Optional<UserCreationDTO> userOptional = findById(id);

        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        UserCreationDTO existingUser = userOptional.get();

        if (request.containsKey("avatarUrl")) {
            Object avatarUrl = request.get("avatarUrl");

            if (avatarUrl instanceof String) {
                existingUser.setAvatarUrl((String) avatarUrl);
            } else {
                throw new IllegalArgumentException(
                        "avatarUrl precisa ser uma String"
                );
            }
        }

        return Optional.of(existingUser);
    }


    public boolean delete(UUID id) {
        return this.users.removeIf(user -> user.getId().equals(id));
    }


    public boolean clear() {
        this.users.clear();
        return true;
    }
}