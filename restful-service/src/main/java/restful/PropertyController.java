package restful;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config/")
public class PropertyController {

    @Autowired
    private MongoTemplate mongoTemplate;
/*
    @RequestMapping(method = RequestMethod.GET)
    public String getTest(){
        return "1234";
    }
*/

    @RequestMapping(method = RequestMethod.GET)
    public String getPropery(@RequestParam(value="id", defaultValue="all") String id) {
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
    public String addPropery(@RequestBody Property property) {
        mongoTemplate.insert(property);
        return "finish";
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deletePropery(@RequestParam(value="name", defaultValue="World") Long id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Property.class);
        return "ok";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updatePropery(@RequestParam(value="id", defaultValue="World") long id, @RequestParam(value="content", defaultValue="World") String content) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = Update.update("content", content).set("updateDate",new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
        mongoTemplate.updateFirst(query, update, Property.class);
    }


}
