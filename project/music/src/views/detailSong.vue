<template>
  <div class="songContent">
    <el-row :gutter="40">
      <el-col :span="8" style="text-align: center">
        <el-image
          :src="this.$RequestUrl + songInfo.picture"
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
        <div class="songInfo">时长: {{ songInfo.duration }}</div>
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
          <!-- <div style="font-size:20px;margin:10px auto 10px 10px">评论区</div> -->
          <commentArea
            :listProp="this.pageData.commentUsers"
            :songId="this.pageData.songInfo.id"
            @freshComments="freshComments"
          ></commentArea>
          <el-button
            slot="reference"
            type="primary"
            plain
            style="margin: 20px auto"
            @click="commentPlaylist()"
            ><i class="el-icon-s-comment"></i> 查看评论 ({{
              pageData.commentCount
            }})</el-button
          >
        </el-popover>
      </el-col>
      <el-col :span="16">
        <div
          style="
            font-size: 20px;
            font-weight: 700;
            display: flex;
            flex-direction: row;
            align-items: center;
            margin-bottom: 15px;
          "
        >
          <el-tag type="warning">单曲</el-tag><span>{{ songInfo.name }}</span>
        </div>
        <div class="songInfo">
          <span>歌手：{{ songInfo.singerName }}</span
          ><span style="margin-left: 40px"
            >专辑：{{ songInfo.introduction }}</span
          >
        </div>
        <!-- <div class="songInfo"></div> -->
        <div style="margin-top: 15px">
          <el-button type="primary" @click="playSong"
            ><i class="el-icon-caret-right"></i>立即播放</el-button
          >
          <el-button type="warning" @click.native="collectSong"
            ><i :class="collectStatusClass"></i>收藏 ({{
              pageData.collectCount
            }})</el-button
          >
          <el-button type="success" @click.native="downloadSong"
            ><i class="el-icon-download"></i>下载</el-button
          >
          <el-button @click.native="addToPlaylist"
            ><i class="el-icon-circle-plus-outline"></i
            >添加到播放列表</el-button
          >
        </div>
        <div class="lyricsArea">
          <el-scrollbar style="height: 100%">
            <p
              v-for="item in songInfo.lyric.split('\n')"
              :key="item.key"
              class="lyricsText"
            >
              {{ item }}
            </p>
          </el-scrollbar>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<style scoped>
