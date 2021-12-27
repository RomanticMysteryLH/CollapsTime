<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" placeholder="账号" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.useraccount" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button v-waves icon="el-icon-circle-plus-outline" type="primary" @click="createSongList">
        创建歌单
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
        <template slot-scope="{row}">
          <el-image style="width:100px;height: 100px;border-radius: 5px" fit="cover" :src="row.pictureshow" />
        </template>
      </el-table-column>
      <el-table-column label="歌单标题" min-width="100px" align="center">
        <template slot-scope="{row}">
          <span> {{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="歌单风格" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.style}}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d}') }}</span>
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

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" top="1%" style="height:96%;overflow-y: auto;" @open="deletePicFlag=false" @close="checkDelete" >
      <el-tabs v-model="activeName" @tab-click="ShowDeleteButton">
        <el-tab-pane label="歌单基本信息" name="first">
          <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
            <el-form-item label="歌单标题" prop="title">
              <el-input v-model="temp.title" />
            </el-form-item>
            <el-form-item label="歌单风格" prop="style">
              <el-input v-model="temp.style" />
            </el-form-item>
            <el-form-item label="创建时间" prop="createTime">
              <el-date-picker v-model="temp.createTime" type="date" placeholder="请选择日期" />
            </el-form-item>
            <el-form-item label="简介">
              <el-input v-model="temp.introduction" :rows="5" type="textarea" />
            </el-form-item>
            <el-form-item label="歌单封面">
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
              <el-button type="primary" @click="uploadimg">上传封面</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="歌单的歌曲" name="second">
          <el-row style="padding: 20px 0px 0px 0px" :gutter="20">

            <!-- span决定大小 -->
            <el-col :span="4" v-for="item in this.Songs" :key="item.id" >
              <div @click="HandleDoCheck(item)">
                <el-card
                  :body-style="{ padding: '0px' }"
                  class="card"
                  style="cursor: pointer"
                >
                  <el-image :src="targetApi+item.cover" fit="cover" class="myimage">
                    <!-- 加载前占位 -->
                    <div slot="placeholder">
                      <img src="@/assets/default/loading1.gif" class="image" />
                    </div>
                    <!-- 加载后占位 -->
                    <div slot="error">
                      <img
                        src="@/assets/default/defaultPlayList.jpg"
                        slot="error"
                        class="image"
                      />
                    </div>
                  </el-image>
                  <div style="display:flex;padding:14px 0px 0px; justify-content: center; overflow: hidden">
                    <p
                      style="font-weight: 600;
                font-size: 14px;
                margin: 0;
                ">
                      {{item.name}}
                    </p>
                  </div>
                  <div style="height: 19px">
                    <el-checkbox ref="checkbox" v-if="showCheck" v-model="checkList" :label="item.id"  @click.stop.native="()=>{}" style="float: right;width: 16px;overflow: hidden"></el-checkbox>
                  </div>
                </el-card>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
        <el-tab-pane v-if="dialogStatus=='update'" label="歌单评论" name="third">
          <el-table
            :key="1"
            v-loading="listLoading"
            :data="commentList"
            border
            fit
            highlight-current-row
            style="width: 100%;"
          >
            <template slot="empty">
              <span>该歌单没有评论</span>
            </template>
            <el-table-column label="用户头像" width="150px" align="center" class="coverPic">
              <template slot-scope="{row}">
                <el-image style="width:100px;height: 100px;border-radius: 5px" fit="cover" :src="row.avatorshow" />
              </template>
            </el-table-column>
            <el-table-column label="用户名" width="100px" align="center">
              <template slot-scope="{row}">
                <span> {{ row.username }}</span>
              </template>
            </el-table-column>
            <el-table-column label="评论内容" min-width="100px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.content}}</span>
              </template>
            </el-table-column>
            <el-table-column label="评论时间" width="160px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{m}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="点赞数量" width="50px" align="center">
              <template slot-scope="{row}">
                <span>{{ row.commentUp }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
              <template slot-scope="{row,$index}">
                <el-button v-if="row.status!='deleted'" icon="el-icon-delete" size="mini" type="danger" @click="handleCommentDelete(row,$index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

      </el-tabs>

      <div slot="footer" class="dialog-footer" style="display: flex">
        <div style="display: flex;justify-content: start;width: 50%;">
          <el-button v-if="showDelete" type="primary" @click="showCheckDelete" style="justify-content: start">
            批量删除
          </el-button>
          <el-button v-if="showHandleDelete" type="primary" @click="confirmCheckDelete" style="justify-content: start">
            确认删除
          </el-button>
          <el-button v-if="showHandleDelete" type="primary" @click="closeShowCheckDelete" style="justify-content: start">
            取消删除
          </el-button>
        </div>
        <div style="display: flex;justify-content: end;width: 50%;">
          <el-button ref="confirm" type="primary" @click="dialogStatus==='create'?createData():updateData()" style="margin-left: 10px">
            保存
          </el-button>
          <el-button @click="dialogFormVisible = false">
            取消
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {fetchSongList,fetchSongListSong,addSongList,sendPic,overridePic,updateSongList,deleteSongList,deleteSongListSong,deleteSongListComment} from '@/api/songList'
  import waves from '@/directive/waves' // waves directive
  import { parseTime } from '@/utils'
  import Pagination from '@/components/Pagination' // 分页操作
  import qs from 'qs'
  import myUpload from 'vue-image-crop-upload/upload-2.vue'
  import {deleteSingerSong} from "@/api/singer";

  export default {
    name: 'ComplexTable',
    components: { Pagination,myUpload},
    directives: { waves },
    filters: {
      sexFilter(status){
        const statusMap = {
          0: '女',
          1: '男',
        }
        return statusMap[status]
      },
      parseTime: parseTime
    },
    data() {
      return {
        tableKey: 0,
        list: null,
        activeName: 'first',
        total: 0,
        checkFlagList:[],
        checkList:[],
        showDelete:false,
        showHandleDelete:false,
        showupload:false,
        showCheck:false,
        targetApi:this.backApi,
        deletePicFlag:false,//删除标记，用于判断是否需要删除之前的图片
        deleteSongFlag:false,
        listLoading: false,
        listQuery: {
          current: 1,
          size: 10,
          total:undefined,
          dataList:{
            id:undefined,
            title: undefined,
            style:undefined,
            createTime:undefined,
            introduction:undefined,
            picture:undefined,
            pictureshow:undefined,
          },
          sort: '+id'
        },
        Songs:[],

        sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
        showReviewer: false,
        temp: {
          id:undefined,
          title: undefined,
          style:undefined,
          createTime:undefined,
          introduction:undefined,
          picture:undefined,
          pictureshow:undefined,
        },
        commentList:null,

        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
        },
        dialogPvVisible: false,
        pvData: [],
        rules: {
          title: [{ required: true, message: '必须填写歌单标题', trigger: 'change' }],
          createTime: [{ required: true, message: '必须选择创建时间', trigger: 'change' }],
        },
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
        fetchSongList(this.listQuery).then(response => {
          this.list = response.dataList
          this.list.map(item=>{
            if(item.picture=='')
            {
              item.pictureshow=require('../../assets/default/defaultPlayList.jpg');
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
       * 显示批量删除按钮
       */
      ShowDeleteButton(tab){
        if(tab.label==="歌单的歌曲"){
          this.showDelete=true
        }else {
          this.showDelete=false
          this.showHandleDelete=false
          this.showCheck=false
        }
      },

      /**
       * 显示批量删除多选框
       */
      showCheckDelete(){
        this.showCheck=true
        this.showHandleDelete=true
      },

      /**
       * 取消显示批量删除框
       */
      closeShowCheckDelete(){
        this.showCheck=false
        this.checkList=[]
      },

      /**
       * 确认删除所选歌曲
       */
      confirmCheckDelete(){
        deleteSongListSong(this.temp.id.toString(),this.checkList.toString()).then(result=>{
          if(result=="success"){
            this.$notify({
              title: result,
              message: '删除成功!',
              type: result,
              duration: 2000
            })
            for(let i in this.Songs){
              for(let j of this.checkList){
                if(this.Songs[i].id===j){
                  this.Songs.splice(i,1)
                }
              }
            }
          }
          else{
            this.$notify({
              title: result,
              message: '删除失败!',
              type: result,
              duration: 2000
            })
          }
        })
      },

      /**
       *点击卡片选中复选框
       */
      HandleDoCheck(val){
        if(this.showCheck==true){
          this.doCheck(val)
        }
      },

      //获取数组中数值的下标
      indexOf(val,ids) {
        for (let i = 0; i < ids.length; i++) {
          //获取当前值的下标
          if (ids[i] === val.id)
            return i;
        }
        return -1;
      },

      doCheck(val){
        let ids = this.checkList;
        //检索下标,判断当前值(或对象是否在数组中); 在则返回下标,不在则返回-1
        let index = this.indexOf(val,ids);
        if (ids.length >0 && index > -1) {
          //删除数组中的某个元素(在取消勾选时,删除数组中的值)
          ids.splice(index,1);
        }else {
          //添加到数组中
          ids.push(val.id);
          //用逗号隔开
          ids.join(",");
        }
        this.checkList = ids
      },

      /**
       * 重置temp的内容
       */
      resetTemp() {
        this.temp = {
          id:undefined,
          title: undefined,
          style:undefined,
          createTime:undefined,
          introduction:undefined,
          picture:undefined,
          pictureshow:undefined,
        }
      },

      /**
       * 控制编辑内容的弹框
       * @param row
       */
      handleUpdate(row) {
        this.$refs.confirm.$el.innerText="保存"
        this.temp = Object.assign({}, row) //把那一行的内容存到temp里面
        fetchSongListSong(this.temp.id,"").then(result=>{
          this.Songs = result.songs//获取歌单歌曲
          this.commentList = result.commentUsers//获取歌单评论和评论的用户信息
          this.commentList.map(item=>{
            if(item.avator=='')
            {
              item.avatorshow=require('../../../public/logo.png');
            }
            else {
              item.avatorshow=this.backApi+item.avator;
            }
          })
        })
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },

      /**
       * 创建歌单的弹窗
       */
      createSongList(){
        this.$refs.confirm.$el.innerText="创建"
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.resetTemp()
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },

      /**
       * 创建歌单主体部分
       */
      createData(){
        this.$refs['dataForm'].validate((valid) => {
          if(valid){
            const tempData = Object.assign({}, this.temp)
            const addData = qs.stringify(tempData)
            addSongList(addData).then(result=>{
              this.deletePicFlag=false//如果点击保存则重置删除标记
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
            const tempData = Object.assign({}, this.temp)
            const upData = qs.stringify(tempData)
            //删除已经有的文件
            if(this.deletePicFlag){
              overridePic(this.temp.id,"").then(result=>{
                console.log(result)
              })
            }
            updateSongList(upData).then(result => {
              this.deletePicFlag=false//如果点击保存则重置删除标记
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
      checkDelete() {
        if (this.deletePicFlag) {
          overridePic("",this.temp.picture).then(result => {
            console.log(result)
          })
        }
        this.showCheck=false
        this.showHandleDelete=false
        this.checkList=[]
        this.Songs=[]
        this.commentList=null
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
       * 上传歌单图片
       */
      PicUpload(file){
        let fd = new FormData();//通过form数据格式来传
        fd.append("file", file); //传文件
        sendPic(fd).then(result=>{
          if(result.msg=="上传成功")
          {
            this.checkDelete()
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
       * 删除歌单
       * @param row
       * @param index
       */
      handleDelete(row, index) {
        const tempData = Object.assign({}, row)
        const idData = qs.stringify(tempData);
        this.$confirm('确认永久删除该歌单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteSongList(idData).then(result=>{
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
       * 删除评论
       * @param row
       * @param $index
       */
      handleCommentDelete(row,index){
        const tempData = Object.assign({}, row)
        const idData = qs.stringify(tempData);
        this.$confirm('确认永久删除该评论吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          console.log(index)
          deleteSongListComment(idData).then(result=>{
            if(result=="success"){
              this.$notify({
                title: 'Success',
                message: '删除成功！',
                type: 'success',
                duration: 2000
              })
              this.commentList.splice(index, 1)
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

  .myimage {
    /* 自适应正方形 */
    width: 100%;
    display: block;
    height: 0;
    padding-bottom: 100%;
  }
  .demo-image__lazy .el-image {
    display: block;
    min-height: 200px;
    margin-bottom: 10px;
  }
  .myimage >>> .el-image__inner {
    position: absolute;
  }
  .image {
    width: 100%;
    position: absolute;
  }
  .card{
    margin-bottom: 30px;
  }
</style>
