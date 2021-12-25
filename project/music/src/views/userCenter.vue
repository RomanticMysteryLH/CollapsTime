<template>
  <div class="content">
    <el-tabs type="border-card">
      <el-tab-pane label="用户信息"
        ><el-descriptions title="用户信息">
          <el-descriptions-item label="账号">{{
            this.$root.userData.account
          }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{
            this.$root.userData.username
          }}</el-descriptions-item>
          <el-descriptions-item label="手机号"
            >18100000000</el-descriptions-item
          >
          <el-descriptions-item label="居住地">苏州市</el-descriptions-item>
          <el-descriptions-item label="备注">
            <el-tag size="small">学校</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="联系地址"
            >江苏省苏州市吴中区吴中大道 1188 号</el-descriptions-item
          >
          <el-descriptions-item label="头像"
            ><el-avatar
              :key="this.imgUrl"
              :src="this.$RequestUrl+this.imgUrl"
              icon="el-icon-user-solid"
              :size="50"
            >
            </el-avatar>
            <el-button size="small" @click="toggleShow">上传头像</el-button>
          </el-descriptions-item>
        </el-descriptions></el-tab-pane
      >
      <el-tab-pane label="账户安全">配置管理</el-tab-pane>
      <el-tab-pane label="音乐上传">角色管理</el-tab-pane>
    </el-tabs>
    <myUpload
      field="img"
      @crop-success="cropSuccess"
      @crop-upload-success="cropUploadSuccess"
      @crop-upload-fail="cropUploadFail"
      :modelValue.sync="show"
      :width="300"
      :height="300"
      url=""
      img-format="png"
    ></myUpload>
  </div>
</template>
<script>
import Qs from "qs";
import "babel-polyfill"; // es6 shim
import myUpload from "vue-image-crop-upload";
export default {
  data: function () {
    return {
      show: false,
      imgDataUrl: "", // the datebase64 url of created image
      imgUrl: "",
    };
  },
  methods: {
    toggleShow() {
      this.show = !this.show;
    },
    /**
     * crop success
     *
     * [param] imgDataUrl
     * [param] field
     */
    cropSuccess(imgDataUrl, field) {
      console.log("-------- crop success --------");
      // this.imgDataUrl = imgDataUrl;
      let axiosThis = this;
      let data = Qs.stringify({
        file: imgDataUrl,
      });
      this.$axios
        .post(`admin/user/userAvatarUpload`, data)
        .then((res) => {
          let response = res.data;
          console.log(response);
          axiosThis.imgUrl = response.filePath;
          axiosThis.$message.success(response.msg);
          console.log(field);
          console.log(axiosThis.$RequestUrl + axiosThis.imgUrl);
        })
        .catch((err) => {
          console.log(err);
          axiosThis.$message.error("上传失败");
        });
    },
    /**
     * upload success
     *
     * [param] jsonData   服务器返回数据，已进行json转码
     * [param] field
     */
    cropUploadSuccess(jsonData, field) {
      console.log("-------- upload success --------");
      console.log(jsonData);
      console.log("field: " + field);
    },
    /**
     * upload fail
     *
     * [param] status    server api return error status, like 500
     * [param] field
     */
    cropUploadFail(status, field) {
      console.log("-------- upload fail --------");
      console.log(status);
      console.log("field: " + field);
    },
  },
  components: {
    myUpload: myUpload,
  },
};
</script>
