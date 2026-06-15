package com.servicecrm.service;

import com.servicecrm.model.dto.ClientDTO;
import com.servicecrm.model.entity.Client;
import com.servicecrm.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {
    
    private final ClientRepository clientRepository;
    
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = Client.builder()
                .firstName(clientDTO.getFirstName())
                .lastName(clientDTO.getLastName())
                .email(clientDTO.getEmail())
                .phoneNumber(clientDTO.getPhoneNumber())
                .address(clientDTO.getAddress())
                .companyName(clientDTO.getCompanyName())
                .build();
        
        Client savedClient = clientRepository.save(client);
        return convertToDTO(savedClient);
    }
    
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Client not found with id: " + id));
        return convertToDTO(client);
    }
    
    public Page<ClientDTO> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Client not found with id: " + id));
        
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setEmail(clientDTO.getEmail());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        client.setAddress(clientDTO.getAddress());
        client.setCompanyName(clientDTO.getCompanyName());
        
        Client updatedClient = clientRepository.save(client);
        return convertToDTO(updatedClient);
    }
    
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new NoSuchElementException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }
    
    private ClientDTO convertToDTO(Client client) {
        return ClientDTO.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .phoneNumber(client.getPhoneNumber())
                .address(client.getAddress())
                .companyName(client.getCompanyName())
                .build();
    }
}
