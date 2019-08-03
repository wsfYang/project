<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <div style="width: 100%;height: 100%;padding: 24px">

        <el-col class="well well-lg" style="background-color: white">
            <el-form :model="queryKey" label-position="right">

                <el-row>

                    <el-col :span="4">
                        <el-form-item label="">
                            <el-select v-model="queryKey.grade" clearable filterable placeholder="请选择年级"    @input="onSelect">
                                <el-option
                                        v-for="item in gradeLists"
                                        :value="item.tag_name"
                                        :label="item.tag_name">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6" style="margin-left: 20px;">
                        <el-date-picker
                                v-model="queryKey.years"
                                type="date"
                                @input="onSelect"
                                placeholder="选择考勤日期">
                        </el-date-picker>

                    </el-col>
                    <el-col :span="4" style="margin-left: 40px;">
                        <el-button
                                icon="el-icon-search"
                                type="primary"
                                @click="onSelect">查询
                        </el-button>
                    </el-col>
                </el-row>
            </el-form>
            <el-table
                    v-loading="loadingUI"
                    element-loading-text="获取数据中..."
                    :data="tableData"
                    border
                    highlight-current-row
                    empty-text="暂无数据..."
                    show-overflow-tooltip="true"
                    style="width: 100%;">
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
                        prop="chineseName"
                        label="姓名">
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="grade"
                        label="年级">
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="classes"
                        label="班级">
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="date"
                        label="出入时间">
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


    </div>

</template>

<script>
    var _this;
    import request from '../../api/request'

    var image = require("../../assets/img/ldh.png");

    export default {
        name: "StudentMange",
        components: {},
        data() {
            _this = this;
            return {	    //分页
                pageSize: EveryPageNum,//每一页的num
                currentPage: 1,
                startRow: 1,
                gradeType: -1,
                tableData: [],
                totalRecords: 0,
                loadingUI: false,
                queryKey: {
                    grade: '',
                    years: ''
                },
                count:0,

                gradeLists: []
            }
        },
        methods: {
            handleCurrentChange(val) {
                this.currentPage = val;
                _this.onSelect();
            },

            judgeNull() {

                if (_this.queryKey.grade == null) {
                    _this.queryKey.grade = "";
                }

                if (_this.queryKey.years == null) {
                    _this.queryKey.years = "";
                }
            },

            onSelect() {

                _this.loadingUI = true;
                _this.judgeNull();
                let params = {
                    page: _this.currentPage,
                    size: _this.pageSize
                }
                request({
                    url: '/unusual/select',
                    method: 'post',
                    params: params,
                    data: _this.queryKey
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.tableData = res.data.data.list;
                        _this.totalRecords = res.data.data.total;
                        //获取页面加载第一次显示的数量
                        _this.count=_this.tableData.length
                        _this.startRow = res.data.data.startRow;
                    }
                    else {
                        showMessage(_this, "获取查询数据失败！");
                    }
                    _this.loadingUI = false;
                }).catch(error => {
                    console.log(error)
                    _this.loadingUI = false;

                })
            },
            getGrade() {
                let condition = {
                    page: _this.currentPage,
                    size: _this.pageSize,
                    type: _this.gradeType,
                }
                let params = new URLSearchParams();
                if (condition) {
                    let keys = Object.keys(condition);
                    for (let key of keys) {
                        params.append(key, condition[key])
                    }
                }
                request({
                    url: '/tag/list/grades',
                    method: 'post',
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.gradeLists = res.data.data.list
                    }
                }).catch(error => {
                    showMessage(_this, '获取年级信息失败', 0)
                })


            }
        },
        created: function () {

        },
        mounted: function () {
            _this.getGrade();
            _this.onSelect();
        },

    }
</script>
<style>
    input[type=file] {
        display: none;
    }

</style>
