package com.eservice.iot.web;
import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.shuttle_bus_staff.ShuttleBusStaff;
import com.eservice.iot.service.ShuttleBusStaffService;
import com.eservice.iot.service.impl.ShuttleBusStaffServiceImpl;
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
@RequestMapping("/shuttle/bus/staff")
public class ShuttleBusStaffController {
    @Resource
    private ShuttleBusStaffServiceImpl shuttleBusStaffService;

    @PostMapping("/add")
    public Result add(@RequestBody @NotNull ShuttleBusStaff shuttleBusStaff) {
        shuttleBusStaffService.save(shuttleBusStaff);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam String staffId) {
        shuttleBusStaffService.deleteByIds(staffId);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(@RequestBody @NotNull ShuttleBusStaff shuttleBusStaff) {
        shuttleBusStaffService.update(shuttleBusStaff);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam @NotNull Integer id) {
        ShuttleBusStaff shuttleBusStaff = shuttleBusStaffService.findById(id);
        return ResultGenerator.genSuccessResult(shuttleBusStaff);
    }

    @PostMapping("/selectlist")
    public Result list(@RequestBody @NotNull ShuttleBusStaff busStaff ,@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ShuttleBusStaff> list = shuttleBusStaffService.selectList(busStaff);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/selectlistApp")
    public Result listApp(ShuttleBusStaff busStaff ,@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ShuttleBusStaff> list = shuttleBusStaffService.selectList(busStaff);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/findbyId")
    public Result findbyId(String staffId) {
        List<ShuttleBusStaff> list = shuttleBusStaffService.findByIds(staffId);
        return ResultGenerator.genSuccessResult(list);
    }
}
