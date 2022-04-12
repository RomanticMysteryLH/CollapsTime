<template>
  <div class="content">
    <p style="font-size:20px;margin-bottom:30px">"{{ this.searchText }}" 的搜索结果</p>
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="歌曲" name="song">
        <el-tag type="success" effect="plain"
          >共{{ this.songPage.totalResult }}条</el-tag
        >
        <songList :listProp="songs" @openSongDetail="openSongDetail"></songList>
        <div class="block">
          <el-pagination
            small
            layout="prev, pager, next"
            :total="songPage.totalResult"
            :page-size="songPage.nowResult"
            :current-page.sync="songPage.nowPage"
            style="text-align: center; margin-top: 30px"
          >
          </el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane label="歌单" name="songList" :disabled="searchMode==1">
        <el-tag type="success" effect="plain"
          >共{{ this.playlistPage.totalResult }}条</el-tag
        >
        <oneColPlaylists :playlists="playlists"></oneColPlaylists>
        <el-pagination
          small
          layout="prev, pager, next"
          :total="playlistPage.totalResult"
          :page-size="playlistPage.nowResult"
          :current-page.sync="playlistPage.nowPage"
          style="text-align: center; margin-top: 30px"
        >
        </el-pagination>
      </el-tab-pane>
      <el-tab-pane label="歌手" name="singer" :disabled="searchMode==1">
        <el-tag type="success" effect="plain"
          >共{{ this.singerPage.totalResult }}条</el-tag
        >
        <oneColMusicianList :musicianList="singers"></oneColMusicianList>
        <el-pagination
          small
          layout="prev, pager, next"
          :total="playlistPage.totalResult"
          :page-size="playlistPage.nowResult"
          :current-page.sync="singerPage.nowPage"
          style="text-align: center; margin-top: 30px"
        >
        </el-pagination>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import Qs from "qs";
import songList from "@/components/songList.vue";
import oneColPlaylists from "@/components/oneColPlaylists.vue";
import oneColMusicianList from "@/components/oneColMusicianList.vue";
export default {
  data() {
    return {
      searchText: "",
      activeName: "",
      searchMode:0,
      songPage: {
        //当前总条数
        totalResult: 0,
        nowPage: 1,
        nowResult: 25,
        totalPage: 50,
      },
      playlistPage: {
        //当前总条数
        totalResult: 0,
        nowPage: 1,
        nowResult: 20,
        totalPage: 50,
      },
      singerPage: {
        //当前总条数
        totalResult: 0,
        nowPage: 1,
        nowResult: 20,
        totalPage: 50,
      },
      songs: [],
      singers: [],
      playlists: [],
    };
  },
  methods: {
    openSongDetail(data) {
    //   console.log(data);
      this.$emit("openSongDetail", data);
    },
    getPage() {
      let axiosThis = this;
      let dataObj = {
        key: this.searchText,
        type: this.activeName,
        current: this.songPage.nowPage,
        size: this.songPage.nowResult,
        userId: this.$root.userData.userId,
      };
      let data = Qs.stringify(dataObj);
      let requestUrl='/user/searchInfo'
      if(this.searchMode==1){
        requestUrl='/user/similarSearchInfo'
      }
      this.$axios
        .post(requestUrl, data)
        .then((res) => {
          // console.log(res);
          //请求成功
          let response = res.data;
          if (dataObj.type == "song") {
            axiosThis.songs = response.dataList;
            axiosThis.songPage.totalResult = response.total;
          } else if (dataObj.type == "singer") {
            axiosThis.singers = response.dataList;
            axiosThis.singerPage.totalResult = response.total;
          } else if (dataObj.type == "songList") {
            axiosThis.playlists = response.dataList;
            axiosThis.playlistPage.totalResult = response.total;
            // console.log(axiosThis.playlists);
          }
        })
        .catch((err) => {
          axiosThis.$message.error("获取失败，未知错误");
          console.log(err);
        });
    },
  },
  created() {
    this.searchText = this.$route.params.searchText;
    this.searchMode=this.$route.params.searchMode;
    this.activeName = "song";
  },
  components: {
    songList: songList,
    oneColPlaylists: oneColPlaylists,
    oneColMusicianList: oneColMusicianList,
  },
  watch: {
    activeName: {
      handler() {
          this.getPage()
      },
    },
    songCollectChangeFlag() {
      this.getPage();
    },
    'songPage.nowPage':{
        handler() {
            this.getPage();
        }
    },
    'singerPage.nowPage':{
        handler() {
            this.getPage();
        }
    },
    'playlistPage.nowPage':{
        handler() {
            this.getPage();
        }
    }
  },
  computed: {
    songCollectChangeFlag() {
      return this.$root.songCollectChangeFlag;
    },
  },
};
</script>
