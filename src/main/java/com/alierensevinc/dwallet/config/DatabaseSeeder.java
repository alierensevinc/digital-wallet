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
            customerRepository.save(new Customer(null, "Employee Name", "Employee Surname", "98765432100", Role.EMPLOYEE));
            customerRepository.save(new Customer(null, "Customer 1 Name", "Customer 1 Surname", "12345678901", Role.CUSTOMER));
            customerRepository.save(new Customer(null, "Customer 2 Name", "Customer 2 Surname", "24681357901", Role.CUSTOMER));
        }
    }
}