package com.eservice.iot.service;
import com.eservice.iot.model.shuttle_bus_info.ShuttleBusInfo;
import com.eservice.iot.core.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Class Description: xxx
* @author Wang
* @date 2019/07/27.
*/
public interface ShuttleBusInfoService extends Service<ShuttleBusInfo> {
    /**
     * 预约班车表查询
     *
     * @param busInfo
     * @return
     */
    List<ShuttleBusInfo> selectList(
            ShuttleBusInfo busInfo
    );
}
