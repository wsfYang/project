package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.core.ResponseCode;
import com.eservice.iot.core.ResponseModel;
import com.eservice.iot.model.park.FaceListBean;
import com.eservice.iot.model.park.Visitor;
import com.eservice.iot.service.common.MqttMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VisitorService {
    private final static Logger logger = LoggerFactory.getLogger(VisitorService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MqttMessageHelper mqttMessageHelper;
    @Autowired
    private ImageService imageService;
    @Autowired
    private TagService tagService;

    private String token;
    private int visitorCount=0;
    private List<Visitor> visitors;


    @Scheduled(cron = "0 59 23 * * ?")
    public void autoCheckOut() {
        logger.info("开始执行访客签出");
        int count = 0;
        if(visitors == null || visitors.size() == 0){
            queryVisitor();
        }
        if (visitors != null && visitors.size() > 0) {
            for(Visitor visitor : visitors){
               if( checkOut(visitor.getVisitor_id())!=null){
                   count++;
               }
            }
        } else {
            logger.warn("visitors size ==> 0");
        }
        logger.info("访客成功签出 ==> {} ",count);

    }

    @Scheduled(initialDelay = 1000, fixedRate = 1000 * 60)
    private void queryVisitor() {
        if (token == null) {
            token = tokenService.getToken();
        }
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.ACCEPT, "application/json");
            headers.add("Authorization", token);
            HttpEntity entity = new HttpEntity(headers);
            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/visitors/", HttpMethod.GET, entity, String.class);
                if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                    String body = responseEntity.getBody();
                    ResponseModel responseModel = JSON.parseObject(body, ResponseModel.class);
                    List<Visitor> visitor = JSON.parseArray(responseModel.getResult(), Visitor.class);
                    this.visitors = visitor;
                }
            } catch (HttpClientErrorException errorException) {
                if (errorException.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                    token = tokenService.getToken();
                    queryVisitor();
                }
            }
        } else {
            logger.error("Token is null, fetch visitor list error!");
        }
    }

    @Scheduled(initialDelay = 2500, fixedRate = 1000)
    public int showNewestStaffCount(){
        if(visitorCount!=visitors.size()){
            logger.info("The number of staff：{} ==> {}", visitorCount,  visitors.size());
            visitorCount=visitors.size();
        }
        return visitorCount;
    }

    public Visitor detailVisitor(String name ,String teeName ,Long time){
        if(visitors!=null&&visitors.size()>0){
            for(Visitor visitor : visitors) {
                if (visitor.getPerson_information().getName().equals(name)
                        && visitor.getPerson_information().getVisitee_name().equals(teeName)
                        && visitor.getPerson_information().getVisit_start_timestamp().equals(time)) {

                    Visitor visitorCopy = new Visitor();
                    BeanUtils.copyProperties(visitorCopy,visitor);
                    //设置人脸图片base64
                    if(visitor.getFace_list()!=null&&visitor.getFace_list().size()>0) {
                        for (FaceListBean faceListBean : visitor.getFace_list()) {
                            visitorCopy.setFace_image_content(imageService.getImageById(faceListBean.getFace_id()));
                        }
                    }
                    visitorCopy.setTag_id_list(tagService.getTagName(visitorCopy.getTag_id_list()));
                    return visitorCopy;
                }
            }
        }else {
            logger.info("The number of visitor in the park is 0");
        }
        return null;
    }

    public Visitor createVisitor(Visitor visitor) {
        if (tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HashMap<String, Object> postParameters = new HashMap<>();
                ArrayList<Visitor> visitors = new ArrayList<>();
                Map map = new HashMap();
                map.put("update_time",System.currentTimeMillis());
                visitor.setMeta(map);
                visitors.add(visitor);
                postParameters.put("visitor_list", visitors);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, tokenService.getToken());
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/visitors", httpEntity, String.class);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(), ResponseModel.class);
                        if (responseModel != null && responseModel.getResult() != null) {
                            List<ResponseModel> responseModels = JSONObject.parseArray(responseModel.getResult(), ResponseModel.class);
                            ResponseModel responseModel1 = responseModels.get(0);
                            if (responseModel1 != null && responseModel1.getRtn() == 0) {
                                mqttMessageHelper.sendToClient("visitor/add",visitor.getPerson_information().getName());
                                try {
                                    Visitor visitorAdd = JSONObject.parseObject(responseModel1.getResult(), Visitor.class);
                                    this.visitors.add(visitorAdd);
                                    return  visitorAdd;
                                } catch (Exception e) {
                                    logger.warn("JSON to Object failed. ===> {} ", e.getMessage());
                                }
                                queryVisitor();
                                return new Visitor();
                            } else {
                                logger.warn("createVisitor failed. ===> {} ", responseModel1.getMessage());
                            }
                        } else {
                            logger.warn("createVisitor failed. ===> {} ", responseModel.getMessage());
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                            return createVisitor(visitor);
                        }
                    }
                }
            } else {
                logger.error("Token is null, fetch policy error!");
            }
        }
        return null;
    }

    public Visitor updateVisitor(Visitor visitor){
        if ( tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                Map map = new HashMap();
                map.put("update_time",System.currentTimeMillis());
                visitor.setMeta(map);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(visitor), headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/visitors/" + visitor.getVisitor_id(), HttpMethod.PUT, httpEntity, String.class);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(),ResponseModel.class);
                        if (responseModel != null && responseModel.getRtn() == 0) {
                            mqttMessageHelper.sendToClient("visitor/update",visitor.getPerson_information().getName());
                            try {
                                Visitor visitorUpdate = JSONObject.parseObject(responseModel.getResult(), Visitor.class);
                                this.visitors.remove(visitor);
                                this.visitors.add(visitorUpdate);
                                return visitorUpdate;
                            } catch (Exception e) {
                                logger.warn("JSON to Object failed. ===> {} ", e.getMessage());
                            }
                            queryVisitor();
                            return new Visitor();
                        } else {
                            logger.warn("updateVisitor failed. ===> {} ", responseModel.getMessage());
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                            return updateVisitor(visitor);
                        }
                    }
                }
            } else {
                logger.error("Token is null, fetch policy error!");
            }
        }
        return null;
    }

    public Visitor checkOut(String id) {
        if (tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity httpEntity = new HttpEntity<>(headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/visitors/checkout/" + id, HttpMethod.DELETE, httpEntity, String.class);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(), ResponseModel.class);
                        if (responseModel != null && responseModel.getResult() != null) {
                           Visitor checkVisitor =  JSONObject.parseObject(responseModel.getResult(), Visitor.class);
                            mqttMessageHelper.sendToClient("visitor/del",checkVisitor.getPerson_information().getName());
                           this.visitors.remove(checkVisitor);
                                return   checkVisitor;
                        } else {
                            logger.warn("createVisitor failed. ===> {} ", responseModel.getMessage());
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                            return checkOut(id);
                        }
                    }
                }
            } else {
                logger.error("Token is null, fetch policy error!");
            }
        }
        return null;
    }

    public List<Visitor> findAll() {
        return visitors;
    }
}
