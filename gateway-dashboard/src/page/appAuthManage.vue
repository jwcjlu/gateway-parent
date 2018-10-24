<template>
    <div class="fillcontain">
        <headTop></headTop>
        <div class="table_container">
            <div style="margin-top: 1px; margin-bottom: 10px; float: right"
                 v-if="competence['91']== undefined?false:true">
                <!--<el-button style="float: right" type="success" @click="refreshAppAuth()" :loading="loadingEnable"><i
                        class="el-icon-upload"></i>同步全部appAuth配置
                </el-button>-->
            </div>
            <div style="margin-top: 15px;margin-bottom: 10px;display: flex;justify-content: flex-start">
                <div style="margin-left: 2%">
                </div>

                <div style="width: 10rem;margin-left: 2%">
                    <el-input v-model="appKey" placeholder="请输入appKey"></el-input>
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
                        v-if="competence['202']== undefined?false:true"
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
                        property="appKey"
                        label="appKey">
                </el-table-column>
                <el-table-column
                        align="center"
                        min-width="200"
                        :show-overflow-tooltip=true
                        property="appSecret"
                        label="appSecret">
                </el-table-column>
                <el-table-column
                        min-width="200"
                        align="center"
                        :show-overflow-tooltip=true
                        property="enabled"
                        :formatter="format"
                        label="签名校验">
                </el-table-column>
                <el-table-column
                        align="center"
                        min-width="200"
                        :show-overflow-tooltip=true
                        property="timeCreated"
                        label="创建时间"
                        :formatter="dateFormat">
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
                    <el-button type="danger" @click="deleteAll()" v-if="competence['201']== undefined?false:true">
                        删除勾选数据
                    </el-button>
                </div>
                <div style="margin-top: 20px; margin-left:20px;float: left">
                    <el-button type="success" @click="addClicked()" v-if="competence['200']== undefined?false:true">
                        添加数据
                    </el-button>
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
                    <el-form-item label="appKey：" :label-width="formLabelWidth" prop="appKey">
                        <el-input type="appKey" v-model="form.appKey" auto-complete="off"
                                  :disabled="itemVisible"></el-input>
                    </el-form-item>
                    <el-form-item label="签名校验：" :label-width="formLabelWidth" prop="enabled">
                        <el-select v-model="form.enabled">
                            <el-option label="开启" value="1"></el-option>
                            <el-option label="关闭" value="0"></el-option>
                        </el-select>
                    </el-form-item>
                   <!-- <el-form-item label="插件：" :label-width="formLabelWidth" prop="pluginIds">
                        <el-select v-model="form.pluginIds" multiple placeholder="请选择"  @remove-tag="removeTag">
                            <el-option
                                    v-for="item in poptions"
                                    :key="item.id"
                                    :label="item.pluginName"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>-->

                </div>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateRetryCount('form')">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import headTop from '../components/headTop'
    import ElButton from "../../node_modules/element-ui/packages/button/src/button.vue";
    import moment from "moment";
    import ElFormItem from "../../node_modules/element-ui/packages/form/src/form-item.vue";

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

                poptions: [],
                selected: "",
                removeIds:[],
                retryCount: '',
                txGroupId: "",
                appKey: "",
                itemVisible: true,
                dialogFormVisible: false,
                form: {
                    appKey: "",
                    id: "",
                    enabled: "",
                    pluginIds:[],
                },
                formLabelWidth: '120px',
                competence: {},
                currentRow: null,
                rules: {
                    appKey: [
                        {required: true, message: '请输入名称', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max: 64}
                    ],
                    enabled: [
                        {required: true, message: '请选择是否开启', trigger: 'change'}
                    ],
                }
            }
        },
        components: {
            ElFormItem,
            ElButton,
            headTop,
        },
        created() {
            this.initCompetence();
            this.$http.post(document.getElementById('serverIpAddress').href + '/appAuth/appList', {
                "pageSize": this.paging.limit,
                "pageNumber": this.paging.currentPage,
                "appAuth": this.appKey.trim(),
                "userId": sessionStorage.getItem("userId")
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
           /* removeTag(val){
                this.removeIds.push(val.value);
            },*/
            initCompetence() {
                var competence = sessionStorage.getItem("competence");
                if (competence == null) {
                    this.$confirm('会话已失效，是否重新登录？')
                        .then(_ => {
                            this.$router.push('/');
                        });
                } else {
                    this.competence = JSON.parse(competence);
                }
            },
            editClicked(row) {
                this.dialogFormVisible = true;
                this.currentRow = row;
                this.itemVisible = true;
                this.form.appKey = this.currentRow.appKey;
                this.form.enabled = "" + this.currentRow.enabled;
                this.form.pluginIds = this.currentRow.ids;

                this.queryPlugin();
            },
            addClicked() {
                this.dialogFormVisible = true;
                this.itemVisible = false;
                this.currentRow = "";
                this.form.appKey = "";
                this.form.enabled = "";
                this.form.pluginIds  =[];
                this.queryPlugin();
            },
            updateRetryCount(formName) {
                let tData = this.tableData;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$http.post(document.getElementById('serverIpAddress').href + '/appAuth/update', {
                            "enabled": this.form.enabled,
                            "id": this.currentRow.id,
                            "appAuth": this.form.appKey.trim(),
                            "userId": sessionStorage.getItem("userId"),
                            "removeIds":this.removeIds,
                        }, {
                            headers: {
                                "token": sessionStorage.getItem("token"),
                            }
                        }).then(
                            response => {
                                if (response.body.code == 200) {
                                    for (var i = 0, len = tData.length; i < len; i++) {
                                        if (this.currentRow.id == tData[i].id) {
                                            tData[i].enabled = this.form.enabled;
                                        }
                                    }
                                    this.dialogFormVisible = false;
                                    this.$message({
                                        type: 'success',
                                        message: '数据处理成功!'
                                    });
                                    this.removeIds=[],
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
                /*}*/
            },
            query: function () {
                this.$http.post(document.getElementById('serverIpAddress').href + '/appAuth/appList', {
                    "pageSize": this.paging.limit,
                    "pageNumber": this.paging.currentPage,
                    "appAuth": this.appKey.trim(),
                    "userId": sessionStorage.getItem("userId")

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

            queryPlugin: function () {
                this.$http.post(document.getElementById('serverIpAddress').href + '/plugin/pluginList', {
                    "pageSize": 100,
                    "pageNumber": 1,
                }, {
                    headers: {
                        "token": sessionStorage.getItem("token"),
                    }
                }).then(
                    response => {
                        if (response.body.code == 200 && response.body.data != null) {
                            let rp = response.body;
                            this.poptions = rp.data.dataList
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
                if (groupIds.length == 0) {
                    this.$message({
                        type: 'error',
                        message: '请先选择数据!'
                    });
                } else {
                    //delete row and update tableData but don't send post request to update all data
                    var oldTableData = this.tableData;
                    var tlen = oldTableData.length;
                    this.$confirm('确认要删除？')
                        .then(_ => {
                            this.$http.post(document.getElementById('serverIpAddress').href + '/appAuth/remove', {
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
                }

            },
            subRow: function () {
                return {"font-size": "0.85em"};
            },

            dateFormat: function (row, column) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            },
            format(row, column) {
                const arr = row[column.property]
                if (arr === undefined) {
                    return '0'
                } else if (arr == '0') {
                    return "关闭"
                } else if (arr == '1') {
                    return "开启"
                }
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
                    this.$http.post(document.getElementById('serverIpAddress').href + '/appAuth/appList', {
                        "pageNumber": this.paging.currentPage,
                        "pageSize": this.paging.limit,
                        "appAuth": this.appKey.trim(),
                        "userId": sessionStorage.getItem("userId")
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
