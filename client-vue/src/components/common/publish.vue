<template>
  <el-container class="publish">
    <el-main class="publish-main">
      <el-upload
        class="publish-upload"
        action=""
        list-type="picture-card"
        :on-preview="handlePictureCardPreview"
        :on-change="imgchange"
        :auto-upload="false"
        :limit="1"
        :file-list="fileList"
        :on-exceed="maxnum"
        accept=".jpg, .jpeg, .png, .JPG, .JPEG"
      >
        <i class="el-icon-plus"></i>
        <div class="el-upload__tip" slot="tip">
          最大支持20MB的JPEG格式照片
          <br>不建议加画框和水印签名
        </div>
      </el-upload>
      <el-dialog :visible.sync="dialogVisible">
        <img width="100%" v-lazy="dialogImageUrl" alt>
      </el-dialog>
    </el-main>
    <el-aside width="30%" class="publish-aside">
      <h5 class="publish-title">作品描述</h5>
      <el-input
        class="publish-width"
        type="textarea"
        :rows="3"
        placeholder="说说你的拍摄经历...."
        v-model="description"
      ></el-input>
      <h5 class="publish-title">标签</h5>
      <span class="publish-title publish-tj">推荐:</span>
      <ul class="publish-ul publish-ulf">
        <li
          v-for="(item,i) of categories"
          :key="item.id"
          v-if="i<5"
          v-text="item.category"
          @click="handleInputConfirm(item.category)"
        ></li>
      </ul>
      <ul class="publish-ul">
        <li
          v-for="(item,i) of categories"
          :key="item.id"
          v-if="i>5"
          v-text="item.category"
          @click="handleInputConfirm(item.category)"
        ></li>
      </ul>
      <el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)">{{tag}}</el-tag>
      <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="mini"
        @keyup.enter.native="handleInputConfirm(inputValue)"></el-input>
      <el-button class="button-new-tag" size="small" @click="showInput" v-if="dynamicTags.length<5">+</el-button>
      <ul class="publish-ult">
        <li class="publish-li-left">(输入标签内容回车即可添加标签)</li>
        <li class="publish-li-right">{{dynamicTags.length}}/5</li>
      </ul>
      <el-button class="publish-btn" @click="publish()" plain>发&nbsp;&nbsp;&nbsp;布</el-button>
    </el-aside>
  </el-container>
</template>

<script>
export default {
  name: "publish",
  data() {
    return {
      uid:localStorage.getItem('uid'),
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
      dialogImageUrl: "",
      dialogVisible: false,
      fileList: [],
      description: "",
      tag: "",
      imgurl:'',
      width:'',
      height:'',
      categories: [
        {
          id: 0,
          category: "人像"
        },
        {
          id: 1,
          category: "风光"
        },
        {
          id: 2,
          category: "城市"
        },
        {
          id: 3,
          category: "旅行"
        },
        {
          id: 4,
          category: "纪实"
        },
        {
          id: 5,
          category: "色彩"
        },
        {
          id: 6,
          category: "手机"
        },
        {
          id: 7,
          category: "黑白"
        },
        {
          id: 8,
          category: "胶片"
        },
        {
          id: 9,
          category: "抓拍"
        }
      ],
      uploadFormdata: new  FormData()
    };
  },
  methods: {
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    imgchange(file,fileList){
      console.log(file)
      this.uploadFormdata = new FormData()
      let f_img = new Image()
      f_img.src=file.url
      let _this = this
      f_img.onload = function(){
        _this.height =  f_img.height
        _this.width =  f_img.width
      }
      this.uploadFormdata.append('img', file.raw)
      console.log(this.uploadFormdata)
    },
    maxnum() {
      this.$message({
              message: "最多只能上传1张图片",
              type: "warning",
              customClass: "zIndex"
      });
    },
    handleClose(tag) {
        this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
      },
      showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },
      handleInputConfirm(inputValue) {
        let flag=false;
        for (let i = 0; i < this.dynamicTags.length; i++) {
          if(inputValue==this.dynamicTags[i]){
            flag=true;
          }
        }
        if(flag){
          this.inputVisible = false;
          this.inputValue = '';
        }else if(inputValue){
          this.dynamicTags.push(inputValue);
          this.inputVisible = false;
          this.inputValue = '';
        }
      },
    publish(){
      let tags=this.dynamicTags.toString();
      this.uploadFormdata.append('height', this.height.toString())
      this.uploadFormdata.append('width',this.width.toString())
      this.uploadFormdata.append('uid',this.uid.toString())
      this.uploadFormdata.append('type_name', tags)
      this.uploadFormdata.append('description',this.description)
      console.log(this.uploadFormdata.get('type_name'))
      this.$http.post('/api/pictureUpload',this.uploadFormdata)
      .then(res=>{
        console.log(res)
        if (res.body.message=="添加成功") {
          this.$message({
              message: "发布成功",
              type: "success",
              customClass: "zIndex"
            });
          this.description='';
          this.dynamicTags=[];
          this.fileList=[];
          this.imgurl='';
          this.width='';
          this.height='';
        }else{
          this.$message({
              message: "发布失败",
              type: "danger",
              customClass: "zIndex"
            });
        }
      })
    }
  }
};
</script>

<style>

.el-tag{
  margin-left: 10px !important;
}
  .button-new-tag {
    margin-left: 10px;
    height: 30px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }

.publish {
  min-height: 541px;
  height: auto;
}
.publish-main {
  background-color: #f9f9f9;
}
.publish-aside {
  background-color: #f5f5f5;
  border-left: #ffffff solid 1px;
}
.publish-upload {
  margin: 35px;
}
.el-upload--picture-card {
  background-color: #ededef !important;
  border: #bbbbbb solid 1px !important;
}
.publish-title {
  margin-left: 10px;

}
.publish-width {
  text-align: center;
}
.publish-width input,
textarea {
  width: 350px !important;
  margin: 0 auto;
}
.publish-tj {
  color: #85888a;
  font-size: 13px;
  display: inline;
}
.publish-ulf {
  display: inline;
  padding: 0;
}
.publish-ul {
  margin: 5px 0 10px 0;
}
.publish-ul li {
  list-style: none;
  display: inline;
  margin: 0 10px 0 10px;
  color: #101010;
  font-size: 14px;
  cursor: pointer;
}
.publish-ult {
  list-style: none;
  margin: 0;
  padding: 0;
}
.publish-ult li {
  display: inline;
  font-size: 13px;
  color: #85888a;
}
.publish-li-left {
  margin-left: 8%;
}
.publish-li-right {
  margin-left: 30%;
}
.publish-btn {
  clear: both;
  width: 60%;
  display: block !important;
  margin:  150px auto 0 auto !important;
  background-color: #d1b200 !important;
  color: #ffffff !important;
  font-size: 16px !important;
}
</style>

