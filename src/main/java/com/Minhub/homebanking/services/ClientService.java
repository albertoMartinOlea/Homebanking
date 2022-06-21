package com.Minhub.homebanking.services;

import com.Minhub.homebanking.dtos.ClientDTO;
import com.Minhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getClientsDTO(Authentication authentication);
    Client getClientCurrent(Authentication authentication);
    ClientDTO getClientDTO(long id);
    Client getClientByEmail(String email);
    void saveClient(Client client);

}
