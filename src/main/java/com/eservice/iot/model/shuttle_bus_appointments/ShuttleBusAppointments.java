package com.eservice.iot.model.shuttle_bus_appointments;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "shuttle_bus_appointments")
public class ShuttleBusAppointments {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 员工工号
     */
    @Id
    @Column(name = "staff_id")
    private String staffId;

    /**
     * 预约时间
     */
    @Column(name = "appointments_time")
    private Date appointmentsTime;

    /**
     * 姓名
     */
    @Column(name = "staff_name")
    private String staffName;

    /**
     * 乘车时间
     */
    @Column(name = "riding_time")
    private Date ridingTime;

    /**
     * 预约班车编号
     */
    @Column(name = "bus_num")
    private String busNum;

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
     * 获取预约时间
     *
     * @return appointments_time - 预约时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getAppointmentsTime() {
        return appointmentsTime;
    }

    /**
     * 设置预约时间
     *
     * @param appointmentsTime 预约时间
     */
    public void setAppointmentsTime(Date appointmentsTime) {
        this.appointmentsTime = appointmentsTime;
    }

    /**
     * 获取姓名
     *
     * @return staff_name - 姓名
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * 设置姓名
     *
     * @param staffName 姓名
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    /**
     * 获取乘车时间
     *
     * @return riding_time - 乘车时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getRidingTime() {
        return ridingTime;
    }

    /**
     * 设置乘车时间
     *
     * @param ridingTime 乘车时间
     */
    public void setRidingTime(Date ridingTime) {
        this.ridingTime = ridingTime;
    }

    /**
     * 获取预约班车编号
     *
     * @return bus_num - 预约班车编号
     */
    public String getBusNum() {
        return busNum;
    }

    /**
     * 设置预约班车编号
     *
     * @param busNum 预约班车编号
     */
    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }
}