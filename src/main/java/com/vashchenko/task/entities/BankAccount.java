package com.vashchenko.task.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Setter
@Getter
@ToString(exclude = "user")
public class BankAccount {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true, insertable = false, updatable = false)
    private String number;
    @Column(updatable = false, nullable = false)
    private final BigDecimal startBalance;
    @Column(nullable = false)
    private BigDecimal currentBalance;
    @Column(nullable = false)
    private boolean isNeedToIncrease = false;
    @OneToOne(mappedBy = "bankAccount",cascade = CascadeType.ALL)
    private ClientUser user;
    public BankAccount() {
        startBalance=null;
    }

    public BankAccount(BigDecimal startBalance){
        this.startBalance=startBalance;
        currentBalance=startBalance;
        isNeedToIncrease=false;
    }

    public void addBalance(BigDecimal amount){
        this.currentBalance.add(amount);
    }

    public void subtractBalance(BigDecimal amount){
        this.currentBalance.subtract(amount);
    }
}
