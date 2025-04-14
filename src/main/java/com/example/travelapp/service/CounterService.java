package com.example.travelapp.service;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;


@Service
public class CounterService {
    private final AtomicLong counter = new AtomicLong(0);


    public void increment() {
        counter.incrementAndGet();
    }


    public long getTotalVisits() {
        return counter.get();
    }
}
