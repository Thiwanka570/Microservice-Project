package com.inventryService.inventry.services;

import com.inventryService.inventry.entity.Inventory;
import com.inventryService.inventry.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getAllInventoryItems(){
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryItemById(Long id){
        return inventoryRepository.findById(id);
    }

    public Inventory updateOrSaveInventoryItem(Inventory inventory){
        return inventoryRepository.save(inventory);
    }

    public void deleteInventoryItemById(Long id){
        inventoryRepository.deleteById(id);
    }
}
