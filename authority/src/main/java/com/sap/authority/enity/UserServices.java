package com.sap.authority.enity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "user_services")
public class UserServices {
    @Id
    @Field("userId")
    private String userId;
    @Field("services")
    private List<String> services;


    public UserServices() {
        this.services = new ArrayList<String>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String server_uuid) {
        this.userId = server_uuid;
    }

    public List<String> getServices() {
        if (services == null) {
            services = new ArrayList<>();
        }
        return services;
    }
    public void setServices(List<String> services) {
        this.services = services;
    }
}
