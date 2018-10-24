npm i(install) webpack --save-dev<template>
  <div class="fillcontain">
    <transition :key="key">
      <router-view></router-view>
    </transition>
  </div>
</template>

<script>
    export default {
        name: 'app',
        computed: {
            key() {
                return this.$route.name !== undefined ? this.$route.name + new Date() : this.$route + new Date();
            }
        }
    };
</script>

<style lang="less" scoped>
  @import './style/common';
</style>