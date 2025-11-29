package com.mercatosys.controller;

import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.client.ClientUpdateDTO;
import com.mercatosys.service.interfaces.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
//    public ClientResponseDTO createClient(@Valid @RequestBody ClientRequestDTO dto) {
//        return clientService.create(dto);
//    }

    @PutMapping("/{id}")
    public ClientResponseDTO updateClient(@PathVariable Long id,
                                          @Valid @RequestBody ClientRequestDTO dto) {
        return clientService.update(id, dto);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id) {
        clientService.delete(id);
    }


    @GetMapping("/{id}")
    public ClientResponseDTO getClientById(@PathVariable Long id) {
        return clientService.getById(id);
    }


    @GetMapping
    public List<ClientResponseDTO> getAllClients() {
        return clientService.getAll();
    }
}
