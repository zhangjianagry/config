package restful;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/consumer/")
public class consumerController {

    @Autowired

    RestTemplate restTemplate;
    /**
     * 实例化RestTemplate
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public String getTest(@RequestParam(value="id", defaultValue="all") String id){
        return   restTemplate.getForObject("http://RESTFUL-API/config/?id="+id,String.class);

    }


}
