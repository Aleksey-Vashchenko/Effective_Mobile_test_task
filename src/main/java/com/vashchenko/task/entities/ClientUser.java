package com.vashchenko.task.entities;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "id")
public class ClientUser extends User{
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @CollectionTable(name = "user_emails", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "email")
    private List<String> emails = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    @CollectionTable(name = "user_phone_numbers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "phone")
    private List<String> phoneNumbers = new ArrayList<>();
    @Column(nullable = false)
    private Date birthDate;
    @Column(nullable = false)
    private String fullName;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id",updatable = false)
    private BankAccount bankAccount;

    public ClientUser(List<String> emails, List<String> phoneNumbers, Date birthDate, String fullName, BankAccount bankAccount) {
        this.emails = emails;
        this.phoneNumbers = phoneNumbers;
        this.birthDate = birthDate;
        this.fullName = fullName;
        this.bankAccount = bankAccount;
    }

    public void addPhoneNumber(String number){
        this.getPhoneNumbers().add(number);
    }

    public void addEmail(String email) {
        this.getEmails().add(email);
    }
}
