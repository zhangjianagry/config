package com.sap.authority.controller;


import com.sap.authority.enity.Config;
import com.sap.authority.enity.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;

@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
public class ConfigController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping("/addConfig")
    public String addConfig(@RequestBody Config config, @RequestParam(value = "serviceId") long serviceId) {
        config.setModifiedDate(Calendar.getInstance().getTime());
        mongoTemplate.insert(config);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(serviceId));
        Service service = mongoTemplate.findOne(query, Service.class);
        List<Config> configs = service.getConfig();
        configs.add(config);
        service.setConfig(configs);
        mongoTemplate.save(service);
        return "add configs";
    }

    @RequestMapping(value = "/updateConfig", method = RequestMethod.POST)
    public String updateConfig(@RequestBody Config config) {
        Query query = Query.query(Criteria.where("_id").is(config.getConfig_id()));
        Config rawConfig = mongoTemplate.findOne(query, Config.class);
        rawConfig.setKey(config.getKey());
        rawConfig.setModifiedDate(Calendar.getInstance().getTime());
        rawConfig.setValue(config.getValue());
        rawConfig.setDescription(config.getDescription());
        rawConfig.setAttr1(config.getAttr1());
        rawConfig.setAttr2(config.getAttr2());
        mongoTemplate.save(rawConfig);
        return "update done";
    }
}
