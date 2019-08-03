package com.eservice.iot.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.core.ResponseCode;
import com.eservice.iot.core.ResponseModel;
import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.Passenger;
import com.eservice.iot.model.shuttle_bus.ShuttleBus;
import com.eservice.iot.model.shuttle_bus_appointments.ShuttleBusAppointments;
import com.eservice.iot.model.shuttle_bus_info.ShuttleBusInfo;
import com.eservice.iot.model.shuttle_bus_staff.ShuttleBusStaff;
import com.eservice.iot.service.ShuttleBusAppointmentsService;
import com.eservice.iot.service.ShuttleBusInfoService;
import com.eservice.iot.service.ShuttleBusService;
import com.eservice.iot.service.ShuttleBusStaffService;
import com.eservice.iot.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/shuttle/bus/passenger")
public class ShuttleBusAutoController {
    private final static Logger logger = LoggerFactory.getLogger(ShuttleBusAutoController.class);

    @Resource
    ShuttleBusStaffService shuttleBusStaffService;
    @Resource
    ShuttleBusAppointmentsService shuttleBusAppointmentsService;

    @Resource
    ShuttleBusService shuttleBusService;

    //发送请求类
    @Autowired
    private RestTemplate restTemplate;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



    @PostMapping("/surplus")
    public Result surplusPassengerNumber(@RequestParam String showTime) {
        int [] numbers = new int[18];
        try {
            long time = simpleDateFormat.parse(showTime).getTime();
            for (int i = 1; i <= 18; i++) {
                ShuttleBus shuttleBus = shuttleBusService.findBy("busNum", i);
                if (shuttleBus != null) {
                    Condition condition = new Condition(ShuttleBusStaff.class);
                    condition.createCriteria()
                            .andEqualTo("busNum", i)
                            .andEqualTo("type", 0);
                    List<ShuttleBusStaff> shuttleBusStaffs = shuttleBusStaffService.findByCondition(condition);

                    condition = new Condition(ShuttleBusAppointments.class);
                    Example.Criteria criteria = condition.createCriteria();
                    criteria.andEqualTo("busNum", i);
                    long restTime = simpleDateFormat.parse(sdf.format(time)+" 17:00:00").getTime();
                    long workTime = simpleDateFormat.parse(sdf.format(time)+" 7:00:00").getTime();
                    if (time <= workTime ) {
                        restTime -= 24 * 60 * 60 * 1000L;
                        criteria.andBetween("appointmentsTime", new Date(restTime), new Date(time));
                    } else if(time <= restTime){
                        criteria.andBetween("appointmentsTime", new Date(workTime), new Date(time));
                    }else {
                        criteria.andBetween("appointmentsTime", new Date(restTime), new Date(time));
                    }
                    List<ShuttleBusAppointments> shuttleBusAppointments = shuttleBusAppointmentsService.findByCondition(condition);
                    int number = shuttleBus.getTotalSeat() -shuttleBusStaffs.size() - shuttleBusAppointments.size();
                    numbers[i]=(number);
            }

            }
            return ResultGenerator.genSuccessResult(numbers);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("日期格式错误！" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("数据查询出错！" + e.getMessage());
        }
    }

}
