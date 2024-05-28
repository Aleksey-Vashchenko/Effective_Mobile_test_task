package com.vashchenko.task.services;

import com.vashchenko.task.dto.requests.SendMoneyRequest;
import com.vashchenko.task.entities.BankAccount;
import com.vashchenko.task.exceptions.BankAccountIsNotFoundException;
import com.vashchenko.task.exceptions.BaseConflictException;
import com.vashchenko.task.exceptions.LackOfMoneyException;
import com.vashchenko.task.exceptions.SystemException;
import com.vashchenko.task.lockers.MoneyOperationLocker;
import com.vashchenko.task.repositories.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoneyService {

    private final AccountRepository accountRepository;
    private final MoneyOperationLocker locker;

    public void sendMoney(String userId, SendMoneyRequest request) {
        try {
            locker.lock();
            BankAccount accountFrom = findBankAccountByUserUuid(userId);
            BankAccount accountTo = accountRepository.findByNumber(request.accountTo()).orElseThrow(BankAccountIsNotFoundException::new);
            switch (accountFrom.getCurrentBalance().compareTo(request.money())){
                case -1:{
                    locker.unlock();
                    throw new LackOfMoneyException();
                }
                default:{
                    accountFrom.setCurrentBalance(accountFrom.getCurrentBalance().subtract(request.money()));
                    accountTo.setCurrentBalance(accountTo.getCurrentBalance().add(request.money()));
                    saveAccounts(accountTo,accountFrom);
                }
            }
            locker.unlock();
        }
        catch (InterruptedException e){
            throw new SystemException(e.getMessage());
        }
    }
    @Transactional
    protected void saveAccounts(BankAccount ac1, BankAccount ac2){
        accountRepository.save(ac1);
        accountRepository.save(ac2);
    }

    private BankAccount findBankAccountByUserUuid(String userId){
        try {
            return accountRepository.findByUser_Id(UUID.fromString(userId)).get();
        }
        catch (NoSuchElementException e){
            throw new BankAccountIsNotFoundException();
        }
    }

    public void updateBalances(BigDecimal maxPercent, BigDecimal currentPercent) {
        List<BankAccount> accounts = accountRepository.findAll();
        if (!accounts.isEmpty()){
            for (BankAccount account : accounts) {
                try {
                    BigDecimal percentDivisor = new BigDecimal("100");
                    BigDecimal currentPercentMultiplier = currentPercent.divide(percentDivisor, 10, RoundingMode.HALF_UP);
                    BigDecimal MaxPercentMultiplier = maxPercent.divide(percentDivisor, 10, RoundingMode.HALF_UP);
                    BigDecimal maxIncrease = account.getStartBalance().multiply(MaxPercentMultiplier).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal currentIncrease = account.getCurrentBalance().multiply(currentPercentMultiplier).setScale(2, RoundingMode.HALF_DOWN);
                    if(currentIncrease.compareTo(maxIncrease) > 0){
                        currentIncrease=maxIncrease;
                    }
                    locker.lock();
                    account.setCurrentBalance(account.getCurrentBalance().add(currentIncrease));
                    accountRepository.save(account);
                    locker.unlock();
                }
                catch (InterruptedException e){
                    log.error(e.getMessage());
                }

            }
        }
    }
}
