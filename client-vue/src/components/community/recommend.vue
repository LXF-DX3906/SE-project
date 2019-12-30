<template>
  <div class="recommend">
    <el-row type="flex" justify="center">
      <el-col :span="23">
        <waterfall class="recommend-div" :col="col" :width="itemWidth" :gutterWidth="gutterWidth" :data="imgs" @loadmore="loadmore"
      @scroll="scroll">
      <template>
        <vue-loading type="spiningDubbles"  class ="wait-loading " :class="{loadingDisplay: !isLoading}" color="#9e9e9e" :size="{ width: '50px', height: '50px' }"></vue-loading>
        <div class="cell-item" v-for="img in imgs" :key="img.id">
          <img v-lazy="img.position" class="recommend-img" @click="show(img)">
          <div class="item-body">
            <div class="recommend-desc">{{img.description}}</div>
            <el-row type="flex" class="recommend-footer">
            <el-col :xs="4" :sm="4" :md="4" :lg="4" :xl="4" class="recommend-tx">
              <img v-lazy="img.head_image" @click="others(img.uid)">
          </el-col>
          <el-col :xs="6" :sm="6" :md="6" :lg="6" :xl="6" class="recommend-name">
            <span @click="others(img.uid)">{{img.username?img.username:'注册用户'}}</span>
          </el-col>
          </el-row>
          </div>
        </div>
      </template>
    </waterfall>
      </el-col>
    </el-row>
    <!--弹出对话框-->
    <el-dialog :visible.sync="dialogVisible" width="70%">
      <div class="recommend-diahead">
        <div class="recommend-diahead-tx">
          <img v-lazy="useritem.head_image" @click="others(diaitem.uid)">
        </div>
        <div class="recommend-diahead-name" @click="others(diaitem.uid)">{{useritem.username?useritem.username:'注册用户'}}</div>
        <el-button size="medium" class="recommend-diahead-btn" type="success" @click="follow(diaitem.uid)">关注</el-button>
      </div>
      <div class="recommend-dia-cont">
        <img v-lazy="diaitem.position">
      </div>
      <div class="recommend-dia-text" v-text="diaitem.description"></div>
      <div class="recommend-dia-like">
        <el-button icon="el-icon-gz-heart" @click="like(diaitem)"></el-button>
          <span v-text="diaitem.like_num"></span>
      </div>
          <div class="recommend-dia-tabs">
            <div class="recommend-dia-comment">
              <el-input class="recommend-dia-input" v-model="comment" placeholder="请输入内容"></el-input>
              <el-button class="recommend-dia-btn" type="primary" @click="addcom(diaitem.pid,diaitem.uid,comment)">评 论</el-button>
            </div>
            <div class="recommend-hot" v-for="com in picdetail.comment" :key="com.cid">
              <div class="recommend-hot-tx">
                <img v-lazy="HOST+com.from_head_image">
              </div>
              <div class="recommend-hot-name" >{{com.from_username?com.from_username:'注册用户'}}</div>
              <div class="recommend-coms-com" >{{com.content}}</div>
            </div>
          </div>
    </el-dialog>
  </div>
</template>
<script>
import store from "../../store";

