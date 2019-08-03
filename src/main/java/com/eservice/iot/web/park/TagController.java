package com.eservice.iot.web.park;

import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.park.Staff;
import com.eservice.iot.model.park.Tag;
import com.eservice.iot.service.park.StaffService;
import com.eservice.iot.service.park.TagService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Description: xxx
 *
 * @author Wilson Hu
 * @date 2018/08/21.
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Resource
    private TagService tagService;
    @Resource
    private StaffService staffService;

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page,size);
        List<Tag> accessPolicies = tagService.getAllTagList();
        PageInfo pageInfo = new PageInfo(accessPolicies);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @PostMapping("/list/{icNumber}")
    public Result query(@PathVariable List<String> icNumber){
        return ResultGenerator.genSuccessResult(tagService.getTagName(icNumber));
    }

    @PostMapping("/add")
    public Result add(@RequestParam String name, @RequestParam String identity) {

        if (tagService != null && name != null && identity != null) {
            if ( tagService.getAllTagList().size() > 0) {
                for (int i = 0; i <tagService.getAllTagList().size(); i++) {
                    if(tagService.getAllTagList().get(i).getTag_name().equals(name)) {
                        return ResultGenerator.genFailResult("Tag名称已存在！");
                    }
                }
            }
            if(tagService.createTag(new Tag())!=null) {
                return ResultGenerator.genSuccessResult();
            } else {
                return ResultGenerator.genFailResult("创建Tag失败！");
            }
        } else {
            if( name == null || identity == null) {
                return ResultGenerator.genFailResult("Tag名称或identity不能为空！");
            } else {
                return ResultGenerator.genFailResult("Tag服务不存在！");
            }
        }
    }

    @GetMapping("/deleteStaffByTagName")
    public Result deleteStaffByTagName(@RequestParam String name) {
        if(name == null || "".equals(name)) {
            return ResultGenerator.genFailResult("标签名字不能为空！");
        } else {
            if(tagService == null) {
                return ResultGenerator.genFailResult("标签服务没有启动！");
            } else {
                List<Tag> allTagList = tagService.getAllTagList();
                String targetTagId = null;
                for (Tag item: allTagList) {
                    if(item.getTag_name().equals(name)) {
                        targetTagId = item.getTag_id();
                        break;
                    }
                }
                if(targetTagId == null) {
                    return ResultGenerator.genFailResult("找不到标签名字！");
                } else {
                    ArrayList<Staff> allStaffList = staffService.getStaffList();
                    ArrayList<Staff> allDeleteStaffList = new ArrayList<>();
                    for (Staff item: allStaffList) {
                        if(item.getTag_id_list().contains(targetTagId)) {
                            allDeleteStaffList.add(item);
                        }
                    }
                    int deleteCount = 0;
                    String resultStr = "";
                    ArrayList<String> failedList = new ArrayList<>();
                    resultStr += "需删除staff总数：" + allDeleteStaffList.size();
                    for (int i = 0; i < allDeleteStaffList.size(); i++) {
                        if(staffService.deleteStaff(allDeleteStaffList.get(i).getPerson_information().getId(),allDeleteStaffList.get(i).getStaff_id())) {
                            deleteCount++;
                        } else {
                            failedList.add(allDeleteStaffList.get(i).getPerson_information().getName());
                        }
                    }
                    resultStr += "; 删除成功staff总数：" + deleteCount;
                    resultStr += "; 删除失败staff数：" + failedList.size();
                    resultStr += "; 失败列表：" + failedList.toString();
                    return ResultGenerator.genSuccessResult(resultStr);
                }
            }
        }
    }

}
