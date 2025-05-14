<template>
  <div class="home">
    <div class="content">
      <div class="carousel">
        <span style="font-weight: 700">主页图片</span>
        <el-carousel height="350px" style="margin: 20px auto 0px auto">
          <el-carousel-item
            v-for="item in carouselPicture"
            :key="item.id"
            style="border-radius: 8px"
          >
            <!-- <h3 class="big">{{ item.id }}</h3> -->
            <el-image style="width: 100%" fit="fill" :src="item.src"></el-image>
          </el-carousel-item>
        </el-carousel>
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
        }
      }
    }
</script>

<style scoped>
  .home{
    width: 100%;
    height: 100%;
  }
  .content {
    border: 1px solid rgb(179, 179, 179);
    border-radius: 10px;
    box-shadow: 2px 2px 10px gainsboro;
    padding: 60px 50px 60px 50px;
  }
</style>
