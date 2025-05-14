import Vue from 'vue'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import enLang from 'element-ui/lib/locale/lang/en' // lang i18n
//import i18n from './lang'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control

import qs from 'qs'
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
// if (process.env.NODE_ENV === 'production') {
//     const { mockXHR } = require('../mock')
//     mockXHR()
// }

Vue.use(ElementUI, {
    size: Cookies.get('size') || 'medium', // set element-ui default size
    //i18n: (key, value) => i18n.t(key, value)
    locale: enLang
})

Vue.config.productionTip = false
Vue.prototype.$qs = qs
Vue.prototype.backApi = "http://localhost:8081"

new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App)
})
