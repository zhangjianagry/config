package com.sap.authority.controller;


import com.sap.authority.enity.Service;
import com.sap.authority.enity.Token;
import com.sap.authority.enity.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
@EnableDiscoveryClient
public class ServiceController {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    RestTemplate restTemplate;

    @LoadBalanced
    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }

    public boolean authentication(String token, String userId) {
        Token TOKEN = new Token();
        TOKEN.setToken(token);
        TOKEN.setUser_id(userId);
        return restTemplate.postForObject("http://AUTHORITY/verify", TOKEN, Boolean.class);
    }

    @RequestMapping(value = "/createService", method = RequestMethod.POST)
    public Service createServices(@RequestBody Service service,
                                 @RequestParam("userId") String UserId) {
        if (service != null) {
            //写入service表
            if (authentication(service.getToken(), UserId)) {
                mongoTemplate.insert(service);
                //写入user service表
                String serviceId =  service.getServer_id();
                restTemplate.getForObject("http://AUTHORITY/addService?userId=" + UserId + "&serviceId=" + serviceId,
                        String.class);
                return service;
            }
        }
        return null;
    }

    @RequestMapping(value = "/deleteService")
    public String deleteServices(@RequestParam("service_id") String serviceId,
                                 @RequestParam("userId") String UserId) {
            //删除user service表
            return restTemplate.getForObject("http://AUTHORITY//removeService?userId=" + UserId + "&serviceId=" + serviceId,
                    String.class);
        }


}
