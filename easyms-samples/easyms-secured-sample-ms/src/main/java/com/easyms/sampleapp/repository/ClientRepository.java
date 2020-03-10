package com.easyms.sampleapp.repository;


import com.easyms.sampleapp.model.dto.ClientDto;
import com.easyms.sampleapp.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    public Optional<Client> findByEmail(String email);
}
