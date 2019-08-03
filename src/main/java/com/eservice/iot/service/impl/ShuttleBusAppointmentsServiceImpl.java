package com.eservice.iot.service.impl;

import com.eservice.iot.dao.ShuttleBusAppointmentsMapper;
import com.eservice.iot.model.shuttle_bus_appointments.ShuttleBusAppointments;
import com.eservice.iot.service.ShuttleBusAppointmentsService;
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
public class ShuttleBusAppointmentsServiceImpl extends AbstractService<ShuttleBusAppointments> implements ShuttleBusAppointmentsService {
    @Resource
    private ShuttleBusAppointmentsMapper shuttleBusAppointmentsMapper;

    @Override
    public List<ShuttleBusAppointments> selectList(ShuttleBusAppointments busAppointments) {
        List<ShuttleBusAppointments> result = shuttleBusAppointmentsMapper.selectList(busAppointments);
        return result;
    }

    @Override
    public List<ShuttleBusAppointments> selectListInfo(String staffId,String startTime,String endTime ) {
        List<ShuttleBusAppointments> result = shuttleBusAppointmentsMapper.selectListInfo(staffId,startTime,endTime);
        return result;
    }

}
