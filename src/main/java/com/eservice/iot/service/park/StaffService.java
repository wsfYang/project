package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.core.ResponseCode;
import com.eservice.iot.core.ResponseModel;
import com.eservice.iot.model.park.FaceListBean;
import com.eservice.iot.model.park.Staff;
import com.eservice.iot.model.park.UpdateFace;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author HT
 */
@Component
public class StaffService {

    private final static Logger logger = LoggerFactory.getLogger(StaffService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MqttMessageHelper mqttMessageHelper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private TagService tagService;

    /**
     * Token
     */
    private String token;
    /**
     * 员工列表
     */
    private ArrayList<Staff> staffList = new ArrayList<>();
    private int staffCount = 0;
    /**
     * 每个小时获取一次全量员工信息
     */
    @Scheduled(initialDelay = 1500, fixedRate = 1000*60*60)
    public void fetchStaffScheduled() {
        if ( tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.ACCEPT, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity entity = new HttpEntity(headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/staffs", HttpMethod.GET, entity, String.class);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        String body = responseEntity.getBody();
                        if (body != null) {
                            processStaffResponse(body);
                        } else {
                            fetchStaffScheduled();
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                            fetchStaffScheduled();
                        }
                    }
                }
            } else {
                logger.error("Token is null, fetch policy error!");
            }
        }
    }

    private void processStaffResponse(String body) {
        ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
        if (responseModel != null && responseModel.getResult() != null) {
            ArrayList<Staff> tmpList = (ArrayList<Staff>) JSONArray.parseArray(responseModel.getResult(), Staff.class);
            if (tmpList != null && tmpList.size() != 0) {
                if (tmpList.size() != staffList.size()) {
                    logger.info("The number of staff：{} ==> {}", staffCount,  tmpList.size() );
                    staffCount=tmpList.size();
                }
                staffList = tmpList;
            }
        }
    }

    @Scheduled(initialDelay = 2500, fixedRate = 1000)
    public int showNewestStaffCount(){
        if(staffCount!=staffList.size()){
            logger.info("The number of staff：{} ==> {}", staffCount,  staffList.size());
            staffCount=staffList.size();
        }
        return staffCount;
    }

    public Staff detailStaff(String id){
        if(staffList!=null&&staffList.size()>0){
            for(Staff staff : staffList){
                if(staff.getPerson_information().getId().equals(id)){
                    Staff staffCopy = new Staff();
                    //复制
                    BeanUtils.copyProperties(staff,staffCopy);
                    //设置人脸图片base64
                    if(staff.getFace_list()!=null&&staff.getFace_list().size()>0) {
                        ArrayList<String> images = new ArrayList<>();
                        for (FaceListBean faceListBean : staff.getFace_list()) {
                            images.add(imageService.getImageById(faceListBean.getFace_image_id()));
                        }
                        staffCopy.setFace_image_content_list(images);
                    }
                    staffCopy.setTag_id_list(tagService.getTagName(staff.getTag_id_list()));
                    return staffCopy;
                }
            }
        }else {
            logger.info("The number of staff in the park is 0");
        }
        return null;
    }

    public Staff createStaff(Staff staff) {
        if ( tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HashMap<String, Object> postParameters = new HashMap<>();
                ArrayList<Staff> staffList = new ArrayList<>();

                Map map = new HashMap();
                map.put("update_time",System.currentTimeMillis());
                staff.setMeta(map);

                staffList.add(staff);
                postParameters.put("staff_list", staffList);

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, tokenService.getToken());
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(postParameters), headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/staffs", httpEntity, String.class);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(), ResponseModel.class);
                        if (responseModel != null && responseModel.getRtn() == 0) {
                            List<ResponseModel> responseModels = JSONObject.parseArray(responseModel.getResult(), ResponseModel.class);
                            ResponseModel responseModel1 = responseModels.get(0);
                            if (responseModel1 != null && responseModel1.getRtn() == 0) {
/*
                                mqttMessageHelper.sendToClient("staff/add",staff.getPerson_information().getId());
*/
                                try {
                                    Staff staffAdd = JSONObject.parseObject(responseModel1.getResult(), Staff.class);
                                    //新增新员工数据
                                    this.staffList.add(staffAdd);
                                    return staffAdd;
                                } catch (Exception e) {
                                    logger.warn("JSON to Object failed. ===> {} ", e.getMessage());
                                }
                                fetchStaffScheduled();
                                return new Staff();
                            } else {
                                logger.warn("createStaff failed. ===> {} ", responseModel1.getMessage());
                            }
                        } else {
                            logger.warn("createStaff failed. ===> {} ", responseModel.getMessage());
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                          return   createStaff(staff);
                        }
                    }
                }
            } else {
                logger.error("Token is null, fetch policy error!");
            }
        }
        return null;
    }

    public Staff updateStaff(Staff staff) {
        if ( tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                Map map = new HashMap();
                map.put("update_time",System.currentTimeMillis());
                staff.setMeta(map);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(staff), headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/staffs/" + staff.getStaff_id(), HttpMethod.PUT, httpEntity, String.class);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(),ResponseModel.class);
                        if (responseModel != null && responseModel.getRtn() == 0) {
/*
                            mqttMessageHelper.sendToClient("staff/update",staff.getPerson_information().getId());
*/
                            try {
                                Staff staffUpdate = JSONObject.parseObject(responseModel.getResult(), Staff.class);
                                //移除修改前的员工数据
                                this.staffList.remove(staff);
                                //添加修改后的员工数据
                                this.staffList.add(staffUpdate);
                                return staffUpdate;
                            } catch (Exception e) {
                                logger.warn("JSON to Object failed. ===> {} ", e.getMessage());
                            }
                            fetchStaffScheduled();
                            return new Staff();
                        } else {
                            logger.warn("updateStaff failed. ===> {} ", responseModel.getMessage());
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                           return updateStaff(staff);
                        }
                    }
                }
            } else {
                logger.error("Token is null, fetch policy error!");
            }
        }
        return null;
    }

    public boolean deleteStaff(String id,String staffId) {
        if ( tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.ACCEPT, "application/json");
                headers.add(HttpHeaders.AUTHORIZATION, token);
                HttpEntity entity = new HttpEntity(headers);
                try {
                    ResponseEntity<String> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/staffs/" + staffId, HttpMethod.DELETE, entity, String.class);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        ResponseModel responseModel = JSONObject.parseObject(responseEntity.getBody(), ResponseModel.class);
                        if (responseModel != null && responseModel.getRtn() == 0) {
                            fetchStaffScheduled();
/*
                            mqttMessageHelper.sendToClient("staff/del",id);
*/
                            return true;
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                         return    deleteStaff(id,staffId);
                        }
                    }
                }
            }
        }
        return false;
    }

    public String isExistToPark(String id,List<String> faceId) {
        String staffId=null;
        if(staffList!=null) {
            for (Staff temp : staffList) {
                if (temp.getPerson_information().getId().equals(id)) {
                    staffId = temp.getStaff_id();
                    for (FaceListBean faceListBean : temp.getFace_list()) {
                        faceId.add(faceListBean.getFace_id());
                    }
                }
            }
        }else {
            logger.error(" staffList is null ！");
        }
        return staffId;
    }

    public String getStaffName(String staffId) {
        for (Staff staff : staffList) {
            if (staff.getPerson_information().getId().equalsIgnoreCase(staffId)) {
                return staff.getPerson_information().getName();
            }
        }
        return null;
    }

    public ArrayList<Staff> getStaffList() {
        return staffList;
    }


}
