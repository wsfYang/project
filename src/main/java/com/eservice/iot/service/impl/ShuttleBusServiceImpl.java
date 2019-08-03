package com.eservice.iot.service.impl;

import com.eservice.iot.dao.ShuttleBusMapper;
import com.eservice.iot.model.shuttle_bus.ShuttleBus;
import com.eservice.iot.model.shuttle_bus_info.ShuttleBusInfo;
import com.eservice.iot.service.ShuttleBusService;
import com.eservice.iot.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.crypto.KeyUsage;

import javax.annotation.Resource;
import java.util.List;


/**
* Class Description: xxx
* @author Wang
* @date 2019/07/27.
*/
@Service
@Transactional
public class ShuttleBusServiceImpl extends AbstractService<ShuttleBus> implements ShuttleBusService {
    @Resource
    private ShuttleBusMapper shuttleBusMapper;

    @Override
    public List<ShuttleBus> selectList(ShuttleBus shuttleBus) {
        List<ShuttleBus> shuttleBuses = shuttleBusMapper.selectList(shuttleBus);
        return shuttleBuses;
    }

    @Override
    public List<ShuttleBus> getBusNum() {
        List<ShuttleBus> shuttleBuses = shuttleBusMapper.getBusNum();
        return shuttleBuses;
    }


}
