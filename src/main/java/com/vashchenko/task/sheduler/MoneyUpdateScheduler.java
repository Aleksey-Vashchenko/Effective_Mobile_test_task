package com.vashchenko.task.sheduler;

import com.vashchenko.task.services.MoneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class MoneyUpdateScheduler {

    private final MoneyService moneyService;
    @Value("${scheduler.percents.max}")
    private BigDecimal maxPercent;
    @Value("${scheduler.percents.current}")
    private BigDecimal currentPercent;

    @Scheduled(cron = "0 * * * * *")
    public void updateBalances() {
        moneyService.updateBalances(maxPercent,currentPercent);
    }
}
