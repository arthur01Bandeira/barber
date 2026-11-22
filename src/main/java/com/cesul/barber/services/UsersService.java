package com.cesul.barber.services;

import com.cesul.barber.dtos.UserCreationDTO;
import com.cesul.barber.entities.User;
import com.cesul.barber.mappers.UserMapper;
import com.cesul.barber.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UsersService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserCreationDTO create(UserCreationDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserCreationDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<UserCreationDTO> findById(UUID id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    @Transactional
    public Optional<UserCreationDTO> update(UUID id, UserCreationDTO updatedUser) {
        return userRepository.findById(id).map(user -> {
            userMapper.updateEntity(user, updatedUser);
            return userMapper.toDTO(userRepository.save(user));
        });
    }

    @Transactional
    public Optional<UserCreationDTO> updateAvatar(UUID id, Map<String, Object> request) {
        return userRepository.findById(id).map(user -> {
            if (request.containsKey("avatarUrl")) {
                Object avatarUrl = request.get("avatarUrl");

                if (avatarUrl != null && !(avatarUrl instanceof String)) {
                    throw new IllegalArgumentException(
                            "avatarUrl precisa ser uma String"
                    );
                }

                user.setAvatarUrl((String) avatarUrl);
            }

            return userMapper.toDTO(userRepository.save(user));
        });
    }

    public boolean delete(UUID id) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        userRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void clear() {
        userRepository.deleteAll();
    }
}