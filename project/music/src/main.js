import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import VueRouter from 'vue-router'
import router from './router' //引入路由设置

//解决跨域问题
import axios from 'axios'
Vue.prototype.$axios = axios
axios.defaults.baseURL = '/api' //关键代码


Vue.use(VueRouter)
Vue.use(ElementUI);
Vue.config.productionTip = false


new Vue({
  router,
  data: function () {
    return {
      //是否显示router-view的加载动画
      routerLoading:false,
      //在此定义的全局变量可以直接用this.$root.xxx访问并修改，
      //Vue.prototype定义的全局变量不可被修改
      // userData: {
      //   login_ed: false,
      //   userId: 0,
      //   account: "",
      //   username: "",
      //   avator: "",
      // },
      userData: {
        login_ed: true,
        userId: 1,
        account: "1912080054",
        username: "123456",
        avator: "",
      },
      nowSongId: 0,
      audio: [
        //   {
        //   name:"",
        //   artist:"",
        //   url:"",
        //   cover:"",
        //   lrc:"",
        // }
      ],
      //只要歌曲的收藏状态改变，就改变这个flag，子组件监听到后就刷新自己（歌单详情、歌手详情）
      songCollectChangeFlag:true,
      
    }
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    setUserData(userId, account, username, avator, login_ed) {
      console.log("setUserData")
      this.userData.userId = userId;
      this.userData.account = account;
      this.userData.username = username;
      this.userData.avator = avator;
      this.userData.login_ed = login_ed;
    },
    cleanUserData() {
      console.log("setUserData")
      this.userData.account = "";
      this.userData.username = "";
      this.userData.avator = "";
      this.userData.login_ed = false;
    },
    startPlay() {
      // this.$refs.aplayer.play();
    }
  },
  computed: {

  },
  created: function () {
    //设置全局变量
    Vue.prototype.$RequestUrl = "http://localhost:8081";
    // Vue.prototype.$RequestUrl = "http://192.168.31.58:8081";
  },

  render: h => h(App),
}).$mount('#app')