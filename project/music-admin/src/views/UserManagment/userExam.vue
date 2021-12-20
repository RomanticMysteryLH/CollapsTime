<template>
  <div>
    <div>
      <el-button @click="trysend">默认按钮</el-button>
    </div>
  <el-table
    ref="multipleTable"
    :data="tableData"
    tooltip-effect="dark"
    style="width: 100%"
    @selection-change="handleSelectionChange">
    <el-table-column
      type="selection"
      width="55">
    </el-table-column>
    <el-table-column
      label="日期"
      width="120">
      <template slot-scope="scope">{{ scope.row.date }}</template>
    </el-table-column>
    <el-table-column
      prop="name"
      label="姓名"
      width="120">
    </el-table-column>
    <el-table-column
      prop="address"
      label="地址"
      show-overflow-tooltip>
    </el-table-column>
  </el-table>
  </div>
</template>

<script>
  import {sendtry} from '@/api/vipuser'
  export default {
    data() {
      return {
        tableData: [{

        }],
        multipleSelection: [],
        senddata:'',
        url:'https://www.baidu.com'
      }
    },

    methods: {
      toggleSelection(rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row);
          });
        } else {
          this.$refs.multipleTable.clearSelection();
        }
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      trysend(){
        // this.senddata=666666;
        // sendtry(this.senddata).then(result => {
        //   console.log((result));
        // })
        window.location.href = this.url;
      }

    }
  }
</script>
