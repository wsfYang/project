package com.eservice.iot.service.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.core.ResponseCode;
import com.eservice.iot.core.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 人脸质量检测
 */
@Component
public class ImageService {
    private final  static Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Value("${park_base_url}")
    private String PARK_BASE_URL;
    @Value("${broker_host}")
    private String HOST;
    @Value("${listener_port}")
    private Integer PORT;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;

    private String token;

    public String verify(String img ) {
        if ( tokenService != null) {
            if (token == null) {
                token = tokenService.getToken();
            }
            if (token != null) {
                    List demo = new ArrayList();
                    demo.add(img);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
                    headers.add(HttpHeaders.AUTHORIZATION, token);
                    HttpEntity httpEntity = new HttpEntity<>(JSON.toJSONString(demo), headers);
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity(PARK_BASE_URL + "/image_quality/verify", httpEntity, String.class);
                    String body = responseEntity.getBody();
                    ResponseModel responseModel = JSONObject.parseObject(body, ResponseModel.class);
                    if(responseModel.getRtn()==0){
                        String result = responseModel.getResult();
                        Map<String, Object> imageMap = JSONObject.parseObject(result.substring(result.indexOf("[") + 1, result.indexOf("]")));
                        for (Map.Entry<String, Object> entry : imageMap.entrySet()) {
                            switch (entry.getKey()) {
                                case "pixel_too_small":
                                case "no_face":
                                case "multi_face":
                                    if(Boolean.parseBoolean(entry.getValue().toString())){
                                        return entry.getKey();
                                    }
                                default:
                                    break;
                            }
                        }
                        return "true";
                    }else {
                        logger.error(""+responseModel.getMessage());
                        return  responseModel.getMessage();
                    }
            } else {
                logger.error("Token is null, fetch policy error!");
            }
        }
        return "Port or service not started";
    }

    public String getImageById(String id) {

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
                    ResponseEntity<byte[]> responseEntity = restTemplate.exchange(PARK_BASE_URL + "/image/" + id, HttpMethod.GET, entity, byte[].class);
                    if (responseEntity.getStatusCodeValue() == ResponseCode.OK) {
                        byte[] body = responseEntity.getBody();
                        if (body != null) {
                            //对字节数组Base64编码
                            BASE64Encoder encoder = new BASE64Encoder();
                            //返回Base64编码过的字节数组字符串
                            String imageBase64 = encoder.encode(body);
                            if (imageBase64 != null&&!"".equals(imageBase64)) {
                                return imageBase64.replaceAll("[\\s*\t\n\r]", "");
                            }
                        }
                    }
                } catch (HttpClientErrorException exception) {
                    if (exception.getStatusCode().value() == ResponseCode.TOKEN_INVALID) {
                        token = tokenService.getToken();
                        if (token != null) {
                            return getImageById(id);
                        }
                    }
                }
            }
        }
        return null;
    }
}
