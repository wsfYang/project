package com.eservice.iot.model.shuttle_bus_info;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "shuttle_bus_info")
public class ShuttleBusInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 乘车人工号
     */
    @Column(name = "staff_id")
    private String staffId;

    /**
     * 班车编号
     */
    @Column(name = "bus_num")
    private Integer busNum;

    /**
     * 乘坐人姓名
     */
    @Column(name = "staff_name")
    private String staffName;

    /**
     * 乘坐时间
     */
    @Column(name = "riding_time")
    private Date ridingTime;

    /**
     * 有无津贴类型(0 固定座位 1 自行安排  2搬到嘉定 )
     */
    private Integer type;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取乘车人工号
     *
     * @return staff_id - 乘车人工号
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * 设置乘车人工号
     *
     * @param staffId 乘车人工号
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * 获取班车编号
     *
     * @return bus_num - 班车编号
     */
    public Integer getBusNum() {
        return busNum;
    }

    /**
     * 设置班车编号
     *
     * @param busNum 班车编号
     */
    public void setBusNum(Integer busNum) {
        this.busNum = busNum;
    }

    /**
     * 获取乘坐人姓名
     *
     * @return staff_name - 乘坐人姓名
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * 设置乘坐人姓名
     *
     * @param staffName 乘坐人姓名
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    /**
     * 获取乘坐时间
     *
     * @return riding_time - 乘坐时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getRidingTime() {
        return ridingTime;
    }

    /**
     * 设置乘坐时间
     *
     * @param ridingTime 乘坐时间
     */
    public void setRidingTime(Date ridingTime) {
        this.ridingTime = ridingTime;
    }

    /**
     * 获取有无津贴类型(0 固定座位 1 自行安排  2搬到嘉定 )
     *
     * @return type - 有无津贴类型(0 固定座位 1 自行安排  2搬到嘉定 )
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置有无津贴类型(0 固定座位 1 自行安排  2搬到嘉定 )
     *
     * @param type 有无津贴类型(0 固定座位 1 自行安排  2搬到嘉定 )
     */
    public void setType(Integer type) {
        this.type = type;
    }
}