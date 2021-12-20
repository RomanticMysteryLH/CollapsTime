<template>
  <div class="home">
    <!-- <el-page-header @back="goBack" content="首页"> </el-page-header> -->
    <!-- 除去页头的部分 -->
    <div class="content">
      <!--首页轮播-->
      <div class="carousel">
        <span style="font-weight: 700">热点</span>
        <el-carousel height="350px" style="margin: 20px auto 0px auto">
          <el-carousel-item
            v-for="item in carouselPicture"
            :key="item.id"
            style="border-radius: 8px"
          >
            <!-- <h3 class="big">{{ item.id }}</h3> -->
            <el-image style="width: 100%" :src="item.src"></el-image>
          </el-carousel-item>
        </el-carousel>
      </div>
      <!--热门单曲-->
      <div class="heatSingle">
        <el-divider></el-divider>
        <p>
          <span style="font-weight: 700">热门单曲</span
          ><el-link
            style="float: right"
            type="primary"
            :underline="false"
            @click="goToHotSingle"
            >更多<i class="el-icon-arrow-right"></i
          ></el-link>
        </p>
        <el-row style="padding: 20px 0px 0px 0px" :gutter="20">
          <!-- span决定大小 -->
          <el-col :span="4" v-for="item in hotSongs" :key="item.id">
            <el-card
              :body-style="{ padding: '0px' }"
              class="card"
              style="cursor: pointer"
              @click.native="songSelect(item.id)"
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
                  {{ item.singer }}
                </p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <div class="heatPlaylist">
        <el-divider></el-divider>
        <p>
          <span style="font-weight: 700">热门歌单</span
          ><el-link
            style="float: right"
            type="primary"
            :underline="false"
            @click="goToHotPlaylist"
            >更多<i class="el-icon-arrow-right"></i
          ></el-link>
        </p>
        <el-row style="padding: 20px 0px 0px 0px" :gutter="20">
          <el-col
            :lg="{ span: '4-8' }"
            v-for="item in hotPlaylists"
            :key="item.id"
          >
            <el-popover
              placement="bottom-start"
              width="350"
              trigger="hover"
              :content="item.introduction"
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

                style="cursor: pointer"
                slot="reference"
                @click.native="playListSelect(item.id)"
              >
                <el-image
                  :src="$RequestUrl + item.picture"
                  class="myimage"
                  lazy
                >
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
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data: function () {
    return {
      carouselPicture: [
        {
          id: 1,
          src: "https://y.qq.com/music/common/upload/MUSIC_FOCUS/4152171.jpg?max_age=2592000",
        },
        {
          id: 2,
          src: "https://y.qq.com/music/common/upload/MUSIC_FOCUS/4154225.jpg?max_age=2592000",
        },
        {
          id: 3,
          src: "https://y.qq.com/music/common/upload/MUSIC_FOCUS/4149422.jpg?max_age=2592000",
        },
      ],
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
      hotPlaylists: [
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
      ],
    };
  },
  methods: {
    goBack() {
      console.log("goBack");
    },
    goToHotSingle() {
      this.$router.push("/home/hotSingle");
    },
    goToHotPlaylist() {
      this.$router.push("/home/hotPlaylist");
    },
  },
};
</script>
<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
/* .content {
  padding: 20px 50px 0px 50px;
} */

.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
.el-col-lg-4-8 {
  width: 20%;
}
.heatSingle {
  padding: 40px auto 0px auto;
}
.heatSingle .myimage {
  /* 自适应正方形 */
  width: 100%;
  display: block;
  height: 0;
  padding-bottom: 100%;
}
.heatSingle .image-slot {
  width: 100%;
  display: block;
}
.heatSingle .demo-image__lazy .el-image {
  display: block;
  min-height: 200px;
  margin-bottom: 10px;
}
.heatSingle .myimage >>> .el-image__inner {
  position: absolute;
}
.heatSingle .image {
  width: 100%;
  position: absolute;
}
.heatPlaylist .myimage {
  /* 自适应正方形 */
  width: 100%;
  display: block;
  height: 0;
  padding-bottom: 100%;
}
.heatPlaylist .image-slot {
  width: 100%;
  display: block;
}
.heatPlaylist .demo-image__lazy .el-image {
  display: block;
  min-height: 200px;
  margin-bottom: 10px;
}
.heatPlaylist .myimage >>> .el-image__inner {
  position: absolute;
}
.heatPlaylist .image {
  width: 100%;
  position: absolute;
}
</style>