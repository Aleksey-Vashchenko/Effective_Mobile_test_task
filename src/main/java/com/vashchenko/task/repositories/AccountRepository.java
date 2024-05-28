package com.vashchenko.task.repositories;

import com.vashchenko.task.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, UUID> {
    Optional<BankAccount> findByUser_Id(UUID uuid);

    Optional<BankAccount> findByNumber(String number);
}
