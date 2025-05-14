
<template>
  <!-- 单行歌曲展示（首页等） -->
  <div>
    <el-row style="padding: 20px 0px 0px 0px" :gutter="20">
      <!-- span决定大小 -->
      <el-col :span="4" v-for="item in songs" :key="item.id">
        <el-card
          :body-style="{ padding: '0px' }"
          class="card"
          style="cursor: pointer"
          @click.native="songSelect(item.id)"
        >
          <el-image :src="$RequestUrl + item.cover" class="myimage" lazy>
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
                height: 32px;
              "
            >
              {{ item.name }}
            </p>
            <p style="font-size:12px;color:gray;position;bottom:5px">
              {{ item.singerName }}
              <el-button
                :icon="
                  item.collectStatus == 0
                    ? 'el-icon-star-off'
                    : 'el-icon-star-on'
                "
                type="text"
                size="medium"
                style="color: orange; float: right"
                @click.stop="collectSong(item)"
              ></el-button>
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col
        v-if="songs.length == 0"
        style="min-height: 240px; padding-top: 100px"
      >
        <p style="text-align: center; font-size: 14px; color: gray">没有内容≧﹏≦</p>
      </el-col>
    </el-row>
  </div>
</template>
<style scoped>
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
.card {
  margin-bottom: 30px;
}
</style>
<script>
import Qs from "qs";
export default {
  data: function () {
    return {
      hotSongs: [
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
    };
  },
  props: {
    songs: {
      type: Array,
    },
  },
  methods: {
    collectSong(row) {
      // console.log(row);
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
            // console.log(axiosThis);
            // console.log("res.data=>", res.data);
            let responseData = res.data;
            if (responseData.status == "success") {
              axiosThis.$emit("getHitSong");
              axiosThis.$emit("getCollectedSong");
              axiosThis.$message.success(responseData.msg + "！");
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
    songSelect(id) {
      this.$emit("openSongDetail", id);
    },
  },
};
</script>