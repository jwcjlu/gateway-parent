<template>
    <div class="login_page fillcontain">
        <transition name="form-fade" mode="in-out">
            <section class="form_contianer" v-show="showLogin">
                <div class="manage_tip">
                    <p>Eway-Admin</p>
                </div>
                <el-form :model="loginForm" :rules="rules" ref="loginForm">
                    <el-form-item prop="username">
                        <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input type="password" placeholder="密码" v-model="loginForm.password"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('loginForm')">登录</el-button>
                    </el-form-item>
                </el-form>
            </section>
        </transition>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                loginForm: {
                    username: '',
                    password: '',
                },
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ],
                },
                showLogin: false,
                baseUrl: document.getElementById('serverIpAddress').href

            }
        },
        mounted() {
            this.showLogin = true;
        },
        computed: {
        },
        methods: {
            submitForm() {
                this.$http.post(document.getElementById('serverIpAddress').href + '/login', {
                    userName: this.loginForm.username.trim(),
                    password: this.loginForm.password.trim()
                }).then(
                    response => {
                        if (response.body.code == 200) {
                                var userName = response.body.data.userName;
                                var token = response.body.data.token;
                                var competence = response.body.data.competence;
                                var treeData = response.body.data.treeData;
                                var id = response.body.data.id;
                                sessionStorage.setItem("userId", id);
                                sessionStorage.setItem("treeData", JSON.stringify(treeData));
                                sessionStorage.setItem("userName", userName);
                                sessionStorage.setItem("token", token);
                                sessionStorage.setItem("competence", JSON.stringify(competence));
                                console.log("success!");
                                this.$router.push('manage');
                        }else{
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
                );
            },
        },

        watch: {
        }
    }
</script>

<style lang="less" scoped>
    @import '../style/mixin';

    .login_page {
        background-color: #324057;
    }

    .manage_tip {
        position: absolute;
        width: 100%;
        top: -100px;
        left: 0;
        p {
            font-size: 34px;
            color: #fff;
        }
    }

    .form_contianer {
        .wh(320px, 210px);
        .ctp(320px, 210px);
        padding: 25px;
        border-radius: 5px;
        text-align: center;
        background-color: #fff;
        .submit_btn {
            width: 100%;
            font-size: 16px;
        }
    }

    .tip {
        font-size: 12px;
        color: red;
    }

    .form-fade-enter-active, .form-fade-leave-active {
        transition: all 1s;
    }

    .form-fade-enter, .form-fade-leave-active {
        transform: translate3d(0, -50px, 0);
        opacity: 0;
    }
</style>
