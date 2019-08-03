package com.eservice.iot.model.park;

/**
 * @program: yttps_shzx
 * @description:
 * @author: Mr.Zhang
 * @create: 2019-05-21 16:13
 **/
public class Device {

    /**
     * device_id : string
     * device_meta : {"device_name":"string","direction_type":0,"docking_config":{"additionalProp1":{},"additionalProp2":{},"additionalProp3":{}},"enable":true,"ip":"string","location":"string","map_id":"string","password":"string","port":0,"position":{"x":0,"y":0},"specification":"string","type":"0","username":"string","version":"string"}
     * device_status : {"device_add_time":0,"last_capture_timestamp":0,"last_heart_beat_timestamp":0,"status":"WAITING","status_description":"string","status_detail":"OK"}
     */

    private String device_id;
    private DeviceMetaBean device_meta;
    private DeviceStatusBean device_status;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public DeviceMetaBean getDevice_meta() {
        return device_meta;
    }

    public void setDevice_meta(DeviceMetaBean device_meta) {
        this.device_meta = device_meta;
    }

    public DeviceStatusBean getDevice_status() {
        return device_status;
    }

    public void setDevice_status(DeviceStatusBean device_status) {
        this.device_status = device_status;
    }

    public static class DeviceStatusBean {
        /**
         * device_add_time : 0
         * last_capture_timestamp : 0
         * last_heart_beat_timestamp : 0
         * status : WAITING
         * status_description : string
         * status_detail : OK
         */

        private int device_add_time;
        private int last_capture_timestamp;
        private int last_heart_beat_timestamp;
        private String status;
        private String status_description;
        private String status_detail;

        public int getDevice_add_time() {
            return device_add_time;
        }

        public void setDevice_add_time(int device_add_time) {
            this.device_add_time = device_add_time;
        }

        public int getLast_capture_timestamp() {
            return last_capture_timestamp;
        }

        public void setLast_capture_timestamp(int last_capture_timestamp) {
            this.last_capture_timestamp = last_capture_timestamp;
        }

        public int getLast_heart_beat_timestamp() {
            return last_heart_beat_timestamp;
        }

        public void setLast_heart_beat_timestamp(int last_heart_beat_timestamp) {
            this.last_heart_beat_timestamp = last_heart_beat_timestamp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus_description() {
            return status_description;
        }

        public void setStatus_description(String status_description) {
            this.status_description = status_description;
        }

        public String getStatus_detail() {
            return status_detail;
        }

        public void setStatus_detail(String status_detail) {
            this.status_detail = status_detail;
        }
    }
}
