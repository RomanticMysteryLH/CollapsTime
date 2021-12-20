<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" placeholder="账号" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.useraccount" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.sex" placeholder="性别" clearable class="filter-item" style="width: 100px">
        <el-option v-for="item in sexOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
        导出
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="封面" width="200px" align="center" class="coverPic">
        <template slot-scope="{row}" >
          <el-avatar shape="square" :size="100" fit="fill" :src="row.pictureshow" />
        </template>
      </el-table-column>
      <el-table-column label="歌曲名" min-width="200px" align="center">
        <template slot-scope="{row}">
          <span> {{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="歌手名" width="200px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.singername }}</span>
        </template>
      </el-table-column>
      <el-table-column label="简介" min-width="300px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.introduction }}</span>
        </template>
      </el-table-column>
      <el-table-column label="歌词" min-width="300px" align="center" v-if="false">
        <template slot-scope="{row}">
          <span>{{ row.lyric }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" top="1%" style="height:95%;overflow-y: auto;"  >
      <el-tabs v-model="activeName">
        <el-tab-pane label="歌曲基本信息" name="first">
          <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
            <el-form-item label="歌曲名" prop="name">
              <el-input v-model="temp.name" />
            </el-form-item>
            <el-form-item label="歌手名" prop="name">
              <el-input v-model="temp.singername" />
            </el-form-item>
            <el-form-item label="简介">
              <el-input v-model="temp.introduction" type="textarea" />
            </el-form-item>
            <el-form-item label="封面">
              <el-avatar :src="temp.pictureshow" />
            </el-form-item>
            <el-upload
              class="avatar-uploader"
              action="#"
              accept=".jpg,.png,.jpeg"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <img v-if="temp.pictureshow" :src="temp.pictureshow" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="歌词" name="second">
          <div class="lyricsArea">
            <el-scrollbar  >
              <p v-for="item in temp.lyricshow" :key="item.key" style="text-align: center">{{ item }}</p>
            </el-scrollbar>
          </div>
        </el-tab-pane>

      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import { fetchSong} from '@/api/song'
  import waves from '@/directive/waves' // waves directive
  import { parseTime } from '@/utils'
  import Pagination from '@/components/Pagination' // 分页操作
  import qs from 'qs'
  import {scrollTo} from "@/utils/scroll-to";

  const sexOptions = [
    { key: '0', display_name: 'female' },
    { key: '1', display_name: 'male' }
  ]

  const sexKeyValue = sexOptions.reduce((acc, cur) => {
    acc[cur.key] = cur.display_name
    return acc
  }, {})

  export default {
    name: 'ComplexTable',
    components: { Pagination },
    directives: { waves },
    filters: {
      SignstatusFilter(status) {
        const statusMap = {
          0: 'success',
          1: 'danger',
        }
        return statusMap[status]
      },
      FreezestatusFilter(status) {
        const statusMap = {
          0: 'success',
          1: 'danger'
        }
        return statusMap[status]
      },
      SignFilter(sign){
        const signMap={
          0:'未标记',
          1:'标记'
        }
        return signMap[sign]
      },
      FreezeFilter(Freeze){
        const signMap={
          0:'未冻结',
          1:'冻结中'
        }
        return signMap[Freeze]
      },
      typeFilter(type) {
        return sexKeyValue[type]
      }
    },
    data() {
      return {
        tableKey: 0,
        list: null,
        targetApi: "http://localhost:8081",//后端ip地址
        activeName: 'first',
        total: 0,
        listLoading: false,
        listQuery: {
          current: 1,
          size: 20,
          total:undefined,
          dataList:{
            id:undefined,
            name: undefined,
            singername:undefined,
            introduction:undefined,
            picture:undefined,
            pictureshow:undefined,
            lyric:undefined
          },
          sort: '+id'
        },
        sexOptions,
        sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
        statusOptions: [{label:'未标记',value:0},{label:'标记',value:1}],//标记状态选择
        showReviewer: false,
        temp: {
          id:'',
          name: '',
          singername:'',
          introduction:'',
          picture:'',
          pictureshow:'',
          lyric:'',
          lyricshow:''
        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: 'Create'
        },
        dialogPvVisible: false,
        pvData: [],
        rules: {
          account: [{ required: true, message: '必须填写账号', trigger: 'change' }],
          username: [{ required: true, message: '必须填写用户名', trigger: 'change' }],
          email: [{ required: true, message: '必须填写邮箱', trigger: 'blur' }],
          location: [{ required: true, message: '必须填写所在地', trigger: 'change' }],
        },
        downloadLoading: false
      }
    },
    created() {
      this.getList()
    },
    methods: {
      /**
       * 生成列表
       * 数据在listQuery里面改
       */
      getList() {
        this.listLoading = true
        fetchSong(this.listQuery).then(response => {
          this.list = response.dataList
          this.list.map(item=>{
            item.singername=item.name.split("-")[0];
            item.name=item.name.split("-")[1];
            if(item.picture=='')
            {
              item.pictureshow="/logo.png";
            }
            else {
              item.pictureshow=this.targetApi+item.picture;
            }
          })
          this.total = response.total

          // Just to simulate the time of the request
          setTimeout(() => {//加载时间
            this.listLoading = false
          }, 1 * 1000)
        })
      },

      handleFilter() {
        this.listQuery.page = 1
        this.getList()
      },
      sortChange(data) {
        const { prop, order } = data
        if (prop === 'id') {
          this.sortByID(order)
        }
      },
      sortByID(order) {
        if (order === 'ascending') {
          this.listQuery.sort = '+id'
        } else {
          this.listQuery.sort = '-id'
        }
        this.handleFilter()
      },

      /**
       * 歌词处理
       */
      handlelyric(){
        let lyrics = this.temp.lyric.split("\n");
        let lrcObj = {};
        for(let i = 0 ;i<lyrics.length;i++){
          let lyric = decodeURIComponent(lyrics[i]);
          let timeReg = /\[\d*:\d*((\.|\:)\d*)*\]/g;
          let timeRegExpArr = lyric.match(timeReg);
          if(!timeRegExpArr)continue;
          let clause = lyric.replace(timeReg,'');
          for(let k = 0,h = timeRegExpArr.length;k < h;k++) {
            let t = timeRegExpArr[k];
            let min = Number(String(t.match(/\[\d*/i)).slice(1)),
              sec = Number(String(t.match(/\:\d*/i)).slice(1));
            let time = min * 60 + sec;
            lrcObj[time] = clause;
          }
        }
        this.temp.lyricshow = lrcObj;


        // //this.Lrc是后端获取到的lrc格式的歌词文件
        // if(this.temp.lyric.length==0) return;
        // // var lyricstxt = []; //存放歌词
        // var lrcs = this.temp.lyric.split('\n');//用回车拆分成数组
        // // console.log(lrcs)
        // for(var i in lrcs) {//遍历歌词数组
        //   lrcs[i] = lrcs[i].replace(/(^\s*)|(\s*$)/g, ""); //去除前后空格
        //   var t = lrcs[i].substring(lrcs[i].indexOf("[") + 1, lrcs[i].indexOf("]"));//取[]间的内容
        //   var s = t.split(":");//分离:前后文字
        //   if(!isNaN(parseInt(s[0]))) { //是数值
        //     var arr = lrcs[i].match(/\[(\d+:.+?)\]/g);//提取时间字段，可能有多个
        //     var start = 0;
        //     for(var k in arr){
        //       start += arr[k].length; //计算歌词位置
        //     }
        //     var content = lrcs[i].substring(start);//获取歌词内容
        //     for (var k in arr){
        //       var t = arr[k].substring(1, arr[k].length-1);//取[]间的内容
        //       var s = t.split(":");//分离:前后文字
        //       this.temp.lyricshow.push({//对象{t:时间,c:歌词}加入ms数组
        //         t: (parseFloat(s[0])*60+parseFloat(s[1])).toFixed(3),
        //         c: content
        //       });
        //     }
        //   }
        // }
      },

      /**
       * 控制编辑内容的弹框
       * @param row
       */
      handleUpdate(row) {
        this.temp = Object.assign({}, row) //把那一行的内容存到temp里面
        this.handlelyric()
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      /**
       * 更新内容的代码
       */
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            const tempData = Object.assign({}, this.temp)
            const upData = qs.stringify(tempData);
            updateuser(upData).then(result => {
              const index = this.list.findIndex(v => v.id === this.temp.id)
              this.list.splice(index, 1, this.temp)
              this.dialogFormVisible = false
              this.$notify({
                title: result,
                message: '更改成功!',
                type: result,
                duration: 2000
              })
            })
          }
        })
      },
      //头像上传
      handleAvatarSuccess(res, file) {
        this.temp.avator = URL.createObjectURL(file.raw);
      },
      /**
       * 头像上传主体部分
       * @param file
       * @returns {boolean}
       */
      beforeAvatarUpload(file) {
        const isJPG = (file.type === 'image/jpeg'||file.type === 'image/png' || file.type === 'image/jpg');
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG/PNG/JPEG格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        let fd = new FormData();//通过form数据格式来传
        fd.append("file", file); //传文件
        sendAvatar(fd).then(result=>{
          if(result.msg=="上传成功")
          {
            this.temp.avator=result.filePath;
            this.temp.avatorshow=this.targetApi+this.temp.avator;
            this.$message.success('上传成功')
          }
          else {
            this.$message.error('上传失败')
          }
        })
        return false;
      },

      /**
       * 删除用户
       * @param row
       * @param index
       */
      handleDelete(row, index) {

        this.$notify({
          title: 'Success',
          message: '删除成功！',
          type: 'success',
          duration: 2000
        })
        this.list.splice(index, 1)
      },

      handleDownload() {
        this.downloadLoading = true
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
          const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
          const data = this.formatJson(filterVal)
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: 'table-list'
          })
          this.downloadLoading = false
        })
      },
      formatJson(filterVal) {
        return this.list.map(v => filterVal.map(j => {
          if (j === 'timestamp') {
            return parseTime(v[j])
          } else {
            return v[j]
          }
        }))
      },
      getSortClass: function(key) {
        const sort = this.listQuery.sort
        return sort === `+${key}` ? 'ascending' : 'descending'
      }
    }
  }
</script>
<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
  ::-webkit-scrollbar {
    width: 10px !important;
    height: 10px !important;
    border-radius: 5px;
  }
  ::-webkit-scrollbar-thumb {
    border-radius: 5px;
    -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.2);
    /* 滚动条的颜色 */
    background-color: #e4e4e4;
  }
  .lyricsArea {
    height: 30rem;
    width: 100%;
    overflow: auto;
    margin-top: 20px;
    border: 1px solid gainsboro;
    padding: 20px;
    border-radius: 10px;
  }
</style>
