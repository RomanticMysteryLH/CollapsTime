<template>
  <!-- 热门单曲 -->
  <div class="content">

    <el-page-header @back="goBack()" content="热门歌单"> </el-page-header>
    <el-divider></el-divider>
        <el-tag type="success" effect="plain">共{{ this.totalResult }}条</el-tag>
    <el-row style="padding: 20px 0px 0px 0px" :gutter="20">
      <!-- span决定大小 -->

      <el-col :span="6" v-for="item in nowPageList" :key="item.id">
        <!-- 弹出框必须在触发的对象上加slot="reference" -->
        <!-- 加入open-delay避免快速滚动时弹出影响滚动，也避免了鼠标在卡片间移动时鼠标被弹框遮挡 -->
        <el-popover
          placement="bottom-start"
          width="350"
          trigger="hover"
          :content="item.introduction"
          :open-delay="400"
          :close-delay="50"
        >
          <p class="subscribeTile">歌单：</p>
          <p>{{ item.title }}</p>
          <p class="subscribeTile">歌单介绍：</p>
          <p>{{ item.introduction }}</p>
          <p class="subscribeTile">创建时间：</p>
          <p>{{ item.createTime }}</p>
          <!-- 以上为弹窗内的内容 -->
          <el-card
            :body-style="{ padding: '0px' }"
            class="card"
            shadow="hover"
            style="cursor: pointer"
            slot="reference"
            @click.native="playListSelect(item.id)"
          >
            <el-image :src="$RequestUrl + item.picture" class="myimage" lazy>
              <!-- 加载前占位 -->
              <div slot="placeholder">
                <img src="@/assets/default/loading1.gif" class="image" />
              </div>
              <!-- 加载后占位 -->
              <div slot="error">
                <img
                  src="@/assets/default/defaultPlayList.jpg"
                  slot="error"
                  class="image"
                />
              </div>
            </el-image>
            <div style="padding: 14px; height: 50px; overflow: hidden">
              <p
                style="
                  font-weight: 600;
                  font-size: 14px;
                  overflow: hidden;
                  height: 40px;
                "
              >
                {{ item.title }}
              </p>
              <p style="font-size:12px;color:gray;position;bottom:5px">
                风格：{{ item.style }}
              </p>
            </div>
          </el-card>
        </el-popover>
      </el-col>
    </el-row>
    <!-- <div class="block">
      <el-pagination
        layout="prev, pager, next"
        :total="totalResult"
        :page-size="nowResult"
        :current-page.sync="nowPage"
        style="text-align: center"
      >
      </el-pagination>
    </div> -->
  </div>
</template>
<style scoped>
.el-row {
  margin-bottom: 20px;
  /* 用flex布局加换行避免错位 */
  display: flex;
  flex-wrap: wrap;
}
.myimage {
  /* 自适应正方形 */
  width: 100%;
  display: block;
  height: 0;
  padding-bottom: 100%;
}
.card {
  margin-bottom: 40px;
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
.myimage >>> .el-image__inner {
  position: absolute;
}
.image {
  width: 100%;
  position: absolute;
}
.subscribeTile {
  font-size: 16px;
  color: gray;
  font-weight: bold;
  margin-top: 6px;
}
</style>
<script>
export default {
  data: function () {
    return {
      //当前页
      nowPage: 1,
      //当前行长度
      colLength: 4,
      //总共页数
      totalPage: 100,
      //当前页条数
      nowResult: 24,
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
    getPage() {
      this.$root.routerLoading = true;
      let axiosThis = this;
      this.$axios
        .get("songList/getHotSongList", {
          params: {
            current: axiosThis.nowPage,
            size: axiosThis.nowResult,
          },
        })
        .then(function (res) {
          let response = res.data;
          // console.log(response);
          axiosThis.totalResult = response.total;
          axiosThis.nowPageList = response.dataList;
          // console.log(axiosThis.totalResult);
          axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          axiosThis.$message.error("获取歌单信息失败");
          console.log(error);
          axiosThis.$root.routerLoading = false;
        });
    },
    playListSelect(itemId) {
      // console.log(itemId);
      // console.log(e);
      this.$router.push({ path: `/detailPlaylist/${itemId}` });
    },
  },
  watch: {
    nowPage() {
      this.getPage();
      //page改变时回到顶部
      document.body.scrollTop = 0;
      document.documentElement.scrollTop = 0;
      this.$emit("goBackToTop");
    },
  },
  created: function () {
    //渲染之前的操作
    this.getPage();
  },
  //   mounted: {},
};
</script>
