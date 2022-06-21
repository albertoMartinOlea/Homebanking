package com.Minhub.homebanking.repositories;

import com.Minhub.homebanking.models.Client;
import com.Minhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientLoanRepository extends JpaRepository<ClientLoan,Long> {

}
