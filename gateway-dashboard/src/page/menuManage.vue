<template>
    <div class="fillcontain">
        <headTop></headTop>
        <div class="table_container">
            <div style="margin-top: 15px;margin-bottom: 10px;display: flex;justify-content: flex-start">
                <div style="width: 10rem;margin-left: 2%">
                    <el-input v-model="parentId" placeholder="请输入父节点"></el-input>
                </div>
                <div style="width: 10rem;margin-left: 2%">
                    <el-input v-model="name" placeholder="请输入节点名称"></el-input>
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
                        property="id"
                        width="180"
                        align="center"
                        label="节点ID">
                </el-table-column>
                <el-table-column
                        min-width="200"
                        align="center"
                        :show-overflow-tooltip=true
                        property="name"
                        label="名称">
                </el-table-column>
                <el-table-column
                        min-width="200"
                        align="center"
                        :show-overflow-tooltip=true
                        property="url"
                        label="请求路径">
                </el-table-column>
                <el-table-column
                        align="center"
                        min-width="200"
                        :show-overflow-tooltip=true
                        :formatter="fmt"
                        property="parentId"
                        label="父节点">
                </el-table-column>
            </el-table>
            <div style="">
                <div style="margin-top: 20px; margin-left:20px;float: left">
                    <el-button type="danger" @click="deleteAll()" v-if="competence['261']== undefined?false:true">删除勾选数据</el-button>
                </div>
                <div style="margin-top: 20px; margin-left:20px;float: left">
                    <el-button type="success" @click="addClicked()" v-if="competence['260']== undefined?false:true">添加数据</el-button>
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
                    <el-form-item label="节点Id：" :label-width="formLabelWidth" prop="id" :rules="[
                          { required: true, message: '节点不能为空'},
                          { type: 'number', message: 'id必须为数字值'},
                           { type: 'number', message: '超出范围1-1000000', max:1000000,min:1}
                        ]">
                        <el-input type="id" v-model.number="form.id" auto-complete="off" style="width:80%; "></el-input>
                    </el-form-item>
                <el-form-item label="名称：" :label-width="formLabelWidth" prop="name">
                    <el-input type="name" v-model="form.name" auto-complete="off" style="width:80%; "></el-input>
                </el-form-item>
                <el-form-item label="请求路径：" :label-width="formLabelWidth" prop="url">
                    <el-input type="url" v-model="form.url" auto-complete="off" style="width:80%; "></el-input>
                </el-form-item>
                <el-form-item label="父节点Id：" :label-width="formLabelWidth" prop="" :rules="[
                          { type: 'number', message: 'id必须为数字值'},
                           { type: 'number', message: '超出范围1-1000000', max:1000000,min:1}
                        ]">
                    <el-input type="parentId" v-model.number="form.parentId" auto-complete="off" style="width:80%; "></el-input>
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
                name:"",
                parentId:"",
                res: null,
                options: [],
                selected: "",
                retryCount: '',
                txGroupId: "",
                nameDisable:false,
                dialogFormVisible: false,
                form: {
                    id: "",
                    name:"",
                    parentId: "",
                    url:"",
                },
                competence:{},
                formLabelWidth: '120px',
                currentRow:null,
                rules: {
                    name: [
                        {required: true, message: '请输入名称', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max:64}
                    ],
                    url: [
                        {required: false, message: '内容过长,请重新输入', trigger: 'blur', max:64}
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
                this.$http.post(document.getElementById('serverIpAddress').href + '/menu/menuList', {
                    "pageSize": this.paging.limit,
                    "pageNumber": this.paging.currentPage,
                    "parentId":this.parentId.trim(),
                    "name":this.name.trim(),
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
                }else{
                    this.competence = JSON.parse(competence);
                }
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
                        this.$http.post(document.getElementById('serverIpAddress').href + '/menu/saveMenu', {
                            "name": this.form.name,
                            "id": this.form.id,
                            "url": this.form.url,
                            "parentId": this.form.parentId,
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
                                            tData[i].name = this.form.name;
                                            tData[i].url = this.form.url;
                                            tData[i].parentId = this.form.parentId;
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
                                    message: response
                                });
                            }
                        )
                    }
                });
            },
            query: function () {
                    this.$http.post(document.getElementById('serverIpAddress').href + '/menu/menuList', {
                        "pageSize": this.paging.limit,
                        "pageNumber": this.paging.currentPage,
                        "parentId":this.parentId.trim(),
                        "name":this.name.trim(),
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
                        this.$http.post(document.getElementById('serverIpAddress').href + '/menu/deleteMenu', {
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

            fmt:function(row, column) {
                var date = row[column.property];
                if(date == "-1"){
                    return "主节点(-1)"
                }
                return date;
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
                        this.$http.post(document.getElementById('serverIpAddress').href + '/menu/menuList', {
                            "pageNumber": this.paging.currentPage,
                            "pageSize": this.paging.limit,
                            "parentId":this.parentId,
                            "name":this.name.trim(),
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
