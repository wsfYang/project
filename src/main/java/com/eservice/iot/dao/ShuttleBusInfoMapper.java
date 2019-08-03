package com.eservice.iot.dao;

import com.eservice.iot.core.Mapper;
import com.eservice.iot.model.shuttle_bus_info.ShuttleBusInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShuttleBusInfoMapper extends Mapper<ShuttleBusInfo> {

    /**
     * 预约班车表查询
     *
     * @param busInfo
     * @return
     */
    List<ShuttleBusInfo> selectList(
            @Param("busInfo") ShuttleBusInfo busInfo
    );

}