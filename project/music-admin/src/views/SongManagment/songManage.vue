<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" placeholder="请输入搜索条件" style="width: 500px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button v-waves icon="el-icon-circle-plus-outline" type="primary" @click="createSong">
        添加
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
      <el-table-column label="封面" width="200px" align="center" class="coverPic">
        <template slot-scope="{row}" >
          <el-avatar shape="square" :size="100" fit="cover" :src="row.pictureshow" />
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
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" icon="el-icon-delete" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" top="1%" style="height:95%;overflow-y: auto;" @open="deletePicFlag=false,deleteSongFlag=false" @close="checkDelete('both')" >
      <el-tabs v-model="activeName" @tab-click="loadlrcshow">
        <el-tab-pane label="歌曲基本信息" name="first">
          <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
            <el-form-item label="歌曲名" prop="name">
              <el-input v-model="temp.name" />
            </el-form-item>
            <el-form-item label="歌手">
              <el-select
                v-model="temp.singerId" value="temp.singerId"
                filterable
                remote
                reserve-keyword
                placeholder="请输入歌手名称"
                :remote-method="remoteMethod"
                :loading="loading">
                <el-option
                  v-for="item in Option"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="简介">
              <el-input v-model="temp.introduction" type="textarea" />
            </el-form-item>
            <el-form-item label="封面">
            <my-upload field="file"
                       v-model="showupload"
                       :width="300"
                       :height="300"
                       @crop-success="PicUpload"
                       :lang-ext="{preview: '图片预览',btn:{save:'上传'}}"
                       ref="myUpload"
                       img-format="png">
            </my-upload>
            <img :src="temp.pictureshow" class="avatar">
              <el-button type="primary" @click="uploadimg">上传头像</el-button>
            </el-form-item>
            <el-form-item label="音频文件">
              <el-upload
                action="#"
                multiple
                :show-file-list="true"
                :file-list="filelist"
                accept=".mp3"
                :on-change="handleSongChange"
                :on-success="handleLyricSuccess"
                :before-upload="handleMp3Upload">
                <el-button icon="el-icon-upload2" type="primary">
                  上传mp3文件
                </el-button>
              </el-upload>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="歌词" name="second">
          <div class="lyricsArea">
            <el-scrollbar>
              <p v-show="temp.lyricshow" v-for="item in temp.lyricshow" :key="item.key" style="text-align: center">{{ item }}</p>
            </el-scrollbar>
          </div>
        </el-tab-pane>
      </el-tabs>

      <div slot="footer" class="dialog-footer" style="display: flex;justify-content: flex-end">
        <el-upload
          action="#"
          :limit="1"
          :show-file-list="false"
          accept=".lrc"
          :on-success="handleLyricSuccess"
          :before-upload="handleLyricUpload">
          <el-button v-if="loadlrc" icon="el-icon-upload2" type="primary">
            上传歌词
          </el-button>
        </el-upload>
        <el-button ref="confirm" type="primary" @click="dialogStatus==='create'?createData():updateData()" style="margin-left: 10px">
          保存
        </el-button>
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { fetchSong,sendPic,sendLrc,updateSong,getSinger,deleteProFile,deleteSongFile,sendMp3,addSong,deleteSong} from '@/api/song'
  import waves from '@/directive/waves' // waves directive
  import Pagination from '@/components/Pagination' // 分页操作
  import qs from 'qs'
  import myUpload from 'vue-image-crop-upload/upload-2.vue'


  export default {
    name: 'ComplexTable',
    components: { Pagination,myUpload },
    directives: { waves },
    filters: {

    },
    data() {
      return {
        tableKey: 0,
        list: null,
        activeName: 'first',
        total: 0,
        //远程搜索组件
        Option:[],//存放下拉框显示的数据
        loading:false,
        sallData:null,//存放请求的全部数据
        stempData:[],//存放处理后的数据


        filelist:[],
        loadlrc:false,
        showupload:false,
        deletePicFlag:false,//删除标记，用于判断是否需要删除之前的图片
        deleteSongFlag:false,
        listLoading: false,
        listQuery: {
          current: 1,
          size: 10,
          total:undefined,
          dataList:{
            id:undefined,
            name: undefined,
            singerId:undefined,
            singername:undefined,
            introduction:undefined,
            picture:undefined,
            pictureshow:undefined,
            lyric:undefined,
            url:undefined
          },
          sort: '+id'
        },
        sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
        showReviewer: false,
        temp: {
          id:'',
          name: '',
          singerId:'',
          singername:'',
          introduction:'',
          picture:'',
          pictureshow:'',
          lyric:'',
          lyricshow:'',
          value:'',
          url:''
        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
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
      getSinger(this.listQuery).then(result=>{
        this.sallData=result
        this.sallData.map(item=>{
          this.stempData.push({
            value:item.singerId,
            label:item.singerId+"-"+item.name.split("-")[0]
          })
        })
        // console.log(typeof this.stempData[0].value)
        this.stempData=this.unique(this.stempData)
      })
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
              item.pictureshow=this.backApi+item.picture;
            }
          })
          this.total = response.total
          // Just to simulate the time of the request
          setTimeout(() => {//加载时间
            this.listLoading = false
          }, 1 * 1000)
        })
      },

      /**
       * 对象数组去重
       */
      unique(arr) {
        const res = new Map();
        return arr.filter((arr) => !res.has(arr.value) && res.set(arr.value, 1));
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
       *选项卡显示上传歌词
       */
      loadlrcshow(tab){
        if(tab.label=="歌词")
          this.loadlrc=true
        else
          this.loadlrc=false
      },

      /**
       *上传歌词
       */
      handleLyricUpload(file){
        let fd = new FormData();//通过form数据格式来传
        fd.append("file", file); //传文件
        sendLrc(fd).then(result=>{
          if(result)
          {
            this.temp.lyric=result.lyric;
            this.handlelyric();
            this.$message.success('上传成功')
            this.$forceUpdate();
          }
          else {
            this.$message.error('上传失败')
          }
        })
        return false;
      },

      handleSongChange(file,filelist){
        if(filelist.length>1){
          this.filelist.splice(0,1)
        }
      },

      /**
       *上传MP3
       */
      handleMp3Upload(file){
        let fd = new FormData();//通过form数据格式来传
        fd.append("file", file); //传文件
        sendMp3(fd).then(result=>{
          if(result)
          {
            this.checkDelete("song")
            this.deleteSongFlag=true;
            this.temp.url=result.filePath;
            this.filelist.push({
              name:file.name
            })
            this.$message.success('上传成功')
            this.$forceUpdate();
          }
          else {
            this.$message.error('上传失败')
          }
        })
        return false;
      },

      /**
       *上传成功
       */
      handleLyricSuccess(file){

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
      },

      /**
       * 重置temp的内容
       */
      resetTemp() {
        this.temp = {
          id:'',
          name: '',
          singerId:'',
          singername:'',
          introduction:'',
          picture:'',
          pictureshow:'',
          lyric:'',
          lyricshow:'',
          url:''
        }
      },

      /**
       * 控制编辑内容的弹框
       * @param row
       */
      handleUpdate(row) {
        this.$refs.confirm.$el.innerText="保存"
        this.temp = Object.assign({}, row) //把那一行的内容存到temp里面
        this.handlelyric()
        //获取当前歌曲的音频文件名称
        this.filelist.push({
          name:this.temp.url.split("/song/")[1]
        })
        this.handleSongChange('',this.filelist)
        this.remoteMethod(this.temp.singerId.toString())//在弹出事件中调用搜索函数，是的option被创建，获取到label的值
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },

      /**
       * 添加歌曲的弹窗
       */
      createSong(){
        this.$refs.confirm.$el.innerText="添加"
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.resetTemp()
        this.filelist=[]
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },

      /**
       * 添加歌曲主体部分
       */
      createData(){
        this.$refs['dataForm'].validate((valid) => {
          if(valid){
            //从选项中分出来歌手名存到数据库中
            this.temp.singername=this.Option.find(item=>{
              return item.value=== this.temp.singerId
            }).label.split('-')[1]

            const tempData = Object.assign({}, this.temp)
            tempData.name=this.temp.singername+"-"+this.temp.name
            const addData = qs.stringify(tempData)
            addSong(addData).then(result=>{
              this.deletePicFlag=false//如果点击保存则重置删除标记
              this.deleteSongFlag=false;
              this.dialogFormVisible = false
              if(result=="success"){
                this.$notify({
                  title: result,
                  message: '更改成功!',
                  type: result,
                  duration: 2000
                })
              }
              else{
                this.$notify({
                  title: result,
                  message: '更改失败!',
                  type: result,
                  duration: 2000
                })
              }
            })
          }
        })
      },

      /**
       * 更新内容的代码
       */
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            //从选项中分出来歌手名存到数据库中
            this.temp.singername=this.Option.find(item=>{
              return item.value=== this.temp.singerId
            }).label.split('-')[1]

            const tempData = Object.assign({}, this.temp)
            tempData.name=this.temp.singername+"-"+this.temp.name
            const upData = qs.stringify(tempData)
            //删除已经有的文件
            if(this.deletePicFlag&&this.deleteSongFlag){
              deleteSongFile(this.temp.id,"both").then(result=>{
                console.log(result)
              })
            }
            else if(this.deletePicFlag){
              deleteSongFile(this.temp.id,"pic").then(result=>{
                console.log(result)
              })
            }
            else if(this.deleteSongFlag){
              deleteSongFile(this.temp.id,"song").then(result=>{
                console.log(result)
              })
            }
            updateSong(upData).then(result => {
              this.deletePicFlag=false//如果点击保存则重置删除标记
              this.deleteSongFlag=false;
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

      /**
       * 判断是否需要删除上一个文件
       */
      checkDelete(type){
        if(this.deletePicFlag&&this.deleteSongFlag&&type=="both"){
          deleteProFile(this.temp.picture,this.temp.url).then(result=>{
            console.log(result)
          })
        }
        else if(this.deletePicFlag&&(type=="img"||type=="both")){
          deleteProFile(this.temp.picture,null).then(result=>{
            console.log(result)
          })
        }
        else if(this.deleteSongFlag&&(type=="song"||type=="both")){
          deleteProFile(null,this.temp.url).then(result=>{
            console.log(result)
          })
        }
      },

      /**
       * 显示上传图片界面
       */
      uploadimg(){
        this.showupload=true
        //调用其组件的方法，将其视图跳转到第一步,解决传过一次之后一直是该界面的问题
        this.$refs.myUpload.setStep(1)
      },

      /**
       * 上传歌曲图片
       */
      PicUpload(file){
        let fd = new FormData();//通过form数据格式来传
        fd.append("file", file); //传文件
        sendPic(fd).then(result=>{
          if(result.msg=="上传成功")
          {
            this.checkDelete("img")
            this.temp.picture=result.filePath;
            this.temp.pictureshow=this.backApi+this.temp.picture;
            this.$message.success('上传成功')
            this.deletePicFlag=true//上传成功后把删除标志置为可删除状态。
          }
          else {
            this.$message.error('上传失败')
          }
        })
      },

      /**
       * 删除用户
       * @param row
       * @param index
       */
      handleDelete(row, index) {
        const tempData = Object.assign({}, row)
        const idData = qs.stringify(tempData);
        this.$confirm('确认永久删除该歌曲吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteSong(idData).then(result=>{
            if(result=="success"){
              this.$notify({
                title: 'Success',
                message: '删除成功！',
                type: 'success',
                duration: 2000
              })
              this.list.splice(index, 1)
            }
            else{
              this.$notify({
                title: 'error',
                message: '删除失败！',
                type: 'error',
                duration: 2000
              })
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },

      /**
       * 远程搜索主体
       */
      remoteMethod(query){
        if (query !== '') {
          this.loading = true;
          setTimeout(() => {
            this.loading = false;
            this.Option = this.stempData.filter(item => {
              return item.label.toLowerCase()
                .indexOf(query.toLowerCase()) > -1;
            });
          }, 200);
        } else {
          this.Option = [];
        }
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

  .avatar {
    width: 250px;
    height: 250px;
    display: block;
    margin-bottom: 10px;
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
