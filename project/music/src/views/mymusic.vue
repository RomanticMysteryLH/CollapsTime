<template>
  <div class="content">
    <div class="musicReport">
      <p>
        <span style="font-weight: 700; font-size: 20px">听歌报告</span>
      </p>
      <el-divider></el-divider>
      <el-row :gutter="30">
        <el-col :span="12">
          <div class="recentSongs board">
            <p style="font-weight: 700; font-size: 18px; margin-bottom: 10px">
              歌曲排行
            </p>
            <template v-for="item in recentListenSong.songs">
              <!-- <span :key="item.id + item.name"
                >{{ item.name }} {{ item.times }}次</span
              > -->
              <el-progress
                v-if="!isNaN(item.playCount / recentListenSong.total)"
                :key="item.songId"
                :percentage="(item.playCount / recentListenSong.total) * 100"
                :color="customColorMethod"
                :format="showPercentage1(item)"
                :text-inside="true"
                :stroke-width="20"
                @click.native="openSongDetail(item.songId)"
                style="margin-bottom: 10px"
              ></el-progress>
            </template>
            <el-progress
              v-for="i in 5 - recentListenSong.songs.length"
              :key="i + 'i'"
              :percentage="0"
              :color="customColorMethod"
              :format="showPercentage1(0)"
              :text-inside="true"
              :stroke-width="20"
              style="margin-bottom: 10px"
            ></el-progress>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="recentSongs board">
            <p style="font-weight: 700; font-size: 18px; margin-bottom: 10px">
              歌手排行
            </p>
            <template v-for="item in recentListenSinger.singers">
              <!-- <span :key="item.id + item.name"
                >{{ item.name }} {{ item.times }}次</span
              > -->
              <el-progress
                v-if="!isNaN(item.times / recentListenSinger.total)"
                :key="item.id"
                :percentage="(item.times / recentListenSinger.total) * 100"
                :color="customColorMethod"
                :format="showPercentage(item)"
                :text-inside="true"
                :stroke-width="20"
                style="margin-bottom: 10px"
                @click.native="musicianSelect(item.singerId)"
              ></el-progress>
            </template>
            <el-progress
              v-for="i in 5 - recentListenSinger.singers.length"
              :key="i + 'i'"
              :percentage="0"
              :color="customColorMethod"
              :format="showPercentage(0)"
              :text-inside="true"
              :stroke-width="20"
              style="margin-bottom: 10px"
            ></el-progress>
          </div>
        </el-col>
      </el-row>
      <!-- <div class="legend">
        <p style="display:flex;align-items:center">
          <span
            style="
              background-color: red;
              width: 10px;
              height: 10px;
              border-radius: 50%;
              display: block;
            "
          ></span>
          <span>次数 > 50%</span>
        </p>
      </div> -->
    </div>
    <div class="myCollect" style="margin-top: 20px">
      <p>
        <span style="font-weight: 700; font-size: 20px">我的收藏</span>
      </p>
      <el-divider></el-divider>
      <div class="myCollectSong">
        <p>
          <span style="font-weight: 700">喜欢的歌曲</span
          ><el-link
            style="float: right"
            type="primary"
            :underline="false"
            @click="goToCollectedSingle"
            >查看全部<i class="el-icon-arrow-right"></i
          ></el-link>
        </p>
        <oneColSongList
          :songs="collectedSongs"
          @getCollectedSong="getCollectedSong"
          @openSongDetail="openSongDetail"
        ></oneColSongList>
      </div>
      <div class="myCollectPlaylist" style="margin-top: 20px">
        <p>
          <span style="font-weight: 700">收藏的歌单</span
          ><el-link
            style="float: right"
            type="primary"
            :underline="false"
            @click="goToCollectedPlaylist"
            >查看全部<i class="el-icon-arrow-right"></i
          ></el-link>
        </p>
        <oneColPlaylists
          :playlists="collectedPlaylists"
          @getCollectedPlaylist="getCollectedPlaylist"
        ></oneColPlaylists>
      </div>
      <div class="myCollectPlaylist" style="margin-top: 20px">
        <p>
          <span style="font-weight: 700">关注的歌手</span
          ><el-link
            style="float: right"
            type="primary"
            :underline="false"
            @click="goToFollowingSinger"
            >查看全部<i class="el-icon-arrow-right"></i
          ></el-link>
        </p>
        <oneColMusicianList
          :musicianList="followingMusicianList"
          @getFollowingSinger="getFollowingSinger"
        ></oneColMusicianList>
      </div>
    </div>
  </div>
