<template>
  <div class="content">
    <p style="font-size: 25px">所有歌单</p>
    <el-tooltip
      content="选择需要查找的流派"
      placement="left-start"
      effect="dark"
      :enterable="false"
    >
      <el-tabs v-model="selectedGenre" type="card" style="margin: 20px auto">
        <!-- 标签包裹的内容是下方显示的内容 -->
        <el-tab-pane
          v-for="item in allGenres"
          :key="item"
          :label="item"
          :name="item"
        >
          {{ item
          }}<el-button
            type="text"
            style="margin-left: 20px"
            size="small"
            @click="selectedGenre = ''"
            >取消筛选</el-button
          >
        </el-tab-pane>
      </el-tabs>
    </el-tooltip>
    <el-tag type="success" effect="plain">共{{ this.totalResult }}条</el-tag>
    <!-- <p style="font-size:13px">共{{ this.totalResult }}条</p> -->
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
    <div class="block">
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
// import axios from "axios";
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
      //所有流派
      allGenres: [],
      //选择的流派
      selectedGenre: "",
      //是否被初始化，用来避免初次加载时create中getPage和watch中getPage重复执行
      isCreated: false,
    };
  },
  methods: {
    //获取未筛选数据
    getPage() {
      this.$root.routerLoading = true;
      let axiosThis = this;
      this.$axios
        .get("songList/songListPage", {
          params: {
            current: axiosThis.nowPage,
            size: axiosThis.nowResult,
          },
        })
        .then(function (res) {
          let response = res.data;
          console.log(response);
          axiosThis.totalResult = response.total;
          axiosThis.nowPageList = response.dataList;
          console.log(axiosThis.totalResult);
          axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          axiosThis.$message.error("获取歌单信息失败");
          console.log(error);
          axiosThis.$root.routerLoading = false;
        });
    },
    //获取流派数据
    getGenre() {
      let axiosThis = this;
      this.$axios
        .get("songList/querySongListAllStyle")
        .then(function (res) {
          let response = res.data;
          axiosThis.allGenres = response;
          console.log(response);
        })
        .catch(function (error) {
          console.log(error);
          axiosThis.$message.error("获取歌单分类信息失败");
        });
    },
    //筛选后的歌单
    async getGenrePage(type) {
      // type=0表示初始化，current=1，type==其它表示非初始化，current=nowPage
      let computCurrent = 0;
      let axiosThis = this;
      if (type == 0) {
        computCurrent = 1;
      } else {
        computCurrent = axiosThis.nowPage;
      }
      var success = false;
      await this.$axios
        .get("songList/songListPageByStyle", {
          params: {
            current: computCurrent,
            size: axiosThis.nowResult,
            style: axiosThis.selectedGenre,
          },
        })
        .then(function (res) {
          let response = res.data;
          console.log(response);
          axiosThis.totalResult = response.total;
          axiosThis.nowPageList = response.dataList;
          console.log(axiosThis.totalResult);
          success = true;
        })
        .catch(function (error) {
          axiosThis.$message.error("获取歌单信息失败");
          console.log(error);
        });
      //返回值用于判断是否成功
      console.log(success);
      return success;
    },
    // tabClick() {},
    playListSelect(itemId) {
      console.log(itemId);
      // console.log(e);
      this.$router.push({ path: `/detailPlaylist/${itemId}` });
    },
  },

  created: function () {
    //渲染之前的操作
    console.log("created");
    this.getPage();
    this.getGenre();
    console.log(this.selectedGenre);
  },
  watch: {
    //监听nowPage属性，变化就执行
    nowPage() {
      if (
        this.selectedGenre == 0 ||
        this.selectedGenre == "" ||
        this.selectedGenre == null
      ) {
        this.getPage();
      } else {
        //不是初始化，获取1
        this.getGenrePage(1);
      }

      //page改变时回到顶部
      document.body.scrollTop = 0;
      document.documentElement.scrollTop = 0;
      this.$emit("goBackToTop");
    },

    selectedGenre(newVal, oldVal) {
      console.log(newVal, oldVal);
      if (newVal == 0 || newVal == "") {
        console.log(newVal, oldVal, this.isCreated);
        //tab组件初始化时会被改为0
        if (this.isCreated == true) this.getPage();
        else return;
      } else {
        //流派变化，应为流派页初始化，传值为0

        if (this.getGenrePage(0)) {
          this.isCreated = true;
          this.$message.success({ message: "筛选成功", duration: 1000 });
        }
      }
    },
  },
  mounted: function () {
    console.log("mounted");
    // this.isCreated = true;
  },
};
</script>