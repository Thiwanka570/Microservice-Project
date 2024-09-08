package com.inventryService.inventry.controllers;

import com.inventryService.inventry.entity.Inventory;
import com.inventryService.inventry.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventoryItems(){
        List<Inventory> inventories=inventoryService.getAllInventoryItems();
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Inventory> getInventoryItemById(@PathVariable("id") Long id){
        Optional<Inventory> inventory=inventoryService.getInventoryItemById(id);
        return inventory.map(
                value->new ResponseEntity<>(value,HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Inventory> addInventoryItem(@RequestBody Inventory inventory){
        Inventory savedInventory=inventoryService.updateOrSaveInventoryItem(inventory);
        return new ResponseEntity<>(savedInventory, HttpStatus.CREATED);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<Inventory> updateInventoryItem(@PathVariable("id") Long id,@RequestBody Inventory inventoryDetails){
        Optional<Inventory> existingInventory=inventoryService.getInventoryItemById(id);
        if (existingInventory.isPresent()){
            Inventory inventory=existingInventory.get();
            inventory.setProductName(inventoryDetails.getProductName());
            inventory.setPrice(inventoryDetails.getPrice());
            inventory.setQuantity(inventoryDetails.getQuantity());

            Inventory updatedInventory=inventoryService.updateOrSaveInventoryItem(inventory);
            return new ResponseEntity<>(updatedInventory,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Inventory> deleteInvenoryItem(@PathVariable("id") Long id){
        Optional<Inventory> existantInventory=inventoryService.getInventoryItemById(id);
        if (existantInventory.isPresent()){
            inventoryService.deleteInventoryItemById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
