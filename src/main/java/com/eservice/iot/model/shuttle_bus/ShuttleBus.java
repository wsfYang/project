package com.eservice.iot.model.shuttle_bus;

import javax.persistence.*;

@Table(name = "shuttle_bus")
public class ShuttleBus {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 车牌号
     */
    @Id
    @Column(name = "licence_num")
    private String licenceNum;

    /**
     * 班车编号
     */
    @Column(name = "bus_num")
    private Integer busNum;

    /**
     * 司机姓名
     */
    private String motorman;

    /**
     * 驾驶线路
     */
    private String circuit;

    /**
     * 司机电话号码
     */
    private String phone;

    /**
     * 车辆状态(1故障0正常)
     */
    private Integer status;

    /**
     * 总座位表
     */
    @Column(name = "total_seat")
    private Integer totalSeat;

    /**
     * 预约人数
     */
    @Column(name = "appointment_num")
    private Integer appointmentNum;

    /**
     * 固定人数
     */
    @Column(name = "fix_num")
    private Integer fixNum;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

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
     * 获取车牌号
     *
     * @return licence_num - 车牌号
     */
    public String getLicenceNum() {
        return licenceNum;
    }

    /**
     * 设置车牌号
     *
     * @param licenceNum 车牌号
     */
    public void setLicenceNum(String licenceNum) {
        this.licenceNum = licenceNum;
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
     * 获取司机姓名
     *
     * @return motorman - 司机姓名
     */
    public String getMotorman() {
        return motorman;
    }

    /**
     * 设置司机姓名
     *
     * @param motorman 司机姓名
     */
    public void setMotorman(String motorman) {
        this.motorman = motorman;
    }

    /**
     * 获取驾驶线路
     *
     * @return circuit - 驾驶线路
     */
    public String getCircuit() {
        return circuit;
    }

    /**
     * 设置驾驶线路
     *
     * @param circuit 驾驶线路
     */
    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    /**
     * 获取司机电话号码
     *
     * @return phone - 司机电话号码
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置司机电话号码
     *
     * @param phone 司机电话号码
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }



    /**
     * 获取车辆状态(1故障0正常)
     *
     * @return status - 车辆状态(1故障0正常)
     */

    public Integer getStatus() {
        return status;
    }

    /**
     * 设置车辆状态(1故障0正常)
     *
     * @param status 车辆状态(1故障0正常)
     */

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(Integer totalSeat) {
        this.totalSeat = totalSeat;
    }

    public Integer getAppointmentNum() {
        return appointmentNum;
    }

    public void setAppointmentNum(Integer appointmentNum) {
        this.appointmentNum = appointmentNum;
    }

    public Integer getFixNum() {
        return fixNum;
    }

    public void setFixNum(Integer fixNum) {
        this.fixNum = fixNum;
    }
}