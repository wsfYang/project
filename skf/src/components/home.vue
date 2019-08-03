<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <div style="background:rgba(250,250,250,1);">
        <el-container>
            <el-container>
            <el-header
                    style="height:120px;background-color: white;box-shadow:0px 0px 15px 0px rgba(0,0,0,0.15);">

                    <i style="font-size: 20px; font-weight: bolder; color: black;line-height:100px;float: left;"
                       @click="goToHome()">skf管理后台
                    </i>

                <div style="position: fixed  ">
                <el-menu v-for="root in $router.options.routes"
                             text-color="#909399"
                             :default-active="filterParentPath($route.path)"
                             active-text-color="#1875F0"
                             @select="handleSelect"
                             mode="horizontal"
                             style="margin-left:200px">
                        <el-menu-item v-for="sub in root.children" v-if="!root.hidden && showMenu(sub.path)"
                                      :index="sub.path"
                                      style=" height: 120px;line-height: 120px;text-align: center;">
                            <template slot="title">
                                <i :class="sub.icon" style="font-size: 24px"></i>
                                <span style="font-weight: bold;font-size: 16px;padding-left: 35px">{{sub.meta}}</span>
                            </template>
                        </el-menu-item>

                    </el-menu>

                <el-row style="cursor: pointer;font-size: 20px; font-weight: bolder; color: black;line-height:70px;float: right;margin-top: -115px;right: -400px ">
                    <el-col :offset="7" :span="1">
                        <div>
                            <img style=" height: 60px;width:60px; border-radius: 50%;align-items: center;justify-content: center;
                            overflow: hidden;margin-top:10px " :src="getPhoto()"/>
                        </div>
                    </el-col>
                    <el-col :span="1" :offset="1" style=" margin-top: 53px">
                        <label
                                style="font-size: 18px;
                            cursor: pointer;
                            font-weight:bolder"

                                @click="logout">
                            {{userinfo.name}}
                        </label>
                        <i v-on:click="logout">
                        </i>
                    </el-col>
                </el-row>

            </div>
            </el-header>
            <el-main width="100%" >
                    <router-view></router-view>
            </el-main>
        </el-container>

        </el-container>

        <div class="modal fade" id="logOutConfirmMsg" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header" style="text-align: left">
                        <h3>提示</h3>
                    </div>
                    <div class="modal-body" style="font-size: 18px;font-family: FontAwesome">
                        <!-- 加上<form>标签可以使得modal窗口在点击按钮都自动dismiss-->
                        确认要注销登录吗？
                        <button type="button" style="margin-top: 50px;font-size: 18px" class="btn btn-danger btn-block"
                                v-on:click="onConfirmLogOut">确 定
                        </button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    import Vue from 'vue'

    var _this
    $(function () {
        $('#modifyPwdDialog').on('hidden.bs.modal', function () {
            _this.onConfirmLogOut();
        })
    });
    export default {
        name: "home",
        components: {},
        data() {
            _this = this;
            return {
                system_name: SYSTEMNAME,
                currentSec: 3,
                timerDestroyed: false,
                current_time: '',
                activeIndex: "1",
                userinfo: {},
                subDepartments: [],
                isError: false,
                errorMsg: '',
                currentUserRoleScope: {},
                formLabelWidth: "100px",
                userInfoDialog: false,
                submitUser: {
                    account: '',
                    name: '',
                    oldPassword: "",
                    password: "",
                },
                isShowPwd: false,
            }
        },
        methods: {
            getPhoto() {
                return require("../assets/img/shzx.png");
            },
            goToHome() {
                _this.$router.push("/home");
            },

            validateUserPwd(userObj) {
                return isStringEmpty(userObj.password) ||
                    isStringEmpty(userObj.oldPassword);
            },

            showUserInfo() {
                _this.submitUser.account = _this.userinfo.account;
                _this.submitUser.name = _this.userinfo.name;
                _this.submitUser.password = "";
                _this.submitUser.oldPassword = "";
                _this.isShowPwd = false;
                _this.isError = false;
                _this.userInfoDialog = true;

            },

            onConfirmUpdate() {
                $.ajax({
                    url: HOST + "user/requestLogin",
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        account: _this.submitUser.account,
                        password: _this.submitUser.oldPassword,
                    },
                    success: function (data) {
                        if (data.code == 200) {
                            _this.submitNewPassword();
                        } else {
                            _this.errorMsg = "原密码不正确，请重新输入!";
                            _this.isError = true;
//                            showMessage(_this, "原密码不正确，请重新输入!", 0);
                        }
                    },
                    error: function (error) {
                        _this.errorMsg = "原密码不正确，请重新输入!";
                        _this.isError = true;
//                        showMessage(_this, "原密码不正确，请重新输入!", 0);
                    }
                });
            },
            getCurrentDate: function () {
                var cdata = new Date();
                this.current_time = cdata.toLocaleTimeString("yyyy-MM-dd HH:mm:ss");
            },

            logout: function () {
                $("#logOutConfirmMsg").modal();

            },
            onConfirmLogOut: function () {
                _this.$router.push("/login");
                window.location.reload()
            },

            handleSelect(key, keyPath) {
                _this.$router.push(key)
            },

            //根据子路径找到父路径
            filterParentPath(childPath) {
                let path = "/home";//default
                for (let i = 0; i < _this.$router.options.routes.length; i++) {
                    if (!_this.$router.options.routes[i].hidden && _this.$router.options.routes[i].children.length > 0) {
                        for (let j = 0; j < _this.$router.options.routes[i].children.length; j++) {
                            if (childPath.indexOf(_this.$router.options.routes[i].children[j].path) != -1) {
                                path = _this.$router.options.routes[i].children[j].path;
                                break;
                            }
                        }
                    }
                }
                return path;
            },

            //是否显示主menu
            showMenu(path) {
                let pathList = path.split("/");
                if (pathList != null && pathList.length == 3) {
                    return true;
                } else {
                    return false;
                }
            },


        },
        computed: {},
        filters: {},
        created: function () {
            this.userinfo = JSON.parse(sessionStorage.getItem('user'));
        },
        mounted: function () {

        },
        destroyed: function () {
            window.clearInterval(this.reduceTime)
        }
    }


</script>
<style>
    .bg {
        overflow-y: scroll;
        height: 100%;
    }

    .el-header, .el-footer {
        background-color: #B3C0D1;
        color: #333;
        text-align: center;
        line-height: 60px;
    }

    .el-aside {
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
        line-height: 200px;
    }

    .el-main {
        height: 840px;
    }

    .el-tree-node__expand-icon {
        font-size: 18px;
    }

    .el-tree-node__expand-icon.expanded {
        -webkit-transform: rotate(90deg);
        transform: rotate(90deg);
        color: #1875F0;
    }


</style>
