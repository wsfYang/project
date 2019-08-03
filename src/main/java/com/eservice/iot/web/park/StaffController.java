package com.eservice.iot.web.park;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.model.park.FaceListBean;
import com.eservice.iot.model.park.PersonInformation;
import com.eservice.iot.model.park.Staff;
import com.eservice.iot.model.park.UpdateFace;
import com.eservice.iot.model.web.StaffDTO;
import com.eservice.iot.service.common.MqttMessageHelper;
import com.eservice.iot.service.park.ImageService;
import com.eservice.iot.service.park.StaffService;
import com.eservice.iot.service.park.TagService;
import com.eservice.iot.util.ImageUtil;
import com.eservice.iot.util.Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.squareup.moshi.Json;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: yttps_shzx
 * @description: 园区人员操作
 * @author: Mr.Zhang
 * @create: 2019-06-10 11:39
 **/
@RestController
@RequestMapping("/staffs")
public class StaffController {

    @Value("${image_path}")
    private String path;

    @Autowired
    StaffService staffService;
    @Autowired
    private TagService tagService;

    @PostMapping("/cloud/list")
    public Result cloudList(){
        ArrayList<Staff> staffs = staffService.getStaffList();
        return ResultGenerator.genSuccessResult(staffs);
    }

    @GetMapping("/cloud/detail")
    public Result cloudDetail(String id){
        Staff staff = staffService.detailStaff(id);
        return ResultGenerator.genSuccessResult(staff);
    }
    @PostMapping("/list")
    public Result list(String classRoom,@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size){
        PageHelper.startPage(page,size);
        List<Staff> staffs = staffService.getStaffList();
        List<Staff> list = new ArrayList<>(16);
        if(classRoom==null||"".equals(classRoom)){
            list=staffs;
        }else {
            for (Staff staff:staffs){
                if(classRoom.equals(staff.getPerson_information().getRemark())){
                    list.add(staff);
                }
            }
        }
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(Util.pagingQuery(page,size,list));
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    @PostMapping("/delete")
    public Result delete(String id,String staffId){
        return ResultGenerator.genSuccessResult(staffService.deleteStaff(id,staffId));
    }

    @PostMapping("/add")
    public Result add(String staffDTO){
        StaffDTO staffDTO1 = JSON.parseObject(staffDTO,StaffDTO.class);
        Staff staff = new Staff();
        ArrayList<String> list =tagService.getStaffTagId(staffDTO1.getTagName().replaceAll("(\"|\\[|\\])","").split(","));

        PersonInformation personInformation = new PersonInformation();
        personInformation.setId(staffDTO1.getId());
        personInformation.setRemark(staffDTO1.getRemark());
        personInformation.setPhone(staffDTO1.getPhone());
        personInformation.setName(staffDTO1.getName());
      /*  personInformation.setEmployed_date(staffDTO1.getEmployedDate());
        personInformation.setBirthday(staffDTO1.getBirthday());*/
        staff.setTag_id_list(list);
        staff.setCard_numbers(staffDTO1.getCardNumbers());
        staff.setPerson_information(personInformation);
        staff.setFace_image_content_list(ImageController.images);
        return ResultGenerator.genSuccessResult(staffService.createStaff(staff));
    }


    @PostMapping("/update")
    public Result update(String date){
        StaffDTO staffDTO1 = JSON.parseObject(date,StaffDTO.class);
        Staff staff =  JSON.parseObject(date,Staff.class);
        ArrayList<String> list =tagService.getStaffTagId(staffDTO1.getTagName().split(","));

        List<String> faceId = new ArrayList<>();
        //返回staffId
        String staffId = staffService.isExistToPark(staff.getPerson_information().getId(), faceId);

        if(staffId!=null) {
            UpdateFace updateFace = new UpdateFace();
            //原有的人脸图片id
            updateFace.setDelete_face_id_list(faceId);
            //存入新的人脸图片
            updateFace.setInsert_face_image_content_list(ImageController.images);
            //强制更新
            updateFace.setEnforce(true);
            staff.setUpdate_face(updateFace);
            staff.setTag_id_list(list);
            if(staffService.updateStaff(staff)!=null){
                return ResultGenerator.genSuccessResult("true");
            }
        }
        return ResultGenerator.genSuccessResult("false");
    }

    @GetMapping("/image")
    public Result image() {
        File file = new File(path);        //获取其file对象
        File[] fs = file.listFiles();    //遍历path下的文件和目录，放在File数组中
        for (File f : fs) {                    //遍历File[]数组
            if (!f.isDirectory()) {    //若非目录(即文件)，则打印
                String tag = f.getName();
                String name = tag.substring(tag.indexOf(" "),tag.indexOf("."));
                tag=tag.substring(0,tag.indexOf(" "));
                Staff staff = new Staff();
                ArrayList<String> list = tagService.getStaffTagId(new String[]{tag});
                PersonInformation personInformation = new PersonInformation();
                personInformation.setId(""+System.currentTimeMillis());
                personInformation.setName(name);
                staff.setTag_id_list(list);
                staff.setPerson_information(personInformation);
                List<String> images = new ArrayList<>();
                images.add(ImageUtil.getImageStr(path+f.getName()));
                staff.setFace_image_content_list(images);
                staffService.createStaff(staff);
            }
        }
        return ResultGenerator.genSuccessResult( );
    }
}
