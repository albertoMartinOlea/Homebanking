package com.Minhub.homebanking.services.implement;

import com.Minhub.homebanking.dtos.ClientDTO;
import com.Minhub.homebanking.models.Client;
import com.Minhub.homebanking.repositories.ClientRepository;
import com.Minhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getClientsDTO(Authentication authentication) {
        if(!authentication.getName().equals("admin@admin.com")){
            List<Client> clients = clientRepository.findAll();
            clients.stream().forEach(client -> client.setCards(client.getCards().stream().filter(card -> card.isState() == true).collect(Collectors.toSet())));
            return  clients.stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
        }
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @Override
    public Client getClientCurrent(Authentication authentication) {
        if(!authentication.getName().equals("admin@admin.com")){
            Client client = clientRepository.findByEmail(authentication.getName());
            client.setCards(client.getCards().stream().filter(card -> card.isState() == true).collect(Collectors.toSet()));
            client.setAccounts(client.getAccounts().stream().filter(account -> account.isVisibility() == true).collect(Collectors.toSet()));
            return  client;
        }
        return clientRepository.findByEmail(authentication.getName());
    }

    @Override
    public ClientDTO getClientDTO(long id) {
        return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
