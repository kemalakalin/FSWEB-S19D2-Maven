package com.workintech.s18d4.controller;

import com.workintech.s18d4.entity.Address;
import com.workintech.s18d4.service.AddressService;
import com.workintech.s18d4.service.AddressServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workintech/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public List<Address> getAll() {
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    public Address getById(@PathVariable Long id) {
        return addressService.find(id);
    }

    @PostMapping
    public Address create(@RequestBody Address address) {
        return addressService.save(address);
    }

    @PutMapping("/{id}")
    public Address update(@PathVariable Long id, @RequestBody Address address) {
        return addressService.update(id, address);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        addressService.delete(id);
    }
}