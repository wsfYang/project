package com.eservice.iot.dao;

import com.eservice.iot.core.Mapper;
import com.eservice.iot.model.shuttle_bus_appointments.ShuttleBusAppointments;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShuttleBusAppointmentsMapper extends Mapper<ShuttleBusAppointments> {
    /**
     * 预约班车表查询
     * @param busAppointments
     * @return
     */
    List<ShuttleBusAppointments> selectList(
            @Param("busAppointments") ShuttleBusAppointments busAppointments
    );

    /**
     * 查询最近一个月的记录
     * @param staffId
     * @param startTime
     * @param endTime
     * @return
     */
    List<ShuttleBusAppointments> selectListInfo(
            @Param("staffId") String staffId, @Param("startTime")String startTime, @Param("endTime")String endTime
    );
}