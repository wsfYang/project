<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <div style="width: 100%;height: 100%;padding: 24px">
        <el-col class="well well-lg" style="background-color: white; margin-left: 50px">
            <el-row>
                <el-col :span="4" style="float: right">
                    <el-button
                            icon="el-icon-plus"
                            size="normal"
                            type="primary"
                            @click="handleAdd">
                        添加员工
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
                    style="width: 1300px;height: 100%">
                <el-table-column
                        width="80"
                        align="center"
                        label="序号">
                    <template scope="scope">
                        {{scope.$index+startRow+1}}
                    </template>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="姓名">
                    <template scope="scope">
                        {{scope.row.person_information.name}}
                    </template>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="标签">
                    <template scope="scope">
                        <el-tag type="info" v-for="(item) in scope.row.tagName">{{item}}</el-tag>&nbsp;
                    </template>
                </el-table-column>

                <el-table-column
                        align="center"
                        label="头像">
                    <template scope="scope">
                        <img style=" height: 60px;width:60px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                    overflow: hidden; margin-top: 10px" :src="scope.row.imageId"/>
                    </template>
                </el-table-column>

                <el-table-column
                        align="center"
                        label="联系方式">
                    <template scope="scope">
                        {{scope.row.person_information.phone}}
                    </template>
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
                height="800px">

            <el-form :model="form">
                <el-row>

                    <el-col :span="22" :offset="1">
                        <h4>新建员工</h4>

                        <el-form :model="form" label-position="top">
                            <el-row style="margin-top: 10px;margin-left: 15px">
                                <el-col :span="15">
                                    <el-form-item label="姓名:">
                                        <el-input v-model="form.name"></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="15" :offset="1">
                                    <el-form-item label="电话:">
                                        <el-input v-model="form.phone"></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="15" :offset="2">
                                    <el-form-item label="备注:">
                                        <el-input v-model="form.remark"></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="15" :offset="2">
                                    <el-form-item label="员工工号:">
                                        <el-input v-model="form.id"></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col>
                                    <el-select
                                            v-model="form.tagName"
                                            multiple
                                            filterable
                                            allow-create
                                            default-first-option
                                            placeholder="请选择标签"
                                            label="标签:">
                                        <el-option
                                                v-for="item in tagList"
                                                :key="item.value"
                                                :label="item.tag_name"
                                                :value="item.tag_name">
                                        </el-option>
                                    </el-select>
                                </el-col>

                            </el-row>
                            <el-row>

                            </el-row>
                        </el-form>

                        <el-row>
                            <el-col :span="2">
                                <img style=" height: 60px;width:60px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                    overflow: hidden; margin-top: 10px" :src="form.headImag"/>
                            </el-col>
                            <el-col :span="6">
                                <el-upload
                                        class="upload-demo"
                                        action="http://127.0.0.1:9090/image_quality/verify"
                                        name="avatarFile"
                                        :limit="1"
                                        :multiple="false"
                                        ref="upload"
                                        :file-list="fileList"
                                        :show-file-list="true"
                                        accept=".png,.jpg"
                                        :on-remove="handleRemove"
                                        :beforeUpload="beforeAvatarUpload"
                                        :on-exceed="handleExceed"
                                        :on-change="handleChange"
                                        :on-success="handleSuccess"
                                        style="margin-top: 25px;margin-left: 20px">
                                    <el-button size="mini" type="primary" pla in>上传</el-button>
                                    <div slot="tip" class="el-upload__tip">仅限于PNG/JPG文件，且不超过2M</div>
                                </el-upload>
                            </el-col>
                        </el-row>

                        <el-row style="margin-top:5px">
                            <el-col :span="18" :offset="15">
                                <el-col>
                                    <el-button type="danger" @click="handleCancel" icon="el-icon-close">取 消</el-button>
                                    <el-button type="primary" @click="onAdd" icon="el-icon-plus">添加</el-button>
                                </el-col>
                            </el-col>
                        </el-row>
                    </el-col>
                </el-row>
            </el-form>
        </el-dialog>

        <el-dialog
                :before-close="handleClose"
                :visible.sync="ChangeDialogVisible"
                width="50%"
                style="height:800px">
            <el-row>
                <el-col :span="22" :offset="1">
                    <h4>更改员工信息</h4>

                    <el-form :model="list" label-position="top">
                        <el-row style="margin-top: 10px">
                            <el-col :span="5">
                                <el-form-item label="姓名:">
                                    <el-input v-model="list.person_information.name"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="8" :offset="1">
                                <el-form-item label="电话:">
                                    <el-input v-model="list.person_information.phone"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="8" :offset="1">
                                <el-select
                                        v-model="tagName"
                                        multiple
                                        filterable
                                        allow-create
                                        default-first-option
                                        placeholder="请选择标签"
                                        label="标签:">
                                    <el-option
                                            v-for="item in tagList"
                                            :key="item.value"
                                            :label="item.tag_name"
                                            :value="item.tag_name">
                                    </el-option>
                                </el-select>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="2">
                                <img style=" height: 60px;width:60px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                    overflow: hidden; margin-top: 10px" :src="associated.headImag"/>
                            </el-col>
                            <el-col :span="6">
                                <el-upload
                                        class="upload-demo"
                                        action="http://127.0.0.1:9090/image_quality/verify"
                                        name="avatarFile"
                                        :limit="1"
                                        :multiple="false"
                                        ref="upload"
                                        :file-list="fileListChange"
                                        :show-file-list="true"
                                        accept=".png,.jpg"
                                        :on-remove="handleRemove"
                                        :beforeUpload="beforeAvatarUpload"
                                        :on-exceed="handleExceed"
                                        :on-change="handleChange"
                                        :on-success="handleChangeSuccess"
                                        style="margin-top: 25px;margin-left: 20px">
                                    <el-button size="mini" type="primary" pla in>更改</el-button>
                                    <div slot="tip" class="el-upload__tip">仅限于PNG/JPG文件，且不超过2M</div>
                                </el-upload>
                            </el-col>
                        </el-row>
                    </el-form>

                    <el-row style="margin-top:10px">
                        <el-col :span="20" :offset="13">
                            <el-button @click="ChangeDialogVisible = false" icon="el-icon-close" type="danger">取 消
                            </el-button>
                            <el-button type="primary" @click="onChange" icon="el-icon-plus">更改</el-button>
                        </el-col>
                    </el-row>
                </el-col>
            </el-row>
        </el-dialog>
        <el-dialog title="提示" :visible.sync="deleteConfirmVisible" width="30%" append-to-body
                   close-on-click-modal>
            <div>确认要删除员工吗？</div>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="deleteConfirmVisible = false" icon="el-icon-close">取 消
                </el-button>
                <el-button type="danger" @click="onConfirmDelete" icon="el-icon-check">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    var _this;
    import request from '../../api/request'

    export default {
        name: "staffManagement",
        components: {},
        data() {
            _this = this;
            return {
                pageSize: EveryPageNum,//每一页的num
                currentPage: 1,
                totalRecords: 0,
                startRow: 0,
                loadingUI: false,
                tableData: [],
                imageId: '',
                tagName: [],
                associated: { //关联更改图片
                    headImag: ''
                },
                list: {
                    person_information: {
                        phone: '',
                        name: '',
                        remark: '',
                        id: '',
                        employedDate: '',
                        cardNumbers: ''
                    },
                    staff_id: '',
                },
                form: {
                    phone: '',
                    name: '',
                    remark: '',
                    tagName: [],
                    headImag: '',
                    cardNumber: ''
                },
                tagList: [],
                addDialogVisible: false,
                deleteConfirmVisible: false,
                ChangeDialogVisible: false,
                fileList: [],
                fileListChange: [],
                deleteId: {}
            }
        },
        methods: {

            handleClose(done) {
                this.$confirm('是否取消?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'

                }).then(() => {
                    done();
                    _this.from = {};
                })
                    .catch(_ => {

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
            handleCurrentChange(val) {
                this.currentPage = val;
                _this.getfetch();
            },
            handleExceed(file, fileList) {
                showMessage(_this, "请先删除已选择文件!");

            },
            handleRemove(file, fileList) {
                _this.fileList = [];
            },
            handleChange(file, fileList) {
                this.fileList = fileList.slice(-3);
            },
            beforeAvatarUpload(file) {
                const isLt2M = file.size / 1024 / 1024 < 2;
                if (!isLt2M) {
                    _this.$message.error('上传文件大小不能超过 2MB!');
                }
                return isLt2M;

            },
            handleSuccess(response, file, fileList) {
                if (response.code === 400) {
                    _this.$message.error(response.message)
                    _this.fileList = [];
                } else if (response.code === 200) {
                    _this.form.headImag = 'data:image/jpg;base64,' + response.data;
                }
            },
            handleChangeSuccess(response, file, fileList) {
                if (response.code === 400) {
                    _this.$message.error(response.message)
                    _this.fileListChange = [];
                } else if (response.code === 200) {
                    _this.associated.headImag = 'data::image/jpg;base64,' + response.data;
                }
            },
            handleEdit(index, item) {
                _this.ChangeDialogVisible = true;
                _this.fileListChange = [];
                _this.list = copyObjectByJSON(item);
                _this.associated.headImag = item.imageId;
                _this.tagName = item.tagName
                _this.getfetch();

            },
            handledel(index, item) {
                _this.deleteConfirmVisible = true;
                _this.deleteId = copyObjectByJSON(item);
            },
            handleAdd() {
                _this.addDialogVisible = true;
            },
            handleCancel() {
                _this.addDialogVisible = false;
                _this.form = {};
            },

            getfetch() {
                _this.loadingUI = true;
                let params = new URLSearchParams();
                params.append("page", _this.currentPage),
                    params.append("size", _this.pageSize)

                request({
                    url: HOST + 'staffs/list',
                    method: 'post',
                    data: params,
                }).then(res => {
                    if (res.data.code == 200) {
                        let data = res.data.data.list
                        _this.tableData = res.data.data.list;
                        _this.totalRecords = res.data.data.total;
                        _this.startRow = res.data.data.startRow;
                        for (let i = 0; i < data.length; i++) {
                            _this.getImage(data[i].face_list[0].face_image_id, data[i]);
                        }
                        for (let i = 0; i < data.length; i++) {
                            _this.getTagNames(data[i].tag_id_list, data[i]);
                        }
                    } else {
                        showMessage(_this, "关联信息获取失败");
                    }
                }).catch(error => {
                    console.log(error);
                })
                _this.loadingUI = false;
            },
            getImage(id, item) {
                request({
                    url: HOST + 'image/' + id,

                    method: 'post'
                }).then(res => {
                    if (res.data.code == 200) {
                        item.imageId = 'data:image/jpg;base64,' + res.data.data;
                    } else {
                        showMessage("没有标签")
                    }
                }).catch(error => {
                    console.log(error)

                })
            },
            getTagNames(id, item) {
                request({
                    url: HOST + '/tag/list/' + id,
                    method: 'post',
                }).then(res => {
                    if (res.data.code == 200) {
                        item.tagName = res.data.data
                        console.log("标签名是" + item.tagName)
                    } else {
                        showMessage(_this, "获取标签信息失败");
                    }
                }).catch(error => {
                    console.log(error),
                        _this.loadingUI = false;

                })
            },
            getTagName() {
                let params = new URLSearchParams();
                params.append("page", _this.currentPage),
                    params.append("size", _this.pageSize)

                request({
                    url: HOST + '/tag/list',
                    method: 'post',
                    data: params,
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.tagList = res.data.data.list
                    } else {
                        showMessage(_this, "获取标签信息失败");
                    }
                }).catch(error => {
                    console.log(error),
                        _this.loadingUI = false;

                })
            },
            onConfirmDelete() {
                let params = new URLSearchParams();
                params.append("staffId", _this.deleteId.staff_id);
                params.append("id", _this.deleteId.person_information.id);
                request({
                    url: HOST + 'staffs/delete',
                    method: 'post',
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        showMessage(_this, "删除员工信息成功", 1);
                        _this.deleteConfirmVisible = false;
                        _this.getfetch();

                    } else {
                        showMessage(_this, "删除员工信息失败");
                    }
                }).catch(error => {
                    console.log(error)
                })
            },
            onAdd() {
                if (_this.verifyForm(_this.form)) {
                    let params = new URLSearchParams();
                    params.append("staffDTO", JSON.stringify(_this.form));
                    request({
                        url: HOST + '/staffs/add',
                        method: 'post',
                        data: params
                    }).then(res => {
                        if (res.data.code == 200) {
                            _this.addDialogVisible = false;
                            showMessage("添加员工成功！", 1)
                            _this.form = {}
                            _this.getfetch();
                        } else {
                            showMessage(_this, "添加员工信息失败");
                        }
                    }).catch(error => {
                        console.log(error)
                    })
                }
            },
            onChange() {
                let params = new URLSearchParams();
                params.append("date", JSON.stringify(_this.list));
                request({
                    url: HOST + 'staffs/update',
                    method: 'post',
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.ChangeDialogVisible = false;
                        showMessage(_this, "更改关联信息成功", 1);
                        _this.getfetch();

                    } else {
                        showMessage(_this, "更改关联信息失败");
                    }
                }).catch(error => {
                    console.log(error)
                })
            },
        },

        mounted: function () {
            setTimeout(function () {
                _this.getfetch();
                _this.getTagName();
            }, 300)
        },
    }

</script>
<style>
    .upload-demo input {
        display: none;
    }

    .logger {
        position: fixed;
        left: 550px;
        right: 0;
        margin: 0 auto;
        width: 65%;
    }
</style>
