import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
import VueResource from 'vue-resource'

import fontawesome from '@fortawesome/fontawesome'
import FontAwesomeIcon from '@fortawesome/vue-fontawesome'
import solid from '@fortawesome/fontawesome-free-solid'
import regular from '@fortawesome/fontawesome-free-regular'
import brands from '@fortawesome/fontawesome-free-brands'

fontawesome.library.add(solid)
fontawesome.library.add(regular)
fontawesome.library.add(brands)

Vue.component('font-awesome-icon', FontAwesomeIcon)


Vue.config.productionTip = true

Vue.use(VueResource);
Vue.use(ElementUI)

var vm = new Vue({
    el: '#app',
    router: router,
    components: {App}
})
