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
            @click="goToHitSingle"
            >更多<i class="el-icon-arrow-right"></i
          ></el-link>
        </p>
        <oneColSongList
          :songs="hitSongs"
          @getHitSong="getHitSong"
          @openSongDetail="openSongDetail"
        ></oneColSongList>
      </div>
      <div class="heatPlaylist">
        <el-divider></el-divider>
        <p>
          <span style="font-weight: 700">热门歌单</span
          ><el-link
            style="float: right"
            type="primary"
            :underline="false"
            @click="goToHitPlaylist"
            >更多<i class="el-icon-arrow-right"></i
          ></el-link>
        </p>
        <oneColPlaylists :playlists="hitPlaylists"></oneColPlaylists>
      </div>
      <div class="hitSinger">
        <el-divider></el-divider>
        <p>
          <span style="font-weight: 700">热门歌手</span
          ><el-link
            style="float: right"
            type="primary"
            :underline="false"
            @click="goToHitSinger"
            >更多<i class="el-icon-arrow-right"></i
          ></el-link>
        </p>
        <oneColMusicianList
          :musicianList="hitMusicianList"
        ></oneColMusicianList>
      </div>
      <el-divider></el-divider>
      <!-- <div>
        <p class="motto">
          你在时间里奔跑，却永远跑不过时间。<br />如果说成年人的世界里，残酷是注定的，<br />那音乐，<br />就是这个残酷世界里，<br />最后的温柔。
        </p>
        <p class="tail" style="text-align: right">
          —— CollapsTime Music 转自
          <el-link type="warning" href="https://space.bilibili.com/261485584"
            >HOPICO</el-link
          >
        </p>
      </div> -->

      <el-carousel
        :interval="4000"
        height="400px"
        indicator-position="outside"
        arrow="never"
      >
        <el-carousel-item>
          <div style="padding: 30px 60px">
            <p class="motto">
              你在时间里奔跑，却永远也跑不过时间。<br />如果说成年人的世界里，残酷是注定的，
              <br />那音乐，<br />就是这个残酷世界里，<br />最后的温柔。
            </p>
            <p class="tail" style="text-align: right">
              —— CollapsTime Music 转自
              <el-link
                type="warning"
                href="https://space.bilibili.com/261485584"
                >HOPICO</el-link
              >
            </p>
          </div>
        </el-carousel-item>
        <el-carousel-item>
          <div style="padding: 30px 60px">
            <p class="motto">
              我们把光速作为有质量的物体的速度上限，<br />但我们也不断构建着，<br />超越光速的存在的幻想。<br />
              也许这束光的声音，有作为人类本身的上限，<br />但在她的声音里，<br />也依旧寄存着许多人现实与幻想的<br />夹缝碎片。
            </p>
            <p class="tail" style="text-align: right">
              —— CollapsTime Music 转自
              <el-link
                type="warning"
                href="https://space.bilibili.com/261485584"
                >HOPICO</el-link
              >
            </p>
          </div>
        </el-carousel-item>
        <el-carousel-item>
          <div style="padding: 30px 60px">
            <p class="motto">
              这个世界，每天都在变，<br />幸福的，和糟糕的，<br />永远都在同时发生。<br />
              但好在，当我们的耳机里响起这些作品的时候，<br />它可以把那个你向往的、充满生气的城市<br />原封不动地<br />还给你。
            </p>
            <p class="tail" style="text-align: right">
              —— CollapsTime Music 转自
              <el-link
                type="warning"
                href="https://space.bilibili.com/261485584"
                >HOPICO</el-link
              >
            </p>
          </div>
        </el-carousel-item>
        <el-carousel-item>
          <div style="padding: 30px 60px">
            <p class="motto">
              随着时间的推移，<br />或许我们面对作品的收听方式在做着改变，<br />但我想，我们也应该学会听到<br />
              为了换回你的耐心，<br />一张好的作品背后付出的用心<br />因为这些用心，<br />和你的耐心<br />一样珍贵。
            </p>
            <p class="tail" style="text-align: right">
              —— CollapsTime Music 转自
              <el-link
                type="warning"
                href="https://space.bilibili.com/261485584"
                >HOPICO</el-link
              >
            </p>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>
<script>
import oneColPlaylists from "@/components/oneColPlaylists";
import oneColSongList from "@/components/oneColSongList";
import oneColMusicianList from "@/components/oneColMusicianList";
import Qs from "qs";
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
      hitSongs: [
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
      hitPlaylists: [
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
      hitMusicianList: [
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
    goToHitSingle() {
      this.$router.push("/home/hitSingle");
    },
    goToHitPlaylist() {
      this.$router.push("/home/hitPlaylist");
    },
    goToHitSinger(){
      this.$router.push("/home/hitSinger");
    },
    getHitSong() {
      let axiosThis = this;
      let data = Qs.stringify({
        current: 1,
        size: 6,
        userId: this.$root.userData.userId,
      });
      this.$axios
        .post(`song/getHotSong`, data)
        .then((res) => {
          let response = res.data;
          // console.log(response);
          axiosThis.hitSongs = response.dataList;
          // console.log(axiosThis.totalResult);
          axiosThis.$root.routerLoading = false;
        })
        .catch((err) => {
          console.log(err);
          axiosThis.$message.error("获取热门歌曲失败");
        });
    },
    getHitPlaylist() {
      this.$root.routerLoading = true;
      let axiosThis = this;
      this.$axios
        .get("songList/getHotSongList", {
          params: {
            current: 1,
            size: 5,
          },
        })
        .then(function (res) {
          let response = res.data;
          // console.log(response);
          axiosThis.hitPlaylists = response.dataList;
          // console.log(axiosThis.totalResult);
          axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          axiosThis.$message.error("获取歌单信息失败");
          console.log(error);
          axiosThis.$root.routerLoading = false;
        });
    },
    getHitSinger() {
      let axiosThis = this;
      let Params = {
        current: 1,
        size: 5,
      };
      this.$axios
        .get("singer/getHotSinger", {
          params: Params,
        })
        .then(function (res) {
          //   axiosThis.$message.success("筛选成功");
          let response = res.data;
          // console.log(response);
          axiosThis.hitMusicianList = response.dataList;
          // console.log(axiosThis.totalResult);
          // axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          console.log(error);
          axiosThis.$message.error("获取歌手信息失败");
        });
    },
    openSongDetail(data) {
      // console.log(data);
      this.$emit("openSongDetail", data);
    },
  },
  created() {
    this.getHitSong();
    this.getHitPlaylist();
    this.getHitSinger();
  },
  components: {
    oneColPlaylists: oneColPlaylists,
    oneColSongList: oneColSongList,
    oneColMusicianList: oneColMusicianList,
  },
};
</script>
<style scoped>
/* .el-carousel__item h3 {
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
.motto {
  margin-top: 25px;
  font-size: 30px;
  line-height: 40px;
  font-family: HanYiZhongSongJian, SimSun;
}
.tail {
  line-height: 25px;
  font-family: HanYiZhongSongJian, SimSun;
}
</style>