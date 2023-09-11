package com.az.unitech.client.impl;

import com.az.unitech.client.CurrencyRateService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private Map<String, Double> currencyRates = new HashMap<>();
    private Map<String, Long> rateTimestamps = new HashMap<>();

    private double fetchCurrencyRateFromExternalService(String currencyPair) {
        return Math.random() * 10;
    }

    private void startBackgroundRateUpdater() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCurrencyRates();
            }
        }, 0, 60 * 1000);
    }

    private synchronized void updateCurrencyRates() {
        for (String currencyPair : currencyRates.keySet()) {
            double newRate = fetchCurrencyRateFromExternalService(currencyPair);
            currencyRates.put(currencyPair, newRate);
            rateTimestamps.put(currencyPair, System.currentTimeMillis());
        }
    }

    public void CurrencyRateService() {
        currencyRates.put("usd-azn", 0.0);
        currencyRates.put("azn-tl", 0.0);

        startBackgroundRateUpdater();
    }

    public synchronized Double getCurrencyRate(String currencyPair) {
        if (!currencyRates.containsKey(currencyPair)) {
            throw new IllegalArgumentException("Invalid currency pair");
        }

        long lastUpdatedTime = rateTimestamps.getOrDefault(currencyPair, 0L);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastUpdatedTime >= 60 * 1000) {
            updateCurrencyRates();
        }

        return currencyRates.get(currencyPair);
    }
}
