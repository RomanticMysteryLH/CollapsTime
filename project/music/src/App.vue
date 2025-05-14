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
          <!-- <i class="el-icon-headset" style="margin-right: 5%">LOGO</i> -->
          <!-- <i style="margin-right: 5%"></i> -->
          <img
            src="@/assets/completeLogo.png"
            style="width: 150px; margin-right: 5%"
            alt="CollapsTime"
            title="CollapsTime"
          />
          <template v-for="item in navigateItem">
            <!--template外层循环，内部分成两种元素-->
            <!-- 如果找不到子菜单就生成el-menu-item -->
            <el-menu-item
              v-if="item.items.length == 0"
              :index="item.path"
              :key="item.key + '-menu-item'"
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
              <template v-slot:title>{{ item.title }}</template>
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
            style="margin-left: 80px; width: 250px"
            popper-class="my-autocomplete"
            v-model="searchBar"
            :fetch-suggestions="querySearch"
            placeholder="请输入内容"
            size="small"
            :trigger-on-focus="false"
            @select="selectSearch"
            ref="myAutocomplete"
            @keyup.enter.native="search()"
          >
            <!-- <i
              slot="suffix"
              class="el-input__icon el-icon-search search_button"
              @click="search()"
            ></i> -->
            <template v-slot:default="{ item }">
              <div class="name">{{ item.name }}</div>
              <span class="type">{{ item.type }}</span>
            </template>
            <!-- suffix表示在搜索框尾部 -->
            <template v-slot:append>
              <el-button
                class="search_button"
                icon="el-icon-search"
                @click="search()"
              ></el-button>
            </template>
            <!-- <el-button
              v-slot:append
              icon="el-icon-search"
              @click="search()"
            ></el-button> -->
          </el-autocomplete>
          <!-- 切换模式 -->
          <el-tooltip
            class="item"
            effect="dark"
            content="相似搜索可以匹配与您搜索的歌曲相似的歌曲"
            placement="bottom-start"
            :enterable="false"
          >
            <el-switch
              class="searchSwitch"
              v-model="searchMode"
              active-text="相似搜索"
              inactive-text="普通搜索"
              :width="35"
              :active-value="1"
              :inactive-value="0"
              inactive-color="#409EFF"
              active-color="#13ce66"
              style="margin-left: 30px"
            >
            </el-switch>
          </el-tooltip>
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
                <el-button size="mini" type="text" @click="goToUserCenter"
                  >个人中心</el-button
                >
                <el-button type="primary" size="mini" @click="log_out()"
                  >退出</el-button
                >
              </div>
              <!-- reference确定触发hover的对象 -->
              <el-avatar
                :key="this.$root.userData.avator"
                :src="$RequestUrl + this.$root.userData.avator"
                icon="el-icon-user-solid"
                :size="30"
                v-slot:reference
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
      :audio="this.audio"
      :fixed="true"
      style="z-index: 9999"
      :lrcType="1"
      @onPlaying="getLyrics"
    ></aplayer>
    <loginView
      :login_ed.sync="this.$root.userData.login_ed"
      ref="loginView"
    ></loginView>
    <transition name="el-fade-in-linear">
      <div
        role="alert"
        class="el-notification right"
        style="top: 70px; z-index: 2005"
        v-show="this.$root.downloadProVis"
      >
        <!---->
        <div class="el-notification__group">
          <h2 class="el-notification__title">下载进度</h2>
          <div class="el-notification__content">
            <p>{{ this.$root.downloadProgress }}</p>
          </div>
          <div
            class="el-notification__closeBtn el-icon-close"
            @click="$root.downloadProVis = false"
          ></div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import loginView from "@/views/login.vue";
