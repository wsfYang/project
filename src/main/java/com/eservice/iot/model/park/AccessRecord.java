package com.eservice.iot.model.park;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author zt
 */
public class AccessRecord {
    @JsonProperty("score")
    private double score;
    @JsonProperty("device_id")
    private String device_id;
    @JsonProperty("access_policy_id_list")
    private List<String> access_policy_id_list;
    @JsonProperty("person")
    private Person person;
    @JsonProperty("identity")
    private String identity;
    @JsonProperty("face_image_id")
    private String face_image_id;
    @JsonProperty("scene_image_id")
    private String scene_image_id;
    @JsonProperty("face_id")
    private String face_id;
    @JsonProperty("record_type")
    private String record_type;
    @JsonProperty("timestamp")
    private int timestamp;
    @JsonProperty("pass_result")
    private String pass_result;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public List<String> getAccess_policy_id_list() {
        return access_policy_id_list;
    }

    public void setAccess_policy_id_list(List<String> access_policy_id_list) {
        this.access_policy_id_list = access_policy_id_list;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getFace_image_id() {
        return face_image_id;
    }

    public void setFace_image_id(String face_image_id) {
        this.face_image_id = face_image_id;
    }

    public String getScene_image_id() {
        return scene_image_id;
    }

    public void setScene_image_id(String scene_image_id) {
        this.scene_image_id = scene_image_id;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public String getRecord_type() {
        return record_type;
    }

    public void setRecord_type(String record_type) {
        this.record_type = record_type;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getPass_result() {
        return pass_result;
    }

    public void setPass_result(String pass_result) {
        this.pass_result = pass_result;
    }
}
