package com.ryansusana.spotitube.users;


import com.elepy.annotations.Inject;
import com.elepy.annotations.Route;
import com.elepy.dao.Crud;
import com.elepy.exceptions.ElepyException;
import com.elepy.http.Filter;
import com.elepy.http.HttpContext;
import com.elepy.http.HttpMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Authentication implements Filter {

    @Inject
    private Crud<User> userDao;

    @Inject
    private ObjectMapper objectMapper;

    private Map<String, User> tokenUserMap = new HashMap<>();

    @Override
    public void authenticate(HttpContext context) {
        context.response().type("application/json");

        String token = context.request().queryParams("token");

        //If null token or one that doesn't exist in map, 403
        if (token == null || !tokenUserMap.keySet().contains(token)) {
            throw new ElepyException("No way Josay!", 403);
        }
    }

    @Route(path = "/login", method = HttpMethod.POST)
    public void login(HttpContext context) throws IOException {

        //Map of how the JSON object looks. 2 keys "user" and "password"
        Map<String, String> requestMap = objectMapper.readValue(context.request().body(), new TypeReference<HashMap<String, String>>() {
        });

        // Looks for users by username.
        User user = this.userDao.searchInField("username", requestMap.get("user"))
                .stream().findFirst().orElseThrow(() -> new ElepyException("User not found", 404));

        //If correct credentials, save user token in memory and give them the access token.
        if (user.getPassword().equals(requestMap.get("password"))) {
            Map<String, String> returnObject = new HashMap<>();

            String token = UUID.randomUUID().toString();
            tokenUserMap.put(token, user);

            returnObject.put("user", user.getName());
            returnObject.put("token", token);

            context.response().result(objectMapper.writeValueAsString(returnObject));
        } else {
            throw new ElepyException("Invalid Password!", 403);
        }

    }

}
