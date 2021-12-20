<template>
  <div id="app">
    <el-container id="">
      <el-header name="navbar" style="padding: 0px; z-index: 99">
        <!--el-header自带padding，导致导航栏有白边-->
        <!-- 这里是$route.path不是$router.path -->
        <!-- 
    this.$router 实际上就是全局路由对象任何页面都可以调用 push(), go()等方法；
    this.$route  表示当前正在用于跳转的路由器对象，可以调用其name、path、query、params等属性。 -->
        <el-menu
          :default-active="this.$route.path"
          router
          class="homepage-menu"
          mode="horizontal"
          @select="handleSelect"
          background-color="white"
          text-color="black"
          active-text-color="#ffd04b"
        >
          <i class="el-icon-headset" style="margin-right: 5%">LOGO</i>
          <template v-for="item in navigateItem">
            <!--template外层循环，内部分成两种元素-->
            <!-- 如果找不到子菜单就生成el-menu-item -->
            <el-menu-item
              v-if="item.items.length == 0"
              :index="item.path"
              :key="item.key"
              :disabled="item.disabled"
              >{{ item.title }}
            </el-menu-item>
            <!--如果找得到子菜单就生成el-submenu-->
            <el-submenu
              v-else
              :index="item.path"
              :key="item.key"
              :show-timeout="100"
            >
              <template slot="title">{{ item.title }}</template>
              <el-menu-item
                v-for="(items1, key1) in item.items"
                :key="key1"
                :index="items1.path"
                :disabled="item.disabled"
              >
                {{ items1.title }}</el-menu-item
              >
            </el-submenu>
          </template>
          <el-autocomplete
            v-model="searchBar"
            :fetch-suggestions="querySearch"
            placeholder="请输入内容"
            style="width: 15%; margin-left: 5%"
            size="small"
          >
            <i
              slot="suffix"
              class="el-input__icon el-icon-search search_button"
              @click="search()"
            ></i>
            <!-- suffix表示在搜索框尾部 -->
          </el-autocomplete>

          <el-button
            v-if="!this.$root.userData.login_ed"
            type="primary"
            round
            size="mini"
            plain
            style="margin-left: 4%"
            @click="changeLoginView()"
            >登录
          </el-button>
          <template v-if="this.$root.userData.login_ed">
            <!-- fit的默认值是cover -->
            <el-popover
              placement="bottom"
              title="我的账户"
              width="200"
              trigger="hover"
              content="这是一段内容,这是一段内容,这是一段内容,这是一段内容。"
              style="margin-left: 4%; cursor: pointer"
            >
              <p>您好，{{ this.$root.userData.username }}！</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="visible = false"
                  >个人中心</el-button
                >
                <el-button type="primary" size="mini" @click="log_out()"
                  >退出</el-button
                >
              </div>
              <!-- reference确定触发hover的对象 -->
              <el-avatar
                src=""
                icon="el-icon-user-solid"
                :size="30"
                slot="reference"
              >
              </el-avatar>
            </el-popover>

            <!-- <el-button
              v-if="login_ed"
              type="success"
              round
              size="mini"
              plain
              style="margin-left: 1%"
              >我的账户
            </el-button> -->
          </template>
        </el-menu>
      </el-header>

      <el-main class="scrollContent" style="padding: 0px">
        <el-scrollbar class="page-component__scroll">
          <div id="mainContent">
            <!-- <el-page-header @back="goBack" content="详情页面">
                    </el-page-header> -->
            <router-view
              id="pageView"
              v-if="isRouterAlive"
              @openSongDetail="openSongDetail"
              @goBackToTop="goBackToTop"
              v-loading="this.$root.routerLoading"
            ></router-view>

            <!-- <my-component></my-component> -->
          </div>
        </el-scrollbar>
      </el-main>
      <el-backtop
        ref="goBackButton"
        target=".page-component__scroll .el-scrollbar__wrap"
        style="bottom: 100px"
      ></el-backtop>
    </el-container>
    <el-drawer
      title="歌曲详情"
      :visible.sync="songDetailVisible"
      direction="btt"
      size="70%"
      destroy-on-close
    >
      <detailSong :songId="nowSongId"></detailSong>
    </el-drawer>
    <aplayer
      ref="aplayer"
      :audio="this.$root.audio"
      :fixed="true"
      style="z-index: 9999"
      :lrcType="1"
    ></aplayer>
    <loginView
      :login_ed.sync="this.$root.userData.login_ed"
      ref="loginView"
    ></loginView>
  </div>
</template>

