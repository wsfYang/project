package com.eservice.iot.web;
import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.shuttle_bus_info.ShuttleBusInfo;
import com.eservice.iot.service.ShuttleBusInfoService;
import com.eservice.iot.service.impl.ShuttleBusInfoServiceImpl;
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
@RequestMapping("/shuttle/bus/info")
public class ShuttleBusInfoController {
    @Resource
    private ShuttleBusInfoServiceImpl shuttleBusInfoService;

    @PostMapping("/add")
    public Result add(@RequestBody @NotNull ShuttleBusInfo shuttleBusInfo) {
        shuttleBusInfoService.save(shuttleBusInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        shuttleBusInfoService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(@RequestBody @NotNull ShuttleBusInfo shuttleBusInfo) {
        shuttleBusInfoService.update(shuttleBusInfo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam @NotNull Integer id) {
        ShuttleBusInfo shuttleBusInfo = shuttleBusInfoService.findById(id);
        return ResultGenerator.genSuccessResult(shuttleBusInfo);
    }

    @PostMapping("/selectlist")
    public Result list(@RequestBody @NotNull  ShuttleBusInfo busInfo, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<ShuttleBusInfo> list = shuttleBusInfoService.selectList(busInfo);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
