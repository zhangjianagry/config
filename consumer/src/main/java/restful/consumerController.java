package restful;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import restful.Service.ResufulService;


@RestController
public class consumerController {

    @Autowired
    ResufulService resufulService;



    @RequestMapping(value = "/ok", method = RequestMethod.GET)
    public String getOk() {
        return resufulService.getOk();
    }


}
