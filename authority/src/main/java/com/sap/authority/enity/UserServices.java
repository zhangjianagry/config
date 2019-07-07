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
    private long userId;
    @Field("services")
    private List<Long> services;


    public UserServices() {
        this.services = new ArrayList<Long>();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long server_uuid) {
        this.userId = server_uuid;
    }

    public List<Long> getServices() {
        return services;
    }
    public void setServices(List<Long> services) {
        this.services = services;
    }
}
