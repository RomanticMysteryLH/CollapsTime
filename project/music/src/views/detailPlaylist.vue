<template>
  <!-- 歌单详情 -->
  <div class="content">
    <el-page-header @back="goBack()" content="歌单详情"> </el-page-header>
    <el-divider></el-divider>
    <!-- <p>{{ this.$route.params.playlistId }}</p> -->
    <el-row :gutter="60">
      <el-col :span="10">
        <div>
          <el-image
            :src="this.$RequestUrl + this.pageData.songListInfo.picture"
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
              <img src="@/assets/default/defaultPlayList.jpg" class="image" />
            </div>
          </el-image>
        </div>
      </el-col>
      <el-col :span="14">
        <p style="font-size: 40px" class="playlistTitle">
          {{ this.pageData.songListInfo.title }}
        </p>
        <el-row :gutter="30">
          <el-col :span="14">
            <el-divider content-position="center">创建者/时间</el-divider>
            <p>暂无 / {{ this.pageData.songListInfo.createTime }}</p>
            <el-divider content-position="center">风格流派</el-divider>
            <el-row :gutter="20"
              ><el-col v-for="item in this.styles" :span="6" :key="item"
                ><el-tag type="success">{{ item }}</el-tag></el-col
              ></el-row
            >
          </el-col>
          <el-col :span="10" style="padding: 10px">
            <div style="line-height: 60px; text-align: center">
              <el-button
                @click="collectPlaylist()"
                type="warning"
                :icon="collectStatusClass"
                circle
              ></el-button>
              收藏：{{ this.pageData.collectCount }}
            </div>
            <div style="line-height: 60px; text-align: center">
              <el-popover
                placement="right-start"
                width="400"
                :offset="-150"
                trigger="click"
                v-model="commentVisible"
              >
                <el-button
                  type="text"
                  style="float: right; margin-left: -15px"
                  @click="commentVisible = false"
                  ><i class="el-icon-close"></i
                ></el-button>
                <!-- <div style="font-size:20px;margin:10px auto 10px 10px">评论区</div> -->
                <commentArea
                  :listProp="this.pageData.commentUsers"
                  :playListId="this.pageData.songListInfo.id"
                  @freshComments="freshComments"
                ></commentArea>
                <el-button slot="reference" type="primary" plain circle
                  ><i class="el-icon-s-comment"></i
                ></el-button>
              </el-popover>
              评论：{{ this.pageData.commentCount }}
            </div>
          </el-col>
        </el-row>
        <el-divider content-position="center">简介</el-divider>
        <p style>
          {{ this.pageData.songListInfo.introduction }}
        </p>
      </el-col>
    </el-row>
    <!-- <songList
      :listProp="this.songList"
      @openSongDetail="openSongDetail"
      @getPlaylistDetail="getPlaylistDetail"
    ></songList> -->
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
}
.myimage {
  width: 100%;
  display: block;
  box-shadow: 0px 0px 20px gray;
  border-radius: 5px;
  margin-top: 15px;
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
.playlistTitle {
  font-family: HanYiZhongSongJian, HanyiZhongSongFan;
}
.el-divider__text {
  color: gray;
  font-size: 12px;
}
</style>
<script>
import songList from "@/components/songList.vue";
import commentArea from "@/components/commentArea.vue";
import Qs from "qs";
export default {
  data: function () {
    return {
      playListId: 0,
      commentVisible: false,
      // commentContent:"",
      //collectStatus转化成图标
      pageData: {
        collectCount: 0,
        commentCount: 0,
        songs: [],
        songListInfo: {
          id: 0,
          title: "",
          // picture: "",
          introduction: "",
          style: "",
          createTime: "",
        },
        commentUsers: [],
        collectStatus: 0,
      },
      //存储风格的数组
      styles: [],
      //存储歌曲列表
      songList: [],
    };
  },
  methods: {
    //点击后路由后退一步
    goBack() {
      console.log("goBack");
      this.$router.go(-1);
    },
    getPlaylistDetail() {
      let axiosThis = this;
      let params = {
        songListId: axiosThis.playListId,
      };
      if (axiosThis.$root.userData.login_ed == true) {
        params.userId = axiosThis.$root.userData.userId;
      }
      // this.$root.routerLoading = true;
      this.$axios
        .get("songList/getsongListDetail", {
          params: params,
        })
        .then(function (res) {
          let response = res.data;
          console.log(response);
          axiosThis.pageData = response;
          axiosThis.songList = response.songs;
          // console.log(axiosThis.songList);
          axiosThis.styles = response.songListInfo.style.split("-");
          axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          axiosThis.$message.error("获取歌单信息失败");
          console.log(error);
          axiosThis.$root.routerLoading = false;
        });
    },
    //收藏歌单
    collectPlaylist() {
      if (this.$root.userData.login_ed == false) {
        this.$message({ message: "请先登陆", type: "warning" });
      } else {
        let axiosThis = this;
        let data = Qs.stringify({
          userId: this.$root.userData.userId,
          songListId: this.playListId,
          flag:this.pageData.collectStatus,
        });
        this.$axios
          .post(`songList/collectSongList`, data)
          .then((res) => {
            //请求成功
            console.log(axiosThis);
            console.log("res.data=>", res.data);
            let responseData = res.data;
            if (responseData.status == "success") {
              axiosThis.$message.success("收藏歌单成功！");
              axiosThis.getPlaylistDetail();
            } else {
              axiosThis.$message.error("收藏歌单失败！");
            }
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("请求失败");
          });
      }
    },
    freshComments(value) {
      this.pageData.commentUsers = value;
    },
    openSongDetail(data) {
      this.$emit("openSongDetail", data);
    },
  },
  created() {
    this.$root.routerLoading = true;
    this.playListId = this.$route.params.playlistId;
    this.getPlaylistDetail();
    console.log(this.$root.songDetailVisible);
  },
  computed: {
    collectStatusClass() {
      if (this.pageData.collectStatus == 1) {
        return "el-icon-star-on";
      } else {
        return "el-icon-star-off";
      }
    },
    songCollectChangeFlag(){
      return this.$root.songCollectChangeFlag;
    }
  },
  watch:{
    songCollectChangeFlag(){
      this.getPlaylistDetail();
    }
  },
  components: {
    songList: songList,
    commentArea: commentArea,
  },
};
</script>
