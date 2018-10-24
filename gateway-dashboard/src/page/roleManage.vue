<template>
    <div class="fillcontain">
        <headTop></headTop>
        <div class="table_container">
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
                        property="role"
                        label="角色名">
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
                <div style="margin-top: 20px; margin-left:20px;float: left">
                    <el-button type="danger" @click="deleteAll()" v-if="competence['241']== undefined?false:true">删除勾选数据</el-button>
                </div>
                <div style="margin-top: 20px; margin-left:20px;float: left">
                    <el-button type="success" @click="addClicked()" v-if="competence['240']== undefined?false:true">添加数据</el-button>
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

        <el-dialog title="信息编辑" :visible.sync="dialogFormVisible">
            <el-form :model="form" :rules="rules" ref="form">
                <div style="margin-left: 0px;width: 80%">
                    <el-form-item label="角色名：" :label-width="formLabelWidth" prop="role">
                        <el-input type="role" v-model="form.role" auto-complete="off"></el-input>
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
                selected: "",
                retryCount: '',
                txGroupId: "",
                nameDisable:false,
                dialogFormVisible: false,
                form: {
                    id: "",
                    role:"",
                },
                competence:{},
                formLabelWidth: '120px',
                currentRow:null,
                rules: {
                    role: [
                        {required: true, message: '请输入名称', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max:64}
                    ],
                }
            }
        },
        components: {
            ElButton,
            headTop,
        },
        created() {
                this.initCompetence();
                this.query();
        },
        watch: {
            '$route' (to, from) {
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
            editClicked(row) {
                this.dialogFormVisible = true;
                this.currentRow = row;
                this.form.id=this.currentRow.id;
                this.form.role=this.currentRow.role;
            },
            addClicked() {
                this.dialogFormVisible = true;
                this.currentRow ="";
                this.form.id="";
                this.form.role="";
            },
            updateRetryCount(formName) {
                let tData = this.tableData;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$http.post(document.getElementById('serverIpAddress').href + '/role/saveRole', {
                            "role": this.form.role,
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
                                            tData[i].id = this.form.id;
                                            tData[i].role = this.form.role;
                                        }
                                    }
                                    this.dialogFormVisible = false;
                                    this.$message({
                                        type: 'success',
                                        message: '数据处理成功!'
                                    });
                                    this.form.role = "";
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
                    this.$http.post(document.getElementById('serverIpAddress').href + '/role/rolePageList', {
                        "pageSize": this.paging.limit,
                        "pageNumber": this.paging.currentPage,
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
                /*}*/
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
                        this.$http.post(document.getElementById('serverIpAddress').href + '/role/deleteRole', {
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
                        this.$http.post(document.getElementById('serverIpAddress').href + '/role/rolePageList', {
                            "pageNumber": this.paging.currentPage,
                            "pageSize": this.paging.limit,
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
