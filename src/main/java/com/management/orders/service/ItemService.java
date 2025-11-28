package com.management.orders.service;

import com.management.orders.domain.entity.Item;
import com.management.orders.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public Item create(Item item) {
        log.info("Creating new Item: {}", item.getName());
        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Item read(UUID id){
        log.info("Reading Item: {}", id);
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }
    @Transactional(readOnly = true)
    public List<Item> readAll(){
        log.info("Reading all Items");
        return itemRepository.findAll();
    }

    public Item updateItem(UUID id, Item updatedItem) {
        log.info("Updating Item with id: {}", id);

        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found for updating with ID " + id));
        existingItem.setName(updatedItem.getName());
        return itemRepository.save(existingItem);
    }

    public void deleteItem(UUID id) {
        log.info("Deleting Item with id: {}", id);
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found for deleting with ID " + id);
        }
        itemRepository.deleteById(id);
    }

    public void deleteByName(String name) {
        log.info("Deleting Item with name: {}", name);
        itemRepository.deleteByName(name);
    }
}
