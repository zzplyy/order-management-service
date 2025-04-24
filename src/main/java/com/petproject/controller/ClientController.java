package com.petproject.controller;

import com.petproject.entity.Client;
import com.petproject.repository.ClientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;

    @PostMapping
    public ResponseEntity<Client> create(@Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientRepository.save(client));
    }

    @GetMapping
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> get(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @Valid @RequestBody Client updatedClient) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setName(updatedClient.getName());
                    client.setEmail(updatedClient.getEmail());
                    client.setPhone(updatedClient.getPhone());
                    return ResponseEntity.ok(clientRepository.save(client));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        System.out.println("Trying to delete client with id: " + id);
        if (!clientRepository.existsById(id)) {
            System.out.println("Client not found!");
            return ResponseEntity.notFound().build();
        }
        clientRepository.deleteById(id);
        System.out.println("Client deleted successfully.");
        return ResponseEntity.noContent().build();
    }
}

