package com.eservice.iot.model.park;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * 人员信息（员工、访客、陌生人、黑名单）
 *
 * @author HT
 */
public class Person {
    @JsonProperty("tag_id_list")
    private List<String> tag_id_list;
    @JsonProperty("upload_time")
    private Integer upload_time;
    @JsonProperty("person_information")
    private PersonInformation person_information;
    @JsonProperty("face_list")
    private List<FaceListBean> face_list;
    @JsonProperty("identity")
    private String identity;
    @JsonProperty("meta")
    private Map meta;
    @JsonProperty("scene_image_id")
    private String scene_image_id;
    @JsonProperty("person_id")
    private String person_id;

    public List<String> getTag_id_list() {
        return tag_id_list;
    }

    public void setTag_id_list(List<String> tag_id_list) {
        this.tag_id_list = tag_id_list;
    }

    public Integer getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(Integer upload_time) {
        this.upload_time = upload_time;
    }

    public PersonInformation getPerson_information() {
        return person_information;
    }

    public void setPerson_information(PersonInformation person_information) {
        this.person_information = person_information;
    }

    public List<FaceListBean> getFace_list() {
        return face_list;
    }

    public void setFace_list(List<FaceListBean> face_list) {
        this.face_list = face_list;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Map getMeta() {
        return meta;
    }

    public void setMeta(Map meta) {
        this.meta = meta;
    }

    public String getScene_image_id() {
        return scene_image_id;
    }

    public void setScene_image_id(String scene_image_id) {
        this.scene_image_id = scene_image_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    /**
     * 目前判断相同的条件是人名、电话都不变
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person person = (Person) obj;
            return (person.person_information.getName().equals(person_information.getName())
                        && person.person_information.getPhone().equals(person_information.getPhone()));
        } else {
            return super.equals(obj);

        }
    }

    public boolean equalsTag(List<Tag> tags){
        for (String tagId : tag_id_list){
            for(Tag tag : tags){
                if(tagId.equals(tag.getTag_id())){
                    return true;
                }
            }
        }
        return false;
    }
}