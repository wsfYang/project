package com.eservice.iot.service.impl;

import com.eservice.iot.dao.ShuttleBusInfoMapper;
import com.eservice.iot.model.shuttle_bus_info.ShuttleBusInfo;
import com.eservice.iot.service.ShuttleBusInfoService;
import com.eservice.iot.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wang
* @date 2019/07/27.
*/
@Service
@Transactional
public class ShuttleBusInfoServiceImpl extends AbstractService<ShuttleBusInfo> implements ShuttleBusInfoService {
    @Resource
    private ShuttleBusInfoMapper shuttleBusInfoMapper;

    @Override
    public List<ShuttleBusInfo> selectList(ShuttleBusInfo busInfo) {
        List<ShuttleBusInfo> shuttleBusInfos = shuttleBusInfoMapper.selectList(busInfo);
        return shuttleBusInfos;
    }
}
