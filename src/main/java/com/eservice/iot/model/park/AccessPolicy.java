package com.eservice.iot.model.park;

import java.util.List;

/**
 * @program: yttps_shzx
 * @description:
 * @author: Mr.Zhang
 * @create: 2019-05-21 11:35
 **/
public class AccessPolicy {

    /**
     * device_id_list : ["string"]
     * enabled : true
     * id : string
     * name : string
     * tag_id_list : ["string"]
     * valid_time : {"end_date":"string","end_time":"string","end_timestamp":0,"mode":"0","start_date":"string","start_time":"string","start_timestamp":0,"valid_weekday":[0]}
     */

    private boolean enabled;
    private String id;
    private String name;
    private ValidTimeBean valid_time;
    private List<String> device_id_list;
    private List<String> tag_id_list;

    public AccessPolicy(){

    }

    public AccessPolicy(AccessPolicy accessPolicy){
        this.enabled = accessPolicy.enabled;
        this.id=accessPolicy.id;
        this.name=accessPolicy.name;
        this.valid_time=accessPolicy.valid_time;
        this.device_id_list=accessPolicy.device_id_list;
        this.tag_id_list = accessPolicy.tag_id_list;
    }
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ValidTimeBean getValid_time() {
        return valid_time;
    }

    public void setValid_time(ValidTimeBean valid_time) {
        this.valid_time = valid_time;
    }

    public List<String> getDevice_id_list() {
        return device_id_list;
    }

    public void setDevice_id_list(List<String> device_id_list) {
        this.device_id_list = device_id_list;
    }

    public List<String> getTag_id_list() {
        return tag_id_list;
    }

    public void setTag_id_list(List<String> tag_id_list) {
        this.tag_id_list = tag_id_list;
    }

}
