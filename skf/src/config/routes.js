import Login from '../components/login.vue'
import NotFound from '../components/404.vue'
import Home from '../components/home.vue'
import Staff from '../components/staff.vue'
import StaffManagement from '../components/staff/staffManagement.vue'
import Subsidy from  '../components/subsidy.vue'
import SubsidyManagement from  '../components/subsidy/subsidyManagement.vue'
import Visitor from '../components/visitor.vue'
import VisitorManagement from '../components/visitor/visitorManagement.vue'
import Passage from '../components/passage.vue'
import PassageManagement from '../components/passage/passageManagement.vue'
import Stop from '../components/stop.vue'
import StopManagement from '../components/stop/stopManagement.vue'
import Canteen  from '../components/canteen.vue'
import canteenManagement from '../components/canteen/canteenManagement.vue'
import Unusual from "../components/visitor/unusual.vue"

import ShuttleBusManagement from '../components/shuttleBus/shuttleBusManagement.vue'
import ShuttleBus from '../components/shuttleBus.vue'
import ShuttleBusInfo from '../components/shuttleBus/shuttleBusInfo.vue'
import ShuttleBusAppointments from '../components/shuttleBus/shuttleBusAppointments.vue'
import ShuttleBusStaff from '../components/shuttleBus/shuttleBusStaff.vue'

export default
[
	{
		path: '/login',
		component: Login,
		name: 'Login',
		hidden: true
	},
	{
		path: '/404',
		component: NotFound,
		name: '404',
		hidden: true
	},
    {
        path: '/home',
        component: Home,
		redirect:'/home/staff/staffManagement',
		children:[
			{
                path: '/home/staff',
                component: Staff,
                name: 'staff',
                meta: "员工管理",
                icon: 'el-icon-user',
                hidden: false,
                redirect: '/home/staff/staffManagement',
					children:[
						{
                            path: '/home/staff/staffManagement',
                            component: StaffManagement,
                            name: 'StaffManagement',
                            meta: '管理'
						},
					]

            },
            {
                path: '/home/visitor',
                component: Visitor,
                name: 'Visitor',
                meta: "访客管理",
                icon: 'el-icon-date',
                hidden: false,
                redirect: '/home/visitor/visitorManagement',
                children:[
                    {
                        path: '/home/visitor/visitorManagement',
                        component: VisitorManagement,
                        name: 'VisitorManagement',
                        meta:'访客'
                    },
                    {
                        path: '/home/visitor/unusual',
                        component: Unusual,
                        name: 'Unusual',
                        meta: '异常统计',
                    },
                ]
            },

            {
                path: '/home/shuttleBus',
                component: ShuttleBus,
                name: 'ShuttleBus',
                meta: "班车管理",
                icon: 'el-icon-date',
                hidden: false,
                redirect: '/home/shuttleBus/shuttleBusManagement',
                children:[
                    {
                        path: '/home/shuttleBus/shuttleBusManagement',
                        component: ShuttleBusManagement,
                        name: 'ShuttleBusManagement',
                        meta: '班车信息'
                    },
                    {
                        path: '/home/shuttleBus/shuttleBusInfo',
                        component: ShuttleBusInfo,
                        name: 'ShuttleBusInfo',
                        meta: '乘车信息'
                    },
                    {
                        path: '/home/shuttleBus/shuttleBusAppointments',
                        component: ShuttleBusAppointments,
                        name: 'ShuttleBusAppointments',
                        meta: '预约班车信息'
                    },
                    {
                        path: '/home/shuttleBus/shuttleBusStaff',
                        component: ShuttleBusStaff,
                        name: 'ShuttleBusStaff',
                        meta: '人员信息'
                    },
                ]
            },
            {
                path: '/home/subsidy',
                component: Subsidy,
                name: 'Subsidy',
                meta: "津贴管理",
                icon: 'el-icon-date',
                hidden: false,
                redirect: '/home/subsidy/subsidyManagement',
                children:[
                    {
                        path: '/home/subsidy/subsidyManagement',
                        component: SubsidyManagement,
                        name: 'SubsidyManagement',
                        meta:'津贴管理'
                    }
                ]
            },
             {
                 path: '/home/stop',
                 component: Stop,
                 name: 'Stop',
                 meta: "停车管理",
                 icon: 'el-icon-date',
                 hidden: false,
                 redirect: '/home/stop/stopManagement',
                 children:[
                     {
                         path: '/home/stop/stopManagement',
                         component: StopManagement,
                         name: 'StopManagement',
                     }
                 ]
             },
             {
                 path: '/home/canteen',
                 component: Canteen,
                 name: 'Canteen',
                 meta: "食堂管理",
                 icon: 'el-icon-date',
                 hidden: false,
                 redirect: '/home/canteen/canteenManagement',
                 children:[
                     {
                         path: '/home/canteen/canteenManagement',
                         component: canteenManagement,
                         name: 'canteenManagement',
                     }
                 ]

            },
             {
                 path: '/home/passage',
                 component: Passage,
                 name: 'Passage',
                 meta: "通行管理",
                 icon: 'el-icon-date',
                 hidden: false,
                 redirect: '/home/passage/passageManagement',
                 children:[
                     {
                         path: '/home/passage/passageManagement',
                         component: PassageManagement,
                         name: 'PassageManagement',
                     }
                 ]
             },
            /* {
                 path: '',
                 component: '',
                 name: '',
                 meta: "安防管理",
                 icon: 'el-icon-date',
                 hidden: false,
                 redirect: '',

             },
             {
                 path: '',
                 component: '',
                 name: '',
                 meta: "Instant Report管理",
                 icon: 'el-icon-date',
                 hidden: false,
                 redirect: '',

             },*/
		],
    },
]
