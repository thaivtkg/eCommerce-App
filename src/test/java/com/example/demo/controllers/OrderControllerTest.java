package com.example.demo.controllers;

import com.example.demo.TestUltis;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUltis.injectObjects(orderController, "userRepository", userRepo);
        TestUltis.injectObjects(orderController, "orderRepository", orderRepo);


        when(userRepo.findByUsername("thai")).thenReturn(generateTestUser());

        // have to use any() matcher here because the returned value is responded beforehand:
        // Reference Argument Matcher: https://www.baeldung.com/mockito-argument-matchers
        when(orderRepo.findByUser(any())).thenReturn((getUserOrders()));
    }

    @Test
    public void testSubmitOrder() {
        ResponseEntity<UserOrder> response = orderController.submit("thai");
        UserOrder order = response.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(order);
        assertEquals("thai", order.getUser().getUsername());
        assertEquals("123",order.getUser().getPassword());
        assertEquals(1,order.getItems().size());
    }

    @Test
    public void testSubmitNullUser() {
        ResponseEntity<UserOrder> response = orderController.submit("");
        UserOrder order = response.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(order);
    }


    @Test
    public void testGetOrderForUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("thai");
        List<UserOrder> orders = response.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(orders);
        assertEquals(1, orders.size());
    }

    @Test
    public void testGetOrderForUserInvalidUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("");
        List<UserOrder> orders = response.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(orders);
    }





    private static User generateTestUser() {
        User user = new User(1L, "thai", "123");
        user.setCart(getCart(user));
        return user;
    }

    private static Cart getCart(User user) {
        Cart cart = new Cart();
        cart.addItem(TestUltis.getItem0());
        cart.setUser(user);

        return cart;
    }

    private static List<UserOrder> getUserOrders() {
        UserOrder userOrder = UserOrder.createFromCart(generateTestUser().getCart());
        return Lists.list(userOrder);
    }
}
