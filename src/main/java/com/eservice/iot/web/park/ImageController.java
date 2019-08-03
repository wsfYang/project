package com.eservice.iot.web.park;

import com.eservice.iot.core.Result;
import com.eservice.iot.core.ResultGenerator;
import com.eservice.iot.service.park.ImageService;
import com.eservice.iot.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: yttps_shzx
 * @description:
 * @author: Mr.Zhang
 * @create: 2019-06-14 16:53
 **/
@RestController
public class ImageController {

    @Autowired
    ImageService imageService;

   public static List<String> images=new ArrayList<>();
    @PostMapping("/image/{icNumber}")
    public Result query(@PathVariable String icNumber){
        return ResultGenerator.genSuccessResult(imageService.getImageById(icNumber));
    }

    @PostMapping("/image_quality/verify")
    public Result qualityVerify(@RequestParam MultipartFile avatarFile){
        images.clear();
        StringBuffer  error = new StringBuffer("");
        try {
            String image=ImageUtil.getImageStr(avatarFile.getInputStream());
            String isTrue = imageService.verify(image);
            if ("true".equals(isTrue)) {
                images.add(image);
                return ResultGenerator.genSuccessResult(image);
            }else{
                return ResultGenerator.genFailResult("图片质量不达标："+isTrue);
            }
        }catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("获取上传图片失败！");
        }
    }

}
