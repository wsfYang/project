package com.eservice.iot.web;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.shuttle_bus_appointments.ShuttleBusAppointments;
import com.eservice.iot.service.ShuttleBusAppointmentsService;
import com.eservice.iot.service.impl.ShuttleBusAppointmentsServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
* Class Description: xxx
* @author Wang
* @date 2019/07/27.
*/
@RestController
@RequestMapping("/shuttle/bus/appointments")
public class ShuttleBusAppointmentsController {
    @Resource
    private ShuttleBusAppointmentsServiceImpl shuttleBusAppointmentsService;

    @PostMapping("/add")
    public Result add(@RequestBody @NotNull String shuttleBusAppointments) {

       /* try {
            String data = URLDecoder.decode(shuttleBusAppointments,"utf-8");
            shuttleBusAppointments = data.split("=")[1];
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        ShuttleBusAppointments appointments = JSONObject.parseObject(shuttleBusAppointments,ShuttleBusAppointments.class);
        shuttleBusAppointmentsService.save(appointments);
        return ResultGenerator.genSuccessResult();
    }
    @PostMapping("/addApp")
    public Result addApp(String shuttleBusAppointments) {

        /*//1.比较当前车辆是否故障
        //2.判断指定车辆指定时间能否预约
        *//*3.判断当前日期的预约是否在 当前日期的班车预约总数表中
        4.判断当前是否有空位 如果有空位就添加到预约表 并更新班车预约表 预约人数+1 剩余-1
        5.没有就直接返回出去 班车不能预约*/


        ShuttleBusAppointments appointments = JSONObject.parseObject(shuttleBusAppointments,ShuttleBusAppointments.class);
        shuttleBusAppointmentsService.save(appointments);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam String staffId) {
        shuttleBusAppointmentsService.deleteByIds(staffId);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(@RequestBody @NotNull ShuttleBusAppointments shuttleBusAppointments) {
        shuttleBusAppointmentsService.update(shuttleBusAppointments);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam @NotNull Integer id) {
        ShuttleBusAppointments shuttleBusAppointments = shuttleBusAppointmentsService.findById(id);
        return ResultGenerator.genSuccessResult(shuttleBusAppointments);
    }

    @PostMapping("/selectlistApp")
    public Result listApp(String staffId,String startTime,String endTime) {
        List<ShuttleBusAppointments> list = shuttleBusAppointmentsService.selectListInfo(staffId,startTime,endTime);
        return ResultGenerator.genSuccessResult(list);
    }
    @PostMapping("/selectlist")
    public Result list(@RequestBody @NotNull ShuttleBusAppointments busAppointments,@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ShuttleBusAppointments> list = shuttleBusAppointmentsService.selectList(busAppointments);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
