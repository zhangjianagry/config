package restful.Controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import restful.Enity.Property;
import restful.Enity.Token;
import restful.RestfulApiApplication;

@RestController
public class PropertyController {

    private static final Logger LOG = Logger.getLogger(RestfulApiApplication.class.getName());
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    RestTemplate restTemplate;

    @LoadBalanced
    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String getTest(){
        return "1234";
    }


    public boolean authentication(String token) {
        Token TOKEN = new Token();
        TOKEN.setToken(token);
        return restTemplate.postForObject("http://AUTHORITY/verify", TOKEN, Boolean.class);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getProperty(@RequestParam(value="id", defaultValue="all") String id, @RequestParam(value = "token") String token ) {
        LOG.log(Level.INFO, "Get config information");
        if (!authentication(token)) {
            return "Unauthorized";
        }
        if (id.equals("all")) {
            Query query = new Query();
            query.with(new Sort(Sort.Direction.DESC, "_id"));
            List<Property> properties = mongoTemplate.find(query, Property.class);
            StringBuilder sb = new StringBuilder();
            for(Property property : properties) {
                sb.append(property.getServer_uuid() + ":" + property.getContent());
                sb.append("\n");
            }
            return sb.toString();
        }
        Query query = Query.query(Criteria.where("_id").is(Long.valueOf(id).longValue()));
        Property property = mongoTemplate.findOne(query, Property.class);
        return property.getContent();
    }


    @RequestMapping(method = RequestMethod.POST)
    public String addProperty(@RequestBody Property property, @RequestParam("token") String token) {
        if (!authentication(token)) {
            return "Unauthorized";
        }
        LOG.log(Level.INFO, "Put config information");
        mongoTemplate.insert(property);
        return "finish";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteProperty(@RequestParam(value="name", defaultValue="World") Long id,
                                 @RequestParam(value = "token", defaultValue = "1") String token) {
        if (!authentication(token)) {
            return "Unauthorized";
        }
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Property.class);
        return "ok";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updateProperty(@RequestParam(value="id", defaultValue="World") long id,
                               @RequestParam(value="content", defaultValue="World") String content,
                               @RequestParam(value = "token", defaultValue = "1") String token) {
        if (!authentication(token)) {
            return "Unauthorized";
        }
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = Update.update("content", content).set("updateDate",new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
        mongoTemplate.updateFirst(query, update, Property.class);
        return "ok";
    }


}
