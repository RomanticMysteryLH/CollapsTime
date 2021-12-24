<template>
  <div class="content">
    <el-descriptions title="用户信息">
      <el-descriptions-item label="账号">{{
        this.$root.userData.account
      }}</el-descriptions-item>
      <el-descriptions-item label="用户名">{{
        this.$root.userData.username
      }}</el-descriptions-item>
      <el-descriptions-item label="手机号">18100000000</el-descriptions-item>
      <el-descriptions-item label="居住地">苏州市</el-descriptions-item>
      <el-descriptions-item label="备注">
        <el-tag size="small">学校</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="联系地址"
        >江苏省苏州市吴中区吴中大道 1188 号</el-descriptions-item
      >
      <el-descriptions-item label="头像"
        ><el-avatar icon="el-icon-user-solid" :size="50"></el-avatar
      ></el-descriptions-item>
    </el-descriptions>
  </div>
</template>
<script>
export default {
  data: function () {
    return {};
  },
};
</script>
