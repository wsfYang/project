<template>
    <div style="width: 100%;height: 100%;padding: 24px">
        <el-col class="well well-lg" style="background-color: white; margin-left: 50px">
            <el-row stayle="margin-top: 15px">
                <el-col :span="4" style="margin-left: 20px">
                    <el-input :span="3" v-model="query.busNum"
                              @input="onSelect"
                              placeholder="班车编号" clearable
                              auto-complete="off"></el-input>
                </el-col>

                <el-col :span="4" style="margin-left: 20px">
                    <el-input :span="3" v-model="query.staffName"
                              @input="onSelect"
                              placeholder="员工姓名" clearable
                              auto-complete="off"></el-input>
                </el-col>
                <el-col :span="4" style="float: right">
                    <el-button
                            icon="el-icon-plus"
                            size="normal"
                            type="primary"
                            @click="handleAdd">
                        添加班车
                    </el-button>
                </el-col>
                <el-col :span="2" style="float: right">
                    <el-button
                            icon="el-icon-search"
                            size="normal"
                            type="primary">查询
                    </el-button>
                </el-col>
            </el-row>
            <el-table
                    v-loading="loadingUI"
                    element-loading-text="获取数据中..."
                    :data="tableData"
                    border
                    highlight-current-row
                    empty-text="暂无数据..."
                    show-overflow-tooltip="true"
                    style="width: 1300px;height: 100%;margin-top: 20px">
                <el-table-column
                        width="80"
                        align="center"
                        label="序号">
                    <template scope="scope">
                        {{scope.$index+startRow}}
                    </template>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="员工工号"
                        prop="staffId">
                </el-table-column>
                <el-table-column
                        align="center"
                        label="员工姓名"
                        prop="staffName">
                </el-table-column>
                <el-table-column
                        align="center"
                        label="预约时间"
                        prop="appointmentsTime">
                </el-table-column>

                <el-table-column
                        align="center"
                        label="乘车时间"
                        prop="ridingTime">
                </el-table-column>
                <el-table-column
                        align="center"
                        label="班车编号"
                        prop="busNum">
                </el-table-column>
                <el-table-column
                        align="center"
                        label="操作"
                        width="400">
                    <template scope="scope">
                        <el-button
                                size="small"
                                icon="el-icon-edit"
                                type="primary"
                                @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button
                                size="small"
                                icon="el-icon-delete"
                                type="primary"
                                @click="handledel(scope.$index, scope.row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="block" style="text-align: center; margin-top: 20px">
                <el-pagination
                        background
                        @current-change="handleCurrentChange"
                        :current-page="currentPage"
                        :page-size="pageSize"
                        layout="total, prev, pager, next, jumper"
                        :total="totalRecords">
                </el-pagination>
            </div>

        </el-col>
        <el-dialog
                :before-close="handleClose"
                :visible.sync="addDialogVisible"
                width="30% "
                height="800px"
                title="添加预约班车信息"
                center>

            <el-form :model="add">

                <el-row style="margin-top: 10px;">
                    <label style="float: left">员工工号:</label>
                    <el-col :span="15" :offset="2">
                        <el-input
                                v-model="add.staffId">
                        </el-input>
                    </el-col>
                </el-row>
                <el-row style="margin-top: 15px">
                    <label style="float: left">员工姓名:</label>
                    <el-col :span="15" :offset="2">
                        <el-input v-model="add.motorman"></el-input>
                    </el-col>
                </el-row>
                <el-row style="margin-top: 15px">
                    <label style="float: left">班车编号:</label>
                    <el-col :span="15" :offset="2">
                        <el-input v-model="add.busNum"></el-input>
                    </el-col>
                </el-row>
                <el-row class="block" style="margin-top: 15px">
                    <label style="float: left">预约时间:</label>
                    <el-date-picker
                            style="margin-left: 45px"
                            v-model="add.appointmentsTime"
                            type="datetime"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-row>
                <el-row class="block" style="margin-top: 15px">
                    <label style="float: left">乘车时间:</label>
                    <el-date-picker
                            style="margin-left: 45px"
                            v-model="add.ridingTime"
                            type="datetime"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-row>

                <el-row style="margin-top:5px">
                    <el-col :span="18" :offset="15">
                        <el-col class="dialog-footer">
                            <el-button type="danger" @click="handleCancel" icon="el-icon-close">取 消</el-button>
                            <el-button type="primary" @click="onAdd" icon="el-icon-plus">添加</el-button>
                        </el-col>
                    </el-col>
                </el-row>
            </el-form>
        </el-dialog>
        <el-dialog
                :before-close="handleClose"
                :visible.sync="ChangeDialogVisible"
                width="30% "
                height="800px"
                title="更改预约班车信息"
                center>

            <el-form :model="add">

                <el-row style="margin-top: 10px;">
                    <label style="float: left">员工工号:</label>
                    <el-col :span="15" :offset="2">
                        <el-input
                                v-model="add.staffId">
                        </el-input>
                    </el-col>
                </el-row>
                <el-row style="margin-top: 15px">
                    <label style="float: left">员工姓名:</label>
                    <el-col :span="15" :offset="2">
                        <el-input v-model="add.staffName"></el-input>
                    </el-col>
                </el-row>
                <el-row style="margin-top: 15px">
                    <label style="float: left">班车编号:</label>
                    <el-col :span="15" :offset="2">
                        <el-input v-model="add.busNum"></el-input>
                    </el-col>
                </el-row>
                <el-row class="block" style="margin-top: 15px">
                    <label style="float: left">预约时间:</label>
                    <el-date-picker
                            style="margin-left: 45px"
                            v-model="add.appointmentsTime"
                            type="datetime"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-row>
                <el-row class="block" style="margin-top: 15px">
                    <label style="float: left">乘车时间:</label>
                    <el-date-picker
                            style="margin-left: 45px"
                            v-model="add.ridingTime"
                            type="datetime"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-row>

                <el-row style="margin-top:5px">
                    <el-col :span="18" :offset="15">
                        <el-col class="dialog-footer">
                            <el-button type="danger" @click="handleCancel" icon="el-icon-close">取 消</el-button>
                            <el-button type="primary" @click="onChange" icon="el-icon-plus">更改</el-button>
                        </el-col>
                    </el-col>
                </el-row>
            </el-form>
        </el-dialog>
        <el-dialog title="提示" :visible.sync="deleteConfirmVisible" width="30%" append-to-body
                   close-on-click-modal>
            <div>确认要删除预约信息吗？</div>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="deleteConfirmVisible = false" icon="el-icon-close">取 消
                </el-button>
                <el-button type="danger" @click="onDelete" icon="el-icon-check">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    var _this;
    import request from '../../api/request'

    export default {
        name: "shuttleBusAppointments",
        comments: {},
        data() {
            _this = this;
            return {
                pageSize: EveryPageNum,//每一页的num
                currentPage: 1,
                totalRecords: 0,
                startRow: 0,
                tableData: [],
                addDialogVisible: false,
                deleteConfirmVisible: false,
                ChangeDialogVisible:false,
                loadingUI: false,
                add: {
                    staffId: '',
                    staffName: '',
                    ridingTime: '',
                    busNum: '',
                    appointmentsTime: ''
                },
                query: {
                    staffName: '',
                    busNum: ''
                },
                deleteId:{
                    staffId:''
                },
            }

        },
        methods: {
            handleCurrentChange(val) {
                this.currentPage = val;
                _this.onSelect();
            }, handleClose(done) {
                this.$confirm('是否取消?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'

                }).then(() => {
                    done();
                    _this.from = {};
                }).catch(_ => {

                    });
            },
            //判断添加 字段是否为空
            verifyForm(formObj) {
                let result = true;
                let phone = /^1[3456789]\d{9}$/;
                if (formObj.headImag == null || formObj.headImag == "") {
                    showMessage(_this, "照片不能为空！");
                    result = false;
                } else if (formObj.tagName == null || formObj.tagName == "") {
                    showMessage(_this, "标签不能为空！");
                    result = false;
                }
                else if (formObj.id == null || formObj.id == "") {
                    showMessage(_this, "员工id不能为空！");
                    result = false;
                } else if (formObj.name == null || formObj.name == "") {
                    showMessage(_this, "姓名不能为空！");
                    result = false;
                } else if (!(phone.test(formObj.phone))) {
                    showMessage(_this, '手机号输入不正确')
                    result = false

                }
                return result;
            },
            handleAdd() {
                _this.addDialogVisible = true;
            },
            handleCancel() {
                _this.addDialogVisible = false;
                _this.add = {};
            },
            handleEdit(index, item) {
                _this.ChangeDialogVisible = true;
                _this.add = copyObjectByJSON(item);
            },
            handledel(index, item) {
                _this.deleteConfirmVisible = true;
                _this.deleteId = copyObjectByJSON(item);
            },
            onSelect() {
                _this.loadingUI = true;
                let params = {
                    page: _this.currentPage,
                    size: _this.pageSize,
                }
                request({
                    url: HOST + '/shuttle/bus/appointments/selectlist',
                    method: 'post',
                    data: _this.query,
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.tableData = res.data.data.list;
                        _this.totalRecords = res.data.data.total;
                        _this.startRow = res.data.data.startRow;

                    }
                    else {
                        showMessage(_this, "获取查询数据失败！");
                    }
                    _this.loadingUI = false;
                }).catch(error => {
                    console.log(error)
                })
                _this.loadingUI = false;
            },
            onAdd() {
                console.log('fsd'+_this.add.staffId)
                request({
                    url: HOST + 'shuttle/bus/appointments/add',
                    method: 'post',
                    data: _this.add
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.addDialogVisible = false;
                        _this.onSelect();
                        _this.add = {};
                        showMessage("添加预约信息成功", 1)


                    } else {
                        showMessage("添加预约信息失败");
                    }
                }).catch(error => {
                    console.log(error)
                })
            },
            onDelete() {
                request({
                    url: HOST + '/shuttle/bus/appointments/delete?staffId='+JSON.stringify(_this.deleteId.staffId),
                    method: 'post'
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.deleteConfirmVisible = false;
                        _this.onSelect();
                        showMessage(_this, "删除班车信息成功", 1);
                    } else {
                        showMessage(_this, "删除班车信息失败");
                    }
                }).catch(error => {
                    console.log(error)
                })
            },
            onChange() {
                let params = new URLSearchParams();
                request({
                    url: HOST + 'shuttle/bus/appointments/update',
                    method: 'post',
                    data: _this.add
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.ChangeDialogVisible = false;
                        _this.onSelect();
                        showMessage(_this, "更改关联信息成功", 1);


                    } else {
                        showMessage(_this, "更改关联信息失败");
                    }
                }).catch(error => {
                    console.log(error)
                })
            },
        },
        mounted: function () {
            _this.onSelect();
        }
    }
</script>

<style>

</style>