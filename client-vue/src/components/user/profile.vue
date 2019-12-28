<template>
  <div class="pr">
    <h2 class="pr-title">基本信息</h2>
    <el-container>
      <el-main class="pr-title">
        <h5>用户名</h5>
        <el-input v-model="username" class="pr-input"></el-input>
        <h5 >性别</h5>
        <el-select v-model="sex" placeholder="请选择">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
        <h5 >生日</h5>
        <div class="block">
          <el-date-picker v-model="birth" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker>
        </div>
        <h5>简介</h5>
        <el-input v-model="desc" class="pr-input"></el-input>
        <h5 >居住城市</h5>
        <v-distpicker @province="changeprovince" @city="changecity" :province='province' :city='city' hide-area></v-distpicker>
        <el-button class="pr-btn" @click="save">保&nbsp;存</el-button>
      </el-main>
      <el-aside width="300px" class="pr-aside">
        <img :src="avatar" class="pr-tx">
        <br>
        <input type="file" @change="changeHead($event)" ref="avatarInput" style="display:none;">
        <el-button class="pr-txbtn" size="mini" @click="setAvatar">上传图片</el-button>
        <el-button class="pr-txbtn" size="mini" @click="confirmSet">确认修改</el-button>
      </el-aside>
    </el-container>
  </div>
</template>

<script>
import VDistpicker from 'v-distpicker';
export default {
  name: "profile",
  components:{
      VDistpicker
  },
   data() {
      return {
        uid:localStorage.getItem('uid'),
        picture: {},
        username:'',
        sex:0,
        options:[
        {
          value: 0,
          label: "保密"
        },
        {
          value: 1,
          label: "男"
        },
        {
          value:2,
          label:'女'
        }
      ],
        birth:'',
        desc:'',
        province:'',
        city:'',
        headimg:'',
        multiple:false,
        avatar:'',
        avatarurl:'',
        background:''
      }
   },
   created(){
     this.getinfo()
   },
   methods: {
     changeprovince(a){
       this.province=a.value;
     },
     changecity(a){
      this.city=a.value;
     },
     getinfo(){
       this.$http.post('/api/basicInfo',{uid:this.uid},{emulateJSON:true})
     .then(result=>{
       this.username=result.body.username;
       this.sex=result.body.sex;
       this.desc=result.body.introduce;
       this.city=result.body.city;
       this.province=result.body.province;
       this.birth=result.body.birthday;
       this.avatar=this.$store.state.HOST+result.body.head_image;
     })
     },
     updateInfo(){
       this.$http.post('/api/updateInfo',{uid:this.uid,
       username:this.username,
       sex:this.sex,
       birthday:this.birth,
       introduce:this.desc,
       province:this.province,
       city:this.city
       },{emulateJSON:true}).then(res=>{
         console.log(res);

         if (res.body.message=="编辑成功") {
          this.$message({
              message: "修改成功",
              type: "success",
              customClass: "zIndex"
            });
           this.getinfo()
        }else{
          this.$message({
              message: res.body.message,
              type: "danger",
              customClass: "zIndex"
            });
           this.getinfo()
        }
       })
     },
     save(){
      // this.uploadimg();
       this.updateInfo();
     },
     changeHead(e){
      var file = e.target.files[0]
      var reader = new FileReader()
      var that = this
      reader.readAsDataURL(file)
      reader.onload = function(e) {
      that.avatar = this.result
      }
     },
     setAvatar(){
       this.$refs.avatarInput.click();
     },
    uploadAvatar(){
      var image = new FormData()
      image.append('avatarFile', this.$refs.avatarInput.files[0])
      image.append('uid', this.uid)
      this.$http.post('/api/uploadAvatar',image)
      .then(res=>{
        console.log(res)
        if (res.body.message=="修改成功") {
          this.$message({
            message: "修改头像成功",
            type: "success",
            customClass: "zIndex"
          });
        }else{
          this.$message({
            message: "修改头像失败",
            type: "danger",
            customClass: "zIndex"
          });
        }
      })
    },
    // updateAvatar(uid,position){
    //   this.$http.post('/api/saveAvatar',{uid:uid,position:position},{emulateJSON:true})
    //   .then(res=>{
    //    if (res.body.message=="修改成功") {
    //       this.$message({
    //           message: "修改头像成功",
    //           type: "success",
    //           customClass: "zIndex"
    //         });
    //     }else{
    //       this.$message({
    //           message: "修改头像失败",
    //           type: "danger",
    //           customClass: "zIndex"
    //         });
    //     }
    //   })
    // },
    confirmSet(){
      this.uploadAvatar()
    }
   }
};
</script>

<style>
.pr {
  background-color: #fff;
  border: #bbbbbb solid 1px;
  width: 80%;
  margin: 30px auto;
}
.pr-aside {
  text-align: center;
}
.pr-tx {
  border-radius: 50%;
  width: 150px;
  height: 150px;
}
.pr-title{
    margin-left: 15px;
}
.pr-input input{
    width: 70%;
}
.pr-btn{
    width: 15%;
    margin: 50px 0 50px 0 !important;
    background-color: #D1B200 !important;
    color: #fff !important;
}
.pr-txbtn{
    margin-top: 20px !important;
    border: #C99A05 solid 1px !important;
    background-color: #fff;
}
.pr-txbtn button{
    border-radius: 5% !important;
}
.pr-txbtn span{
    color:#C99A05;
}
</style>
