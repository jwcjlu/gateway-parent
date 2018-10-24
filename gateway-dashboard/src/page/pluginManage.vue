<template>
    <div class="fillcontain">
        <headTop></headTop>
        <div class="table_container">

            <div style="margin-top: 15px;margin-bottom: 10px;display: flex;justify-content: flex-start">
                <div style="width: 10rem;margin-left: 2%">
                    <el-input v-model="pluginNames" placeholder="请输入插件名"></el-input>
                </div>
                <div style="margin-left: 2%">
                    <el-button @click="query">查询</el-button>
                </div>

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
                        v-if="competence['222']== undefined?false:true"
                        align="center"
                        fixed="right"
                        label="操作"
                        width="150">
                    <template slot-scope="scope">
                        <el-button type="text" @click="editClicked(scope.row)" size="small">编辑</el-button>
                    </template>
                </el-table-column>
               <!-- <el-table-column
                        property="id"
                        width="180"
                        align="center"
                        label="ID">
                </el-table-column>-->
                <el-table-column
                        align="center"
                        min-width="200"
                        :show-overflow-tooltip=true
                        property="pluginName"
                        label="插件名">
                </el-table-column>
                <el-table-column
                        align="center"
                        min-width="200"
                        :show-overflow-tooltip=true
                        :formatter="fmtbool"
                        property="enabled"
                        label="状态">
                </el-table-column>
                <el-table-column
                        min-width="200"
                        align="center"
                        :show-overflow-tooltip=true
                        property="timeCreated"
                        :formatter="dateFormat"
                        label="创建时间">
                </el-table-column>
                <el-table-column
                        align="center"
                        min-width="200"
                        :show-overflow-tooltip=true
                        property="timeUpdated"
                        label="更新时间"
                        :formatter="dateFormat">
                </el-table-column>
            </el-table>
            <div style="">
               <!-- <div style="margin-top: 20px; margin-left:20px;float: left">
                    <el-button type="danger" @click="deleteAll()" v-if="competence['103']== undefined?false:true">删除勾选数据</el-button>
                </div>
                <div style="margin-top: 20px; margin-left:20px;float: left">
                    <el-button type="success" @click="addClicked()" v-if="competence['102']== undefined?false:true">添加数据</el-button>
                </div>-->
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

        <el-dialog title="信息编辑" :visible.sync="dialogFormVisible">
            <el-form :model="form" :rules="rules" ref="form">
                <div style="margin-left: 0px;width: 80%">
                    <el-form-item label="插件名：" :label-width="formLabelWidth" prop="name" >
                        <el-input type="name" v-model="form.name" auto-complete="off" :disabled="disabled" @change="checkCNN(form.name)"></el-input>
                        <label v-if="cnShow" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;不能输入中文</label>
                    </el-form-item>
                </div>
                <el-form-item label="状态：" :label-width="formLabelWidth" prop="enable">
                    <el-select v-model="form.enable">
                        <el-option label="关闭" value="0"></el-option>
                        <el-option label="开启"  value="1"></el-option>
                    </el-select>
                </el-form-item>
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
                selected: "",
                retryCount: '',
                txGroupId: "",
                pluginNames:"",
                cnShow:false,
                loadingEnable: false,
                dialogFormVisible: false,
                disabled: false,
                form: {
                    name:"",
                    enable:"",
                    id:"",
                },
                competence:{},
                formLabelWidth: '120px',
                currentRow:null,
                rules: {
                    name: [
                        {required: true, message: '请输入名称', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max:64}
                    ],
                    enable: [
                        {required: true, message: '请选择状态', trigger: 'change'}
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
            this.$http.post(document.getElementById('serverIpAddress').href + '/plugin/pluginList', {
                "pageSize": this.paging.limit,
                "pageNumber": this.paging.currentPage,
                "name": this.pluginName,

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
        methods: {
            initCompetence(){
                var competence = sessionStorage.getItem("competence");
                if(competence == null){
                    this.$confirm('会话已失效，是否重新登录？')
                        .then(_ => {
                            this.$router.push('/');
                        });
                }else {
                    this.competence = JSON.parse(competence);
                }
            },
            editClicked(row) {
                this.dialogFormVisible = true;
                this.currentRow = row;
                this.disabled = true;
                this.form.id = this.currentRow.id;
                this.form.enable = ""+this.currentRow.enabled;
                this.form.name = ""+this.currentRow.pluginName;
            },
            addClicked() {
                this.dialogFormVisible = true;
                this.disabled = false;
                this.currentRow ="";
                this.form.enable ="";
                this.form.id ="";
                this.form.name = "";
            },
            updateRetryCount(formName) {
                let tData = this.tableData;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$http.post(document.getElementById('serverIpAddress').href + '/plugin/update', {
                            "id": this.form.id,
                            "enable": this.form.enable,
                            "name": this.form.name,
                            "userId":sessionStorage.getItem("userId")
                        }, {
                            headers: {
                                "token": sessionStorage.getItem("token"),
                            }
                        }).then(
                            response => {
                                if (response.body.code == 200) {
                                    for (var i = 0, len = tData.length; i < len; i++) {
                                        if (this.currentRow.id == tData[i].id) {
                                            tData[i].name = this.form.name;
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
                                    message: response
                                });
                            }
                        )
                    }
                });
            },
            checkCNN(val){
                var reg = /([\u4E00-\u9FA5])+/;//只要包含中文或者字母就提示
                if(reg.test(val)){
                    this.cnShow = true;
                }else{
                    this.cnShow = false;
                }
            },
            refresh(){
                this.$confirm('确认要更新配置？')
                    .then(_ => {
                    this.loadingEnable = true;
                    this.$http.post(document.getElementById('serverIpAddress').href + '/plugin/refresh', {}
                        , {
                            headers: {
                                "token": sessionStorage.getItem("token"),
                            }
                        }).then(
                        response => {
                            if (response.body.code == 200 && response.body.data != null) {
                                this.$message({
                                    type: 'success',
                                    message: '更新配置成功!'
                                });
                                this.loadingEnable = false;
                            } else {
                                this.$message({
                                    type: 'error',
                                    message: '获取数据失败或者数据为空!'
                                });
                                this.loadingEnable = false;
                            }
                            console.log("success!");
                        },
                        response => {
                            this.$message({
                                type: 'error',
                                message: response
                            });
                            this.loadingEnable = false;
                        }
                    )
                });
            },
            fmtbool (row, column) {
                const arr = row[column.property]
                if(arr === undefined){
                    return '0'
                } else if(arr == '0'){
                    return "关闭"
                } else if(arr == '1'){
                    return "开启"
                }
            },
            query: function () {
                    this.$http.post(document.getElementById('serverIpAddress').href + '/plugin/pluginList', {
                        "pageSize": this.paging.limit,
                        "pageNumber": this.paging.currentPage,
                        "name": this.pluginNames,
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
                                message: response
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
                        this.$http.post(document.getElementById('serverIpAddress').href + '/plugin/remove', {
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
                                    message: response
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
                        this.$http.post(document.getElementById('serverIpAddress').href + '/plugin/pluginList', {
                            "pageNumber": this.paging.currentPage,
                            "pageSize": this.paging.limit,
                            "name": this.pluginNames,
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
                                    message: response
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
