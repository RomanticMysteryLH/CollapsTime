<template>
  <!-- 热门单曲 -->
  <div class="content">
    <el-page-header @back="goBack()" content="我收藏的单曲"> </el-page-header>
    <el-divider></el-divider>
    <songList
      :listProp="nowPageList"
      @openSongDetail="openSongDetail"
    ></songList>
    <div class="block">
      <el-pagination
        small
        layout="prev, pager, next"
        :total="totalResult"
        :page-size="nowResult"
        :current-page.sync="nowPage"
        style="text-align: center; margin-top: 30px"
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
      //当前页
      nowPage: 1,
      //当前行长度
      colLength: 4,
      //总共页数
      totalPage: 50,
      //当前页条数
      nowResult: 25,
      //当前总条数
      totalResult: 0,
      //当前页面所有歌单信息
      nowPageList: [],
      //是否被初始化，用来避免初次加载时create中getPage和watch中getPage重复执行
      isCreated: false,
    };
  },
  methods: {
    //点击后路由后退一步
    goBack() {
      console.log("goBack");
      this.$router.go(-1);
    },
    getHitSong() {
      this.$root.routerLoading = true;
      let axiosThis = this;
      let data = Qs.stringify({
        current: this.nowPage,
        size: this.nowResult,
        userId: this.$root.userData.userId,
      });
      this.$axios
        .post(`song/getUserCollectSong`, data)
        .then((res) => {
          let response = res.data;
          // console.log(response);
          axiosThis.totalResult = response.total;
          axiosThis.nowPageList = response.dataList;
          console.log(axiosThis.totalResult);
          axiosThis.$root.routerLoading = false;
        })
        .catch((err) => {
          console.log(err);
          axiosThis.$message.error("获取收藏歌曲失败");
          axiosThis.$root.routerLoading = false;
        });
    },
    openSongDetail(data) {
      // console.log(data);
      this.$emit("openSongDetail", data);
    },
  },

  mounted: {},
  components: {
    songList: songList,
  },
  watch: {
    nowPage() {
      this.getHitSong();
    },
  },
  created() {
    this.getHitSong();
  },
};
</script>
