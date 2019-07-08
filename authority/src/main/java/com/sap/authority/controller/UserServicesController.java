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

@CrossOrigin
@RestController
public class UserServicesController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/getServices")
    public List<Long> getServices(@RequestParam("id") Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<UserServices> userServices = mongoTemplate.find(query, UserServices.class);
        return userServices == null ? new ArrayList<>() : userServices.get(0).getServices();
    }


    @RequestMapping(value = "/addService", method = RequestMethod.GET)
    public String addServices(@RequestParam("userId") Long userId,
                                  @RequestParam("serviceId") Long serviceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        UserServices userServices = mongoTemplate.findOne(query, UserServices.class);
        if (userServices == null) {
            userServices = new UserServices();
            userServices.setUserId(userId);
        }
        List<Long> services = userServices.getServices();
        services.add(serviceId);
        userServices.setServices(services);
        mongoTemplate.save(userServices);
        return "success";
    }

    @RequestMapping(value = "/removeService")
    public List<Long> removeServices(@RequestParam("userId") Long userId,
                                     @RequestParam("serviceId") Long serviceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        List<UserServices> userServices = mongoTemplate.find(query, UserServices.class);
        List<Long> services = userServices == null? new ArrayList<Long>() : userServices.get(0).getServices();
        int deleteIndex = -1;
        // 后面修改成二分搜索降低时间复杂度
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i) == serviceId) {
                deleteIndex = i;
                break;
            }
        }
        services.remove(deleteIndex);
        Update update = Update.update("services", services);
        mongoTemplate.updateFirst(query, update, UserServices.class);
        return services;
    }



    @RequestMapping(value = "/getServices/{user_id}")
    public List<Service> getServices(@PathVariable(name = "user_id") long user_id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(user_id));
        UserServices userServices = mongoTemplate.findOne(query, UserServices.class);
        if (userServices == null) {
            return new ArrayList<Service>();
        }
        List<Long> seriviceId = userServices.getServices();
        List<Service> res = new ArrayList<>();
        for (long id : seriviceId) {
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
