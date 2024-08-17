package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CartControllerTest {
    private static final Logger log = LoggerFactory.getLogger(CartControllerTest.class);

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        log.info("Setup called");
        cartController = new CartController(userRepository, cartRepository, itemRepository);
        when(itemRepository.findById(1L)).thenReturn(getItem());
    }

    @Test
    public void testAddToCart() {
        ModifyCartRequest modifyCartRequest = generateModifyCartRequest();
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(generateTestUser());
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Cart responseCart = response.getBody();



        assertNotNull(response);
        assertNotNull(responseCart);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, responseCart.getId());
        List<Item> items = responseCart.getItems();
        assertNotNull(items);
        assertEquals("thai",responseCart.getUser().getUsername());
        Item item=items.get(0);
        assertEquals(1L, item.getId());
        assertEquals(2, items.size());

    }

    @Test
    public void testAddToCartWithInvalidInput() {
        ModifyCartRequest modifyCartRequest = generateModifyCartRequest();
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCartWithInvalidInput2() {
        ModifyCartRequest modifyCartRequest = generateModifyCartRequest();
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(generateTestUser());
        modifyCartRequest.setItemId(0L);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCart() {
        ModifyCartRequest modifyCartRequest = generateModifyCartRequest();
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(generateTestUser());
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Cart responseCart = response.getBody();


        assertNotNull(response);
        assertNotNull(responseCart);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, responseCart.getItems().size());
        assertEquals("thai", responseCart.getUser().getUsername());
    }

    @Test
    public void testRemoveFromCartInvalidUserInput() {
        ModifyCartRequest modifyCartRequest = generateModifyCartRequest();
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Cart responseCart = response.getBody();


        assertNull(responseCart);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartInvalidItemInput() {
        ModifyCartRequest modifyCartRequest = generateModifyCartRequest();
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(generateTestUser());
        modifyCartRequest.setItemId(0L);
        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Cart responseCart = response.getBody();


        assertNull(responseCart);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private ModifyCartRequest generateModifyCartRequest() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("thai");
        cartRequest.setItemId(1);
        cartRequest.setQuantity(2);
        return cartRequest;
    }

    private User generateTestUser() {
        Cart cart = new Cart();
        User user = new User(1L, "thai", "123", cart);
        cart.setId(1L);
        cart.setUser(user);
        cart.setTotal(null);
        cart.setItems(null);
        return user;
    }

    private static Optional<Item> getItem() {
        Item item = new Item(1L, "Charmin Ultra Soft Toilet Paper", new BigDecimal(19.99), "18 Mega Rolls");

        return Optional.of(item);
    }
}
