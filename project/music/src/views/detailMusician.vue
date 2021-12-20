<template>
  <!-- 歌单详情 -->
  <div class="content">
    <el-page-header @back="goBack()" content="歌手详情"> </el-page-header>
    <el-divider></el-divider>
    <!-- <p>{{ this.$route.params.playlistId }}</p> -->
    <el-row :gutter="60">
      <el-col :span="10">
        <div>
          <el-image
            :src="this.$RequestUrl + this.pageData.singer.picture"
            class="myimage"
            fit="cover"
            lazy
          >
            <!-- 加载前占位 -->
            <div slot="placeholder">
              <img src="@/assets/default/loading1.gif" class="image" />
            </div>
            <!-- 加载后占位 -->
            <div slot="error">
              <img src="@/assets/default/defaultSinger.jpg" class="image" />
            </div>
          </el-image>
        </div>
      </el-col>
      <el-col :span="14">
        <p style="font-size: 40px" class="musicianTitle">
          {{ this.pageData.singer.name }}
        </p>
        <el-row :gutter="30">
          <el-col :span="14">
            <el-divider content-position="center">性别/生日</el-divider>
            <p>{{ this.singerSex }} / {{ this.pageData.singer.birth }}</p>
            <el-divider content-position="center">所在地</el-divider>
            <p>{{ this.pageData.singer.location }}</p>
          </el-col>
          <el-col :span="10" style="padding: 10px">
            <div style="line-height: 60px; text-align: center">
              <el-button type="warning" circle @click="collectSinger"
                ><span :class="likeStatusClass"></span
              ></el-button>
              <p>粉丝数：{{this.pageData.followSingerCount}}</p>
            </div>
            <!-- <div style="line-height: 60px; text-align: center">
              <el-popover
                placement="right-start"
                width="400"
                trigger="click"
                v-model="commentVisible"
              >
                <el-button
                  type="text"
                  style="float: right; margin-left: -15px"
                  @click="commentVisible = false"
                  ><i class="el-icon-close"></i
                ></el-button>
                <commentArea></commentArea>

                <el-button
                  type="primary"
                  plain
                  circle
                  slot="reference"
                  @click="commentPlaylist()"
                  ><i class="el-icon-s-comment"></i
                ></el-button>
              </el-popover>
              评论：0
            </div> -->
          </el-col>
        </el-row>
        <el-divider content-position="center">简介</el-divider>
        <p style>
          {{ this.pageData.singer.introduction }}
        </p>
      </el-col>
    </el-row>
    <songList
      :listProp="this.songList"
      @openSongDetail="openSongDetail"
    ></songList>
  </div>
</template>
<style scoped>
.image {
  width: 100%;
  display: block;
  /* box-shadow: 0px 0px 20px gray; */
  border-radius: 50%;
}
.myimage {
  width: 340px;
  height: 340px;
  display: block;
  border-radius: 50%;
  box-shadow: 0px 0px 20px gray;

}
.image-slot {
  width: 100%;
  display: block;
}
.demo-image__lazy .el-image {
  display: block;
  min-height: 200px;
  margin-bottom: 10px;
}
.subscribeTile {
  font-size: 16px;
  color: gray;
  font-weight: bold;
  margin-top: 6px;
}
.musicianTitle {
  font-family: HanYiZhongSongJian, HanyiZhongSongFan;
}
.el-divider__text {
  color: gray;
  font-size: 12px;
}
</style>
<script>
import songList from "@/components/songList.vue";
// import commentArea from "@/components/commentArea.vue";
import Qs from "qs";
// import loginView from "./views/login.vue";
export default {
  data: function () {
    return {
      musicianId: 0,
      //数字字符串转换成男女
      singerSex: "",
      commentVisible: false,
      // likeStatusClass:"iconfont icon-aixin",
      pageData: {
        singerCollectStatus: 0,
        followSingerCount:0,
        singer: {
          id: 0,
          name: "",
          sex: -1,
          // picture: "",
          birth: "",
          location: "",
          introduction: "",
        },
        songs: [],
      },
      songList: [],
      songData: [
        {
          songName: "",
          musician: "",
          time: "",
        },
      ],
    };
  },
  methods: {
    //点击后路由后退一步
    goBack() {
      console.log("goBack");
      this.$router.go(-1);
    },
    getMusicianDetail() {
      // this.$root.routerLoading = true;
      let axiosThis = this;
      let params = {
        singerId: axiosThis.musicianId,
      };
      if (axiosThis.$root.userData.login_ed == true) {
        params.userId = axiosThis.$root.userData.userId;
      }
      this.$axios
        .get("singer/getSingerDetail", {
          params: params,
        })
        .then(function (res) {
          let response = res.data;
          console.log(response);
          axiosThis.pageData = response;
          axiosThis.songList = response.songs;
          if (response.singer.sex == 0) {
            axiosThis.singerSex = "女";
          } else if (response.singer.sex == 1) {
            axiosThis.singerSex = "男";
          } else if (response.singer.sex == 2) {
            axiosThis.singerSex = "乐队组合";
          }
          axiosThis.$root.routerLoading = false;
          // console.log(axiosThis.songList);
          // axiosThis.styles=response.songListInfo.style.split('-')
        })
        .catch(function (error) {
          axiosThis.$message.error("获取歌手信息失败");
          console.log(error);
          axiosThis.$root.routerLoading = false;
        });
    },
    openSongDetail(data) {
      console.log(data);
      this.$emit("openSongDetail", data);
    },
    collectSinger() {
      if (this.$root.userData.login_ed == false) {
        this.$message({ message: "请先登陆", type: "warning" });
      } else {
        let axiosThis = this;
        let data = Qs.stringify({
          userId: this.$root.userData.userId,
          singerId: this.musicianId,
          flag: this.pageData.singerCollectStatus,
        });
        this.$axios
          .post(`singer/followSinger`, data)
          .then((res) => {
            //请求成功
            console.log(axiosThis);
            console.log("res.data=>", res.data);
            let responseData = res.data;
            if (responseData.status == "success") {
              axiosThis.$message.success(responseData.msg+"！");
              axiosThis.getMusicianDetail();
            } else {
              axiosThis.$message.error(responseData.msg+"！");
            }
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("请求失败");
          });
      }
    },
  },
  watch: {
    // likeStatusClass(){
    //   if(this.pageData.singerCollectStatus==1){
    //     return "iconfont icon-aixin1"
    //   }else{
    //     return "iconfont icon-aixin"
    //   }
    // }
    songCollectChangeFlag() {
      this.getMusicianDetail();
    },
  },
  computed: {
    likeStatusClass() {
      if (this.pageData.singerCollectStatus == 1) {
        return "iconfont icon-aixin1";
      } else {
        return "iconfont icon-aixin";
      }
    },
    songCollectChangeFlag() {
      return this.$root.songCollectChangeFlag;
    },
  },
  created() {
    this.$root.routerLoading = true;
    this.musicianId = this.$route.params.musicianId;
    this.getMusicianDetail();
  },
  components: {
    //重要
    songList: songList,
    // commentArea: commentArea,
  },
};
</script>
