<template>
  <el-card class="box-card" style="margin-top: 30px">
    <el-table :data="songData" style="width: 100%">
      <el-table-column label="" width="60">
        <template slot="header" slot-scope="scope">
          <el-tooltip
            class="item"
            effect="dark"
            content="播放全部"
            :enterable="false"
            placement="top"
          >
            <el-button
              type="primary"
              icon="el-icon-caret-right"
              circle
              style="padding: 1px"
              size="mini"
              @click="playAllSong(scope.row)"
            ></el-button>
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          <el-button
            type="primary"
            icon="el-icon-caret-right"
            circle
            style="padding: 1px"
            size="mini"
            @click="playSong(scope.row)"
          ></el-button>
          <!-- <el-button size="mini" @click="collect(scope.$index, scope.row)"
              >编辑</el-button
            > -->
        </template>
      </el-table-column>
      <el-table-column
        label="歌曲标题"
        width="330"
        :show-overflow-tooltip="true"
      >
        <template slot-scope="scope">
          <el-link @click="showSongDetail(scope.row)">{{
            scope.row.name
          }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="duration" label="时长" width="150">
      </el-table-column>
      <el-table-column label="歌手" width="150">
        >
        <template slot-scope="scope">
          <el-link @click="goToMusician(scope.row)">{{
            scope.row.singerName
          }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="operator" label="操作">
        <template slot-scope="scope">
          <el-tooltip
            class="item"
            effect="light"
            content="收藏"
            placement="bottom"
            :enterable="false"
          >
            <el-button
              :icon="
                scope.row.collectStatus == 0
                  ? 'el-icon-star-off'
                  : 'el-icon-star-on'
              "
              circle
              type="text"
              size="medium"
              style="color: orange"
              @click="collectSong(scope.row)"
            ></el-button>
          </el-tooltip>
          <el-tooltip
            class="item"
            effect="light"
            content="下载"
            placement="bottom"
            :enterable="false"
          >
            <el-button
              icon="el-icon-download"
              circle
              type="text"
              size="medium"
              style="color: green"
              @click="downloadSong(scope.row)"
            ></el-button>
          </el-tooltip>
          <el-tooltip
            class="item"
            effect="light"
            content="添加到播放列表"
            placement="bottom"
            :enterable="false"
          >
            <el-button
              icon="el-icon-circle-plus-outline"
              circle
              type="text"
              size="medium"
              style="color: gray"
              @click="addToPlaylist(scope.row)"
            ></el-button>
          </el-tooltip>
          <!-- <el-button size="mini" @click="collect(scope.$index, scope.row)"
              >编辑</el-button
            > -->
        </template>
      </el-table-column>
    </el-table>
    <div class="block">
      <el-pagination
        small
        layout="prev, pager, next"
        :total="totalResult"
        :page-size="nowResult"
        :current-page.sync="nowPage"
        style="text-align: center"
      >
      </el-pagination>
    </div>
  </el-card>
</template>
<style scoped>
.block {
  margin-top: 20px;
}
</style>
<script>
import Qs from "qs";
export default {
  name: "songList",
  data: function () {
    return {
      //当前页
      nowPage: 1,
      totalPage: 100,
      //当前页条数
      nowResult: 24,
      //当前总条数
      totalResult: 0,
      //   musicianId: 0,
      songData: [],
    };
  },
  methods: {
    //点击后路由后退一步
    goBack() {
      console.log("goBack");
      this.$router.go(-1);
    },
    // openSong(row,column) {
    //   console.log(row,column);
    //   this.$emit('openSongDetail')
    // },
    showSongDetail(row) {
      console.log(row);
      this.$emit("openSongDetail", row.id);
    },
    goToMusician(row) {
      console.log(row);
      //待写
      let id = row.singerId;
      console.log(this.songData);
      this.$router.push({ path: `/detailMusician/${id}` }).catch((failure) => {
        if (String(failure).indexOf("NavigationDuplicated") >= 0) {
          this.$message("已在当前页面");
          // console.log(failure);
        } else {
          this.$message.error("未知错误");
        }
      });
    },
    downloadSong(row) {
      // console.log(row);
      // let axiosThis = this;
      this.$axios
        .get("user/downloadSong", {
          params: {
            songId: row.id,
          },
          responseType: "blob",
        })
        .then((res) => {
          let data = res.data;
          let url = window.URL.createObjectURL(new Blob([data]));
          let link = document.createElement("a");
          link.style.display = "none";
          link.href = url;
          link.setAttribute("download", row.name);

          document.body.appendChild(link);
          link.click();
          // window.location.href=res;
          console.log(res);
        });
    },
    //歌词还没加
    addToPlaylist(row) {
      // console.log(row);
      // console.log(this.$root.audio);
      //如果当前状态是未加载音频，则把audio中唯一一项删除
      if (
        (this.$root.audio[0].url == "" || this.$root.audio[0].id == "NaN") &&
        this.$root.audio.length == 1
      ) {
        this.$root.audio.splice(0, 1);
      }
      let newAudio = {
        id: row.id,
        name: row.name,
        artist: row.singerName,
        url: this.$RequestUrl + row.url,
        cover: this.$RequestUrl + row.cover,
        lrc: this.getLyrics(row.id),
      };
      let index = this.$root.audio.findIndex((n) => n.url == newAudio.url);
      if (index >= 0) {
        this.$root.audio.splice(index, 1);
        this.$message.warning("已在队列中，将重新添加到队尾");
      } else {
        this.$message.success("添加成功！");
      }
      this.$root.audio.push(newAudio);
      console.log(this.$root.audio);
    },
    playSong(row) {
      console.log(this.$root.audio);
      this.$root.audio = [];
      let newAudio = {
        id: row.id,
        name: row.name,
        artist: row.singerName,
        url: this.$RequestUrl + row.url,
        cover: this.$RequestUrl + row.cover,
        // lrc: this.songInfo.lyricWithTime,
        lrc: this.getLyrics(row.id),
      };
      this.$root.audio = [newAudio];
      console.log(this.$root.audio);
      this.$root.startPlay();
    },
    playAllSong() {
      this.$root.audio = [];
      for (let item in this.songData) {
        let newAudio = {
          id: this.songData[item].id,
          name: this.songData[item].name,
          artist: this.songData[item].singerName,
          url: this.$RequestUrl + this.songData[item].url,
          cover: this.$RequestUrl + this.songData[item].cover,
          // lrc: this.songData[item].lyricWithTime,
          lrc: this.getLyrics(this.songData[item].id),
        };
        this.$root.audio.push(newAudio);
      }
      this.$message.success("添加成功！");
      this.$root.startPlay();
    },
    collectSong(row) {
      console.log(row);
      if (this.$root.userData.login_ed == false) {
        this.$message({ message: "请先登陆", type: "warning" });
      } else {
        let axiosThis = this;
        let data = Qs.stringify({
          userId: this.$root.userData.userId,
          songId: row.id,
          flag: row.collectStatus,
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
              // axiosThis.$emit("getPlaylistDetail")
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
    async getLyrics(id) {
      let newLyrics = "";
      // let axiosThis = this;
      let data = Qs.stringify({
        songId: id,
      });
      await this.$axios
        .post(`song/getLyric`, data)
        .then((res) => {
          //请求成功
          // console.log(axiosThis);
          // console.log("res.data=>", res.data);
          newLyrics=res.data.lyric;
        })
        .catch((err) => {
          console.log(err);
          this.$message.error("请求失败，无法发送");
        });
      return newLyrics;
    },
  },
  props: {
    listProp: {
      type: [Array],
    },
  },
  watch: {
    listProp() {
      console.log(this.listProp);
      this.songData = this.listProp;
    },
  },
  mounted() {
    console.log(this.listProp);
  },

  //   created() {
  //     // this.musicianId = this.$route.params.musicianId;
  //   },
};
</script>