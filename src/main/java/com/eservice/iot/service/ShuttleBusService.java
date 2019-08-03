package com.eservice.iot.service;
import com.eservice.iot.model.shuttle_bus.ShuttleBus;
import com.eservice.iot.core.Service;
import com.eservice.iot.model.shuttle_bus_info.ShuttleBusInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Class Description: xxx
* @author Wang
* @date 2019/07/27.
*/
public interface ShuttleBusService extends Service<ShuttleBus> {
    /**
     * 预约班车表查询
     *
     * @param shuttleBus
     * @return
     */
    List<ShuttleBus> selectList(
         ShuttleBus shuttleBus
    );
    List<ShuttleBus> getBusNum();
}

