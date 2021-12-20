
<template>
  <div>
    <div style="font-size: 20px; margin: 10px auto 10px 10px">评论区</div>
    <!-- // 评论区 -->

    <!-- <el-divider>评论区</el-divider> -->

    <div class="commentArea">
      <el-collapse v-model="activeNames" style="margin: 10px">
        <el-collapse-item
          v-for="item in commentData"
          :name="item.commentId"
          :key="item.commentId"
        >
          <template slot="title">
            <el-avatar
              src=""
              icon="el-icon-user-solid"
              :size="30"
              slot="reference"
            ></el-avatar>
            <span style="margin-left: 10px; color: gainsboro"
              >{{ item.username }} | {{ item.createTime }}</span
            >
          </template>
          <div class="commentText">
            {{ item.content }}
          </div>
          <p style="float: right; width: 60px; user-select: none">
            <el-link
              :underline="false"
              @click.native="submitDianzan(item.commentId, item)"
              ><span class="iconfont icon-dianzan"></span
              >{{ item.commentUp }}</el-link
            >
          </p>
        </el-collapse-item>
      </el-collapse>
      <div style="color: gainsboro; text-align: center">没有评论了哦</div>
    </div>
    <el-row
      :gutter="10"
      class="writeComment"
      type="flex"
      justify="center"
      align="middle"
    >
      <el-col :span="18">
        <el-input
          type="textarea"
          :rows="3"
          :placeholder="commentTip"
          :maxlength="maxCommentLength"
          v-model="textarea"
          :disabled="!this.$root.userData.login_ed"
        >
        </el-input>
      </el-col>
      <el-col :span="4">
        <el-button
          type="primary"
          :disabled="!this.$root.userData.login_ed"
          @click.native="sendComment()"
          >发送</el-button
        >
        <div style="text-align: center; color: gainsboro">
          <span v-if="this.textarea.length <= 8" style="color: red">{{
            this.textarea.length
          }}</span
          ><span v-else>{{ this.textarea.length }}</span
          >/{{ this.maxCommentLength }}
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<style scoped>
.commentArea {
  height: 500px;
  overflow: auto;
}
.box-card {
  margin-top: 10px;
}
.commentText {
  margin: 0px 40px 0px 40px;
}
.writeComment {
  margin: 20px 0px 10px 0px;
}
</style>
<script>
import Qs from "qs";
export default {
  data: function () {
    return {
      number: 10,
      textarea: "",
      commentData: [],
      maxCommentLength: 100,
    };
  },
  computed: {
    activeNames() {
      let activeArr = [];
      for (let item in this.commentData) {
        activeArr.push(this.commentData[item].commentId);
      }
      console.log(activeArr);
      return activeArr;
    },
    commentTip() {
      if (!this.$root.userData.login_ed) {
        return "请登陆后发表评论";
      } else {
        return "发表评论";
      }
    },
  },
  props: {
    listProp: {
      type: [Array],
    },
    playListId: {
      type: Number,
    },
    songId: {
      type: Number,
    },
  },
  watch: {
    listProp() {
      console.log(this.listProp);
      this.commentData = this.listProp;
    },
  },
  methods: {
    sendComment() {
      let axiosThis = this;
      if (this.textarea.length <= 8) {
        this.$message.warning("评论字数不少于8！");
        return;
      }
      console.log(this.playListId, this.songId);
      if (this.playListId) {
        let data = Qs.stringify({
          userId: this.$root.userData.userId,
          songListId: this.playListId,
          content: this.textarea,
        });
        this.$axios
          .post(`songList/addSongListComment`, data)
          .then((res) => {
            //请求成功
            console.log(axiosThis);
            console.log("res.data=>", res.data);
            let responseData = res.data;
            if (responseData.status == "success") {
              axiosThis.$message.success("评论成功！");
              axiosThis.$emit("freshComments", responseData.commentUser);
              axiosThis.textarea = "";
            } else {
              //   //请求失败
              axiosThis.$message.error("评论发送失败！");
              //   axiosThis.changeCode();
              //   return;
            }
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("请求失败，无法发送");
          });
      } else if (this.songId) {
        let data = Qs.stringify({
          userId: this.$root.userData.userId,
          songId: this.songId,
          content: this.textarea,
        });
        this.$axios
          .post(`song/addSongComment`, data)
          .then((res) => {
            //请求成功
            console.log(axiosThis);
            console.log("res.data=>", res.data);
            let responseData = res.data;
            if (responseData.status == "success") {
              axiosThis.$message.success("评论成功！");
              axiosThis.$emit("freshComments", responseData.commentUser);
              axiosThis.textarea = "";
            } else {
              //   //请求失败
              axiosThis.$message.error("评论发送失败！");
              //   axiosThis.changeCode();
              //   return;
            }
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("请求失败，无法发送");
          });
      }
    },
    submitDianzan(commentId, item) {
      let axiosThis = this;
      if (this.playListId) {
        let data = Qs.stringify({
          commentId: commentId,
        });
        this.$axios
          .post(`songList/upSongListComment`, data)
          .then((res) => {
            //请求成功
            console.log(axiosThis);
            console.log("res.data=>", res.data);
            let responseData = res.data;
            if (responseData.status == "success") {
              // axiosThis.$message.success(responseData.msg + "！");
              item.commentUp = item.commentUp + 1;
              // axiosThis.$emit("freshComments", responseData.commentUser);
            } else {
              //   //请求失败
              axiosThis.$message.error(responseData.msg + "！");
              //   axiosThis.changeCode();
              //   return;
            }
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("请求失败，无法发送");
          });
      } else if (this.songId) {
        let data = Qs.stringify({
          commentId: commentId,
        });
        this.$axios
          .post(`song/upSongComment`, data)
          .then((res) => {
            //请求成功
            console.log(axiosThis);
            console.log("res.data=>", res.data);
            let responseData = res.data;
            if (responseData.status == "success") {
              // axiosThis.$message.success(responseData.msg + "！");
              item.commentUp = item.commentUp + 1;
              // axiosThis.$emit("freshComments", responseData.commentUser);
            } else {
              //   //请求失败
              axiosThis.$message.error(responseData.msg + "！");
              //   axiosThis.changeCode();
              //   return;
            }
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("请求失败，无法发送");
          });
      }
    },
  },
};
</script>