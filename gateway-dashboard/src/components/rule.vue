<template>
    <div class="fillcontain">
        <div style="margin-top: 1px;  margin-bottom: 10px;">
            <label style="float: left"> <span style="font-weight: bold">选择器【{{ruleTitle}}】规则列表</span></label>
            <div v-if="ruleShow.ruleAddShow">
                <el-button style="float: right" type="info" @click="addRule()"><i class="el-icon-plus"></i>&nbsp;添加规则
                </el-button>
            </div>
            </div>
        <div class="table_container">
            <el-table
                    :border=false
                    ref="multipleTable"
                    :data="tableData1"
                    highlight-current-row
                    style="width: 100%;margin-top: 40px;">
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <el-form label-position="center" inline class="demo-table-expand">
                            <el-form-item label="处理:" align="center">
                                <span>{{ props.row.handle}}</span>
                            </el-form-item>
                        </el-form>
                    </template>
                </el-table-column>

                <el-table-column
                        align="center"
                        fixed="right"
                        label="操作"
                        width="180">
                    <template slot-scope="scope">
                        <div style="float:left; margin-left: 20px" v-if="ruleShow.ruleAddShow">
                            <el-button type="info" @click="editRule(scope.row)" size="small"><i class="el-icon-edit"></i>
                            </el-button>
                        </div>
                        <div style="margin-left: 40px" v-if="ruleShow.ruleDeleteShow">
                            <el-button type="warning" @click="deleRule(scope.row)" size="small"><i
                                class="el-icon-delete"></i></el-button>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        min-width="150"
                        align="center"
                        :show-overflow-tooltip=true
                        property="name"
                        label="规则名称">
                </el-table-column>
                <el-table-column
                        min-width="80"
                        align="center"
                        :show-overflow-tooltip=true
                        :formatter="fmtbool"
                        property="enabled"
                        label="开启">
                </el-table-column>
                <el-table-column
                        align="center"
                        width="210"
                        :show-overflow-tooltip=true
                        property="dateUpdated"
                        label="更新时间"
                        :formatter="dateFormat">
                </el-table-column>
            </el-table>

            <div style="">
                <div class="Pagination" style="text-align: left;margin-top: 20px;float: right">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="paging.currentPage"
                            :page-sizes="[10,20,50, 100, 200]"
                            :page-size="paging.limit"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="count1">
                    </el-pagination>
                </div>
            </div>
        </div>
        <!-- Form -->

        <el-dialog title="规则编辑 " :visible.sync="dialogFormVisible">
            <el-form :model="form" :rules="rules" ref="form">
                <div style="margin-left: 0px;width: 80%">
                    <el-form-item label="ID：" :label-width="formLabelWidth" v-show="false">
                        <el-input type="name" v-model="form.id" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="名称：" :label-width="formLabelWidth" prop="name">
                        <el-input type="name" v-model="form.name" auto-complete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="匹配方式：" :label-width="formLabelWidth" prop="tselected">
                        <el-select v-model="form.tselected" placeholder="请选择">
                            <el-option
                                    v-for="x in typeOptions"
                                    :key="x"
                                    :label="x"
                                    :value="x">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <!--condition start-->

                    <el-form-item v-for="(condition, index) in form.conditions" :key="condition.key" label="条件："
                                  :prop="'conditions.' + index + '.value'" :label-width="formLabelWidth">
                        <el-col :span="5">
                            <el-form-item prop="pselected">
                                <el-select v-model="condition.paramType" placeholder="请选择" style="width:120px;" v-on:input="checkParamDisabled(condition.paramType,index)">
                                    <el-option
                                            v-for="x in paramOptions"
                                            :key="x"
                                            :label="x"
                                            :value="x">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item >
                                <el-input type="condition.paramName" v-model="condition.paramName" style="width:120px;" :disabled="condition.paramDisabled" v-on:input="pass()"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item >
                                <el-select v-model="condition.operator" placeholder="请选择" style="width:120px;" v-on:input="pass()">
                                    <el-option
                                            v-for="x in oOptions"
                                            :key="x.name"
                                            :label="x.name"
                                            :value="x.name"
                                            :disabled="x.disabled">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item >
                                <el-input type="detailValue" v-model="condition.paramValue" style="width:120px;" v-on:input="pass()"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-button @click.prevent="removeCondition(condition)" type="danger" >删除</el-button>
                    </el-form-item>
                    <el-form-item :label-width="formLabelWidth"><slot name="label"></slot>
                        <el-button @click="addCondition" type="info" >新增</el-button>
                        <label v-if="conditionrrShow" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;存在空值</label>
                        <label v-if="conditionLengthShow" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;输入过长字符，最多允许输入64字符</label>
                    </el-form-item>
                    <div>
                        <!--自定义start-->
                        <el-form-item label="处理：" :label-width="formLabelWidth" v-if="pluginDialogList[0].value">
                            <el-col :span="11">
                                <el-form-item prop="continued">
                                    <el-select v-model="form.continued">
                                        <el-option label="继续后续选择器" value="1"></el-option>
                                        <el-option label="略过后续选择器" value="0"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-form-item>
                        <!--自定义end-->
                        <!--重定向start-->
                        <el-form-item label="处理：" :label-width="formLabelWidth" v-if="pluginDialogList[1].value">
                            <el-input type="detail" v-model="form.detail" auto-complete="off"
                                      placeholder="url template to redirect"></el-input>
                            <div style="margin-top: 10px">
                                <el-select v-model="form.cleanQuery" style="width:200px;">
                                    <el-option label="不清除Query" value="1"></el-option>
                                    <el-option label="清除Query" value="0"></el-option>
                                </el-select>
                                <el-select v-model="form.status" style="width:200px;">
                                    <el-option label="301" value="1"></el-option>
                                    <el-option label="302" value="0"></el-option>
                                </el-select>
                            </div>
                        </el-form-item>
                        <!--重定向end-->
                        <!--重写start-->
                        <el-form-item label="处理：" :label-width="formLabelWidth" v-if="pluginDialogList[2].value" prop="detail">
                            <el-input type="detail" v-model="form.detail" auto-complete="off"
                                      style="width:300px;" placeholder="具体地址"></el-input>
                        </el-form-item>
                        <!--重写end-->
                        <!--RateLimiting start-->
                        <el-form-item label="处理：" :label-width="formLabelWidth" v-if="pluginDialogList[3].value">
                            <el-col :span="10">
                                <el-form-item prop="onceTime">
                                    <el-select v-model="form.onceTime">
                                        <el-option label="1秒" value="1"></el-option>
                                        <el-option label="1分钟" value="60"></el-option>
                                        <el-option label="1小时" value="3600"></el-option>
                                        <el-option label="1天" value="86400"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="7">
                                <el-form-item prop="times" :rules="[
                          { required: true, message: '次数不能为空'},
                          { type: 'number', message: '次数必须为数字值'},
                           { type: 'number', message: '超出范围', max:100000000,min:1}
                        ]">
                                    <el-input type="times" v-model.number="form.times" auto-complete="off"
                                              placeholder="最多次数" style="width:150px;"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-form-item>
                        <!--RateLimiting end-->
                        <!--SignAuth start-->
                        <el-form-item label="处理：" :label-width="formLabelWidth" v-if="pluginDialogList[4].value">
                            <el-input type="status" v-model="form.status" auto-complete="off"
                                      placeholder="authorization fail status code" style="width:215px;"></el-input>
                        </el-form-item>
                        <!--SignAuth end-->
                        <!--PrateLimiting start-->
                        <!--PrateLimiting end-->
                        <!--waf start-->
                        <el-form-item label="处理：" :label-width="formLabelWidth" v-if="pluginDialogList[6].value">
                            <el-col :span="11">
                                <el-form-item prop="access">
                                    <el-select v-model="form.access"   @change="supRes(form.access)">
                                        <el-option
                                                v-for="x in aoptions"
                                                :key="x"
                                                :label="x"
                                                :value="x">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="11">
                                <el-form-item prop="status" :rules="[
                          { required: true, message: 'error code 不能为空'},
                        ]" v-if="isDenyDisable">
                                    <el-input type="status" v-model="form.status" auto-complete="off"
                                              placeholder="error code" style="width:215px;"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-form-item>
                        <!--waf end-->
                        <!--divide start-->
                        <el-form-item  v-if="pluginDialogList[7].value">
                            <el-form-item label="熔断处理：" :label-width="formLabelWidth" v-if="pluginDialogList[7].value">
                                <el-form-item>
                                    <div>
                                    <slot name="label">rw请求数:</slot>
                                    <el-input type="requestVolumeThreshold" v-model="form.requestVolumeThreshold" auto-complete="off"
                                              placeholder="rolling window min request" style="width: 82px;" property="requestVolumeThreshold " @change="checkNumber(form.requestVolumeThreshold,1)"></el-input>
                                    <slot name="label">&nbsp;&nbsp;并发数:</slot>
                                    <el-input type="maxConcurrentRequests" v-model="form.maxConcurrentRequests" auto-complete="off"
                                              placeholder="最大并发数" style="width: 81px" property="maxConcurrentRequests" @change="checkNumber(form.maxConcurrentRequests,1)"></el-input>

                                    <slot name="label">&nbsp;&nbsp;错误阀值:</slot>
                                    <el-input type="errorThresholdPercentage" v-model="form.errorThresholdPercentage" auto-complete="off"
                                              placeholder="错误比率阀值" style="width: 81px" property="errorThresholdPercentage" @change="checkNumber(form.errorThresholdPercentage,1)"></el-input>
                                    <slot name="label">&nbsp;&nbsp;熔断时间:</slot>
                                    <el-input type="sleepWindowInMilliseconds" v-model="form.sleepWindowInMilliseconds" auto-complete="off"
                                              placeholder="触发短路的时间值" style="width: 81px" property="sleepWindowInMilliseconds"  @change="checkNumber(form.sleepWindowInMilliseconds,1)"></el-input>
                                    <label v-if="numShowH" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;请输入数字</label>
                                    </div>
                                    <slot name="label">groupKey:&nbsp;&nbsp;&nbsp;&nbsp;</slot>
                                    <el-input type="groupKey" v-model="form.groupKey" auto-complete="off"
                                              placeholder="熔断分组时候key" style="width: 195px; margin-top: 10px" property="groupKey"></el-input>

                                    <slot name="label">&nbsp;&nbsp;commandKey:</slot>
                                    <el-input type="commandKey" v-model="form.commandKey" auto-complete="off"
                                              placeholder="具体命令key" style="width: 210px; margin-top: 10px" property="commandKey"></el-input>
                                </el-form-item>
                            </el-form-item>
                            <el-form-item :label-width="formLabelWidth">
                                <slot name="label">loadBalance:</slot>
                                <el-select v-model="form.loadBalance" style="width:195px; margin-top: 10px">
                                    <el-option
                                            v-for="x in loptions"
                                            :key="x"
                                            :label="x"
                                            :value="x">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item v-for="(domain, index) in form.domains" :key="domain.key"
                                          :prop="'domains.' + index + '.value'" :label-width="formLabelWidth" label="处理：" style="margin-top: 20px">
                                <el-form-item prop="upstreamUrl" >
                                    <slot name="label">转发地址:</slot>
                                    <el-input type="upstreamUrl" v-model="domain.upstreamUrl" auto-complete="off"
                                              placeholder="转发地址" style="width: 89%"></el-input>

                                    <slot name="label">转发host:</slot>
                                    <el-input type="upstreamHost" v-model="domain.upstreamHost" auto-complete="off"
                                              placeholder="转发host" style="width: 98px;margin-top: 10px"></el-input>

                                    <slot name="label">&nbsp;&nbsp;&nbsp;超时时间(ms):</slot>
                                    <el-input type="timeout" v-model="domain.timeout" auto-complete="off"
                                              placeholder="超时时间(ms),只能填写整数"
                                              style="width: 68px;margin-top: 10px" @change="checkNumber(domain.timeout,2)"></el-input>

                                    <slot name="label">&nbsp;&nbsp;&nbsp;权重:</slot>
                                    <el-input type="domain.weight" v-model="domain.weight" auto-complete="off"
                                              placeholder="权重,只能填写整数" style="width: 67px;margin-top: 10px" @change="checkNumber(domain.weight,2)"></el-input>
                                    <slot name="label">&nbsp;&nbsp;&nbsp;重试次数:</slot>
                                    <el-input type="domain.retry" v-model="domain.retry" auto-complete="off"
                                              placeholder="重试次数" style="width: 68px;margin-top: 10px" @change="checkNumber(domain.retry,2)"></el-input>
                                </el-form-item>

                                <el-button @click.prevent="removeDomain(domain)" type="danger" style="margin-top: 10px">
                                    删除
                                </el-button>
                                <el-button @click="addDomain" type="info" style="margin-top: 5px">新增</el-button>
                                <label v-if="enabledShow" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;处理数据存在空值</label>
                                <label v-if="numShowF" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;请输入数字且在范围内</label>
                            </el-form-item><el-form-item :label-width="formLabelWidth">
                                <slot name="label">loadBalance:</slot>
                                <el-select v-model="form.loadBalance" style="width:195px; margin-top: 10px">
                                    <el-option
                                            v-for="x in loptions"
                                            :key="x"
                                            :label="x"
                                            :value="x">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item v-for="(domain, index) in form.domains" :key="domain.key"
                                          :prop="'domains.' + index + '.value'" :label-width="formLabelWidth" label="处理：" style="margin-top: 20px">
                                <el-form-item prop="upstreamUrl" >
                                    <slot name="label">转发地址:</slot>
                                    <el-input type="upstreamUrl" v-model="domain.upstreamUrl" auto-complete="off"
                                              placeholder="转发地址" style="width: 89%"></el-input>

                                    <slot name="label">转发host:</slot>
                                    <el-input type="upstreamHost" v-model="domain.upstreamHost" auto-complete="off"
                                              placeholder="转发host" style="width: 98px;margin-top: 10px"></el-input>

                                    <slot name="label">&nbsp;&nbsp;&nbsp;超时时间(ms):</slot>
                                    <el-input type="timeout" v-model="domain.timeout" auto-complete="off"
                                              placeholder="超时时间(ms),只能填写整数"
                                              style="width: 68px;margin-top: 10px" @change="checkNumber(domain.timeout,2)"></el-input>

                                    <slot name="label">&nbsp;&nbsp;&nbsp;权重:</slot>
                                    <el-input type="domain.weight" v-model="domain.weight" auto-complete="off"
                                              placeholder="权重,只能填写整数" style="width: 67px;margin-top: 10px" @change="checkNumber(domain.weight,2)"></el-input>
                                    <slot name="label">&nbsp;&nbsp;&nbsp;重试次数:</slot>
                                    <el-input type="domain.retry" v-model="domain.retry" auto-complete="off"
                                              placeholder="重试次数" style="width: 68px;margin-top: 10px" @change="checkNumber(domain.retry,2)"></el-input>
                                </el-form-item>

                                <el-button @click.prevent="removeDomain(domain)" type="danger" style="margin-top: 10px">
                                    删除
                                </el-button>
                                <el-button @click="addDomain" type="info" style="margin-top: 5px">新增</el-button>
                                <label v-if="enabledShow" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;处理数据存在空值</label>
                                <label v-if="numShowF" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;请输入数字且在范围内</label>
                            </el-form-item>
                        </el-form-item>
                        <!--divide end-->
                        <!--dubbo start-->
                        <el-form-item  v-if="pluginDialogList[8].value">
                            <el-form-item label="处理：" :label-width="formLabelWidth" v-if="pluginDialogList[8].value">
                                   <slot name="label">rw请求数:</slot>
                                   <el-input type="requestVolumeThreshold" v-model="form.requestVolumeThreshold" auto-complete="off"
                                          placeholder="rolling window min request" style="width: 130px;" property="requestVolumeThreshold " @change="checkNumber(form.requestVolumeThreshold,1)"></el-input>

                                    <!--  <slot name="label">&nbsp;&nbsp;并发数:</slot>
                                    <el-input type="maxConcurrentRequests" v-model="form.maxConcurrentRequests" auto-complete="off"
                                              placeholder="最大并发数" style="width: 81px" property="maxConcurrentRequests" @change="checkNumber(form.maxConcurrentRequests,1)"></el-input>
