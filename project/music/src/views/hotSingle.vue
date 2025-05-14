<template>
  <!-- 热门单曲 -->
  <div class="content">
    <el-page-header @back="goBack()" content="热门单曲"> </el-page-header>
    <el-divider></el-divider>
  </div>

</template>

<script>
export default {
  methods: {
    //点击后路由后退一步
    goBack() {
      console.log("goBack");
      this.$router.go(-1);
    },
  },
  created:{
    
  },
  mounted:{

  }
};
</script>
