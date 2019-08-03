<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml" >
    <el-container>
    <el-aside style="background-color: white; margin-left: -20px;margin-top:-19px; width: 170px;height: 800px">
        <div   v-for="root in $router.options.routes" v-if="!root.hidden">
            <el-menu  v-for="sub in root.children" v-if="!sub.hidden" mode="horizontal" :default-active="$route.path"
                      text-color="#909399"
                      active-text-color="#1875F0"
                      @select="handleSelect" heigth="400px">
                <el-menu-item v-for="item in sub.children"
                              :index="item.path"
                              v-if="sub.path == '/home/subsidy'"
                              style="text-align: center; font-size: 15px; font-weight: bold;height: 90px;line-height: 90px;margin-left: 36px;">
                    {{item.meta}}
                </el-menu-item>
            </el-menu>
        </div>

    </el-aside>

    <div >
        <router-view  ></router-view>
    </div>
    </el-container>
</template >

<script >
    import Vue from 'vue'
    var _this

    export default {
	    name: "subsidy",
	    components: {},
	    data () {
		    _this = this;
		    return {
			    userinfo: {},
			    activeName: "",
		    }
	    },
	    methods: {
		    handleSelect(key, keyPath) {
			    _this.activeName = key;
			    _this.$router.push(key)
		    },
		    getIndex(item, list)
		    {
			    return list.indexOf(item);
		    },
	    },
	    computed: {},
	    created: function () {
		    this.userinfo = JSON.parse(sessionStorage.getItem('user'));
		    // console.log(`routes:\r\n${JSON.stringify(_this.$router.options.routes)}`)

	    },
	    mounted: function () {

	    },
    }

</script >