</template>
<style scoped>
.board {
  border: 1px solid gainsboro;
  border-radius: 5px;
  padding: 20px;
  box-shadow: 2px 2px 8px 1px gainsboro;
}
.board >>> .el-progress-bar__outer {
  height: 6px;
  border-radius: 100px;
  background-color: #b3dee0;
  overflow: hidden;
  position: relative;
  vertical-align: middle;
  cursor: pointer;
}
</style>
<script>
import oneColPlaylists from "@/components/oneColPlaylists";
import oneColSongList from "@/components/oneColSongList";
import oneColMusicianList from "@/components/oneColMusicianList";
import Qs from "qs";
export default {
  data: function () {
    return {
      collectedSongs: [
        {
          id: 1,
        },
        {
          id: 2,
        },
        {
          id: 3,
        },
        {
          id: 4,
        },
        {
          id: 5,
        },
        {
          id: 6,
        },
      ],
      collectedPlaylists: [
        {
          id: 1,
        },
        {
          id: 2,
        },
        {
          id: 3,
        },
        {
          id: 4,
        },
        {
          id: 5,
        },
      ],
      followingMusicianList: [
        {
          id: 1,
        },
        {
          id: 2,
        },
        {
          id: 3,
        },
        {
          id: 4,
        },
        {
          id: 5,
        },
      ],
      recentListenSong: {
        total: 0,
        songs: [
          {
            songId: 0,
            name: "",
            playCount: 0,
          },
        ],
      },
      recentListenSinger: {
        total: 0,
        singers: [
          {
            id: 0,
            name: "",
            times: 0,
          },
        ],
        // total: 72,
        // singers: [
        //   {
        //     id: 1,
        //     name: "林俊杰",
        //     times: 33,
        //   },
        //   {
        //     id: 2,
        //     name: "王力宏",
        //     times: 23,
        //   },
        //   {
        //     id: 3,
        //     name: "陶喆",
        //     times: 10,
        //   },
        //   {
        //     id: 4,
        //     name: "周杰伦",
        //     times: 5,
        //   },
        //   {
        //     id: 5,
        //     name: "椎名林檎",
        //     times: 1,
        //   },
        // ],
      },
    };
  },
  methods: {
    customColorMethod(percentage) {
      if (percentage < 5) {
        return "#909399";
      } else if (percentage < 10) {
        return "#6f7ad3";
      } else if (percentage < 20) {
        return "#e6a23c";
      } else if (percentage < 40) {
        return "#67c23a";
      } else {
        return "#F56C6C";
      }
    },
    showPercentage(item) {
      // console.log(item);
      //return是个函数
      return () => {
        if (item == 0) {
          return "暂无";
        }
        return item.singerName + "\n" + item.times + "次";
      };
    },
    showPercentage1(item) {
      // console.log(item);
      //return是个函数
      return () => {
        if (item == 0) {
          return "暂无";
        }
        return item.name + "\n" + item.playCount + "次";
      };
    },
    openSongDetail(id) {
      this.$emit("openSongDetail", id);
    },
    musicianSelect(id) {
      console.log(id);
      // console.log(e);
      this.$router.push({ path: `/detailMusician/${id}` });
    },

    getCollectedSong() {
      this.$root.routerLoading = true;
      let axiosThis = this;
      let data = Qs.stringify({
        current: 1,
        size: 5,
        userId: this.$root.userData.userId,
      });
      this.$axios
        .post(`song/getUserCollectSong`, data)
        .then((res) => {
          let response = res.data;
          // console.log(response);
          axiosThis.collectedSongs = response.dataList;
          // console.log(axiosThis.totalResult);
          axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          axiosThis.$message.error("获取歌曲信息失败");
          console.log(error);
          axiosThis.$root.routerLoading = false;
        });
    },
    getCollectedPlaylist() {
      this.$root.routerLoading = true;
      let axiosThis = this;
      let data = Qs.stringify({
        current: 1,
        size: 5,
        userId: this.$root.userData.userId,
      });
      this.$axios
        .post(`songList//getUserCollectSongList`, data)
        .then(function (res) {
          let response = res.data;
          // console.log(response);
          axiosThis.collectedPlaylists = response.dataList;
          // console.log(axiosThis.totalResult);
          axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          axiosThis.$message.error("获取歌单信息失败");
          console.log(error);
          axiosThis.$root.routerLoading = false;
        });
    },
    getFollowingSinger() {
      let axiosThis = this;
      let data = Qs.stringify({
        current: 1,
        size: 5,
        userId: this.$root.userData.userId,
      });
      this.$axios
        .post(`singer/getUserCollectSinger`, data)
        .then(function (res) {
          //   axiosThis.$message.success("筛选成功");
          let response = res.data;
          // console.log(response);
          axiosThis.followingMusicianList = response.dataList;
          console.log(axiosThis.totalResult);
          // axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          console.log(error);
          axiosThis.$message.error("获取歌手信息失败");
        });
    },
    getSongRank() {
      let axiosThis = this;
      let data = Qs.stringify({
        userId: this.$root.userData.userId,
      });
      this.$axios
        .post(`song/getSongRank`, data)
        .then((res) => {
          // console.log(res);
          //请求成功
          let response = res.data;
          axiosThis.recentListenSong = response;
        })
        .catch((err) => {
          axiosThis.$message.error("获取歌曲排行失败，未知错误");
          console.log(err);
        });
    },
    getSingerRank() {
      let axiosThis = this;
      let data = Qs.stringify({
        userId: this.$root.userData.userId,
      });
      this.$axios
        .post(`singer/getSingerRank`, data)
        .then((res) => {
          // console.log(res);
          //请求成功
          let response = res.data;
          axiosThis.recentListenSinger = response;
        })
        .catch((err) => {
          axiosThis.$message.error("获取歌曲排行失败，未知错误");
          console.log(err);
        });
    },
    goToCollectedSingle() {
      this.$router.push("/myMusic/collectedSong");
    },
    goToCollectedPlaylist() {
      this.$router.push("/myMusic/collectedPlaylist");
    },
    goToFollowingSinger() {
      this.$router.push("/myMusic/followingSinger");
    },
  },
  components: {
    oneColPlaylists: oneColPlaylists,
    oneColSongList: oneColSongList,
    oneColMusicianList: oneColMusicianList,
  },
  created() {
    if(this.$root.userData.login_ed==false){
      this.$router.push('/')
    }
    this.getSongRank();
    this.getSingerRank();
    this.getCollectedSong();
    this.getCollectedPlaylist();
    this.getFollowingSinger();
  },
};
</script>
