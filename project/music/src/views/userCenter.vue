<template>
  <div class="content">
    <el-tabs type="card">
      <el-tab-pane label="用户信息"
        ><el-descriptions title="用户信息">
          <template slot="extra">
            <el-button
              type="primary"
              size="small"
              @click="changeInfoVisible = true"
              >修改</el-button
            >
          </template>
          <el-descriptions-item label="账号">{{
            this.userInfo.account
          }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{
            this.userInfo.username
          }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{
            this.userInfo.email
          }}</el-descriptions-item>
          <el-descriptions-item label="所在地">{{
            this.userInfo.location
          }}</el-descriptions-item>
          <el-descriptions-item label="性别">
            <el-tag size="small" v-if="this.userInfo.sex == 1">男</el-tag>
            <el-tag size="small" v-else>女</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="个人简介">{{
            this.userInfo.introduction
          }}</el-descriptions-item>
          <!-- <el-descriptions-item label="头像"> </el-descriptions-item> -->
        </el-descriptions>
        <div style="display: flex; align-items: center">
          <!-- <span style="margin-right:30px">头像:</span> -->
          <el-avatar
            :key="this.userInfo.avator"
            :src="this.$RequestUrl + this.userInfo.avator"
            icon="el-icon-user-solid"
            :size="100"
            @click.native="toggleShow"
            class="myAvatar"
          >
          </el-avatar>
          <el-button size="small" @click="toggleShow" style="margin-left: 30px"
            >上传头像</el-button
          >
        </div>

        <!-- <el-button size="small" @click="toggleShow">上传头像</el-button> -->
      </el-tab-pane>
      <el-tab-pane label="账户安全">
        <p style="line-height: 35px; font-weight: 600">修改密码</p>
        <el-form
          :model="newPassword"
          :rules="changePassWordRules"
          ref="newPassword"
          style="width: 460px; margin: 0 auto"
          label-width="100px"
          status-icon
        >
          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="newPassword.email"
              placeholder="邮箱"
              maxlength="30"
              :disabled="true"
            ></el-input>
          </el-form-item>
          <el-form-item label="验证码" prop="code">
            <el-input
              v-model="newPassword.code"
              placeholder="验证码"
              maxlength="10"
              style="width: 150px"
            ></el-input>
            <el-button
              type="primary"
              style="float: right"
              @click="sendEmail('newPassword')"
              :disabled="emailButtonDis"
              >发送验证码</el-button
            >
          </el-form-item>
          <el-form-item label="新密码" prop="password">
            <el-input
              v-model="newPassword.password"
              type="password"
              placeholder="密码"
              maxlength="20"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="passwordAgain">
            <el-input
              v-model="newPassword.passwordAgain"
              type="password"
              placeholder="再次输入密码"
              autocomplete="off"
              maxlength="20"
            ></el-input>
          </el-form-item>
          <el-form-item prop="submit">
            <el-button
              @click="submitPasswordChange('newPassword')"
              type="success"
              >提交</el-button
            >
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="注销账号">
        <p style="line-height: 35px; font-weight: 600">注销账号</p>
        <div style="text-align: center">
          <el-button type="danger" @click="destroy()">注销</el-button>
        </div>
      </el-tab-pane>
    </el-tabs>
    <myUpload
      field="img"
      @crop-success="cropSuccess"
      @crop-upload-fail="cropUploadFail"
      :modelValue.sync="show"
      :width="300"
      :height="300"
      url=""
      img-format="png"
    ></myUpload>
    <el-dialog
      title="修改信息"
      :visible.sync="changeInfoVisible"
      class="changeInfo"
      :append-to-body="true"
      :lock-scroll="false"
    >
      <el-form
        :model="newUserInfo"
        :rules="changeInfoRules"
        ref="newUserInfo"
        style="width: 460px; margin: 0 auto"
        label-width="100px"
        status-icon
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="newUserInfo.username"
            placeholder="用户名"
            maxlength="20"
          ></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio v-model="newUserInfo.sex" label="男">男</el-radio>
          <el-radio v-model="newUserInfo.sex" label="女">女</el-radio>
        </el-form-item>
        <el-form-item label="所在地" prop="location">
          <el-select v-model="newUserInfo.location" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="个人简介" prop="introduction">
          <el-input
            v-model="newUserInfo.introduction"
            placeholder="请输入个人简介"
            type="textarea"
            maxlength="60"
            :show-word-limit="true"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            round
            type="success"
            style="width: 100%; margin-top: 20px"
            @click="submitInfoChange('newUserInfo')"
            >提交</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      :visible.sync="confirmVisible"
      title="提示"
      :append-to-body="true"
      :lock-scroll="false"
    >
      <p>验证码已经发送到您的邮箱，请进入邮箱查看！</p>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmVisible = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import Qs from "qs";
