package com.eservice.iot.dao;

import com.eservice.iot.core.Mapper;
import com.eservice.iot.model.shuttle_bus.ShuttleBus;
import com.eservice.iot.model.shuttle_bus_staff.ShuttleBusStaff;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShuttleBusStaffMapper extends Mapper<ShuttleBusStaff> {
    /**
     * 预约班车表查询
     *
     * @param busStaff
     * @return
     */
    List<ShuttleBusStaff> selectList(
            @Param("busStaff") ShuttleBusStaff busStaff
    );
}