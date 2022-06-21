package com.Minhub.homebanking.repositories;

import com.Minhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
    //List<Client> findByLastName(String lastName);
    Client findByEmail (String email);
}
