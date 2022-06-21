package com.Minhub.homebanking.controllers;

import com.Minhub.homebanking.dtos.ClientDTO;
import com.Minhub.homebanking.models.Account;
import com.Minhub.homebanking.models.AccountType;
import com.Minhub.homebanking.models.Client;
import com.Minhub.homebanking.services.AccountService;
import com.Minhub.homebanking.services.ClientService;
import com.Minhub.homebanking.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;


    @GetMapping(path = "/clients")
        public List<ClientDTO> getClients(Authentication authentication){
        return clientService.getClientsDTO(authentication);
        } /*El conjunto de anotacion + metodo = servlet -> servicio que me devuelve un recurso*/

    @GetMapping(path = "/clients/{id}")//Asocia la peticion con la ruta especificada-mapeando una peticion con la ruta

    public ClientDTO getClient(@PathVariable Long id){
        return clientService.getClientDTO(id);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (clientService.getClientByEmail(email) !=  null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }

        if(!Util.validationPassword(password)){
            return new ResponseEntity<>("The password must contain at least one uppercase letter," +
                    "at least one lowercase letter, 1 number and a length between 8 to 20 characters",HttpStatus.FORBIDDEN);
        }



        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);
        Account account = new Account(accountService.validateAccount(), LocalDateTime.now(),0.0,client,true, AccountType.CURRENT);
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping(path = "/clients/current")

    public ClientDTO clientCurrent(Authentication authentication) {

        Client client = clientService.getClientCurrent(authentication);
        return new ClientDTO(client);

    }

}
