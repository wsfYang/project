package com.eservice.iot.service.impl;

import com.eservice.iot.dao.ShuttleBusStaffMapper;
import com.eservice.iot.model.shuttle_bus_staff.ShuttleBusStaff;
import com.eservice.iot.service.ShuttleBusStaffService;
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
public class ShuttleBusStaffServiceImpl extends AbstractService<ShuttleBusStaff> implements ShuttleBusStaffService {
    @Resource
    private ShuttleBusStaffMapper shuttleBusStaffMapper;

    @Override
    public List<ShuttleBusStaff> selectList(ShuttleBusStaff busStaff) {
        List<ShuttleBusStaff> shuttleBusStaffs = shuttleBusStaffMapper.selectList(busStaff);
        return shuttleBusStaffs;
    }
}
