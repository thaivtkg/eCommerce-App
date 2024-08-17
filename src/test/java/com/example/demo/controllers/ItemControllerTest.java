package com.example.demo.controllers;

import com.example.demo.TestUltis;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    public static final String ROUND_WIDGET = "Round Widget";

    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp()  {
        itemController = new ItemController();
        TestUltis.injectObjects(itemController,"itemRepository", itemRepository);
    }

    @Test
    public void testGetItems(){
        List<Item> items = new ArrayList<>();
        Item item = getItem0();
        Item item1 = getItem1();
        items.add(item);
        items.add(item1);
        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> itemResponse = itemController.getItems();
        assertNotNull(itemResponse);
        assertEquals(HttpStatus.OK,itemResponse.getStatusCode());
        items = itemResponse.getBody();
        assertNotNull(items);
        assertEquals(2,items.size());
        assertEquals(item,items.get(0));
        assertEquals(item1,items.get(1));
    }

    @Test
    public void testGetItemById(){
        Item item = getItem0();
        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));
        ResponseEntity<Item> itemResponse = itemController.getItemById(0L);
        assertNotNull(itemResponse);
        assertEquals(HttpStatus.OK,itemResponse.getStatusCode());
        Item item1 = itemResponse.getBody();
        assertEquals(item,item1);
        assertEquals(item.getId(),item1.getId());
        assertEquals(item.getName(),item1.getName());
        assertEquals(item.getDescription(),item1.getDescription());
    }

    @Test
    public void testGetItemByName(){
        Item item = getItem0();
        List<Item> items = new ArrayList<>(2);
        items.add(item);
        when(itemRepository.findByName(ROUND_WIDGET)).thenReturn(items);

        ResponseEntity<List<Item>> responseItem = itemController.getItemsByName(ROUND_WIDGET);
        assertNotNull(responseItem);
        assertEquals(HttpStatus.OK,responseItem.getStatusCode());
        items = responseItem.getBody();
        assertNotNull(items);
        assertEquals(1,items.size());
        assertEquals(item,items.get(0));
    }


    public static Item getItem0() {
        Item item = new Item();
        item.setId(0L);
        item.setName(ROUND_WIDGET);
        item.setPrice(new BigDecimal("2.99"));
        item.setDescription("A widget that is round");
        return item;
    }

    public static Item getItem1() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Square Widget");
        item.setPrice(new BigDecimal("1.99"));
        item.setDescription("A widget that is square");
        return item;
    }
}
