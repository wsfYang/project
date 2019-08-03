package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.core.Constant;
import com.eservice.iot.core.ResponseCode;
import com.eservice.iot.core.ResponseModel;
import com.eservice.iot.model.park.Tag;
import com.eservice.iot.service.common.MqttMessageHelper;
import com.eservice.iot.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author HT
 */
@Component
public class TagService {

    private final static Logger logger = LoggerFactory.getLogger(TagService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;
    @Value("${broker_host}")
    private String HOST;
    @Value("${listener_port}")
    private Integer PORT;
    @Value("${head_tag}")
    private String headTag;
    @Value("${inter_tag}")
    private String interTag;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MqttMessageHelper mqttMessageHelper;

    private ThreadPoolTaskExecutor mExecutor;

    /**
     * Token
     */
    private String token;
    /**
     * 访客tag
     */
    private List<Tag> allTagList = new ArrayList<>();
    /**
     * 访客tag
     */
    private List<Tag> visitorTagList = new ArrayList<>();

    /**
     * 员工tag
     */
    private List<Tag> staffTagList = new ArrayList<>();

    /**
     * 每60分钟更新一次TAG
     */
    @Scheduled(initialDelay = 1000,fixedRate = 1000*60)
    public void fetchTags() {
        if (Util.isPortUsing(HOST,PORT) && tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.ACCEPT, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity entity = new HttpEntity(headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/tags?size=0", HttpMethod.GET, entity, String.class);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        String body = responseEntity.getBody();
                        if (body != null) {
                            processTagResponse(body);
                        }
                    }
                } catch (HttpClientErrorException errorException) {
                    if (errorException.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        //token失效,重新获取token后再进行数据请求
                        token = tokenService.getToken();
                        if (token != null) {
                            fetchTags();
                        }
                    }
                }
            }

        } else {
            ///等待tokenService初始化完成，TAG标签被其他很多service依赖，所以需要其先初始化完毕后
            if (mExecutor == null) {
                mExecutor = new ThreadPoolTaskExecutor();
                mExecutor.setCorePoolSize(1);
                mExecutor.setMaxPoolSize(2);
                mExecutor.setThreadNamePrefix("YTTPS-");
                mExecutor.initialize();
            }
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        fetchTags();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void processTagResponse(String body) {
        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
        if (responseModel != null && responseModel.getResult() != null) {
            List<Tag> tmpList =JSONArray.parseArray(responseModel.getResult(), Tag.class);
            if (tmpList != null &&tmpList.size()>0) {
                ArrayList<Tag> visitorTagList = new ArrayList<>();
                ArrayList<Tag> staffTagList = new ArrayList<>();
                for (Tag tag : tmpList) {
                    for (String str : tag.getVisible_identity()) {
                        if (Constant.VISITOR.equals(str)) {
                            visitorTagList.add(tag);
                        }
                        if (Constant.STAFF.equals(str)) {
                            staffTagList.add(tag);
                        }
                    }
                }
                if(this.allTagList.size()!=tmpList.size()){
                    logger.info("The number of allTagList：    {} ==> {}", this.allTagList.size(), tmpList.size());
                    logger.info("The number of visitorTagList：{} ==> {}", this.visitorTagList.size(), visitorTagList.size());
                    logger.info("The number of staffTagList：  {} ==> {}", this.staffTagList.size(), staffTagList.size());
                }
                this.allTagList = tmpList;
                this.visitorTagList = visitorTagList;
                this.staffTagList = staffTagList;
            }
        }
    }

    public Tag createTag(Tag tag) {
        if (Util.isPortUsing(HOST,PORT) && tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HashMap<String, Object> postParameters = new HashMap<>();
                ArrayList<Tag> tagList = new ArrayList<>();
                tagList.add(tag);
                postParameters.put("tag_list", tagList);

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, tokenService.getToken());
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/tags", httpEntity, String.class);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(), ResponseModel.class);
                        if (responseModel != null && responseModel.getRtn() == 0) {
                            List<ResponseModel> responseModels = JSONObject.parseArray(responseModel.getResult(), ResponseModel.class);
                            ResponseModel responseModel1 = responseModels.get(0);
                            if (responseModel1 != null && responseModel1.getRtn() == 0) {
                                fetchTags();
                                try {
                                    return JSONObject.parseObject(responseModel1.getResult(), Tag.class);
                                } catch (Exception e) {
                                    logger.warn("JSON to Object failed. ===> {} ", e.getMessage());
                                }
                                return new Tag();
                            } else {
                                logger.warn("createTag failed. ===> {} ", responseModel1.getMessage());
                            }
                        } else {
                            logger.warn("createTag failed. ===> {} ", responseModel.getMessage());
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                            return createTag(tag);
                        }
                    }
                }
            } else {
                logger.error("Token is null, fetch policy error!");
            }
        }
        return null;
    }

    public Tag updateTag(Tag tag ) {
        if (Util.isPortUsing(HOST, PORT) && tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, tokenService.getToken());
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(tag), headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/tags/" + tag.getTag_id(), HttpMethod.PUT, httpEntity, String.class);
                    fetchTags();
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(),ResponseModel.class);
                        if (responseModel != null && responseModel.getRtn() == 0) {
                            fetchTags();
                            try {
                                return JSONObject.parseObject(responseModel.getResult(), Tag.class);
                            } catch (Exception e) {
                                logger.warn("JSON to Object failed. ===> {} ", e.getMessage());
                            }
                            return new Tag();
                        } else {
                            logger.warn("updateTag failed. ===> {} ", responseModel.getMessage());
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                            return updateTag(tag);
                        }
                    }
                }
            } else {
                logger.error("Token is null, fetch policy error!");
            }
        }
        return null;
    }

    /**
     * 根据tagName获取 TagId
     *
     * @param stringList 机构名称集合
     * @return 机构id集合
     */
    public ArrayList<String> getStaffTagId(String[] stringList) {
        ArrayList<String> idList = new ArrayList<>();
        for (String str : stringList) {
            isExistAndAdd(str);
            for (Tag tag : staffTagList) {
                if (str.equals(tag.getTag_name())) {
                    idList.add(tag.getTag_id());
                }
            }
        }
        return idList;
    }

    /**
     * 根据tagId获取tagName
     * @param tagIds
     * @return
     */
    public ArrayList<String> getTagName(List<String> tagIds){
        ArrayList<String> tagNames = new ArrayList<>();
        for(String tagId : tagIds){
            for(Tag tag : allTagList){
                if(tagId.equals(tag.getTag_id())){
                    tagNames.add(tag.getTag_name());
                    break;
                }
            }
        }
        return tagNames;
    }

    /**
     * 判断标签是否存在，不存在则先新增
     * @param tagName
     */
    public boolean isExistAndAdd(String tagName) {
        boolean isExist = false;
        for (Tag tag : staffTagList) {
            if (tagName.equals(tag.getTag_name())) {
                isExist = true;
            }
        }
        if (!isExist) {
            ArrayList<String> identity = new ArrayList<>();
            identity.add(Constant.STAFF);
            createTag(new Tag(tagName,identity,null));
        }
        return isExist;
    }

    /**
     * 判断标签是否存在,
     * @param tagName
     * @return
     */
    public Tag isExist(String tagName) {
        if(!"".equals(tagName)) {
            for (Tag tag : allTagList) {
                if (tagName.equals(tag.getTag_name())) {
                    return tag;
                }
            }
        }
    return null;
    }

    public List<Tag> getAllTagList() {
        return allTagList;
    }

    public List<Tag> getVisitorTagList() {
        return visitorTagList;
    }

    public List<Tag> getStaffTagList() {
        return staffTagList;
    }

}
