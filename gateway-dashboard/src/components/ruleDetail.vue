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
                        property="ruleName"
                        label="规则名称">
                </el-table-column>

                <el-table-column
                        align="center"
                        min-width="200"
                        :show-overflow-tooltip=true
                        property="serviceList"
                        label="过滤服务">
                </el-table-column>
                <el-table-column
                        align="center"
                        min-width="200"
                        :show-overflow-tooltip=true
                        property="handler"
                        label="处理逻辑">
                </el-table-column>
                <el-table-column
                        v-if="this.ruleShow.ruleUpdateShow"
                        align="center"
                        fixed="right"
                        label="操作"
                        width="200">
                    <template slot-scope="scope">
                        <el-button type="text" @click="editClicked(scope.row)" size="small">编辑</el-button>
                    </template>
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
                <div style="margin-top: 20px; margin-left:20px;float: left" v-if="ruleShow.ruleDeleteShow">
                    <el-button type="danger" @click="deleteAll()">删除勾选数据</el-button>
                </div>
                <div style="margin-top: 20px; margin-left:20px;float: left" v-if="ruleShow.ruleAddShow">
                    <el-button type="success" @click="addClicked()">添加数据</el-button>
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
        <el-dialog title="信息编辑" :visible.sync="dialogFormVisible">
            <el-form :model="form" :rules="rules" ref="form">
                <div style="margin-left: 0px;width: 80%">
                    <el-form-item label="规则名称：" :label-width="formLabelWidth" prop="ruleName">
                        <el-input type="ruleName" v-model="form.ruleName" auto-complete="off"
                                  placeholder="规则名称"></el-input>
                    </el-form-item>
                </div>
                <div style="margin-left: 0px;width: 80%" >
                    <el-form-item label="并发控制：" :label-width="formLabelWidth" prop="rateLimiterType" v-if="pluginDialogList[0].value">
                        <el-select v-model="form.concurrencyType">
                            <el-option label="redis限流法" value="0"></el-option>
                            <el-option label="本地计数法"  value="1"></el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="处理：" :label-width="formLabelWidth" v-if="pluginDialogList[0].value">
                       <!-- <el-col :span="10">
                            <el-form-item prop="replenishRate">
                                <el-select v-model="form.replenishRate">
                                    <el-option label="1秒" value="1"></el-option>
                                    <el-option label="1分钟" value="60"></el-option>
                                    <el-option label="1小时" value="3600"></el-option>
                                    <el-option label="1天" value="86400"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>-->
                        <el-col :span="7">
                            <el-form-item prop="burstCapacity" :rules="[
                          { required: true, message: '次数不能为空'},
                          { type: 'number', message: '次数必须为数字值'},
                           { type: 'number', message: '超出范围', max:100000000,min:1}
                        ]">
                                <el-input type="burstCapacity" v-model.number="form.burstCapacity" auto-complete="off"
                                          placeholder="并发数" style="width:150px;"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                </div>
                <div style="margin-left: 0px;width: 80%" >
                    <el-form-item  v-if="pluginDialogList[1].value" >
                        <el-form-item :label-width="formLabelWidth" label="均衡方式：">
                            <el-select v-model="form.loadBalance" >
                                <el-option label="random" value="random"></el-option>
                                <el-option label="roundRobin" value="roundRobin"></el-option>
                            </el-select>
                            <label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            <el-radio-group v-model="form.weightType">
                                <el-radio :label="0">智能分配权重</el-radio>
                                <el-radio :label="1">手动分配权重</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    <el-form-item v-for="(domain, index) in form.domains" :key="domain.key"
                                  :prop="'domains.' + index + '.value'" :label-width="formLabelWidth" label="处理：" >
                        <el-form-item>
                            <slot name="label">转发ip/host:</slot>
                            <el-input type="upstreamHost" v-model="domain.upstreamHost" auto-complete="off"
                                      placeholder="转发地址" style="width: 325px"></el-input>

                            <slot name="label">&nbsp;&nbsp;&nbsp;port:</slot>
                            <el-input type="port" v-model="domain.upstreamPort" auto-complete="off"
                                      placeholder="转发port" style="width: 145px;margin-top: 10px"></el-input>

                            <slot name="label">超时时间(ms):</slot>
                            <el-input type="timeout" v-model="domain.timeout" auto-complete="off"
                                      placeholder="填写整数"
                                      style="width: 140px;margin-top: 10px" @change="checkNumber(domain.timeout,2)"></el-input>

                            <slot name="label">&nbsp;&nbsp;&nbsp;权重:</slot>
                            <el-input type="domain.weight" v-model="domain.weight" auto-complete="off"
                                      placeholder="填写整数" style="width: 125px;margin-top: 10px" @change="checkNumber(domain.weight,2)"></el-input>
                            <slot name="label">&nbsp;&nbsp;&nbsp;重试次数:</slot>
                            <el-input type="domain.retry" v-model="domain.retry" auto-complete="off"
                                      placeholder="重试次数" style="width: 115px;margin-top: 10px" @change="checkNumber(domain.retry,2)"></el-input>
                        </el-form-item>

                        <el-button @click.prevent="removeDomain(domain)" type="danger" style="margin-top: 10px">
                            删除
                        </el-button>
                        <el-button @click="addDomain" type="info" style="margin-top: 5px">新增</el-button>
                        <label v-if="enabledShow" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;处理数据存在空值</label>
                        <label v-if="numShowF" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;请输入数字且在范围内</label>
                    </el-form-item>
                    </el-form-item>
                </div>
                <div style="margin-left: 0px;width: 80%">
                    <el-form-item label="服务列表：" :label-width="formLabelWidth" prop="serviceIds">
                        <el-select v-model="form.serviceIds" multiple placeholder="请选择" @remove-tag="removeTag">
                            <el-option-group v-for="group in soptions"
                                             :key="group.label"
                                             :label="group.label">
                                <el-option v-for="item in group.options"
                                           :key="item.value"
                                           :label="item.label"
                                           :value="item.value">
                                </el-option>
                            </el-option-group>
                        </el-select>
                    </el-form-item>
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
    import ElOption from "../../node_modules/element-ui/packages/select/src/option.vue";
    import ElFormItem from "../../node_modules/element-ui/packages/form/src/form-item.vue";
    import ElDialog from "../../node_modules/element-ui/packages/dialog/src/component.vue";

    export default {
        data() {
            return {
                plugin: "",
                tableData: [],
                paging: {
                    limit: 10,
                    currentPage: 1,
                    count: 0,
                },
                soptions: [{
                    label: '热门城市',
                    options: [{
                        value: 1,
                        label: '上海'
                    }, {
                        value: 2,
                        label: '北京'
                    }]
                }, {
                    label: '城市名',
                    options: [{
                        value: 3,
                        label: '成都'
                    }, {
                        value: 4,
                        label: '深圳'
                    }, {
                        value: 5,
                        label: '广州'
                    }, {
                        value: 6,
                        label: '大连'
                    }]
                }],
                removeIds:[],
                count: 0,
                ruleTitle: "",
                dialogFormVisible:false,
                formLabelWidth: '120px',
                currentRow: null,
                rowId: "",
                conditionLengthShow: false,
                oldIds:[],
                oldIdsCp:[],
                form: {
                    rateType:"",
                    id: "",
                    name: "",
                    replenishRate: "",
                    burstCapacity: 0,
                    concurrencyType:"",

                    serviceIds:[],
                    concurrencyHandle:{
                        concurrencyType:"",
                        burstCapacity:0,
                    },
                    loadBalance:"",
                    weightType:1,
                    domains: [{
                        weight: '',
                        timeout: '',
                        upstreamHost: '',
                        upstreamPort: '',
                        retry:"",
                    }],
                },
                pluginDialogList: [
                    {"tag": "dialogRateLimitingVisible", "value": false},
                    {"tag": "dialogDivideVisible", "value": false},
                ],
                rules: {
                    ruleName: [
                        {required: true, message: '请输入规则名称', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max: 64}
                    ],
                    concurrencyType: [
                        {required: true, message: '请选择', trigger: 'change'}
                    ],
                    replenishRate: [
                        {required: true, message: '请选择', trigger: 'change'}
                    ],
                    serviceIds: [
                        {required: true, message: '请选择', trigger: 'change' , type:'array'}
                    ],

                }
            }
        },
        props:["paging","tableData","count","ruleShow"],
        components: {
            ElFormItem,
            ElOption,
            ElButton,
        },
        created() {
            /* this.initEnum();
             document.getElementById("selectorId").value = "";*/
            this.getServiceList();
            this.query();
        },
        methods: {
            removeTag(val){
                this.removeIds.push(val.value);
            },
            checkNumber(val,num){
                var reg = /^[0-9]{1,20}$/;//只要包含中文或者字母就提示
                if(reg.test(val)){
                    if(num == 1){
                        this.numShowH = false;
                    }else{
                        this.numShowF = false;
                    }
                }else{
                    if(num == 1){
                        if(val.length == 0){
                            this.numShowH = false;
                        }else{
                            this.numShowH = true;
                        }

                    }else{
                        this.numShowF = true;
                    }
                }
            },
            removeDomain(item) {
                var index = this.form.domains.indexOf(item)
                if (index !== 0) {
                    this.form.domains.splice(index, 1)
                }
            },
            addDomain() {
                this.form.domains.push({
                    weight: '',
                    timeout: '',
                    upstreamHost: '',
                    upstreamPort: '',
                    retry: '',
                });
            },
            initDialog(){
                let plugin = document.getElementById('plugin').value;
                if (plugin == "concurrency") {
                    this.pluginDisableView = "dialogRateLimitingVisible";
                }  else if (plugin == "divide") {
                    this.pluginDisableView = "dialogDivideVisible";
                }
                this.checktools(this.pluginDisableView);
            },
            checktools(disable) {
                for (var i = 0; i < 2; i++) {
                    if (disable == this.pluginDialogList[i].tag) {
                        this.pluginDialogList[i].value = true;
                    } else {
                        this.pluginDialogList[i].value = false;
                    }
                }
            },
            editClicked(row) {
                this.dialogFormVisible = true;
                this.currentRow = row;
                this.form.id = this.currentRow.id;
                this.form.ruleName = this.currentRow.ruleName;
                this.form.serviceIds =  this.currentRow.ids;
                this.oldIds = this.currentRow.ids;
                //alert("edit"+this.oldIds);
                let plugin = document.getElementById('plugin').value;
                var myObject = eval("(" + this.currentRow.handler + ")");
                if (plugin == "rate_limiter") {
                    this.form.burstCapacity = myObject.burstCapacity;
                    this.form.rateLimiterType = ""+myObject.rateLimiterType;
                    this.form.replenishRate = ""+myObject.replenishRate;
                }
                if (plugin == "concurrency") {
                    this.form.burstCapacity = myObject.burstCapacity;
                    this.form.concurrencyType = ""+myObject.concurrencyType;
                }
                if (plugin == "divide") {
                    this.form.loadBalance = "" + myObject.loadBalance;
                    this.form.weightType =  myObject.weightType;
                    var upstreamList = myObject.upstreamList;
                    this.form.domains = upstreamList;
                }
                this.removeIds=[];
                this.getServiceList();
                this.initDialog();
            },
            addClicked() {
                this.dialogFormVisible = true;
                this.currentRow = "";
                this.form.id = "";
                this.form.ruleName = "";
                this.form.serviceIds = [];
                this.form.burstCapacity = "";
                this.form.replenishRate = "";
                this.form.rateLimiterType = "";
                this.form.burstCapacity = "";
                this.form.loadBalance="random";
                this.form.weightType = 1;
                this.form.domains= [{
                    weight: '',
                    timeout: '',
                    upstreamHost: '',
                    upstreamPort: '',
                    retry:"",
                }],
                this.removeIds=[];
                this.oldIds=[];
                this.form.serviceIds=[],
                this.getServiceList();
                this.initDialog();
            },
            getServiceList() {
                this.$http.post(document.getElementById('serverIpAddress').href + '/service/getServiceListForRule', {
                    "pageSize": 100,
                    "pageNumber": 1,
                    "userId": sessionStorage.getItem("userId")
                }, {
                    headers: {
                        "token": sessionStorage.getItem("token"),
                    }
                }).then(
                    response => {
                        if (response.body.code == 200 && response.body.data != null) {
                            this.soptions = response.body.data.tree;
                            console.log(this.soptions);
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
                );
            },
            //删除选择器
            deleteAll() {
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
                        this.$http.post(document.getElementById('serverIpAddress').href + '/rule/removeRule', {
                            "ids": groupIds,
                            "plugin": document.getElementById('plugin').value,
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
                                    this.count = this.count - 1;
                                    this.groups.tableData = [];
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

            fmtbool(row, column) {
                const arr = row[column.property]
                if (arr === undefined) {
                    return '0'
                } else if (arr == '0') {
                    return "否"
                } else if (arr == '1') {
                    return "是"
                }
            },
            fmt(row, column) {
                const arr = row[column.property]
                if (column.property == "matchMode") {
                    if (arr === undefined) {
                        return '0'
                    } else if (arr == '0') {
                        return "and"
                    } else if (arr == '1') {
                        return "or"
                    }
                } else if (column.property == "type") {
                    if (arr === undefined) {
                        return '0'
                    } else if (arr == '0') {
                        return "全流量"
                    } else if (arr == '1') {
                        return "自定义流量"
                    }
                }
            },

            //保存数据
            updateRetryCount(formName) {
                //alert(document.getElementById("plugin").value);
                this.form.concurrencyHandle.concurrencyType = this.form.concurrencyType;
                this.form.concurrencyHandle.burstCapacity = this.form.burstCapacity;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let oldIdsCp = Object.assign([],this.oldIds);
                        let equalNum = [];
                        if(this.oldIds.length>0){
                            let index;
                            for(index=0;index<this.oldIds.length;index++){
                                if(this.form.serviceIds.length>0){
                                    let serviceIndex;
                                    for(serviceIndex=0;serviceIndex<this.form.serviceIds.length;serviceIndex++){
                                        if(this.oldIds[index] == this.form.serviceIds[serviceIndex]){
                                            oldIdsCp[index]=0;
                                            equalNum.push(this.form.serviceIds[serviceIndex]);
                                            this.form.serviceIds[serviceIndex]=0;
                                        }
                                    }
                                }
                            }
                        }
                        oldIdsCp = this.removeWithoutCopy(oldIdsCp,0);
                        //alert(this.form.serviceIds);
                        this.form.serviceIds = this.removeWithoutCopy(this.form.serviceIds,0);
                        //alert(oldIdsCp);

                        this.$http.post(document.getElementById('serverIpAddress').href + '/rule/saveRule', {
                            "plugin": document.getElementById("plugin").value,
                            "id": this.form.id,
                            "ruleName": this.form.ruleName,
                            "userId": sessionStorage.getItem("userId"),
                            "concurrencyHandle":this.form.concurrencyHandle,
                            "ids":this.form.serviceIds,
                            "domains": this.form.domains,
                            "loadBalance": this.form.loadBalance,
                            "weightType": this.form.weightType,
                            "removeIds":oldIdsCp,
                            "equalNums":equalNum
                        }, {
                            headers: {
                                "token": sessionStorage.getItem("token"),
                            }
                        }).then(
                            response => {
                                if (response.body.code == 200) {
                                    this.dialogFormVisible = false;
                                    this.$message({
                                        type: 'success',
                                        message: '数据处理成功!'
                                    });
                                    this.currentRow = "";
                                    this.form.serviceIds=[];
                                    this.query();//刷新页面
                                } else {
                                    this.form.serviceIds=[];
                                    this.$message({
                                        type: 'error',
                                        message: response.body.message
                                    });

                                }
                            },
                            response => {
                                this.form.serviceIds=[];
                                this.$message({
                                    type: 'error',
                                    message: response
                                });
                            }
                        )
                    }
                });

            },
            removeWithoutCopy(arr, item) {
                var n = arr.length;
                for (var i = 0; i<n;i++) {
                    if(arr[0] !== item){
                        arr.push(arr[0]);
                    }
                    arr.splice(0,1);
                }
                return arr;
            },
            query: function () {
                this.$http.post(document.getElementById('serverIpAddress').href + '/rule/ruleList', {
                    "pageSize": this.paging.limit,
                    "pageNumber": this.paging.currentPage,
                    "plugin": document.getElementById('plugin').value,
                    "userId": sessionStorage.getItem("userId"),
                }, {
                    headers: {
                        "token": sessionStorage.getItem("token"),
                    }
                }).then(
                    response => {
                        if (response.body.code == 200 && response.body.data != null) {
                            let rp = response.body;
                            this.count = rp.data.totalCount;
                            this.tableData = rp.data.dataList;
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
                );
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
            oldIds:{
              handler: function(){
                  console.log(this.oldIds);
              }
            },
            paging: {
                handler: function () {
                    this.$http.post(document.getElementById('serverIpAddress').href + '/rule/ruleList', {
                        "pageSize": this.paging.limit,
                        "pageNumber": this.paging.currentPage,
                        "plugin": document.getElementById("plugin").value,
                        "userId": sessionStorage.getItem("userId"),
                    }, {
                        headers: {
                            "token": sessionStorage.getItem("token"),
                        }
                    }).then(
                        response => {
                            if (response.body.code == 200 && response.body.data != null) {
                                let rp = response.body;
                                this.groups.count = rp.data.totalCount;
                                this.groups.tableData = rp.data.dataList
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
                deep: true
            }

        }
    }
</script>
@import '../style/mixin';
<style scoped>
    .table_container {
        padding: 5px;
    }

    .subTableHeaderFont {
        font-size: 0.9em;
        padding: 0px;
        text-align: center;
        color: rebeccapurple !important;
        background-color: white !important;
    }

    .tip {
        position: relative;
        width: 100%;
    }

    .el-table__expanded-cell {
        padding: 5px !important;
    }

    .left {
        float: left;
        width: 30%;
        border: 2px solid #FFF5EE;
        margin-top: 10px;
        margin-bottom: 10px;
    }

    .right {
        float: right;
        width: 66%;
        border: 2px solid #FFF5EE;
        margin-top: 10px;
        margin-bottom: 10px;
    }
</style>
