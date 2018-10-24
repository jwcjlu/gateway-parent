<template>
    <div class="fillcontain">
        <headTop></headTop>

        <div class="table_container">
            <div id="app">
                <div class="radio-wrap">
                    <div style="margin:10px 0;"></div>
                    <keep-alive>
                        <select1 v-bind:paging="this.paging" v-bind:tableData="this.tableData" v-bind:ruleShow="ruleShow" v-bind:count="this.count"></select1>
                    </keep-alive>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import select1 from './../components/ruleDetail.vue'
    import headTop from '../components/headTop'
    import ElButton from "../../node_modules/element-ui/packages/button/src/button.vue"
    import moment from "moment";

    export default {
        name: 'app',
        data () {
            return {
                tabView: 'select1',
                dialogFormVisible: false,
                formLabelWidth: '120px',
                currentRow:null,
                tableData:[],
                paging: {
                    limit: 10,
                    currentPage: 1,
                },
                count:0,
                competence:{},
                ruleShow:{
                    ruleAddShow:false,
                    ruleDeleteShow:false,
                    ruleUpdateShow:false,
                },
            }
        },
        components: {
            select1,
            ElButton,
            headTop,
        },
        created() {
            document.getElementById("plugin").value = "concurrency";
            this.initCompetence();
            //alert("ratefg:"+document.getElementById("plugin").value);
        },
        watch: {
            '$route' (to, from) {
                if(this.$route.path == "/concurrency"){
                    //alert("change to /rate_limiter");
                    document.getElementById('plugin').value = "concurrency";
                    this.query();
                    this.initCompetence();
                }
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
                    this.ruleShow.ruleAddShow = this.competence['100'] == undefined?false:true;
                    this.ruleShow.ruleDeleteShow = this.competence['101'] == undefined?false:true;
                    this.ruleShow.ruleUpdateShow = this.competence['102'] == undefined?false:true;

                }
            },
            query(){
                //alert(document.getElementById('plugin').value);
                this.$http.post(document.getElementById('serverIpAddress').href + '/rule/ruleList', {
                    "pageSize": 10,
                    "pageNumber": 1,
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
            }
        }
    }
</script>

<style>
    .radio-group{font-size:0;height: 26px;line-height:26px;}
    .radio-group>span{cursor:pointer;display:inline-block;font-size:16px;text-align:center;width:100px;}
    .cur{color:#fff;background-color: #20a0ff;}
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
    .el-table__expanded-cell {
        padding: 5px !important;
    }
    .span{
        background:#ccc;
        padding:2px 5px;
        line-height:30px;
        text-align:center;
        cursor:pointer;
    }

</style>