<script>
import loginView from "@/views/login.vue";
import "./common/font/font.css";
import detailSong from "@/views/detailSong.vue";
import Vue from "vue";
import VueCookies from "vue-cookies";
import APlayer from "@moefe/vue-aplayer";
Vue.use(APlayer, {
  defaultCover: "@/assets/default/defaultPlayList.jpg",
  productionTip: true,
});
Vue.use(VueCookies);
export default {
  name: "App",
  data: function () {
    return {
      //搜索框文本
      searchBar: "",
      //搜索建议
      search_suggestions: [],
      //当前路由
      currentRoute: window.location.pathname,
      activeIndex: "/home",
      // //登录标记
      // login_ed: false,
      //用户信息
      isRouterAlive: true,
      songDetailVisible: false,
      //songDetail展示的歌曲的id，与子组件detailSong的相同
      nowSongId: 0,
    };
  },
  methods: {
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
    //打开详细歌曲信息，由子组件调用
    openSongDetail(data) {
      this.songDetailVisible = true;
      this.nowSongId = data;
      console.log(this.nowSongId);
      //获取到了
    },
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    },
    search() {
      //click能正常使用，onclick不行
      if (this.searchBar === "" || this.searchBar == null) {
        this.$message.error("您还没有输入要搜索的内容");
      } else {
        console.log(this.searchBar);
      }
    },
    //搜索建议
    querySearch(queryString, cb) {
      console.log(queryString);
      // var restaurants = this.restaurants;
      // var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
      // // 调用 callback 返回建议列表的数据
      // cb(results);
      cb(["1", "2", "3", "4"]);
    },
    createFilter(queryString) {
      console.log(queryString);
      // return (restaurant) => {
      //   return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      // };
    },
    changeLoginView() {
      this.$refs.loginView.loginVisible = true; // 修改子组件数据
    },
    // changeLoginSitu(val) {
    //   this.login_ed = val;
    // },
    log_out() {
      this.$root.cleanUserData();
      console.log(this.$root.userData);
      this.reload();
    },
    // changeNowSong(data){
    //   this.nowSongId=data;
    // }
    // onPlay(e){
    //   console.log(e);
    //   this.$refs.aplayer.play();
    // }
    goBackToTop() {
      console.log("goBackToTop");
      this.$refs.goBackButton.$el.click();
    },
  },
  computed: {
    navigateItem: function () {
      //由于disabled属性是由login_ed属性计算出来的，computed直接将数据值绑定到视图，
      // 若放在data里，则login_ed变化时disabled不会变化
      let item = [
        {
          title: "首页",
          key: "1",
          path: "/home",
          items: [],
        },
        {
          title: "分类",
          key: "2",
          path: "",
          items: [
            {
              title: "歌单",
              key: "2-1",
              path: "/playlist",
            },
            {
              title: "歌手",
              key: "2-2",
              path: "/musician",
            },
          ],
        },
        {
          title: "我的音乐",
          key: "3",
          path: "/myMusic",
          items: [],
          //未登录时不可选择
          disabled: !this.$root.userData.login_ed,
        },
        {
          title: "消息中心",
          key: "4",
          path: "/message",
          items: [],
        },
      ];
      return item;
    },
    // isLoginChange(){
    //   return  this.$root.userData.login_ed;
    // }
    audio() {
      return this.$root.audio;
    },
  },
  components: {
    loginView,
    detailSong,
  },
  watch: {
    // login_ed(newVal, oldVal) {
    //   console.log("father new" + newVal);
    //   console.log("father old" + oldVal);
    //   console.log(this.login_ed);
    //   console.log(this.login_ed);
    //   this.$forceUpdate();
    // },
    audio() {
      //audio值改变时播放
      this.$refs.aplayer.play();
      console.log(this.audio);
    },
  },
  provide() {
    return {
      reload: this.reload,
    };
  },
  mounted() {
    console.log(this.$refs.aplayer.currentMusic);
    // this.$root.audio = [this.$refs.aplayer.currentMusic];
    this.$root.audio = [
      {
        name: "还没有播放歌曲哦",
        artist: "",
        url: "",
        cover: "@/assets/default/defaultPlayList.jpg",
        lrc: "[00:00:00] 歌词φ(゜▽゜*)♪",
      },
    ];
    console.log(this.$root.audio);
    // if(this.$root.audio.length<1){
    //   this.$root.audio.push(this.$refs.aplayer.currentMusic)
    // }
    // console.log(this.$root.audio)
  },
};
</script>

<style>
@import "./assets/icon/like/iconfont.css";
@import "./assets/icon/dianzan/iconfont.css";
* {
  margin: 0;
  padding: 0;
}
.homepage-menu {
  align-items: center;
  display: flex;
  justify-content: center;
  position: fixed;
  width: 100%;
}
.search_button {
  cursor: pointer;
}
#mainContent {
  margin: 20px auto; /*中间没有逗号*/
  width: 1020px;
  box-sizing: border-box;
}
.content {
  /* 用于存放路由页面的容器 */
  padding: 40px 60px 40px 60px;
  background-color: white;
  box-shadow: 0 0 10px 8px rgba(129, 129, 129, 0.219);
  border-radius: 8px;
}
.aplayer .aplayer-lrc {
  height: 80px;
  text-align: right;
  padding-right: 30px;
}
.aplayer .aplayer-lrc p {
  font-size: 14px;
}
#app {
  padding-bottom: 50px;
  /* background-image: url("./assets/background/Star-Emission0 (2).png");
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-size:100% 100%; */
}
body {
  background-image: url("./assets/background/Star-Emission0 (2).png");
  background-repeat: no-repeat;
  background-attachment: fixed;
  background-size: 100% 100%;
}

html,
body {
  height: 100%;
  overflow: hidden;
  font-family: "PingFang SC", SimSun, "Helvetica Neue", Helvetica,
    "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
.page-component__scroll {
  height: 100%;
}
.page-component__scroll .el-scrollbar__wrap {
  overflow-x: auto;
}
.scrollContent {
  height: calc(100vh - 60px);
  padding: 0;
  /* margin-top: -60px; */
  z-index: 0;
}
</style>
