
<template>
  <!-- 单行歌曲展示（首页等） -->
  <div>
    <el-row style="padding: 20px 0px 0px 0px" :gutter="20">
      <el-col :lg="{ span: '4-8' }" v-for="item in playlists" :key="item.id">
        <el-popover
          placement="bottom-start"
          width="350"
          trigger="hover"
          :content="item.introduction"
          :close-delay="50"
          :open-delay="400"
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
            <div style="padding: 14px; height: 60px; overflow: hidden">
              <p
                style="
                  font-weight: 600;
                  font-size: 14px;
                  overflow: hidden;
                  height: 34px;
                "
              >
                {{ item.title }}
              </p>
              <p style="font-size:12px;color:gray;position;margin-top:6px">
                风格：{{ item.style }}
                <el-button
                  v-if="item.collectState == 1"
                  :icon="
                    item.collectStatus == 0
                      ? 'el-icon-star-off'
                      : 'el-icon-star-on'
                  "
                  type="text"
                  size="medium"
                  style="color: orange; float: right"
                  @click.stop="collectPlaylist(item)"
                ></el-button>
              </p>
            </div>
          </el-card>
        </el-popover>
      </el-col>
    </el-row>
  </div>
</template>
<style scoped>
.el-col-lg-4-8 {
  width: 20%;
}
.myimage {
  /* 自适应正方形 */
  width: 100%;
  display: block;
  height: 0;
  padding-bottom: 100%;
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
.card{
  margin-bottom: 30px;
}
</style>
<script>
import Qs from "qs";
export default {
  data: function () {
    return {};
  },
  props: {
    playlists: {
      type: Array,
    },
  },
  methods: {
    playListSelect(itemId) {
      console.log(itemId);
      // console.log(e);
      this.$router.push({ path: `/detailPlaylist/${itemId}` });
    },
    collectPlaylist(item) {
      if (this.$root.userData.login_ed == false) {
        this.$message({ message: "请先登陆", type: "warning" });
      } else {
        let axiosThis = this;
        let data = Qs.stringify({
          userId: this.$root.userData.userId,
          songListId: item.id,
          flag: item.collectState,
        });
        this.$axios
          .post(`songList/collectSongList`, data)
          .then((res) => {
            //请求成功
            console.log(axiosThis);
            console.log("res.data=>", res.data);
            let responseData = res.data;
            if (responseData.status == "success") {
              axiosThis.$message.success(responseData.msg + "！");
              axiosThis.$emit("getCollectedPlaylist");
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
};
</script>
