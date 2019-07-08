package com.sap.authority.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sap.authority.enity.Result;
import com.sap.authority.enity.Token;
import com.sap.authority.enity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
public class UserController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value="/verify", method = RequestMethod.POST)
    public  boolean  verify(@RequestBody Token TOKEN){
        String token = TOKEN.getToken();
        Result<User> result =new Result<>();
        if(token !=null){
            try {
                DecodedJWT jwt = null;
                JWTVerifier verifier = JWT.require(Algorithm.HMAC256("test")).build();
                jwt = verifier.verify(token);
                String username = jwt.getClaim("username").asString();
                String password = jwt.getClaim("password").asString();
                Query query = new Query();
                query.addCriteria(Criteria.where("username").is(username));
                query.addCriteria(Criteria.where("password").is(password));
                User user = mongoTemplate.findOne(query, User.class);
                return user.getUser_id() == TOKEN.getUser_id();
            } catch (Exception e) {
                return false;
            }
        }else{
           return false;
        }
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(@RequestBody User user) {
        mongoTemplate.insert(user);
        return "finish";
    }

    @RequestMapping(value="/getToken/{username}/{password}")
    public  Result<String>  getToken(@PathVariable(name = "username")  String username , @PathVariable(name = "password")   String password) {
        Result<String> result = new Result<>();
        if(username!=null && password !=null){
            try {
                Query query = new Query();
                query.addCriteria(Criteria.where("username").is(username));
                query.addCriteria(Criteria.where("password").is(password));
                User user = mongoTemplate.findOne(query, User.class);
                if (user == null) {
                    result.setCode(400);
                    result.setMessage("failed");
                } else {
                    result.setCode(200);
                    result.setMessage("success");
                    Date expiresAt = new Date(System.currentTimeMillis() + 3600 * 1000 * 24);
                    String token = JWT.create()
                            .withClaim("username", username)
                            .withClaim("password", password).withExpiresAt(expiresAt).sign(Algorithm.HMAC256("test"));
                    result.setToken(token);
                    result.setUserId(user.getUser_id());
                }
            }catch (Exception e){
                result.setCode(400);
                result.setMessage("failed");
            }
        }else{
            result.setCode(400);
            result.setMessage("failed");
        }
        return result;
    }

}
