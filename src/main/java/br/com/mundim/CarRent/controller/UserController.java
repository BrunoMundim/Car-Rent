package br.com.mundim.CarRent.controller;

import br.com.mundim.CarRent.model.dto.UserDTO;
import br.com.mundim.CarRent.model.entity.User;
import br.com.mundim.CarRent.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "jwt")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(tags = "User", summary = "Create a User")
    public ResponseEntity<User> create(@Valid @RequestBody UserDTO dto) {
        return new ResponseEntity<>(userService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("find-by-id")
    @Operation(tags = "User", summary = "Find a User by ID")
    public ResponseEntity<User> findById(@RequestParam Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("find-by-email")
    @Operation(tags = "User", summary = "Find a User by Email")
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("find-by-cpf")
    @Operation(tags = "User", summary = "Find a User by CPF")
    public ResponseEntity<User> findByCpf(@RequestParam String cpf) {
        return ResponseEntity.ok(userService.findByCpf(cpf));
    }

    @GetMapping("/find-all")
    @RolesAllowed("ADMIN")
    @Operation(tags = "User", summary = "Find all Users (ADMIN ONLY)")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping
    @Operation(tags = "User", summary = "Update a User by ID")
    public ResponseEntity<User> update(@RequestParam Long id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping
    @Operation(tags = "User", summary = "Delete a User by ID")
    public ResponseEntity<User> deleteById(@RequestParam Long id) {
        return ResponseEntity.ok(userService.deleteById(id));
    }

}
