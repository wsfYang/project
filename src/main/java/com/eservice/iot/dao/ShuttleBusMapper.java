package com.eservice.iot.dao;

import com.eservice.iot.core.Mapper;
import com.eservice.iot.model.shuttle_bus.ShuttleBus;
import com.eservice.iot.model.shuttle_bus_info.ShuttleBusInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShuttleBusMapper extends Mapper<ShuttleBus> {
    /**
     * 预约班车表查询
     *
     * @param shuttleBus
     * @return
     */
    List<ShuttleBus> selectList(
            @Param("shuttleBus") ShuttleBus shuttleBus
    );
    List<ShuttleBus> getBusNum();

}