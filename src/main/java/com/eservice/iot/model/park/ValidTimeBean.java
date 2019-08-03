package com.eservice.iot.model.park;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: yttps_shzx
 * @description:
 * @author: Mr.Zhang
 * @create: 2019-05-21 11:38
 **/
public class ValidTimeBean {
    /**
     * end_date : string
     * end_time : string
     * end_timestamp : 0
     * mode : 0
     * start_date : string
     * start_time : string
     * start_timestamp : 0
     * valid_weekday : [0]
     */

    private String end_date;
    private String end_time;
    private int end_timestamp;
    private String mode;
    private String start_date;
    private String start_time;
    private int start_timestamp;
    private List<Integer> valid_weekday;

    public ValidTimeBean(){

    }

    public ValidTimeBean(ValidTimeBean validTimeBean){
        this.end_date=validTimeBean.end_date;
        this.end_time=validTimeBean.end_time;
        this.end_timestamp=validTimeBean.end_timestamp;
        this.mode=validTimeBean.mode;
        this.start_date=validTimeBean.start_date;
        this.start_time=validTimeBean.start_time;
        this.start_timestamp=validTimeBean.start_timestamp;
        List<Integer> weeks = new ArrayList<>(16);
        for (Integer integer : validTimeBean.valid_weekday){
            Integer week = new Integer(integer.intValue());
            weeks.add(week);
        }
        this.valid_weekday=weeks;
    }


    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getEnd_timestamp() {
        return end_timestamp;
    }

    public void setEnd_timestamp(int end_timestamp) {
        this.end_timestamp = end_timestamp;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getStart_timestamp() {
        return start_timestamp;
    }

    public void setStart_timestamp(int start_timestamp) {
        this.start_timestamp = start_timestamp;
    }

    public List<Integer> getValid_weekday() {
        return valid_weekday;
    }

    public void setValid_weekday(List<Integer> valid_weekday) {
        this.valid_weekday = valid_weekday;
    }
}