import "babel-polyfill"; // es6 shim
import myUpload from "vue-image-crop-upload";
import province from "@/assets/json/province.json";
export default {
  data: function () {
    var validUsername = (rule, value, callback) => {
      console.log(value);
      if (value.length >= 5 && value.length <= 20) {
        //只判定成功情况，并给userNameValid赋值，之后监听直接读取valid值
        this.usernameValid = true;
        callback();
      } else {
        //有callback才会执行
        this.usernameValid = false;
        callback(new Error("长度在 5 到 20 个字符"));
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
        this.passwordValid = false;
        // this.stepStatus.second = "error";
      } else if (value !== this.newPassword.password) {
        callback(new Error("两次输入密码不一致!"));
        this.passwordValid = false;
        // this.stepStatus.second = "error";
      } else {
        //在后面监听改变step状态了
        // this.stepStatus.second = "success";
        this.passwordValid = true;
        callback();
      }
    };
    var validateCode = (rule, value, callback) => {
      // console.log(rule, value, callback);
      // console.log(this.emailCode)
      // console.log(this.registerForm.code)
      if (this.emailCode == this.newPassword.code) {
        callback();
      } else {
        callback(new Error("请输入正确的验证码"));
      }
    };
    return {
      //已发送邮件弹框的可见
      confirmVisible: false,
      userInfo: {},
      newUserInfo: {},
      show: false,
      imgDataUrl: "", // the datebase64 url of created image
      //剪裁后图片地址
      imgCropUrl: "",
      //上传后图片地址
      imgUrl: "",
      emailButtonDis: false,
      changeInfoVisible: false,
      newPassword: {
        password: "",
        passwordAgain: "",
        code: "",
        email: "",
      },
      changeInfoRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          // {
          //   min: 5,
          //   max: 20,
          //   message: "长度在 5 到 20 个字符",
          //   trigger: "blur",
          // },
          { validator: validUsername, trigger: "blur" },
        ],
      },
      changePassWordRules: {
        code: [
          {
            required: true,
            message: "请输入发送到您邮箱的验证码",
            trigger: "blur",
          },
          { required: true, validator: validateCode, trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            max: 20,
            message: "长度在 5 到 20 个字符",
            trigger: "blur",
          },
        ],
        passwordAgain: [
          { required: true, validator: validatePass2, trigger: "blur" },
        ],
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          { type: "email", message: "请输入正确的邮箱", trigger: "blur" },
        ],
      },
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
          // console.log(response);
          axiosThis.imgCropUrl = response.filePath;
          axiosThis.$message.success(response.msg);
          console.log(field);
          console.log(axiosThis.$RequestUrl + axiosThis.imgUrl);
          axiosThis.cropUploadSuccess();
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
      let axiosThis = this;
      let data = Qs.stringify({
        account: this.$root.userData.account,
        filePath: this.imgCropUrl,
      });
      this.$axios
        .post(`/user/updateAvator`, data)
        .then((res) => {
          let response = res.data;
          // console.log(response);
          axiosThis.imgCropUrl = response.filePath;
          // axiosThis.$message.success(response.msg);
          console.log(field);
          axiosThis.getUserInfo();
          // console.log(axiosThis.$RequestUrl + axiosThis.imgUrl);
        })
        .catch((err) => {
          console.log(err);
          axiosThis.$message.error("上传失败");
        });
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
    getUserInfo() {
      if (this.$root.userData.login_ed == false) {
        return;
      }
      this.$root.routerLoading = true;
      let axiosThis = this;
      let data = Qs.stringify({
        account: this.$root.userData.account,
      });
      this.$axios
        .post(`user/getInfo`, data)
        .then(function (res) {
          let response = res.data;
          // console.log(response);
          axiosThis.userInfo = response;
          axiosThis.newUserInfo = JSON.parse(JSON.stringify(response));
          if (axiosThis.newUserInfo.sex == 1) {
            axiosThis.newUserInfo.sex = "男";
          } else if (axiosThis.newUserInfo.sex == 0) {
            axiosThis.newUserInfo.sex = "女";
          }
          axiosThis.newPassword.email = response.email;
          axiosThis.$root.routerLoading = false;
        })
        .catch(function (error) {
          axiosThis.$message.error("获取用户信息失败");
          console.log(error);
          axiosThis.$root.routerLoading = false;
        });
    },
    sendEmail(formName) {
      console.log("sendEmail");
      //部分表单验证validateField

      this.$refs[formName].validateField("email", (valid) => {
        console.log(valid);
        if (valid != "") {
          return;
        }
        let data = Qs.stringify({ email: this.newPassword.email });
        let axiosThis = this;
        this.$axios
          .post(`getCheckCode`, data)
          .then((res) => {
            // console.log(res);
            axiosThis.emailButtonDis = true;
            axiosThis.confirmVisible = true;
            axiosThis.emailCode = res.data;
            setTimeout(() => {
              axiosThis.emailButtonDis = false;
            }, 20000);
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    submitInfoChange(formName) {
      //按钮前验证，需要给form设置ref，给button的click调用设置参数为表格数据
      this.$refs[formName].validate((valid) => {
        //回调函数，不能用返回值终止调用
        if (valid) {
          let data = Qs.stringify({
            account: this.newUserInfo.account,
            username: this.newUserInfo.username,
            sex: this.newUserInfo.sex == "男" ? 1 : 0,
            location: this.newUserInfo.location,
            introduction: this.newUserInfo.introduction,
          });
          console.log(data);
          let axiosThis = this;
          this.$axios
            .post(`user/updateUserBaseInfo`, data)
            .then((res) => {
              console.log(res);
              //请求成功
              if (res.data.status == "success") {
                axiosThis.$message.success("修改成功！");
                axiosThis.changeInfoVisible = false;
                axiosThis.getUserInfo();
              } else if (res.data.status == "error") {
                axiosThis.$message.error("修改失败！");
                return;
              }
            })
            .catch((err) => {
              axiosThis.$message.error("未知错误");
              console.log(err);
            });
          //   });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    submitPasswordChange(formName) {
      //按钮前验证，需要给form设置ref，给button的click调用设置参数为表格数据
      this.$refs[formName].validate((valid) => {
        //回调函数，不能用返回值终止调用
        if (valid) {
          let data = Qs.stringify({
            password: this.newPassword.password,
            account: this.$root.userData.account,
          });
          console.log(data);
          let axiosThis = this;
          this.$axios
            .post(`user/updatePwd`, data)
            .then((res) => {
              console.log(res);
              //请求成功
              if (res.data.status == "success") {
                axiosThis.$message.success("修改成功！");
                // axiosThis.changeInfoVisible = false;
                axiosThis.getUserInfo();
              } else if (res.data.status == "error") {
                axiosThis.$message.error("修改失败！");
                return;
              }
            })
            .catch((err) => {
              axiosThis.$message.error("未知错误");
              console.log(err);
            });
          //   });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    destroy() {
      let axiosThis = this;
      this.$confirm("此操作将永久删除该账户, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let data = Qs.stringify({
            account: axiosThis.$root.userData.account,
          });

          axiosThis.$axios
            .post(`user/deleteUser`, data)
            .then((res) => {
              //请求成功
              // console.log(axiosThis);
              // console.log("res.data=>", res.data);
              if (res.data.status == "success") {
                axiosThis.$message({
                  type: "success",
                  message: "注销成功!",
                });
                axiosThis.$router.go(0);
              } else {
                axiosThis.$message.error("注销失败");
              }
            })
            .catch((err) => {
              console.log(err);
              axiosThis.$message.error("请求失败，无法发送");
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消注销",
          });
        });
    },
    autoLogin() {
      let axiosThis = this;
      if (localStorage.getItem("collapstimeUser") == null) {
        return;
      }
      let data = {};
      this.$axios
        .post(`/user/verifyToken`, data, {
          headers: {
            token: localStorage.getItem("collapstimeUser"),
          },
        })
        .then((res) => {
          //请求成功
          // console.log(axiosThis);
          // console.log("res.data=>", res.data);
          let responseData = res.data;
          if (responseData.status == "success") {
            axiosThis.$root.userData = {
              login_ed: true,
              userId: responseData.userId,
              username: responseData.username,
              avator: responseData.avator,
              account: responseData.account,
            };
            axiosThis.$message.success("自动登录成功！");
          } else {
            axiosThis.$message.error(responseData.msg + "，请重新登陆！");
          }
          this.reload();
        })
        .catch((err) => {
          console.log(err);
          this.$message.error("请求失败，无法自动登陆！");
        });
    },
  },
  computed: {
    options() {
      var options = [];
      for (let item in province) {
        options.push({ value: province[item], label: province[item] });
      }
      console.log(options);
      return options;
    },
  },
  watch: {
    "userInfo.avator": {
      handler: function () {
        console.log("this.userInfo.avator");
        this.$root.userData.avator = this.userInfo.avator;
      },
    },
    "userInfo.username": {
      handler: function () {
        this.$root.userData.username = this.userInfo.username;
      },
    },
  },
  components: {
    myUpload: myUpload,
  },
  mounted() {
    this.getUserInfo();
  },
  created() {
    if (this.$root.userData.login_ed == false) {
      this.$router.push("/");
    }
  },
};
</script>
<style scoped>
.myAvatar {
  cursor: pointer;
}
</style>
