package com.eservice.iot.service;
import com.eservice.iot.model.shuttle_bus_appointments.ShuttleBusAppointments;
import com.eservice.iot.core.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Class Description: xxx
* @author Wang
* @date 2019/07/27.
*/
public interface ShuttleBusAppointmentsService extends Service<ShuttleBusAppointments> {
    /**
     * 预约班车表查询
     * @param busAppointments
     * @return
     */
    List<ShuttleBusAppointments> selectList(
            ShuttleBusAppointments busAppointments
    );

    /**
     * 查询一个月的预约记录
     * @param staffId
     * @param startTime
     * @param endTime
     * @return
     */
    List<ShuttleBusAppointments> selectListInfo(
            String staffId,String startTime,String endTime
    );
}
