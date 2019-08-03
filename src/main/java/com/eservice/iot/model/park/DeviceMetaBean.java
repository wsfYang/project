package com.eservice.iot.model.park;

import java.util.Map;

/**
 * @program: yttps_shzx
 * @description:
 * @author: Mr.Zhang
 * @create: 2019-05-21 16:14
 **/
public class DeviceMetaBean {
    /**
     * device_name : string
     * direction_type : 0
     * docking_config : {"additionalProp1":{},"additionalProp2":{},"additionalProp3":{}}
     * enable : true
     * ip : string
     * location : string
     * map_id : string
     * password : string
     * port : 0
     * position : {"x":0,"y":0}
     * specification : string
     * type : 0
     * username : string
     * version : string
     */

    private String device_name;
    private int direction_type;
    private Map docking_config;
    private boolean enable;
    private String ip;
    private String location;
    private String map_id;
    private String password;
    private int port;
    private Map position;
    private String specification;
    private String type;
    private String username;
    private String version;

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public int getDirection_type() {
        return direction_type;
    }

    public void setDirection_type(int direction_type) {
        this.direction_type = direction_type;
    }

    public Map getDocking_config() {
        return docking_config;
    }

    public void setDocking_config(Map docking_config) {
        this.docking_config = docking_config;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMap_id() {
        return map_id;
    }

    public void setMap_id(String map_id) {
        this.map_id = map_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Map getPosition() {
        return position;
    }

    public void setPosition(Map position) {
        this.position = position;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
