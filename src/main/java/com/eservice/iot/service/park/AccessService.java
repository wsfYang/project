package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.core.ResponseCode;
import com.eservice.iot.core.ResponseModel;
import com.eservice.iot.model.park.AccessRecord;
import com.eservice.iot.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author HT
 */
@Component
public class AccessService {

    private final static Logger logger = LoggerFactory.getLogger(AccessService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;
    @Value("${broker_host}")
    private String HOST;
    @Value("${listener_port}")
    private Integer PORT;

    private static ArrayList<String> recordExistList = new ArrayList<>();

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Token
     */
    private String token;

    @Autowired
    private TokenService tokenService;

    @Scheduled(cron = "0 59 23 * * ?")
    public void setRecordExistList() {
        logger.info("\n================ 清空去重记录 ===============");
        if(recordExistList.size() > 0) {
            logger.info("Clear attendance record. size: {}", recordExistList.size());
            recordExistList.clear();
        }
    }

    public ArrayList<AccessRecord> querySignInStaff(Long startTime, Long endTime) {
            if ( tokenService != null) {
                if(token==null){
                    token = tokenService.getToken();
                }
                if (token != null) {
                    HashMap<String, Object> postParameters = new HashMap<>();
                    //考勤记录查询开始时间
                    postParameters.put("start_timestamp", startTime);
                    //考勤记录查询结束时间
                    postParameters.put("end_timestamp", endTime);
                    //只获取员工数据
                    ArrayList<String> identity = new ArrayList<>();
                    identity.add("STAFF");
                    postParameters.put("identity_list", identity);

                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    headers.add(HttpHeaders.AUTHORIZATION, token);
                    HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
                    try {
                        ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/access/record", httpEntity, String.class);
                        if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                            String body = responseEntity.getBody();
                            if (body != null) {
                                ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                                if (responseModel != null && responseModel.getResult() != null) {
                                    ArrayList<AccessRecord> tempList = (ArrayList<AccessRecord>) JSONArray.parseArray(responseModel.getResult(), AccessRecord.class);
                                    //去重
                                    tempList = romeRecord(tempList);
                                    return tempList;
                                }
                            }
                        }
                    } catch (HttpClientErrorException exception) {
                        if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                            //token失效,重新获取token后再进行数据请求
                            token = tokenService.getToken();
                            querySignInStaff(startTime, endTime);
                        }
                        logger.error(exception.getMessage());
                    }
                }else {
                    logger.error("Token is null, fetch policy error!");
                }
            }
        return null;
    }

    public ArrayList<AccessRecord> romeRecord(ArrayList<AccessRecord> recordList){
        Collections.reverse(recordList);
        ArrayList<AccessRecord> temp = new ArrayList<>();
        for (AccessRecord accessRecord : recordList) {
            if (!recordExistList.contains(accessRecord.getScene_image_id())) {
                recordExistList.add(accessRecord.getScene_image_id());
                temp.add(accessRecord);
            }
        }
        return temp;
    }
}
