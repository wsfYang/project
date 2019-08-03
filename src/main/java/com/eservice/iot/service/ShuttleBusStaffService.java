package com.eservice.iot.service;
import com.eservice.iot.model.shuttle_bus_staff.ShuttleBusStaff;
import com.eservice.iot.core.Service;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Class Description: xxx
* @author Wang
* @date 2019/07/27.
*/
public interface ShuttleBusStaffService extends Service<ShuttleBusStaff> {
    /**
     * 预约班车表查询
     *
     * @param busStaff
     * @return
     */
    List<ShuttleBusStaff> selectList(
        ShuttleBusStaff busStaff
    );
}
