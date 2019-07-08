package com.sap.authority.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sap.authority.enity.Result;
import com.sap.authority.enity.Service;
import com.sap.authority.enity.User;
import com.sap.authority.enity.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
public class UserServicesController {
    @Autowired
    private MongoTemplate mongoTemplate;


    @RequestMapping(value = "/addService", method = RequestMethod.GET)
    public String addServices(@RequestParam("userId") String userId,
                                  @RequestParam("serviceId") String serviceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        UserServices userServices = mongoTemplate.findOne(query, UserServices.class);
        if (userServices == null) {
            userServices = new UserServices();
            userServices.setUserId(userId);
        }
        List<String> services = userServices.getServices();
        services.add(serviceId);
        userServices.setServices(services);
        mongoTemplate.save(userServices);
        return "success";
    }

    @RequestMapping(value = "/removeService")
    public boolean removeServices(@RequestParam("userId") String userId,
                                     @RequestParam("serviceId") String serviceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        UserServices userService = mongoTemplate.findOne(query, UserServices.class);
        if (userService != null) {
            if (!userService.getServices().remove(serviceId)) {
                return false;
            }
            mongoTemplate.save(userService);
            return true;
        }
        return false;
    }



    @RequestMapping(value = "/getServices/{user_id}")
    public List<Service> getServices(@PathVariable(name = "user_id") String user_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user_id));
        UserServices userServices = mongoTemplate.findOne(query, UserServices.class);
        if (userServices == null) {
            return new ArrayList<Service>();
        }
        List<String> seriviceId = userServices.getServices();
        List<Service> res = new ArrayList<>();
        for (String id : seriviceId) {
            Query query1 = new Query();
            query1.addCriteria(Criteria.where("_id").is(id));
            Service tem = mongoTemplate.findOne(query1, Service.class);
            if (tem != null) {
                res.add(tem);
            }
        }
        return res;
    }
}