.image {
  width: 100%;
  display: block;
  /* box-shadow: 0px 0px 20px gray; */
  border-radius: 3px;
}
.myimage {
  width: 100%;
  display: block;
  border-radius: 3px;
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
.songContent {
  margin: 0 auto; /*中间没有逗号*/
  width: 900px;
  padding: 20px 50px 0px 50px;
}
.lyricsArea {
  height: 220px;
  width: 100%;
  overflow: auto;
  margin-top: 20px;
  border: 1px solid gainsboro;
  padding: 20px;
  border-radius: 10px;
}
.songInfo {
  margin-top: 5px;
}
.lyricsText {
  text-align: center;
  line-height: 25px;
}
/* 样式穿透 */
.lyricsArea >>> .el-scrollbar__wrap {
  overflow-x: hidden;
}
</style>
<script>
import commentArea from "@/components/commentArea.vue";
import Qs from "qs";
export default {
  data: function () {
    return {
      //songDetail展示的歌曲的id
      //   nowSongId: 0,
      commentVisible: false,
      pageData: {
        collectCount: 0,
        commentCount: 0,
        songInfo: {
          id: 0,
          singerId: 1,
          singerName: "",
          name: "",
          introduction: "",
          lyric: "",
          // picture: "",
          url: "",
        },
        commentUsers: [],
        collectStatus: 0,
      },
      songInfo: {
        id: 0,
        singerId: 1,
        name: "",
        introduction: "",
        lyric: "",
        // picture: "",
        url: "",
      },
    };
  },
  props: ["songId"],
  computed: {
    nowSongId:{
      get(){
        return this.songId;
      },
      set(newVal){
        return newVal;
      }
    },
    // nowSongId() {
    //   return this.songId;
    // },
    // collectStyle() {
    //   if (this.pageData.collectStatus == 1) {
    //     return "el-icon-star-on";
    //   } else {
    //     return "el-icon-star-off";
    //   }
    // },
    collectStatusClass() {
      if (this.pageData.collectStatus == 1) {
        return "el-icon-star-on";
      } else {
        return "el-icon-star-off";
      }
    },
  },
  methods: {
    getSongDetail() {
      // this.$root.routerLoading = true;
      let axiosThis = this;
      let params = {
        songId: this.nowSongId,
      };
      if (axiosThis.$root.userData.login_ed == true) {
        params.userId = axiosThis.$root.userData.userId;
      }
      this.$axios
        .get("song/getSongDetail", {
          params: params,
        })
        .then(function (res) {
          let response = res.data;
          console.log(response);
          axiosThis.pageData = response;
          // console.log(axiosThis.pageData.commentCount);
          axiosThis.songInfo = response.songInfo;
          // axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          axiosThis.$message.error("获取歌曲信息失败");
          console.log(error);
          // axiosThis.$root.routerLoading = false;
        });
    },
    downloadSong() {
      let axiosThis = this;
      this.$axios
        .get("user/downloadSong", {
          params: {
            songId: axiosThis.songInfo.id,
          },
          responseType: "blob",
          // headers: {
          //   "Content-type": "audio/mpeg",
          // },
        })
        .then((res) => {
          let blob = new Blob([res.data], {
            type: "audio/mpeg",
          }); // 2.获取请求返回的response对象中的blob 设置文件类型，这里以excel为例
          let url = window.URL.createObjectURL(blob); // 3.创建一个临时的url指向blob对象
          // 4.创建url之后可以模拟对此文件对象的一系列操作，例如：预览、下载
          let a = document.createElement("a");
          a.href = url;
          a.download = this.pageData.songInfo.name + ".mp3";
          a.click();
          // 5.释放这个临时的对象url
          window.URL.revokeObjectURL(url);
          axiosThis.$message.success("已开始下载，请稍等");
        });
    },
    playSong() {
      console.log(this.$root.audio);
      let newAudio = {
        id: this.songInfo.id,
        name: this.songInfo.name,
        artist: this.songInfo.singerName,
        url: this.$RequestUrl + this.songInfo.url,
        cover: this.$RequestUrl + this.songInfo.picture,
        lrc: this.songInfo.lyricWithTime,
      };
      this.$root.audio = [newAudio];
      console.log(this.$root.audio);
      this.$root.startPlay();
    },
    addToPlaylist() {
      if (this.$root.audio[0].id == null && this.$root.audio.length == 1) {
        this.$root.audio.splice(0, 1);
      }
      let newAudio = {
        id: this.songInfo.id,
        name: this.songInfo.name,
        artist: this.songInfo.singerName,
        url: this.$RequestUrl + this.songInfo.url,
        cover: this.$RequestUrl + this.songInfo.picture,
        lrc: this.songInfo.lyricWithTime,
      };
      let index = this.$root.audio.findIndex((n) => n.url == newAudio.url);
      if (index >= 0) {
        this.$root.audio.splice(index, 1);
        this.$message.warning("已在队列中，将重新添加到队尾");
      } else {
        this.$message.success("添加成功！");
      }
      this.$root.audio.push(newAudio);
      //   this.$root.startPlay();
    },
    collectSong() {
      if (this.$root.userData.login_ed == false) {
        this.$message({ message: "请先登陆", type: "warning" });
      } else {
        let axiosThis = this;
        let data = Qs.stringify({
          userId: this.$root.userData.userId,
          songId: this.pageData.songInfo.id,
          flag: this.pageData.collectStatus,
        });
        this.$axios
          .post(`song/collectSong`, data)
          .then((res) => {
            //请求成功
            console.log(axiosThis);
            console.log("res.data=>", res.data);
            let responseData = res.data;
            if (responseData.status == "success") {
              this.$root.songCollectChangeFlag =
                !this.$root.songCollectChangeFlag;
              axiosThis.$message.success(responseData.msg + "！");
              axiosThis.getSongDetail();
            } else {
              axiosThis.$message.error(responseData.msg + "！");
            }
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("请求失败！");
          });
      }
    },
    freshComments(value) {
      this.pageData.commentUsers = value;
    },
  },
  watch: {
    immediate: true,
    nowSongId: {
      immediate: true,
      handler: function () {
        this.nowSongId = this.songId;
        console.log(this.nowSongId);
        this.getSongDetail();
      },
    },
  },
  components: {
    commentArea: commentArea,
  },
  created() {
    // this.$root.routerLoading = true;
  },
};
</script>