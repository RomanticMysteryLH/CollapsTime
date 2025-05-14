<template>
  <div class="content">
    <p style="font-size: 25px">所有歌手</p>
    <div style="margin-top: 20px">
      <p style="margin-bottom: 10px">分类：</p>
      <el-radio-group v-model="radio1" size="medium">
        <el-radio-button label="男歌手"></el-radio-button>
        <el-radio-button label="女歌手"></el-radio-button>
        <el-radio-button label="乐队组合"></el-radio-button>
      </el-radio-group>
      <el-button
        type="text"
        style="margin-left: 20px"
        size="small"
        @click="radio1 = ''"
        >取消分类</el-button
      >
    </div>
    <div style="margin-top: 20px; margin-bottom: 20px">
      <p style="margin-bottom: 10px; font-size: 14px">筛选：</p>
      <el-radio-group v-model="radio2" size="small">
        <el-radio-button
          v-for="i in 26"
          :key="i"
          :label="String.fromCharCode(i + 64)"
          style="margin-bottom: 5px"
        ></el-radio-button>
        <el-button
          type="text"
          style="margin-left: 20px"
          size="small"
          @click="radio2 = ''"
          >取消筛选</el-button
        >
      </el-radio-group>
    </div>
    <el-tag type="success" effect="plain">共{{ this.totalResult }}条</el-tag>
    <el-row style="padding: 20px 0px 0px 0px" :gutter="15">
      <!-- span决定大小 -->

      <el-col :lg="{ span: '4-8' }" v-for="item in nowPageList" :key="item.id">
        <!-- 弹出框必须在触发的对象上加slot="reference" -->
        <el-popover
          placement="bottom-start"
          width="350"
          trigger="hover"
          :content="item.introduction"
          :open-delay="400"
          :close-delay="50"
        >
          <p class="subscribeTile">性别：</p>
          <p v-if="item.sex == 1">男</p>
          <p v-else-if="item.sex == 0">女</p>
          <p v-else>五</p>
          <p class="subscribeTile">歌手简介：</p>
          <p>{{ item.introduction }}</p>
          <p class="subscribeTile">生日：</p>
          <p>{{ item.birth }}</p>
          <p class="subscribeTile">所在地：</p>
          <p>{{ item.location }}</p>

          <!-- 以上为弹窗内的内容 -->
          <el-card
            :body-style="{ padding: '0px' }"
            class="card"
            shadow="hover"
            style="cursor: pointer"
            slot="reference"
            @click.native="musicianSelect(item.id)"
          >
            <el-image
              :src="$RequestUrl + item.picture"
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
            <div
              style="
                padding: 2px 14px 14px 14px;
                height: 30px;
                overflow: hidden;
              "
            >
              <p
                style="
                  font-weight: 600;
                  font-size: 14px;
                  overflow: hidden;
                  height: 22px;
                  text-align: center;
                "
              >
                {{ item.name }}
              </p>
              <p
                style="
                  float: left;
                  color: rgb(200, 200, 200);
                  font-size: 12px;
                  line-hight: 20px;
                  height: 22px;
                "
              >
                艺人
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
.el-col-lg-4-8 {
  width: 20%;
}

.el-row {
  margin-bottom: 20px;
  /* 用flex布局加换行避免错位 */
  display: flex;
  flex-wrap: wrap;
}
.image {
  width: 100%;
  display: block;
  border-radius: 50%;
  box-shadow: 0px 0px 20px gray;
}
.myimage {
  width: 80%;
  height: 0;
  display: block;
  border-radius: 50%;
  margin: 10% auto;
  box-shadow: 0px 0px 20px gray;
  padding-bottom: 80%;
}
.myimage >>> .el-image__inner {
  vertical-align: top;
  position: absolute;
}
.card {
  margin-bottom: 20px;
  color: gray;
  /* background-color:rgb(254, 255, 228); */
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
      nowResult: 30,
      //当前总条数
      totalResult: 0,
      //当前页面所有歌单信息
      nowPageList: [],
      //   //是否被初始化，用来避免初次加载时create中getPage和watch中getPage重复执行
      //   isCreated: false,
      radio1: "",
      radio2: "",
    };
  },
  methods: {
    //获取未筛选数据

    async getPage() {
      this.nowPageList=[];
      this.$root.routerLoading = true;
      let axiosThis = this;
      let Params = {
        current: axiosThis.nowPage,
        size: axiosThis.nowResult,
      };
      let radio1 = axiosThis.radio1;
      let radio2 = axiosThis.radio2;
      let isFiltering = false;

      if (radio1 != "" && radio1 != null && radio1 != 0) {
        Params.category = radio1;
        isFiltering = true;
      }
      if (radio2 != "" && radio2 != null && radio2 != 0) {
        Params.initial = radio2;
        isFiltering = true;
      }
      console.log(isFiltering);
      //是否需要显示提示，如果是正在筛选（isFiltering==true）且不是在翻页（后面用监听区分）
      var messageFlag = false;
      await this.$axios
        .get("singer/singerPage", {
          params: Params,
        })
        .then(function (res) {
          //   axiosThis.$message.success("筛选成功");
          let response = res.data;
          // console.log(response);
          axiosThis.totalResult = response.total;
          axiosThis.nowPageList = response.dataList;
          console.log(axiosThis.totalResult);
          if (isFiltering) {
            messageFlag = true;
          }
          axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          axiosThis.$message.error("获取歌手信息失败");
          console.log(error);
          axiosThis.$root.routerLoading = false;
        });
      return messageFlag;
    },
    musicianSelect(id) {
      console.log(id);
      // console.log(e);
      this.$router.push({ path: `/detailMusician/${id}` });
    },
  },

  created: function () {
    //渲染之前的操作
    console.log("created");
    this.getPage();
  },
  watch: {
    //监听nowPage属性，变化就执行
    nowPage() {
      this.getPage();
      //page改变时回到顶部
      document.body.scrollTop = 0;
      document.documentElement.scrollTop = 0;
      this.$emit("goBackToTop");
    },
    radio1() {
      if (this.getPage()) {
        document.body.scrollTop = 0;
        document.documentElement.scrollTop = 0;
        this.$emit("goBackToTop");
        this.$message.success({ message: "筛选成功", duration: 1000 });
      }
    },
    radio2() {
      if (this.getPage()) {
        document.body.scrollTop = 0;
        document.documentElement.scrollTop = 0;
        this.$emit("goBackToTop");
        this.$message.success({ message: "筛选成功", duration: 1000 });
      }
    },
  },
  mounted: function () {
    console.log("mounted");
    // this.isCreated = true;
  },
};
</script>