import "./common/font/font.css";
import detailSong from "@/views/detailSong.vue";
import Vue from "vue";
import VueCookies from "vue-cookies";
import APlayer from "@moefe/vue-aplayer";
import Qs from "qs";
Vue.use(APlayer, {
  defaultCover: "./assets/default/defaultPlayList.jpg",
  productionTip: false,
});
Vue.use(VueCookies);
export default {
  name: "App",
  data: function () {
    return {
      //搜索框文本
      searchBar: "",
      //搜索模式，0为普通搜索，1为相似搜索
      searchMode: 0,
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
      // console.log(this.nowSongId);
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
        // console.log(this.searchBar);
        this.$router.push({
          path: `/searchResult/${this.searchBar}/${this.searchMode}`,
        });
        this.$refs.myAutocomplete.suggestions = [];
        this.reload();
      }
    },
    //搜索建议
    querySearch(queryString, cb) {
      // console.log(queryString);
      let axiosThis = this;
      let data = Qs.stringify({
        key: queryString,
      });
      this.timeout = setTimeout(() => {
        // cb(results);
        let requestURL = "/user/search";
        if (axiosThis.searchMode == 0) {
          requestURL = "/user/search";
        } else {
          requestURL = "/user/similarSearch";
        }
        this.$axios
          .post(requestURL, data)
          .then((res) => {
            let response = res.data;
            // console.log(response);
            let cbArray = [];
            if (response.songs) {
              cbArray.push.apply(
                cbArray,
                response.songs.map((item) => {
                  // console.log(item);
                  return { name: item.name, id: item.id, type: "-歌曲-" };
                })
              );
            }
            if (response.singers) {
              cbArray.push.apply(
                cbArray,
                response.singers.map((item) => {
                  // console.log(item);
                  return { name: item.name, id: item.id, type: "-歌手-" };
                })
              );
            }
            if (response.songLists) {
              cbArray.push.apply(
                cbArray,
                response.songLists.map((item) => {
                  // console.log(item);
                  return { name: item.title, id: item.id, type: "-歌单-" };
                })
              );
            }
            cb(cbArray);
          })
          .catch((err) => {
            console.log(err);
          });
      }, 1000);

      // var restaurants = this.restaurants;
      // var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
      // // 调用 callback 返回建议列表的数据
      // cb(results);
    },
    changeLoginView() {
      this.$refs.loginView.loginVisible = true; // 修改子组件数据
    },
    // changeLoginSitu(val) {
    //   this.login_ed = val;
    // },
    log_out() {
      this.$root.cleanUserData();
      // console.log(this.$root.userData);
      localStorage.removeItem("collapstimeUser");
      this.$message("退出成功");
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
    detailSongOpened() {
      // console.log("opened");
      document.body.style = "padding-right:0px";
    },
    goToUserCenter() {
      this.$router.push("/userCenter");
    },
    async getLyrics(e) {
      console.log(e);
      // let newLyrics = "";
      // // let axiosThis = this;
      // let dataObj = {
      //   songId: id,
      // };
      // if(this.$root.userData.login_ed==true){
      //   dataObj.userId=this.$root.userData.userId;
      // }
      // let data=Qs.stringify(dataObj);
      // await this.$axios
      //   .post(`song/getLyric`, data)
      //   .then((res) => {
      //     //请求成功
      //     // console.log(axiosThis);
      //     // console.log("res.data=>", res.data);
      //     newLyrics = res.data.lyric;
      //   })
      //   .catch((err) => {
      //     console.log(err);
      //     this.$message.error("请求失败，无法发送");
      //   });
      // return newLyrics;
    },
    selectSearch(item) {
      // console.log(item);
      if (item.type.indexOf("歌曲") >= 0) {
        this.openSongDetail(item.id);
      } else if (item.type.indexOf("歌手") > 0) {
        this.$router.push({ path: `/detailMusician/${item.id}` });
        //这里不reload会导致歌手详情打开时搜索另一个歌手并选中却没跳转
        this.reload();
      } else {
        this.$router.push({ path: `/detailPlaylist/${item.id}` });
        this.reload();
      }
    },
    autoLogin() {
      let axiosThis = this;
      if (localStorage.getItem("collapstimeUser") == null) {
        return;
      }
      let data = {};
      this.$axios
        .post(`/user/verifyToken`, data, {
          headers: {
            token: localStorage.getItem("collapstimeUser"),
          },
        })
        .then((res) => {
          //请求成功
          // console.log(axiosThis);
          // console.log("res.data=>", res.data);
          let responseData = res.data;
          if (responseData.status == "success") {
            axiosThis.$root.userData = {
              login_ed: true,
              userId: responseData.userId,
              username: responseData.username,
              avator: responseData.avator,
              account: responseData.account,
            };
            axiosThis.$message.success("自动登录成功！");
          } else {
            axiosThis.$message.error(responseData.msg + "，请重新登陆！");
          }
          this.reload();
        })
        .catch((err) => {
          console.log(err);
          this.$message.error("请求失败，无法自动登陆！");
        });
    },
    // clickOther: function () {
    //   //关闭所有消息
    //   this.$message.closeAll();
    // },
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
            {
              title:"歌曲",
              key:"2-3",
              path:"/songs",
            }
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
        // {
        //   title: "消息中心",
        //   key: "4",
        //   path: "/message",
        //   items: [],
        // },
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
      setTimeout(() => {
        this.$refs.aplayer.skipForward();
        this.$refs.aplayer.skipBack();
        this.$refs.aplayer.play();
      }, 300);
      // console.log(this.audio);
    },
  },
  provide() {
    return {
      reload: this.reload,
    };
  },
  mounted() {
    // console.log(this.$refs.aplayer.currentMusic);
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
    // console.log(this.$root.audio);
    //三秒后再显示歌词，避免bug
    this.$refs.aplayer.hideLrc();
    setTimeout(() => {
      console.log("歌词");
      this.$refs.aplayer.hideLrc();
      this.$refs.aplayer.showLrc();
    }, 3000);
    // window.addEventListener("click", this.clickOther);
    // if(this.$root.audio.length<1){
    //   this.$root.audio.push(this.$refs.aplayer.currentMusic)
    // }
    // console.log(this.$root.audio)
  },
  created() {
    // myToken=localStorage.getItem("musicUser");
    this.autoLogin();
  },
  // beforeDestroy() {
  //   // 实例销毁之前对点击事件进行解绑
  //   window.removeEventListener("click", this.clickOther);
  // },
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
  height: 80px !important;
  text-align: right !important;
  padding-right: 30px !important;
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
  scrollbar-width: none;
  height: 100%;
  overflow: hidden;
  font-family: "PingFang SC", SimSun, "Helvetica Neue", Helvetica,
    "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
body .el-popup-parent--hidden {
  scrollbar-width: none;
  overflow: hidden;
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
.my-autocomplete li {
  /* 加入important解决无法覆盖 */
  line-height: normal !important;
  padding: 8px 14px !important;
}
.my-autocomplete li .name {
  text-overflow: ellipsis;
  overflow: hidden;
}
.my-autocomplete li .type {
  font-size: 10px;
  color: #fdcb5e;
}
.my-autocomplete li .highlighted .addr {
  color: #ddd;
}
.el-message {
  top: 60px !important;
}
.searchSwitch .el-switch__label span {
  font-size: 10px;
}
.searchSwitch .is-active span {
  color: rgb(255, 208, 75);
}
</style>
