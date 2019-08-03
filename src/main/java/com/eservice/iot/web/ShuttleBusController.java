package com.eservice.iot.web;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.shuttle_bus.ShuttleBus;
import com.eservice.iot.model.web.StaffDTO;
import com.eservice.iot.service.ShuttleBusService;
import com.eservice.iot.service.impl.ShuttleBusServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* Class Description: xxx
* @author Wang
* @date 2019/07/27.
*/
@RestController
@RequestMapping("/shuttle/bus")
public class ShuttleBusController {
    @Resource
    private ShuttleBusServiceImpl shuttleBusService;

    @PostMapping("/add")
    public Result add(@RequestBody @NotNull String shuttleBus) {
        ShuttleBus shuttleBuss = JSONObject.parseObject(shuttleBus,ShuttleBus.class);
        shuttleBusService.save(shuttleBuss);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam String licenceNum) {
         shuttleBusService.deleteByIds(licenceNum);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(@RequestBody @NotNull ShuttleBus shuttleBus) {
        shuttleBusService.update(shuttleBus);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam @NotNull Integer id) {
        ShuttleBus shuttleBus = shuttleBusService.findById(id);
        return ResultGenerator.genSuccessResult(shuttleBus);
    }

    @PostMapping("/selectlist")
    public Result list(@RequestBody @NotNull  ShuttleBus shuttleBus,  @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ShuttleBus> list = shuttleBusService.selectList(shuttleBus);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/selectlistApp")
    public Result listApp(ShuttleBus shuttleBus,  @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ShuttleBus> list = shuttleBusService.selectList(shuttleBus);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/getBusNum")
    public Result getBusNum() {
        List<ShuttleBus> list = shuttleBusService.getBusNum();
        return ResultGenerator.genSuccessResult(list);
    }



}