export default {
  name: "recommend",
  components: {
  },
  data() {
    return {
      uid:localStorage.getItem('uid'),
      dialogVisible: false,
      comment: "",
      col: 4,
      avatar:'http://188.131.192.194/head_images/5LSk0zVtyKDq9UciiWPab50dwjoNI2324KtwSyBD.jpeg',
      imgs: [],
      diaitem: [],
      picdetail:[],
      useritem:{},
      HOST: this.$store.state.HOST,
      isLoading: true
    };
  },
  created() {
    this.getData();
  },
  computed: {
    itemWidth() {
      return 138 * 0.5 * (document.documentElement.clientWidth /320);
    },
    gutterWidth() {
      return 9 * 0.5 * (document.documentElement.clientWidth /300);
    }
  },
  methods: {
    others(uid){
      this.$router.push({path: "/community/others",query:{my:false,uid:uid}})
    },
    follow(uuid){
        this.$http.post('/api/addFocus',{uid:this.uid,uuid:uuid},{emulateJSON:true})
        .then(res=>{
          if (res.body.message=="关注成功") {
            this.$message({
              message: "关注成功",
              type: "success",
              customClass: "zIndex"
            });
          }else if (res.body.message=="已关注该用户"){
            this.$message({
              message: "您已关注",
              type: "warning",
              customClass: "zIndex"
            });
          } else {
            this.$message({
              message: "关注失败",
              type: "warning",
              customClass: "zIndex"
            });
          }
        })
      },
    scroll() {
    },
    loadmore() {
    },
    getData(){
      this.$http.get('/api/pictureUsersList').then(res=>{
        console.log(res)
        this.imgs = []
        let imgs = Object.assign(res.body.result);
        for (let item of  imgs) {
          let new_item = {
            pid: item.pictureId,
            position: this.$store.state.HOST + item.position,
            weight: item.width,
            height: item.height,
            description: item.description,
            uid: item.userId,
            username: item.userName,
            head_image: this.$store.state .HOST+item.headImg,
            like_num:item.likeNum,
          }
          this.imgs.push(new_item)
          this.isLoading = false
        }
      })
    },
    getuserinfo(uid){
      this.$http.post('/api/basicInfo',{uid:uid},{emulateJSON:true})
      .then(res=>{
        console.log(res)
        this.useritem = {
          username: res.body.username,
          sex: res.body.sex,
          desc: res.body.introduction,
          city: res.body.city,
          province: res.body.province,
          birth: res.body.birthday,
          head_image: this.$store.state.HOST+res.body.head_image,
        }
      })
    },
    getpicdetail(pid){
      this.$http.post('/api/pictureDetail',{pid:pid},{emulateJSON:true})
      .then(res=>{
        console.log(res);
        this.picdetail=Object.assign(res.body.result);

      })
    },
    addcom(pid,uuid,content){
      this.$http.post('/api/userComment',{pid:pid,uid:this.uid,uuid:uuid,content:content}, {emulateJSON:true})
      .then(res=>{
        if(res.body.message=='评论成功'){
          this.getpicdetail(pid)
          this.comment=''
          this.$message({
            message: "评论成功",
            type: "success",
            customClass: "zIndex"
          })
        }else{
          this.$message({
              message: "评论失败",
              type: "danger",
              customClass: "zIndex"
            })
        }
      })
    },
    like(diaitem){
      this.$http.post('/api/pictureLike',{uid:this.uid,pid:diaitem.pid}, {emulateJSON:true})
      .then(res=>{
        console.log(res);

        if (res.body.message=='点赞成功') {
          this.$message({
              message: "点赞成功",
              type: "success",
              customClass: "zIndex"
            })
          diaitem.like_num++;
          this.show(diaitem)
        }else{
          this.$message({
              message: "点赞失败",
              type: "danger",
              customClass: "zIndex"
            })
        }
      })
    },
    show(item) {
      if (this.uid) {
        this.useritem = {}
        this.dialogVisible = true;
        this.diaitem = item;
        this.getuserinfo(item.uid)
        this.getpicdetail(item.pid)
      }else{
        this.$message({
              message: "您还未登录",
              type: "warning",
              customClass: "zIndex"
            })
      }

    }
  }
};
</script>
<style>
.recommend {
  background-color: #ededef;
  width: 100%;
  min-height: 500px;
  height: auto;
  overflow: hidden;
}
.recommend-div {
  margin: 20px auto;
}
.cell-item{
  text-align: center;
  background: #fff;
  border: #bbbbbb solid 1px;
  margin-bottom: 20px;
}
.recommend-img {
  margin-top: 0;
  width: 100%;
  height: 80%;
  cursor: pointer;
}
.recommend-desc{
  margin: 5px 0 5px 10px;
  text-align: left;
  font-size: 14px;
  height: auto;
  word-wrap:break-word;
}
.recommend-footer{
  border-top:1px solid #F2F2F2;
}
.recommend-tx{
  width: 35px;
  height: 35px;
  margin: 10px 10px 10px 20px;
  vertical-align: middle;
}
.recommend-tx img{
  width: 100%;
  height: 100%;
  border-radius: 50%;
  vertical-align: middle;
  cursor: pointer;
}
.recommend-name{
  color: #9E7E6B;
  font-size: 14px;
  display: table-cell;
  vertical-align: middle;
  line-height: 55px;
  cursor: pointer;
}

.recommend-card-foot-btn button {
  padding: 0;
  border: none;
  margin-top: 25%;
}
.el-dialog__body {
  padding-top: 0;
}
.recommend-diahead {
  height: 50px;
  margin-bottom: 20px;
}
.recommend-diahead-tx {
  width: 50px;
  height: 50px;
  display: inline-block;
  text-align: center;
}
.recommend-diahead-tx img {
  width: 100%;
  height: 100%;
  vertical-align: middle;
  cursor: pointer;
}
.recommend-diahead-name {
  width: auto;
  margin: auto 30px;
  font-size: 20px;
  text-align: center;
  display: inline-block;
  cursor: pointer;
}
.recommend-diahead-btn {
  display: inline-block;
}
.recommend-dia-cont {
  margin: 0 auto;
  width: auto;
  height: auto;
  text-align: center;
}
.recommend-dia-cont img{
  width: 100%;
  height: 100%;
}
.recommend-dia-text {
  margin: 10px 0 0 0px;
  display: inline-block;
  font-family: "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", tahoma, arial,
    simsun, "宋体";
  font-size: 1rem;
  color: #444;
}
.recommend-dia-tabs {
  margin-top: 40px;
  background-color: #fafafa;
  overflow: hidden;
}
.recommend-dia-like {
  font-size: 15x;
  text-align: center;
  height: 25px;
  width: 70px;
  margin-top: 10px;
  float: right;
}
.recommend-dia-like button{
  border: none;
  padding: 0;
}
.recommend-dia-like span{
  line-height: 15px;
  margin-left: 10px;
  display:inline-block;
  position: relative;
  top:-29%;
}
.recommend-hot {
  border-bottom: 1px solid #e7e7e7;
  padding: 10px 0 10px 10px;
  font-size: 12px;
}
.recommend-hot-tx {
  display: inline-block;
  width: 30px;
  height: 30px;
  text-align: center;
}
.recommend-hot-tx img {
  width: 100%;
  height: 100%;
  vertical-align: middle;
}
.recommend-hot-name {
  display: inline-block;
  margin: 0 10px;
  color: #7594b3;
}
.recommend-coms-com {
  display: inline-block;
}
.recommend-dia-comment {
  margin: 10px auto;
  width: 800px;
}
.recommend-dia-input {
  display: inline-block;
  width: 700px;
}
.recommend-dia-btn {
  display: inline-block;
  margin-left: 10px;
}
  .wait-loading{
    margin-left:210%!important;
  }
  .loadingDisplay{
    display: none;
  }
</style>
