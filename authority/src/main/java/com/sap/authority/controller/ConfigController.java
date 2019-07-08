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
    public String addConfig(@RequestBody Config config, @RequestParam(value = "serviceId") String serviceId) {
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
    public String updateConfig(@RequestBody Config config, @RequestParam(value = "serviceId") String serviceId) {
        config.setModifiedDate(Calendar.getInstance().getTime());
        Query query = Query.query(Criteria.where("_id").is(config.getConfig_id()));
        Config rawConfig = mongoTemplate.findOne(query, Config.class);
        rawConfig.setKey(config.getKey());
        rawConfig.setModifiedDate(Calendar.getInstance().getTime());
        rawConfig.setValue(config.getValue());
        rawConfig.setDescription(config.getDescription());
        rawConfig.setAttr1(config.getAttr1());
        rawConfig.setAttr2(config.getAttr2());

        Query query1 = Query.query(Criteria.where("_id").is(serviceId));
        Service service = mongoTemplate.findOne(query1, Service.class);
        for (int i = 0; i < service.getConfig().size(); i++) {
            Config config1 = service.getConfig().get(i);
            if (config1.getConfig_id() == config.getConfig_id()) {
                service.getConfig().set(i, config);
                break;
            }
        }
        mongoTemplate.save(service);
        return "update done";
    }

    @RequestMapping(value = "/deleteConfig", method = RequestMethod.GET)
    public String updateConfig(@RequestParam(value = "configId") String configId,
                               @RequestParam(value = "serviceId") String serviceId) {

        Query query = Query.query(Criteria.where("_id").is(configId));
        mongoTemplate.remove(query, Config.class);



        Query query1 = Query.query(Criteria.where("_id").is(serviceId));
        Service service = mongoTemplate.findOne(query1, Service.class);
        for (int i = 0; i < service.getConfig().size(); i++) {
            Config config = service.getConfig().get(i);
            if (config.getConfig_id().equals(configId)) {
                service.getConfig().remove(i);
                break;
            }
        }
        mongoTemplate.save(service);
        return "delete done";
    }

}
