<template>
  <div class="content">
    <p style="font-size: 25px">歌曲分类</p>
    <el-tooltip
      content="选择心情"
      placement="left-start"
      effect="dark"
      :enterable="false"
    >
      <el-tabs v-model="selectedMood" type="card" style="margin: 20px auto">
        <!-- 标签包裹的内容是下方显示的内容 -->
        <el-tab-pane
          v-for="item in allMoods"
          :key="item"
          :label="item"
          :name="item"
        >
          <!-- {{ item
          }}<el-button
            type="text"
            style="margin-left: 20px"
            size="small"
            @click="selectedMood = ''"
            >取消筛选</el-button
          > -->
        </el-tab-pane>
      </el-tabs>
    </el-tooltip>
    <!-- <el-tag type="success" effect="plain"
      >共{{ this.totalResult }}个结果</el-tag
    > -->
    <songList
      :listProp="this.pageData.songs"
      @openSongDetail="openSongDetail"
    ></songList>
    <div class="block" style="margin-top: 30px">
      <el-pagination
        layout="prev, pager, next"
        :total="totalResult"
        :page-size="nowResult"
        :current-page.sync="nowPage"
        style="text-align: center"
      >
      </el-pagination>
    </div>
  </div>
</template>
<script>
import songList from "@/components/songList.vue";
import Qs from "qs";
export default {
  data: function () {
    return {
      pageData: {
        songs: [],
      },
      selectedMood: "",
      allMoods: [],
      totalResult: 0,
      //当前页条数
      nowResult: 20,
      //当前页
      nowPage: 1,
      //总共页数
      totalPage: 100,
    };
  },
  components: {
    songList: songList,
  },
  methods: {
    getMoods() {
      let axiosThis = this;
      this.$axios
        .post("song/getAllEmotion")
        .then(function (res) {
          let response = res.data;
          axiosThis.allMoods = response;
          console.log(axiosThis.allMoods);
          axiosThis.selectedMood = axiosThis.allMoods[0];
        })
        .catch(function (error) {
          console.log(error);
          axiosThis.$message.error("获取心情信息失败");
        });
    },
    getPage() {
      let axiosThis = this;
      console.log(this.selectedMood);
      this.$root.routerLoading = true;
      let data = Qs.stringify({
        current: this.nowPage,
        size: this.nowResult,
        userId: this.$root.userData.userId,
        key: this.selectedMood,
      });
      console.log(data);
      this.$axios
        .post("song/getSongByEmotion", data)
        .then(function (res) {
          let response = res.data;
          axiosThis.pageData.songs = response.dataList;
          axiosThis.totalResult=response.total;
          console.log(axiosThis.pageData);
          axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          console.log(error);
          axiosThis.$root.routerLoading = false;
          axiosThis.$message.error("获取歌曲失败");
        });
    },
    async getMoodPage() {},
    openSongDetail(data) {
      // console.log(data);
      this.$emit("openSongDetail", data);
    },
  },
  created() {
    this.getMoods();
  },
  watch: {
    allMoods() {
      this.selectedMood = this.allMoods[0];
    },
    selectedMood() {
      this.getPage();
    },
    //监听nowPage属性，变化就执行
    nowPage() {
      if (
        this.selectedGenre == 0 ||
        this.selectedGenre == "" ||
        this.selectedGenre == null
      ) {
        this.getPage();
      }
      //page改变时回到顶部
      document.body.scrollTop = 0;
      document.documentElement.scrollTop = 0;
      this.$emit("goBackToTop");
    },
  },
};
</script>
