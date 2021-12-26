<template>
  <!-- 单行歌曲展示（首页等） -->
  <div>
    <el-row style="padding: 20px 0px 0px 0px" :gutter="15">
      <!-- span决定大小 -->

      <el-col :lg="{ span: '4-8' }" v-for="item in musicianList" :key="item.id">
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
                height: 40px;
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
              <el-button
                v-if="item.collectState == 1"
                class="likeButton"
                :icon="
                  item.collectStatus == 0
                    ? 'iconfont icon-aixin'
                    : 'iconfont icon-aixin1'
                "
                type="text"
                size="medium"
                style="color: orange; float: right"
                @click.stop="deleteCollectedSinger(item)"
              ></el-button>
            </div>
          </el-card>
        </el-popover>
      </el-col>
      <el-col
        v-if="musicianList.length == 0"
        style="min-height: 240px; padding-top: 100px"
      >
        <p style="text-align: center; font-size: 14px; color: gray">暂无</p>
      </el-col>
    </el-row>
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
.likeButton >>> .iconfont {
  font-size: 12px;
  color: rgb(255, 78, 78);
}
.card {
  margin-bottom: 30px;
}
</style>
<script>
// 引入
import Qs from "qs";
export default {
  data: function () {
    return {};
  },
  methods: {
    musicianSelect(id) {
      console.log(id);
      // console.log(e);
      this.$router.push({ path: `/detailMusician/${id}` });
    },
    deleteCollectedSinger(item) {
      if (this.$root.userData.login_ed == false) {
        this.$message({ message: "请先登陆", type: "warning" });
      } else {
        let axiosThis = this;
        // console.log(item);
        let data = Qs.stringify({
          userId: this.$root.userData.userId,
          singerId: item.id,
          flag: item.collectState,
        });
        this.$axios
          .post(`singer/followSinger`, data)
          .then((res) => {
            //请求成功
            // console.log(axiosThis);
            // console.log("res.data=>", res.data);
            let responseData = res.data;
            if (responseData.status == "success") {
              axiosThis.$message.success(responseData.msg + "！");
              axiosThis.$emit("getFollowingSinger");
            } else {
              axiosThis.$message.error(responseData.msg + "！");
            }
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("请求失败");
          });
      }
    },
  },
  props: {
    musicianList: {
      type: Array,
    },
  },
};
</script>
