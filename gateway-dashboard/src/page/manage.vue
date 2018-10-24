<template>
	<div class="manage_page fillcontain">
		<el-row style="height: 100%;">
	  		<el-col :span="3"  style="min-height: 100%; background-color: #324057;">
				<el-menu :default-active="defaultActive" style="min-height: 100%;" theme="dark" router>
					<el-menu-item index="manage"><i class="el-icon-menu"></i>首页</el-menu-item>
					<el-submenu index="2" v-if="competence['1']== undefined?false:true">
						<template slot="title" ><i class="el-icon-setting"></i>插件列表</template>
						<el-menu-item index="concurrency" v-if="competence['10']== undefined?false:true">concurrency</el-menu-item>
						<el-menu-item index="divide" v-if="competence['11']== undefined?false:true">divide</el-menu-item>
					</el-submenu>
					<el-submenu index="3" v-if="competence['2']== undefined?false:true">
						<template slot="title"><i class="el-icon-setting"></i>系统管理</template>
						<el-menu-item index="appAuthManage" v-if="competence['20']== undefined?false:true">接入管理</el-menu-item>
						<el-menu-item index="serviceManage" v-if="competence['21']== undefined?false:true">服务管理</el-menu-item>
						<el-menu-item index="pluginManage" v-if="competence['22']== undefined?false:true">插件管理</el-menu-item>
						<el-menu-item index="userManage" v-if="competence['23']== undefined?false:true">用户管理</el-menu-item>
						<el-menu-item index="roleManage" v-if="competence['24']== undefined?false:true">角色管理</el-menu-item>
						<el-menu-item index="resourceManage" v-if="competence['25']== undefined?false:true">权限管理</el-menu-item>
						<el-menu-item index="menuManage" v-if="competence['26']== undefined?false:true">菜单管理</el-menu-item>
					</el-submenu>
					<el-submenu index="4" v-if="competence['3']== undefined?false:true">
						<template slot="title"><i class="el-icon-warning"></i>说明</template>
						<el-menu-item index="explain" v-if="competence['31']== undefined?false:true">说明</el-menu-item>
					</el-submenu>
				</el-menu>
			</el-col>
			<el-col :span="21" style="height: 100%;overflow: auto;">
				<keep-alive>
				    <router-view></router-view>
				</keep-alive>
			</el-col>
		</el-row>
  	</div>
</template>

<script>
    let Base64 = require('js-base64').Base64;

    export default {
        data(){
            return {
                name: "",
				sid:"",
                baseUrl: document.getElementById('serverIpAddress').href,
				baseWebUrl: document.getElementById('webIpAddress').href,
				competence:{},
            }
        },
		computed: {
			defaultActive: function(){
				return this.$route.path.replace('/', '');
			}
		},
        created(){
			//初始化菜单列表
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
		methods:{
		}
    }
</script>
<style lang="less">
	@import '../style/mixin';
	.el-menu-item {
		min-width: inherit !important;
	}
	.manage_page{
		
	}
</style>
