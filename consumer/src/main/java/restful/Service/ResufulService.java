package restful.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Service
public class ResufulService {
    @Autowired
    RestTemplate restTemplate;

    public String getOk() {
        return restTemplate.getForEntity("http://RESTFUL-API/test",String.class).getBody();
    }

}
