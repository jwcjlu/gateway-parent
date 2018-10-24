<template>
    <div class="fillcontain">
        <headTop></headTop>
        <div class="table_container">
            <div style="margin-left: 2%; margin-top: 10px" v-if="competence['251']== undefined?false:true" >
                 <el-select v-model="roles" placeholder="请选择角色" v-on:input="getPremissionList(roles)">
                     <el-option
                             v-for="x in options"
                             :key="x.role"
                             :label="x.role"
                             :value="x.role">
                      </el-option>
                 </el-select>
            </div>
            <div style="margin-left: 2%; margin-top: 10px" v-if="competence['250']== undefined?false:true" >
                <el-button type="primary" @click="saveRulePremission"  class="submit_btn" >保存权限</el-button>
            </div>

            <div style="float:right;margin-right: 2%; margin-top: 10px">
                <el-button type="success" @click="checkPremission(2)"  class="submit_btn" >基本权限</el-button>
                <el-button type="success" @click="checkPremission(1)"  class="submit_btn" >所有权限</el-button>
            </div>

            <div style="margin-left: 2%; margin-top: 10px" >

                <el-tree :data="data2"
                         ref="tree"
                         :props="defaultProps"
                         show-checkbox
                         node-key="id"
                         :auto-expand-parent="true"
                         @check-change="handleCatalogChkChange"
                         check-strictly="true"
                         :default-checked-keys="defaultCheckedKeys"
                         :default-expanded-keys="defaultExpandedKeys"></el-tree>
            </div>
    </div>
    </div>
</template>

<script>
    import headTop from '../components/headTop'
    import ElButton from "../../node_modules/element-ui/packages/button/src/button.vue";
    import moment from "moment";

    export default {
        data() {
            return {
                roles:"",
                options:[{
                    id:"",
                    role:"",
                    competence:"",
                }],
                dataList:[],
                data2:[] ,
                defaultProps:{},
                defaultCheckedKeys: [],
                defaultExpandedKeys: [],
                defaultProps: {
                    children: 'children',
                    label: 'label'
                }
            };

        },
        components: {
            ElButton,
            headTop,
        },
        created(){
            var array = sessionStorage.getItem("treeData");
            this.data2 = JSON.parse(array);
            this.initCompetence();
            this.query();
        },
        watch: {
            '$route' (to, from) {
                this.query();
            }
        },
        methods:{
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
            query(){
                this.$http.post(document.getElementById('serverIpAddress').href + '/role/roleList', {
                }, {
                    headers: {
                        "token": sessionStorage.getItem("token"),
                    }
                }).then(
                    response => {
                        if (response.body.code == 200 && response.body.data != null) {
                            let rp = response.body;
                            this.options = rp.data.dataList;
                            this.role = this.options[0].role;
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
            checkPremission(temp){
              if(temp == 1){
                  for(index =0;index<this.options.length;index++){
                      if(this.options[index].id == 1){
                          var a = this.options[index].competence;
                          var array = new Array();
                          a = a.replace("[","").replace("]","");
                          var list = a.split(",");
                          var ins;
                          for(ins in list){
                              array.push(parseInt(list[ins]));
                          }
                          this.$refs.tree.setCheckedKeys(array,true);
                      }

                  }
              }else if(temp == 2){
                  var index;
                  for(index =0;index<this.options.length;index++){
                      if(this.options[index].id == 2){
                          var a = this.options[index].competence;
                          var array = new Array();
                          a = a.replace("[","").replace("]","");
                          var list = a.split(",");
                          var ins;
                          for(ins in list){
                              array.push(parseInt(list[ins]));
                          }
                          this.$refs.tree.setCheckedKeys(array,true);

                      }

                  }
              }
            },
            getPremissionList(role){
                var index;
                var flag = false;
                for(index =0;index<this.options.length;index++){
                    if(this.options[index].role == role){
                        var a = this.options[index].competence;
                        var array = new Array();
                        a = a.replace("[","").replace("]","");
                        var list = a.split(",");
                        var ins;
                        for(ins in list){
                            array.push(parseInt(list[ins]));
                        }
                        this.$refs.tree.setCheckedKeys(array,true);
                        flag = true;
                    }
                    if(index == this.options.length-1 && !flag){
                        this.$refs.tree.setCheckedKeys([]);
                    }
                }
            },
            saveRulePremission(){
                var array = this.$refs.tree.getCheckedKeys();
                if(this.roles == ""){
                    this.$message({
                        type: 'error',
                        message: "请先选择角色"
                    });
                }else{
                    var index;
                    var roleId;
                    for(index =0;index<this.options.length;index++) {
                        if (this.options[index].role == this.roles) {
                            roleId = this.options[index].id;
                        }
                    }
                    if(roleId == "1SDFSDFSDFSRWER@#$!#$") {//暂时放开管理员更改权限的权限
                        this.$message({
                            type: 'error',
                            message: "超级管理员权限不能更改"
                        });
                    }else{
                        this.$http.post(document.getElementById('serverIpAddress').href + '/role/saveCompetence',
                            {
                                "id" :roleId,
                                "role" :this.roles,
                                "competence":array.toString()
                            }, {
                                headers: {
                                    "token": sessionStorage.getItem("token"),
                                }
                            }).then(
                            response => {
                                if (response.body.code == 200 && response.body.data != null) {
                                    this.$message({type: 'success', message: '保存数据成功!'});
                                    this.query();
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
                        this.query();
                    }
                }
            }
        }
    };
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
