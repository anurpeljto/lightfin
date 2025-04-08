package com.anurpeljto.fiscalizationlistener.services.impl;

import com.anurpeljto.fiscalizationlistener.domain.Item;
import com.anurpeljto.fiscalizationlistener.repositories.ItemRepository;
import com.anurpeljto.fiscalizationlistener.services.ItemService;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    public ItemServiceImpl(final ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Override
    public void save(Item item){
        this.itemRepository.save(item);
    }
}
