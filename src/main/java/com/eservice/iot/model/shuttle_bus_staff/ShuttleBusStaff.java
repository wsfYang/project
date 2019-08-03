package com.eservice.iot.model.shuttle_bus_staff;

import io.swagger.models.auth.In;

import javax.persistence.*;

@Table(name = "shuttle_bus_staff")
public class ShuttleBusStaff {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 员工工号
     */
    @Id
    @Column(name = "staff_id")
    private String staffId;

    /**
     * 员工姓名
     */
    @Column(name = "staff_name")
    private String staffName;

    /**
     * 有无津贴类型(0 固定座位 1 自行安排  2搬到嘉定 )
     */
    private Integer type;

    /**
     * 班车编号
     */
    @Column(name = "bus_num")
    private Integer busNum;

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
     * 获取员工工号
     *
     * @return staff_id - 员工工号
     */
    public String getStaffId() {
        return staffId;
    }

    /**
     * 设置员工工号
     *
     * @param staffId 员工工号
     */
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * 获取员工姓名
     *
     * @return staff_name - 员工姓名
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * 设置员工姓名
     *
     * @param staffName 员工姓名
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
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
}