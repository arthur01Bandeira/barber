
package com.cesul.barber.controllers;

import com.cesul.barber.dtos.UserCreationDTO;
import com.cesul.barber.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Gerenciamento de usuários da barbearia")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    @Operation(summary = "Listar todos", description = "Retorna a lista de todos os usuários cadastrados.")
    public ResponseEntity<List<UserCreationDTO>> getAll() {
        return ResponseEntity.ok(usersService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Busca um usuário pelo ID.")
    public ResponseEntity<UserCreationDTO> getById(@PathVariable UUID id) {
        return usersService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza o usuário via ID.")
    public ResponseEntity<UserCreationDTO> update(
            @PathVariable UUID id,
            @RequestBody UserCreationDTO updatedUser) {

        return usersService.update(id, updatedUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria o usuário.")
    public ResponseEntity<UserCreationDTO> create(@RequestBody UserCreationDTO user) {
        UserCreationDTO newUser = usersService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PatchMapping("/{id}/avatar")
    @Operation(summary = "Atualizar informação", description = "Atualiza informação específica do usuário.")
    public ResponseEntity<UserCreationDTO> patchUser(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> avatar) {

        return usersService.updateAvatar(id, avatar)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário", description = "Apaga todas as informações do usuário via ID.")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (usersService.delete(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearUsers() {
        usersService.clear();
        return ResponseEntity.noContent().build();
    }
}
