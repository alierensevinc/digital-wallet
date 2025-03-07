package com.alierensevinc.dwallet.config;

import com.alierensevinc.dwallet.entity.Customer;
import com.alierensevinc.dwallet.entity.Role;
import com.alierensevinc.dwallet.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) {
        if (customerRepository.count() == 0) { // Ensure data is inserted only once
            customerRepository.save(new Customer(null, "Luffy", "Monkey", "12345678901", Role.CUSTOMER));
            customerRepository.save(new Customer(null, "Zoro", "Roronoa", "98765432100", Role.EMPLOYEE));
            System.out.println("✅ Database seeded with initial users!");
        }
    }
}