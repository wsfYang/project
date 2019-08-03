//Server root address

var HOST = "http://127.0.0.1:9090/";
//var HOST = "http://211.144.105.121/gate_api/";
var MqttServer = `10.250.62.200`;
//var MqttServer = `211.144.105.121`;
var ServerPort = Number(9001);
var ServerTOPIC = ["/renew"];
var SYSTEMNAME = "skf管理系统";

var EveryPageNum = 7;

const DateRangeOptions = {
    shortcuts: [{
        text: '最近一周',
        onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
        }
    }, {
        text: '最近一个月',
        onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
        }
    }, {
        text: '最近三个月',
        onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
        }
    }]
}
const DateOptions = {
    shortcuts: [{
        text: '今天',
        onClick(picker) {
            picker.$emit('pick', new Date());
        }
    }, {
        text: '昨天',
        onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
        }
    }, {
        text: '一周前',
        onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
        }
    }]
}

const startTime=" 12:00:00"
const endTime=" 14:00:00"

const inDevice="10_250_62_101"
const outDevice="10_250_62_103"


const holidayWeek=[
    {
        id:"1",
        name:"星期一"
    },
    {
        id:"2",
        name:"星期二"
    },
    {
        id:"3",
        name:"星期三"
    },
    {
        id:"4",
        name:"星期四"
    },
    {
        id:"5",
        name:"星期五"
    },
    {
        id:"6",
        name:"星期六"
    },
    {
        id:"7",
        name:"星期天"
    },
]

