package com.sap.authority.enity;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;
import java.util.*;

@Document(collection = "service")
public class Service {

    @Id
    private long server_id;
    @Field("name")
    private String name;
    @Field("description")
    private String description;
    @Field("createDate")
    private String createDate;
    @Field("updateDate")
    private String updateDate;
    @Field("config")
    private List<Config> config;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Field("token")
    private String token;

    public Service() {
        this.server_id = RandomUtils.nextLong();
        this.createDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.updateDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.config = new ArrayList<>();
    }






    public long getServer_id() {
        return server_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Config> getConfig() {
        return config;
    }

    public void setConfig(List<Config> config) {
        this.config = config;
    }

}
