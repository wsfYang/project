package com.eservice.iot.web.park;

import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.park.Visitor;
import com.eservice.iot.service.park.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/visitor")
public class VisitorController {

    @Autowired
    VisitorService visitorService;

    @PostMapping("/list")
    public Result list(){
        List<Visitor> visitors = visitorService.findAll();
        return ResultGenerator.genSuccessResult(visitors);
    }

    @GetMapping("/detail")
    public Result detail(String name,String teeName,Long time){
        Visitor visitor = visitorService.detailVisitor(name,teeName,time);
        return ResultGenerator.genSuccessResult(visitor);
    }

}
