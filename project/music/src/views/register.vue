<template>
  <div class="content">
    <el-row>
      <el-col :span="8">
        <el-steps direction="vertical" style="height: 550px">
          <el-step
            title="输入账号"
            description="输入账号并确认账号不存在，账号将作为您的登录凭证"
            :status="stepStatus.first"
          ></el-step>
          <el-step
            title="输入用户名、密码、确认密码"
            description="用户名即为您的昵称，确认密码输入正确"
            :status="stepStatus.second"
          ></el-step>
          <el-step
            title="输入邮箱并发送验证码"
            description="我们将会发送邮件到您的邮箱，确认是您本人操作，将您收到的验证码填入验证码输入框中"
            :status="stepStatus.third"
          ></el-step>
          <el-step
            title="选择性别和所在地"
            description="我们尊重您的隐私，您可以选择不用填写"
            :status="stepStatus.fourth"
          ></el-step>
        </el-steps>
      </el-col>
      <el-col :span="16">
        <el-form
          ref="registerForm"
          :model="registerForm"
          style="width: 460px; margin: 0 auto"
          label-width="100px"
          :rules="registerRules"
          status-icon
        >
          <el-form-item>
            <p style="font-size: 20px; font-weight: 700">欢迎注册</p>
          </el-form-item>
          <el-form-item label="账号" prop="account">
            <el-input
              v-model="registerForm.account"
              placeholder="账号"
              maxlength="20"
            ></el-input>
          </el-form-item>
          <el-form-item label="用户名" prop="userName">
            <el-input
              v-model="registerForm.userName"
              placeholder="用户名"
              maxlength="20"
            ></el-input>
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="密码"
              maxlength="20"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="passwordAgain">
            <el-input
              v-model="registerForm.passwordAgain"
              type="password"
              placeholder="再次输入密码"
              autocomplete="off"
              maxlength="20"
            ></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="邮箱"
              maxlength="30"
              style="width: 220px"
            ></el-input>
            <el-button
              type="primary"
              style="float: right"
              @click="sendEmail('registerForm')"
              :disabled="emailButtonDis"
              >发送验证码</el-button
            >
          </el-form-item>
          <el-form-item label="验证码" prop="code">
            <el-input
              v-model="registerForm.code"
              placeholder="验证码"
              maxlength="10"
              style="width: 150px"
            ></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sex">
            <el-radio v-model="registerForm.sex" label="男">男</el-radio>
            <el-radio v-model="registerForm.sex" label="女">女</el-radio>
          </el-form-item>
          <el-form-item label="所在地" prop="location">
            <el-select v-model="registerForm.location" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              round
              type="success"
              style="width: 100%; margin-top: 20px"
              @click="sendRegister('registerForm')"
              >注册</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-dialog
      :visible.sync="confirmVisible"
      title="提示"
      :append-to-body="true"
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
<style scoped>
.content {
  border: 1px solid rgb(179, 179, 179);
  border-radius: 10px;
  box-shadow: 2px 2px 10px gainsboro;
  padding: 60px 50px 60px 50px;
}
</style>
<script>
import province from "@/assets/json/province.json";
import Qs from "qs";
// console.log(province);
export default {
  data() {
    //自定义校验，确认密码功能
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
        this.passwordValid = false;
        // this.stepStatus.second = "error";
      } else if (value !== this.registerForm.password) {
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
    var validateDub = (rule, value, callback) => {
      console.log(rule, value, callback);
      let data = Qs.stringify({ account: this.registerForm.account });
      let axiosThis = this;
      this.$axios
        .post(`user/accountDetection`, data)
        .then((res) => {
          if (res.data == "ok") {
            axiosThis.stepStatus.first = "success";
            callback();
          } else if (res.data == "exist") {
            axiosThis.stepStatus.first = "error";
            callback(new Error("账号已存在！"));
          } else {
            callback();
          }
        })
        .catch((err) => {
          console.log(err);
          callback();
        });
    };
    var validateCode = (rule, value, callback) => {
      // console.log(rule, value, callback);
      // console.log(this.emailCode)
      // console.log(this.registerForm.code)
      if (this.emailCode == this.registerForm.code) {
        this.stepStatus.third = "success";
        callback();
      } else {
        this.stepStatus.third = "error";
        callback(new Error("请输入正确的验证码"));
      }
    };
    var validUsername = (rule, value, callback) => {
      // console.log(value);
      if (value.length >= 5 && value.length <= 20) {
        //只判定成功情况，并给userNameValid赋值，之后监听直接读取valid值
        this.userNameValid = true;
        callback();
      } else {
        //有callback才会执行
        this.userNameValid = false;
        callback(new Error("长度在 5 到 20 个字符"));
      }
    };
    return {
      //邮箱发送按钮的禁用，防止多次点击
      emailButtonDis: false,
      //已发送邮件弹框的可见
      confirmVisible: false,
      //邮箱收到的验证码
      emailCode: "",
      //用于存储valid状态，之后watch改变步骤条时直接读取，避免再次验证
      userNameValid: false,
      passwordValid: false,
      //正在进行的步骤
      stepStatus: {
        first: "process",
        second: "process",
        third: "process",
        fourth: "wait",
      },
      registerForm: {
        account: "",
        userName: "",
        email: "",
        password: "",
        passwordAgain: "",
        sex: "",
        birth: "",
        location: "",
        code: "",
      },
      registerRules: {
        account: [
          { required: true, message: "请输入账号", trigger: "blur" },
          {
            min: 5,
            max: 12,
            message: "长度在 5 到 12 个字符",
            trigger: "blur",
          },
          { required: true, validator: validateDub, trigger: "blur" },
        ],
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          // {
          //   min: 5,
          //   max: 20,
          //   message: "长度在 5 到 20 个字符",
          //   trigger: "blur",
          // },
          { validator: validUsername, trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            max: 20,
            message: "长度在 5 到 20 个字符",
            trigger: "blur",
          },
        ],
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          { type: "email", message: "请输入正确的邮箱", trigger: "blur" },
        ],
        passwordAgain: [
          { required: true, validator: validatePass2, trigger: "blur" },
        ],
        code: [
          {
            required: true,
            message: "请输入发送到您邮箱的验证码",
            trigger: "blur",
          },
          { required: true, validator: validateCode, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    sendRegister(formName) {
      //按钮前验证，需要给form设置ref，给button的click调用设置参数为表格数据
      this.$refs[formName].validate((valid) => {
        //回调函数，不能用返回值终止调用
        if (valid) {
          let data = Qs.stringify({
            account: this.registerForm.account,
            username: this.registerForm.userName,
            password: this.registerForm.password,
            email: this.registerForm.email,
            sex: this.registerForm.sex,
            location: this.registerForm.location,
          });
          // console.log(data);
          let axiosThis = this;
          this.$axios
            .post(`user/register`, data)
            .then((res) => {
              // console.log(res);
              //请求成功
              if (res.data == "success") {
                axiosThis.$message.success("注册成功！");
                axiosThis.$router.push("/home");
              } else if (res.data == "error") {
                axiosThis.$message.error("注册失败，请检查注册信息");
                return;
              }
            })
            .catch((err) => {
              axiosThis.$message.error("注册失败，未知错误");
              console.log(err);
            });
          //   });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    // confirmClose() {
    //   this.$router.push("/home");
    // },
    sendEmail(formName) {
      console.log("sendEmail");
      //部分表单验证validateField
      this.$refs[formName].validateField("email", (valid) => {
        // console.log(valid);
        if(valid!=""||null){
          return;
        }
        let data = Qs.stringify({ email: this.registerForm.email });
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
  },
  computed: {
    options() {
      var options = [];
      for (let item in province) {
        options.push({ value: province[item], label: province[item] });
      }
      // console.log(options);
      return options;
    },
    //同时监听多个属性，用option返回object再在watch中监听
    finishFourth() {
      const { sex, location } = this.registerForm;
      return { sex, location };
    },
    finishSecond() {
      const { userNameValid, passwordValid } = this;
      return { userNameValid, passwordValid };
    },
  },
  watch: {
    finishFourth() {
      if (this.registerForm.sex != "" && this.registerForm.location != "") {
        this.stepStatus.fourth = "success";
      }
    },
    finishSecond() {
      // let formName = "registerForm";
      //如果两个都通过验证，则改变step状态
      // let validCount = 0;
      // this.$refs[formName].validateField("passwordAgain", (valid) => {
      //   if (valid==null) {
      //     console.log(valid)
      //     validCount = validCount + 1;
      //   }
      // });
      // console.log(this.userNameValid, this.passwordValid);
      if (this.userNameValid && this.passwordValid) {
        this.stepStatus.second = "success";
      } else if (
        this.registerForm.password != "" &&
        this.registerForm.userName != "" &&
        this.registerForm.passwordAgain != ""
      ) {
        //若都已经输入且校验值不对，就设置为error
        this.stepStatus.second = "error";
      }
    },
  },
};
</script>