package com.ryansusana.spotitube.service;

import com.elepy.dao.Crud;
import com.elepy.exceptions.ElepyException;
import com.elepy.http.HttpContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryansusana.spotitube.Base;
import com.ryansusana.spotitube.presentation.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthenticationTest extends Base {

    @InjectMocks
    private Authentication authentication;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private Crud<User> userCrud;


    @BeforeEach
    void setUp() {

        initMocks(this);

        User user = new User();
        user.setName("Ryan Susana");
        user.setUsername("ryan");
        user.setPassword("susana");

        when(userCrud.getById(any())).thenReturn(Optional.of(user));
        when(userCrud.searchInField("username", "ryan")).thenReturn(Collections.singletonList(user));
    }

    @Test
    void testNoTokenAuth() {

        HttpContext mockedContext = mockedContext();
        when(mockedContext.request().queryParams(anyString())).thenReturn(null);


        ElepyException elepyException = assertThrows(ElepyException.class, () -> authentication.authenticate(mockedContext));

        assertEquals(403, elepyException.getStatus());
    }

    @Test
    void testInvalidTokenAuth() {

        HttpContext mockedContext = mockedContext();
        when(mockedContext.request().queryParams(anyString())).thenReturn("Nah man this invalid");


        ElepyException elepyException = assertThrows(ElepyException.class, () -> authentication.authenticate(mockedContext));

        assertEquals(403, elepyException.getStatus());
    }

    @Test
    void testValidLogin() {
        String request = "{ \"user\": \"ryan\", \"password\": \"susana\" }";

        HttpContext mockedContext = mockedContext();

        when(mockedContext.request().body()).thenReturn(request);

        assertDoesNotThrow(() -> authentication.login(mockedContext));

    }

    @Test
    void testWrongPassword() {
        String request = "{ \"user\": \"ryan\", \"password\": \"anasus\" }";

        HttpContext mockedContext = mockedContext();

        when(mockedContext.request().body()).thenReturn(request);

        ElepyException elepyException = assertThrows(ElepyException.class, () -> authentication.login(mockedContext));

        assertEquals(403, elepyException.getStatus());
    }

    @Test
    void testCanUserTokenAfterLogin() throws IOException {

        //Phase 1 Login
        String previousReturnedResult = lastReturnedResult();

        String request = "{ \"user\": \"ryan\", \"password\": \"susana\" }";

        HttpContext mockedContext = mockedContext();

        when(mockedContext.request().body()).thenReturn(request);

        assertDoesNotThrow(() -> authentication.login(mockedContext));
        assertNotEquals(previousReturnedResult, lastReturnedResult());

        //Phase 2 Authenticate
        String s = loginStringToToken(lastReturnedResult());
        when(mockedContext.request().queryParams("token")).thenReturn(s);

        assertDoesNotThrow(() -> authentication.authenticate(mockedContext));

    }


}