-->
                                    <slot name="label">&nbsp;&nbsp;错误阀值:</slot>
                                    <el-input type="errorThresholdPercentage" v-model="form.errorThresholdPercentage" auto-complete="off"
                                              placeholder="错误比率阀值" style="width: 130px" property="errorThresholdPercentage" @change="checkNumber(form.errorThresholdPercentage,1)"></el-input>
                                    <slot name="label">&nbsp;&nbsp;时间间隔:</slot>
                                    <el-input type="sleepWindowInMilliseconds" v-model="form.sleepWindowInMilliseconds" auto-complete="off"
                                              placeholder="触发短路的时间值" style="width: 129px" property="sleepWindowInMilliseconds"  @change="checkNumber(form.sleepWindowInMilliseconds,1)"></el-input>

                                   <el-form-item :label-width="formLabelWidth">
                                        <label v-if="numShowH" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;请输入数字</label>
                                    </el-form-item>
                                    <slot name="label">groupKey:&nbsp;&nbsp;&nbsp;&nbsp;</slot>
                                    <el-input type="groupKey" v-model="form.groupKey" auto-complete="off"
                                              placeholder="熔断分组时候key" style="width: 195px; margin-top: 10px" property="groupKey"></el-input>

                                    <slot name="label">&nbsp;&nbsp;commandKey:</slot>
                                    <el-input type="commandKey" v-model="form.commandKey" auto-complete="off"
                                              placeholder="具体命令key" style="width: 210px; margin-top: 10px" property="commandKey"></el-input>
                                <slot name="label">loadBalance:</slot>
                                <el-select v-model="form.loadBalance" style="width:195px; margin-top: 10px">
                                    <el-option
                                            v-for="x in loptions"
                                            :key="x"
                                            :label="x"
                                            :value="x">
                                    </el-option>
                                </el-select>
                                    <slot name="label">&nbsp;&nbsp;注册地址:</slot>
                                    <el-input type="registry" v-model="form.registry" auto-complete="off"
                                              placeholder="zookeeper register url" style="width: 246px"></el-input>

                                    <slot name="label">应用名:</slot>
                                    <el-input type="appName" v-model="form.appName" auto-complete="off"
                                              placeholder="app name" style="width: 98px;margin-top: 10px"></el-input>

                                <slot name="label">&nbsp;&nbsp;&nbsp;协议:</slot>
                                <el-input type="protocol" v-model="form.protocol" auto-complete="off"
                                          placeholder="dubbo protocol" style="width: 120px;margin-top: 10px" ></el-input>

                                <slot name="label">&nbsp;&nbsp;&nbsp;端口号:</slot>
                                <el-input type="port" v-model="form.port" auto-complete="off"
                                          placeholder="port"  style="width: 75px;margin-top: 10px" @change="checkNumber(form.port,2)"></el-input>

                                <slot name="label">&nbsp;&nbsp;&nbsp;版本号:</slot>
                                <el-input type="version" v-model="form.version" auto-complete="off"
                                          placeholder="version" style="width: 79px;margin-top: 10px" ></el-input>

                                    <slot name="label">超时时间(ms):</slot>
                                    <el-input type="timeout" v-model="form.timeout" auto-complete="off"
                                              placeholder="超时时间(ms) 只能填写整数"
                                              style="width: 132px;margin-top: 10px" @change="checkNumber(form.timeout,2)"></el-input>

                                <slot name="label">&nbsp;&nbsp;&nbsp;group:</slot>
                                <el-input type="group" v-model="form.group" auto-complete="off"
                                          placeholder="group" style="width: 116px;margin-top: 10px" ></el-input>

                                    <slot name="label">&nbsp;&nbsp;&nbsp;重试次数:</slot>
                                    <el-input type="retry" v-model="form.retry" auto-complete="off"
                                              placeholder="重试次数" style="width: 120px;margin-top: 10px" @change="checkNumber(form.retry,2)"></el-input>
                                </el-form-item>
                                <el-form-item :label-width="formLabelWidth">
                                    <label v-if="enabledShow" :label-width="formLabelWidth" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;处理数据存在空值</label>
                                    <label v-if="numShowF" :label-width="formLabelWidth" style="color: crimson; font-size: small">&nbsp;&nbsp;&nbsp;&nbsp;请输入数字且在范围内</label>
                                </el-form-item>
                        </el-form-item>
                        <!--dubbo end-->
                    <div>
                        <el-form-item prop="loged" :label-width="formLabelWidth" label="日志：">
                            <el-select v-model="form.loged">
                                <el-option label="记录日志" value="1"></el-option>
                                <el-option label="不记录日志" value="0"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="是否开启：" :label-width="formLabelWidth" prop="enabled">
                            <el-select v-model="form.enabled">
                                <el-option label="是" value="1"></el-option>
                                <el-option label="否" value="0"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="执行顺序：" :label-width="formLabelWidth" prop="rank" :rules="[
                          { required: true, message: '顺序不能为空'},
                          { type: 'number', message: '顺序必须为数字值且属于0-100范围内',max:100,min:1}
                        ]">
                            <el-input type="form.rank" v-model.number="form.rank" style="width:300px;"
                                      placeholder="可以填写1-100之间的数字标识执行先后顺序"></el-input>
                        </el-form-item>
                    </div>
                    </div>
                </div>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveRule('form')">确 定</el-button>
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
    import ElCol from "element-ui/packages/col/src/col";
    import ElCard from "../../node_modules/element-ui/packages/card/src/main.vue";
    import ElInput from "../../node_modules/element-ui/packages/input/src/input.vue";
    import ElDialog from "../../node_modules/element-ui/packages/dialog/src/component.vue";
    export default {
        data() {
            return {
                pluginDisableView: "",
                tableData1: [],
                tableDataRule: [],
                paging: {
                    limit: 10,
                    currentPage: 1,
                },
                count1: 0,
                enabledShow: false,
                ruleTitle: "",
                dialogHrefVisible: false,
                dialogAddRuleVisible: false,
                dialogFormVisible: false,
                dialogRuleVisible: false,
                conditionrrShow: false,
                isDenyDisable:true,
                typeOptions: "",
                loptions: "",
                paramOptions: "",
                oOptions:[{
                    name:"",
                    disabled: false,
                }],
                ruleShow:{
                    updateShow:false,
                    startShow:false,
                    ruleAddShow:false,
                    ruleDeleteShow:false
                },
                aoptions:"",
                oselected: "",
                pselected: "",
                tselected: "",
                lselected: "",
                numShowH:false,
                numShowF:false,
                formLabelWidth: '120px',
                currentRow: null,
                conditionLengthShow: false,
                form: {
                    sselected: "",
                    pselected: "",
                    tselected: "",
                    oselected: "",
                    lselected: "",
                    detailValue: "",
                    continued: "",
                    param: "",
                    id: "",
                    name: "",
                    loged: "",
                    enabled: "",
                    cleanQuery: "",
                    status: "",
                    onceTime: "",
                    detail:"",
                    times: "",
                    totalCount: "",
                    access: "",
                    loadBalance: "",
                    groupKey: "",
                    commandKey: "",
                    maxConcurrentRequests:"",
                    errorThresholdPercentage:"",
                    sleepWindowInMilliseconds:"",
                    requestVolumeThreshold:"",
                    registry:"",
                    appName:"",
                    protocol:"",
                    port:"",
                    version:"",
                    group:"",
                    timeout: '',
                    retry:"",
                    domains: [{
                        weight: '',
                        timeout: '',
                        upstreamHost: '',
                        upstreamUrl: '',
                        retry:"",
                    }],
                    ruleShow:{
                        ruleAddShow:false,
                        ruleDeleteShow:false
                    },
                    conditions: [{
                        paramType: '',
                        paramName: '',
                        paramValue: '',
                        operator: '',
                        paramDisabled: false,
                    }],
                },
                pluginDialogList: [
                    {"tag": "dialogMonitorVisible", "value": false},
                    {"tag": "dialogUrlRedirectVisible", "value": false},
                    {"tag": "dialogUriRewriteVisible", "value": false},
                    {"tag": "dialogRateLimitingVisible", "value": false},
                    {"tag": "dialogSignAuthVisible", "value": false},
                    {"tag": "dialogPropertyRateLimitingVisible", "value": false},
                    {"tag": "dialogWafVisible", "value": false},
                    {"tag": "dialogDivideVisible", "value": false},
                    {"tag": "dialogDubboVisible", "value": false}
                ],
                rules: {
                    name: [
                        {required: true, message: '请输入选择器名称', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max:64}
                    ],
                    enabled: [
                        {required: true, message: '请选择是否开启', trigger: 'change'}
                    ],
                    sselected: [
                        {required: true, message: '请选择类型', trigger: 'change'}
                    ],
                    continued: [
                        {required: true, message: '请选择处理方式', trigger: 'change'}
                    ],
                    loged: [
                        {required: true, message: '请选择是否打印日志', trigger: 'change'}
                    ],
                    tselected: [
                        {required: true, message: '请选择匹配方式', trigger: 'change'}
                    ],

                    onceTime: [
                        {required: true, message: '请选择', trigger: 'change'}
                    ],
                    loadBalance: [
                        {required: true, message: '不允许为空', trigger: 'change'},
                    ],
                    groupKey: [
                        {required: true, message: '不允许为空', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max:64}
                    ],
                    commandKey: [
                        {required: true, message: '不允许为空', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max:64}
                    ],
                    detail: [
                        {required: true, message: '不允许为空', trigger: 'blur'},
                        {required: true, message: '内容过长,请重新输入', trigger: 'blur', max:64}
                    ],
                    access: [
                        {required: true, message: '不允许为空', trigger: 'change'}
                    ],

                }
            }
        },
        props: ["tableData1", "count1", "ruleTitle","ruleShow"],
        components: {
            ElInput,
            ElCard,
            ElCol,
            ElFormItem,
            ElOption,
            ElButton,
        },
        created() {
            this.initEnum();
        },
        methods: {
            //初始化下拉参数
            initEnum() {
                this.$http.post(document.getElementById('serverIpAddress').href + '/enum/getEnum', {}
                    , {
                        headers: {
                            "token": sessionStorage.getItem("token"),
                        }
                    }).then(
                    response => {
                        if (response.body.code == 200 && response.body.data != null) {
                            this.typeOptions = response.body.data.matchModeList;
                            this.tselected = this.typeOptions[0];
                            this.paramOptions = response.body.data.paramTypeList;
                            this.pselected = this.paramOptions[0];
                            this.oOptions = response.body.data.operatorTypeList;
                            this.oselected = this.oOptions[0].name;
                            this.loptions = response.body.data.loadBalanceTypeList;
                            this.lselected = this.loptions[0];
                            this.aoptions = response.body.data.wafTypeList;
                            this.form.access = this.aoptions[0];
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
                );
                var pluginId = document.getElementById("plugin").value;
                //alert("rule:"+pluginId);
                if (pluginId == "monitor") {
                    this.pluginDisableView = "dialogMonitorVisible";
                } else if (pluginId == "redirect") {
                    this.pluginDisableView = "dialogUrlRedirectVisible";
                } else if (pluginId == "rewrite") {
                    this.pluginDisableView = "dialogUriRewriteVisible";
                } else if (pluginId == "rate_limiter") {
                    this.pluginDisableView = "dialogRateLimitingVisible";
                } else if (pluginId == "5") {
                    this.pluginDisableView = "dialogSignAuthVisible";
                } else if (pluginId == "6") {
                    this.pluginDisableView = "dialogPropertyRateLimitingVisible";
                } else if (pluginId == "waf") {
                    this.pluginDisableView = "dialogWafVisible";
                } else if (pluginId == "divide") {
                    this.pluginDisableView = "dialogDivideVisible";
                } else if (pluginId == "dubbo") {
                    this.pluginDisableView = "dialogDubboVisible";
                }
                this.checktools(this.pluginDisableView);

            },
            supRes(val) {
                if(val == 'reject'){
                    this.isDenyDisable = true;
                }else{
                    this.isDenyDisable = false;
                }
            },
            checktools(disable) {
                for (var i = 0; i < 9; i++) {
                    if (disable == this.pluginDialogList[i].tag) {
                        this.pluginDialogList[i].value = true;
                    } else {
                        this.pluginDialogList[i].value = false;
                    }
                }
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
            //main dialog
            addRule() {
                var value = document.getElementById("selectorId").value;
                //alert(value);
                if (value == '' || value == null) {
                    this.$message({
                        type: 'error',
                        message: '请先选定选择器!'
                    });
                } else {
                    //this.initEnum();
                    this.dialogFormVisible = true;
                    this.dialogHrefVisible = false;
                    this.conditionLengthShow = false;
                    this.currentRow = "";
                    this.form.name = "";
                    this.form.sselected = "";
                    this.form.continued = "";
                    this.form.loged = "";
                    this.form.enabled = "";
                    this.form.rank = "";
                    this.form.id = "";
                    this.numShowH=false;
                    this.numShowF=false;
                    this.isDenyDisable = false;
                    this.form.tselected = "";
                    this.form.cleanQuery = "";
                    this.form.status = "";
                    this.form.onceTime = "";
                    this.form.times = "";
                    this.form.totalCount = "";
                    this.form.access = "";
                    this.form.groupKey = "";
                    this.form.commandKey = "";
                    this.form.loadBalance = "";
                    this.form.detail = "";
                    this.form.registry="";
                    this.form.appName="";
                    this.form.protocol="";
                    this.form.port="";
                    this.form.version="";
                    this.form.group="";
                    this.form.timeout= '';
                    this.form.retry="";
                    this.form.maxConcurrentRequests="";
                    this.form.errorThresholdPercentage="";
                    this.form.sleepWindowInMilliseconds="";
                    this.form.requestVolumeThreshold="";
                    this.form.domains= [{
                        weight: '',
                        timeout: '',
                        upstreamHost: '',
                        upstreamUrl: '',
                        retry:"",
                    }],
                    this.form.conditions=[{
                        paramType: '',
                        paramName: '',
                        paramValue: '',
                        operator: '',
                        paramDisabled: false,
                    }];
                }
            },

            editRule(row) {
                //this.initEnum();
                this.dialogFormVisible = true;
                this.dialogHrefVisible = true;
                this.currentRow = row;
                this.form.name = this.currentRow.name;
                this.form.id = this.currentRow.id;
                this.form.sselected = "" + this.currentRow.type;
                this.form.continued = "" + this.currentRow.continued;
                this.form.loged = "" + this.currentRow.loged;
                this.form.enabled = "" + this.currentRow.enabled;
                this.form.rank = this.currentRow.rank;
                this.conditionLengthShow= false;
                this.queryCondition();
                //解析handle
                var myObject = eval("(" + this.currentRow.handle + ")");
                var pluginId = document.getElementById("plugin").value;
                // alert("rule:"+pluginId);
                if (pluginId == "monitor") {
                    this.form.continued = "" + myObject.continued;
                    this.form.loged = "" + myObject.loged;
                } else if (pluginId == "2") {

                } else if (pluginId == "3") {

                } else if (pluginId == "rate_limiter") {
                    this.form.times = myObject.burstCapacity;
                    this.form.loged = "" + myObject.loged;
                    this.form.onceTime = "" + myObject.replenishRate;
                } else if (pluginId == "5") {

                } else if (pluginId == "rewrite") {
                    this.form.detail =  myObject.rewriteURI;
                } else if (pluginId == "waf") {
                    this.form.access =  myObject.permission;
                    if(this.form.access == 'reject'){
                        this.isDenyDisable = true;
                        this.form.status = myObject.statusCode;
                    }else{
                        this.isDenyDisable = false;
                    }
                } else if (pluginId == "divide") {
                    this.form.groupKey = "" + myObject.groupKey;
                    this.form.loadBalance = "" + myObject.loadBalance;
                    this.form.commandKey = "" + myObject.commandKey;
                    this.form.maxConcurrentRequests= "" + myObject.maxConcurrentRequests;
                    this.form.errorThresholdPercentage= "" + myObject.errorThresholdPercentage;
                    this.form.sleepWindowInMilliseconds= "" + myObject.sleepWindowInMilliseconds;
                    this.form.requestVolumeThreshold= "" + myObject.requestVolumeThreshold;
                    var upstreamList = myObject.upstreamList;
                    this.form.domains = upstreamList;
                }
                else if (pluginId == "dubbo") {
                    this.form.groupKey = "" + myObject.groupKey;
                    this.form.loadBalance = "" + myObject.loadBalance;
                    this.form.commandKey = "" + myObject.commandKey;
                    this.form.maxConcurrentRequests= "" + myObject.maxConcurrentRequests;
                    this.form.errorThresholdPercentage= "" + myObject.errorThresholdPercentage;
                    this.form.sleepWindowInMilliseconds= "" + myObject.sleepWindowInMilliseconds;
                    this.form.requestVolumeThreshold= "" + myObject.requestVolumeThreshold;
                    this.form.registry="" + myObject.registry;
                    this.form.appName="" + myObject.appName;
                    this.form.protocol="" + myObject.protocol;
                    this.form.port= myObject.port;
                    this.form.version="" + myObject.version;
                    this.form.group="" + myObject.group;
                    this.form.timeout=  myObject.timeout;
                    this.form.retry= myObject.retries;
                    this.form.loadBalance = "" + myObject.loadbalance;
                }
                //初始化参数
                if ("" + this.currentRow.matchMode == "0") {
                    this.form.tselected = "and"
                } else {
                    this.form.tselected = "or"
                }
            },
            //保存数据
            saveRule(formName) {
                //首先搞点事情
                this.$refs[formName].validate((valid) => {
                    if (valid && this.pass() && !this.numShowF && !this.numShowH) {
                        //处理校验
                            this.$http.post(document.getElementById('serverIpAddress').href + '/rule/saveRule', {
                                "plugin": document.getElementById("plugin").value,
                                "selectorId": document.getElementById("selectorId").value,
                                "id": this.form.id,
                                "name": this.form.name.trim(),
                                "matchMode": this.form.tselected,
                                "continued": this.form.continued,
                                "loged": this.form.loged,
                                "enabled": this.form.enabled,
                                "rank": this.form.rank,
                                "cleanQuery": this.form.cleanQuery,
                                "status": this.form.status,
                                "rewriteURI": this.form.detail.trim(),
                                "onceTime": this.form.onceTime,
                                "times": this.form.times,
                                "access": this.form.access,
                                "domains": this.form.domains,
                                "groupKey": this.form.groupKey.trim(),
                                "loadBalance": this.form.loadBalance,
                                "commandKey": this.form.commandKey.trim(),
                                "conditions":this.form.conditions,
                                "registry":this.form.registry,
                                "appName":this.form.appName,
                                "protocol":this.form.protocol,
                                "port":this.form.port,
                                "version":this.form.version,
                                "group":this.form.group,
                                "timeout": this.form.timeout,
                                "retry":this.form.retry,
                                "maxConcurrentRequests":this.form.maxConcurrentRequests,
                                "errorThresholdPercentage":this.form.errorThresholdPercentage,
                                "sleepWindowInMilliseconds":this.form.sleepWindowInMilliseconds,
                                "requestVolumeThreshold":this.form.requestVolumeThreshold,
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
                                        this.form.newRetrySecretKey = "";
                                        this.form.newRetrySysMark = "";
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
            queryCondition: function(){
                this.$http.post(document.getElementById('serverIpAddress').href + '/rule/conditionList', {
                    "id":  this.form.id,
                }, {
                    headers: {
                        "token": sessionStorage.getItem("token"),
                    }
                }).then(
                    response => {
                        if (response.body.code == 200 && response.body.data != null) {
                            this.form.conditions = response.body.data.dataList;
                            var index;

                            for(index in this.form.conditions){
                                if(this.form.conditions[index].paramType == "post" || this.form.conditions[index].paramType == "header"){
                                    this.form.conditions[index].paramDisabled = false;
                                    var indexs;
                                    for(indexs in this.oOptions){
                                        this.oOptions[indexs].disabled = false;
                                    }
                                } else{
                                   /* var indexs;
                                    for(indexs in this.oOptions){
                                        if(this.oOptions[indexs].name == '<' || this.oOptions[indexs].name == '>'){
                                            this.oOptions[indexs].disabled = true;
                                        }else{
                                            this.oOptions[indexs].disabled = false;
                                        }
                                    }*/
                                    this.form.conditions[index].paramName = "";
                                    this.form.conditions[index].paramDisabled = true;
                                }
                            }
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
                );
            },
            pass() {
                var x;
                var domains = this.form.domains;
                if(document.getElementById("plugin").value == 'divide') {
                    if (this.form.loadBalance == '' || this.form.groupKey.trim() == '' || this.form.commandKey.trim() == '' ){
                        this.enabledShow = true;
                        return false;
                    }
                    for (x in domains) {
                        if((domains[x].weight == undefined || domains[x].weight=="")  || (domains[x].timeout == undefined || domains[x].timeout =="")|| (domains[x].upstreamHost == undefined || domains[x].upstreamHost.trim() =="")
                            || (domains[x].upstreamUrl == undefined || domains[x].upstreamUrl.trim() =="")|| (domains[x].retry == undefined || domains[x].retry =="")){
                            this.enabledShow = true;
                            return false;
                        }
                    }
                    this.enabledShow = false;
                }
                if(document.getElementById("plugin").value == 'dubbo') {
                    if ( this.form.groupKey == '' || this.form.commandKey == ''
                    || this.form.appName == ""
                    || this.form.registry == "" || this.form.timeout == ""){
                        this.enabledShow = true;
                        return false;
                    }
                    this.enabledShow = false;
                }
                var y;
                var conditions = this.form.conditions;
                for (y in conditions) {
                    let paramType = conditions[y].paramType;
                    let paramValue = conditions[y].paramValue;
                    let paramName = conditions[y].paramName;
                    let operator = conditions[y].operator;
                    if ((paramType == undefined || paramType.trim()=='')|| (paramValue == undefined || paramValue.trim()=='')|| (operator == undefined || operator.trim()=='')
                        || (paramType == "post" && (paramName == undefined || paramName.trim() == '')) || (paramType == "header" && (paramName == undefined || paramName =='' ))) {
                        this.conditionrrShow = true;
                        this.conditionLengthShow = false;
                        return false;
                    }else{
                        this.conditionrrShow = false;
                    }
                    if ((paramValue !=undefined && paramValue.length > 64)
                        || (paramType == "post" && paramName !=undefined && paramName.length > 64)
                        || (paramType == "header" && paramName != undefined && paramName.length > 64)) {
                        this.conditionLengthShow = true;
                        return false;
                    }else{
                        this.conditionLengthShow = false;
                    }

                    /*
                    if( conditions[y].paramType == '' || conditions[y].paramValue.trim() == '' || conditions[y].operator.trim() == ''
                        || (conditions[y].paramName.trim() == "" && conditions[y].paramType == "post") || (conditions[y].paramName.trim() == "" && conditions[y].paramType == "header")){
                        this.conditionrrShow = true;
                        return false;
                    }
                    if(conditions[y].paramValue.length >64
                        || (conditions[y].paramType == "post" && conditions[y].paramName.length > 64) || (conditions[y].paramType == "header" && conditions[y].paramName.length > 64)){
                        this.conditionLengthShow = true;
                        return false;
                    }*/
                }
                this.conditionrrShow = false;
                this.enabledShow = false;
                this.conditionLengthShow = false;
                return true;
            },
            checkParamDisabled(val,index){
                if(val == "post" || val == "header"){
                    var indexs;
                    this.form.conditions[index].paramDisabled = false;
                    /*for(indexs in this.oOptions){
                        this.oOptions[indexs].disabled = false;
                    }*/
                } else{
                    /*var indexs;*/
                   /* for(indexs in this.oOptions){
                        if(this.oOptions[indexs].name == '<' || this.oOptions[indexs].name == '>'){
                            this.oOptions[indexs].disabled = true;
                        }else{
                            this.oOptions[indexs].disabled = false;
                        }
                    }*/
                    this.form.conditions[index].operator = "";
                    this.form.conditions[index].paramName = "";
                    this.form.conditions[index].paramDisabled = true;
                    this.pass();
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
            //删除选择器
            deleRule(row) {
                //delete row and update tableData but don't send post request to update all data
                var oldTableData = this.tableData1;
                var tlen = oldTableData.length;
                this.$confirm('确认要删除？')
                    .then(_ => {
                        this.$http.post(document.getElementById('serverIpAddress').href + '/rule/removeRule', {
                            "id": row.id,
                            "selectorId": document.getElementById("selectorId").value,
                            "plugin": document.getElementById('plugin').value,
                        }, {
                            headers: {
                                "token": sessionStorage.getItem("token"),
                            }
                        }).then(
                            response => {
                                if (response.body.code == 200) {
                                    this.$message({type: 'success', message: '删除数据成功!'});
                                    for (let j = 0; j < tlen; j++) {
                                        if (row.id == oldTableData[j].id) {
                                            oldTableData.splice(j, 1);
                                            tlen = tlen - 1;
                                        }
                                    }
                                    this.count1 = this.count1 - 1;
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
            query: function () {
                this.$http.post(document.getElementById('serverIpAddress').href + '/rule/ruleList', {
                    "pageSize": this.paging.limit,
                    "pageNumber": this.paging.currentPage,
                    "id": document.getElementById("selectorId").value,
                }, {
                    headers: {
                        "token": sessionStorage.getItem("token"),
                    }
                }).then(
                    response => {

                        if (response.body.code == 200 && response.body.data != null) {
                            let rp = response.body;
                            this.count1 = rp.data.totalCount;
                            this.tableData1 = rp.data.dataList
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
                    upstreamUrl: '',
                    retry: '',
                });
            },
            removeCondition(item) {
                var index = this.form.conditions.indexOf(item)
                if (index !== 0) {
                    this.form.conditions.splice(index, 1)
                }
            },
            addCondition() {
                this.form.conditions.push({
                    paramType: '',
                    paramName: '',
                    paramValue: '',
                    operator: '',
                });
            }
        },
        watch: {
            paging: {
                handler: function () {
                    this.$http.post(document.getElementById('serverIpAddress').href + '/rule/ruleList', {
                        "pageSize": this.paging.limit,
                        "pageNumber": this.paging.currentPage,
                        "id": document.getElementById("selectorId").value,
                    }, {
                        headers: {
                            "token": sessionStorage.getItem("token"),
                        }
                    }).then(
                        response => {

                            if (response.body.code == 200 && response.body.data != null) {
                                let rp = response.body;
                                this.count1 = rp.data.totalCount;
                                this.tableData1 = rp.data.dataList
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
            },

        }
    }
</script>

<style lang="less">
    @import '../style/mixin';

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

    .el-input {

    }

    .el-table__expanded-cell {
        padding: 5px !important;
    }

    .demo-table-expand {
        font-size: 0;
    }

    .demo-table-expand label {
        width: 90px;
        color: #99a9bf;
    }

    .demo-table-expand .el-form-item {
        margin-right: 0;
        margin-bottom: 0;
        width: 50%;
    }
</style>
