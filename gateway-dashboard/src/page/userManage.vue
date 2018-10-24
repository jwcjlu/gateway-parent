<template>
    <div class="fillcontain">
        <headTop></headTop>
        <div class="table_container">
            <div style="margin-top: 15px;margin-bottom: 10px;display: flex;justify-content: flex-start">
                <div style="margin-left: 2%">
                </div>

                <div style="width: 10rem;margin-left: 2%">
                    <el-input v-model="userName" placeholder="请输入用户名"></el-input>
                </div>
                <div style="margin-left: 2%">
                    <el-button @click="query">查询</el-button>
                </div>
                <div style="clear:both"></div>
            </div>
            <el-table
                    :border=true
                    ref="multipleTable"
                    :data="tableData"
                    highlight-current-row
                    style="width: 100%">
                <el-table-column
                        align="center"
                        type="selection"
                        width="40">
                </el-table-column>
                <el-table-column
                        v-if="competence['232']== undefined?false:true"
                        align="center"
                        fixed="right"
                        label="操作"
                        width="150">
                    <template slot-scope="scope">
                        <el-button type="text" @click="editClicked(scope.row)" size="small" >编辑</el-button>
                    </template>
                </el-table-column>
              <!--  <el-table-column
                        property="id"
                        width="180"
                        align="center"
                        label="ID">
                </el-table-column>-->
                <el-table-column
                        align="center"
                        min-width="200"
                        :show-overflow-tooltip=true
                        property="userName"
                        label="用户名">
                </el-table-column>
                <el-table-column
                        min-width="200"
                        align="center"
                        :show-overflow-tooltip=true
                        property="dateCreated"
                        :formatter="dateFormat"
                        label="创建时间">
                </el-table-column>
                <el-table-column
                        align="center"
                        min-width="200"
                        :show-overflow-tooltip=true
                        property="dateUpdated"
                        label="更新时间"
                        :formatter="dateFormat">
                </el-table-column>
            </el-table>
            <div style="">
                <div style="margin-top: 20px; margin-left:20px;float: left">
                    <el-button type="danger" @click="deleteAll()" v-if="competence['231']== undefined?false:true">删除勾选数据</el-button>
                </div>
                <div style="margin-top: 20px; margin-left:20px;float: left">
                    <el-button type="success" @click="addClicked()" v-if="competence['230']== undefined?false:true" >添加数据</el-button>
                </div>
                <div class="Pagination" style="text-align: left;margin-top: 20px;float: right">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="paging.currentPage"
                            :page-sizes="[10,20,50, 100, 200]"
                            :page-size="paging.limit"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="count">
                    </el-pagination>
                </div>
            </div>
        </div>
        <!-- Form -->

        <el-dialog title="信息编辑" :visible.sync="dialogFormVisible" >
            <el-form :model="form" :rules="rules" ref="form">
                <div style="margin-left: 0px;width: 80%">
                    <el-form-item label="用户名：" :label-width="formLabelWidth" prop="newUserName">
                        <el-input type="newUserName" v-model="form.newUserName" auto-complete="off" :disabled="nameDisable"></el-input>
                    </el-form-item>
                    <el-form-item label="密码：" :label-width="formLabelWidth" prop="newPassword">
                        <el-input type="newPassword" v-model="form.newPassword" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="角色：" :label-width="formLabelWidth" prop="role">
                        <el-select v-model="form.role" style="width:195px; ">
                            <el-option
                                    v-for="x in roptions"
                                    :key="x.role"
                                    :label="x.role"
                                    :value="x.role">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </div>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateRetryCount('form')" >确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import headTop from '../components/headTop'
    import ElButton from "../../node_modules/element-ui/packages/button/src/button.vue";
    import moment from "moment";

    export default {
        data() {
            return {

                tableData: [],
                searchValue: "",
                paging: {
                    limit: 10,
                    currentPage: 1,
                },
                count: 0,
                res: null,
                options: [],
                roptions:[{
                    role:"",
                    id:""
                }],
                competence:{},
                selected: "",
                retryCount: '',
                txGroupId: "",
                nameDisable:false,
                dialogFormVisible: false,
                form: {
                    id: "",
                    newUserName:"",
                    newPassword:"",
                    role:"",
                    roleId:"",
                },
                formLabelWidth: '120px',
                currentRow:null,
                rules: {
                    newUserName: [
                        {required: true, message: '请输入姓名', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max:64}
                    ],
                    newPassword: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max:64}
                    ],
                    role: [
                        {required: true, message: '请选择角色', trigger: 'change'},
                    ]
                }
            }
        },
        components: {
            ElButton,
            headTop,
        },
        created() {
           this.initCompetence();
            this.initRole();
            this.query();
        },
        watch: {
            '$route' (to, from) {
                this.initCompetence();
                this.initRole();
                this.query();
            }
        },
        methods: {
            initCompetence(){
                var competence = sessionStorage.getItem("competence");
                if(competence == null){
                    this.$confirm('会话已失效，是否重新登录？')
                        .then(_ => {
                            this.$router.push('/');
                        });
                }else{
                    this.competence = JSON.parse(competence);
                }
            },
            initRole(){
                this.$http.post(document.getElementById('serverIpAddress').href + '/role/roleList', {
                }, {
                    headers: {
                        "token": sessionStorage.getItem("token"),
                    }
                }).then(
                    response => {
                        if (response.body.code == 200 && response.body.data != null) {
                            let rp = response.body;
                            this.roptions = rp.data.dataList;
                            //this.form.role = this.roptions[0].role;
                        } else {
                            this.$message({
                                type: 'error',
                                message: '获取数据失败或者数据为空!'
                            });
                        }
                        console.log("success!");
                    },
                    response => {
                        this.$message({
                            type: 'error',
                            message: response.body.message
                        });
                    }
                )
            },
            editClicked(row) {
                this.initRole();
                this.dialogFormVisible = true;
                this.currentRow = row;
                this.form.id=this.currentRow.id;
                this.form.newUserName=this.currentRow.userName;
                this.form.newPassword=this.currentRow.password;
                var roleId = this.currentRow.role;
                var index;
                for(index in this.roptions){

                    if(this.roptions[index].id == roleId){
                        this.form.role = this.roptions[index].role;
                    }
                }

                this.nameDisable = true;
            },
            addClicked() {
                this.dialogFormVisible = true;
                this.currentRow ="";
                this.form.id="";
                this.form.newUserName="";
                this.form.newPassword="";
                this.initRole();
                this.nameDisable = false;
            },
            updateRetryCount(formName) {
                let tData = this.tableData;
                var index;
                for(index in this.roptions){
                    if(this.roptions[index].role == this.form.role){
                        this.form.roleId = this.roptions[index].id;
                    }
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$http.post(document.getElementById('serverIpAddress').href + '/user/update', {
                            "userName": this.form.newUserName,
                            "password": this.form.newPassword,
                            "role":this.form.roleId,
                            "id": this.form.id
                        }, {
                            headers: {
                                "token": sessionStorage.getItem("token"),
                            }
                        }).then(
                            response => {
                                if (response.body.code == 200) {
                                    for (var i = 0, len = tData.length; i < len; i++) {
                                        if (this.currentRow.id == tData[i].id) {
                                            tData[i].username = this.form.newUserName;
                                            tData[i].password = this.form.newPassword;
                                        }
                                    }
                                    this.dialogFormVisible = false;
                                    this.$message({
                                        type: 'success',
                                        message: '数据处理成功!'
                                    });
                                    this.form.newUserName = "";
                                    this.currentRow = "";
                                    this.query();//刷新页面
                                } else {
                                    this.$message({
                                        type: 'error',
                                        message: response.body.message
                                    });
                                }
                            },
                            response => {
                                this.$message({
                                    type: 'error',
                                    message: response.body.message
                                });
                            }
                        )
                    }
                });
            },
            query: function () {
                    this.$http.post(document.getElementById('serverIpAddress').href + '/user/userList', {
                        "pageSize": this.paging.limit,
                        "pageNumber": this.paging.currentPage,
                        "userName": this.userName,
                    }, {
                        headers: {
                            "token": sessionStorage.getItem("token"),
                        }
                    }).then(
                        response => {
                            if (response.body.code == 200 && response.body.data != null) {
                                let rp = response.body;
                                this.count = rp.data.totalCount;
                                this.tableData = rp.data.dataList
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: '获取数据失败或者数据为空!'
                                });
                            }

                            console.log("success!");
                        },
                        response => {
                            this.$message({
                                type: 'error',
                                message: response.body.message
                            });
                        }
                    )
            },
            deleteAll: function () {
                var Selection = this.$refs.multipleTable.selection;
                var groupIds = [];
                var slen = Selection.length;
                for (var i = 0; i < slen; i++) {
                    groupIds.push(Selection[i].id);
                }
                //delete row and update tableData but don't send post request to update all data
                var oldTableData = this.tableData;
                var tlen = oldTableData.length;
                this.$confirm('确认要删除？')
                    .then(_ => {
                        this.$http.post(document.getElementById('serverIpAddress').href + '/user/remove', {
                            "ids": groupIds,
                        }, {
                            headers: {
                                "token": sessionStorage.getItem("token"),
                            }
                        }).then(
                            response => {
                                if (response.body.code == 200) {
                                    this.$message({type: 'success', message: '删除数据成功!'});
                                    for (let x = 0; x < slen; x++) {
                                        for (let j = 0; j < tlen; j++) {
                                            if (groupIds[x] == oldTableData[j].id) {
                                                oldTableData.splice(j, 1);
                                                tlen = tlen - 1;
                                            }
                                        }
                                    }
                                    this.count = this.count - slen;
                                } else {
                                    this.$message({
                                        type: 'error',
                                        message: '删除数据失败!'
                                    });
                                }
                            },
                            response => {
                                this.$message({
                                    type: 'error',
                                    message: response.body.message
                                });
                            }
                        )
                    })
            },
            subRow: function () {
                return {"font-size": "0.85em"};
            },

            dateFormat:function(row, column) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            subTableHeader: function () {
                return {"background-color": "red", "font-size": "0.6em"};
            },
            handleSizeChange(val) {
                this.paging.limit = val;
            },
            handleCurrentChange(val) {
                this.paging.currentPage = val;
            },
        },
        watch: {
            paging: {
                handler: function () {
                        this.$http.post(document.getElementById('serverIpAddress').href + '/user/userList', {
                            "pageNumber": this.paging.currentPage,
                            "pageSize": this.paging.limit,
                            "userName": this.userName
                        }, {
                            headers: {
                                "token": sessionStorage.getItem("token"),
                            }
                        }).then(
                            response => {
                                if (response.body.code == 200) {
                                    let rp = response.body;
                                    this.count = rp.data.totalCount;
                                    this.tableData = rp.data.dataList
                                } else {
                                    this.$message({
                                        type: 'error',
                                        message: '获取数据失败!'
                                    });
                                }
                                console.log("success!");
                            },
                            response => {
                                this.$message({
                                    type: 'error',
                                    message: response.body.message
                                });
                            }
                        )
                },
                deep: true
            }

        }
    }
</script>

<style lang="less">
    @import '../style/mixin';

    .table_container {
        padding: 0px;
    }

    .subTableHeaderFont {
        font-size: 0.9em;
        padding: 0px;
        text-align: center;
        color: rebeccapurple !important;
        background-color: white !important;
    }

    .el-input {

    }

    .el-table__expanded-cell {
        padding: 5px !important;
    }
</style>
