<template>
  <div class="community">
    <div class="head" :style="{backgroundImage:'url('+bgurl+')'}">
      <div class="head-bg">
        <ul class="community-ul">
          <li class="community-tx">
            <img v-lazy="txurl">
          </li>
          <li class="community-li2">
            <ul class="community-ul2">
              <li class="community-name" v-text="username"></li>
              <li>
                <ul class="community-ul3">
                  <li>
                      <el-button type="text" @click="show('follows');showfollows()">关注&nbsp;{{follows}}</el-button>
                  </li>
                  <li>
                      <el-button type="text" @click="show('fans');showfans()">粉丝&nbsp;{{fans}}</el-button>
                  </li>
                </ul>
              </li>
              <li class="community-intr" v-text="introduction"></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
    <el-container>
      <el-header>
        <el-row type="flex" justify="center" class="community-head">
          <el-col :xs="1" :sm="1" :md="1" :lg="1" :xl="1" :class="['community-btn',flag1?'community-btn-act':'']">
            <el-button type="text" @click="show('mywork');showwork()">我的作品</el-button>
          </el-col>
          <el-col
            :xs="1"
            :sm="1"
            :md="1"
            :lg="1"
            :xl="1"
            :class="['community-btn','community-like',flag2?'community-btn-act':'']"
          >
            <el-button type="text" @click="show('mylike');showlike()">喜欢</el-button>
          </el-col>
          <el-col
            :xs="1"
            :sm="1"
            :md="1"
            :lg="1"
            :xl="1"
            :class="['community-btn','community-collection',collection?'community-btn-act':'']"
          >
            <el-button type="text" @click="show('mycollection');showcollection()">收藏</el-button>
          </el-col>
          <el-col :xs="1" :sm="1" :md="1" :lg="1" :xl="1" :class="['community-btn',flag3?'community-btn-act':'']">
            <el-button type="text" @click="show('myalbum');showalbum()">相册</el-button>
          </el-col>
          <div v-if="flag4" :class="[flag5?'community-head-btn3':'community-head-btn']">
            <el-button size="mini" class="mycommunity-btn-al" @click="createalbum">新建相册</el-button>
          </div>
        </el-row>

        <el-dialog title="新建相册" :visible.sync="newalbum">
          <el-form>
            <el-form-item label="相册名称" :label-width="formLabelWidth1">
              <el-input v-model="alname" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="相册描述" :label-width="formLabelWidth1">
              <el-input v-model="alinfo" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="是否公开" :label-width="formLabelWidth2">
              <el-select v-model="status" placeholder="请选择是否公开">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="newalbum = false">取 消</el-button>
            <el-button type="primary" @click="createsubmit">确 定</el-button>
          </div>
        </el-dialog>
      </el-header>
      <el-main class="mycommunity-main">
        <router-view ref="child" v-on:getmsg="getmsg"></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "mycommunity",
  components: {},
  data() {
    return {
      comName: this.$route.path,
      flag1: true,
      flag2: false,
      flag3: false,
      flag4: false,
      flag5: false,
      collection: false,
      uid:localStorage.getItem('uid'),
      username: "",
      txurl: require("../../assets/tx6.jpg"),
      bgurl: require("../../assets/shequ-bg.jpg"),
      introduction: "做个自我介绍吧..",
      follows: '',
      fans: '',
      newalbum: false,
      alname:'',
      alinfo:'',
      status:0,
      options:[
        {
          value: 0,
          label: "公开"
        },
        {
          value: 1,
          label: "私密"
        }
      ],
      formLabelWidth1: "100px",
      formLabelWidth2: "100px"
    };
  },
  created() {
    this.getinfo()
    if (/follows/gi.test(this.comName)) {
      this.flag1 = false;
      this.flag2 = false;
      this.flag3 = false;
      this.flag4 = false;
      this.collection = false;
    } else if (/fans/gi.test(this.comName)) {
      this.flag1 = false;
      this.flag2 = false;
      this.flag3 = false;
      this.flag4 = false;
      this.collection = false;
    } else if (/mywork/gi.test(this.comName)) {
      this.flag1 = true;
      this.flag2 = false;
      this.flag3 = false;
      this.flag4 = false;
      this.collection = false;
    } else if (/mylike/gi.test(this.comName)) {
      this.flag1 = false;
      this.flag2 = true;
      this.flag3 = false;
      this.flag4 = false;
      this.collection = false;
    } else if (/mycollection/gi.test(this.comName)) {
      this.flag1 = false;
      this.flag2 = false;
      this.flag3 = false;
      this.flag4 = false;
      this.collection = true;
    } else if (/detail/gi.test(this.comName)) {
      this.flag1 = false;
      this.flag2 = false;
      this.flag3 = true;
      this.flag4 = false;
      this.collection = false;
    } else if (/myalbum/gi.test(this.comName)) {
      this.flag1 = false;
      this.flag2 = false;
      this.flag3 = true;
      this.flag4 = true;
      this.collection = false;
    }
  },
  watch: {
    $route: function(to) {
      if (/follows/gi.test(to.path)) {
      this.flag1 = false;
      this.flag2 = false;
      this.flag3 = false;
      this.flag4 = false;
      this.collection = false;
    } else if (/fans/gi.test(to.path)) {
      this.flag1 = false;
      this.flag2 = false;
      this.flag3 = false;
      this.flag4 = false;
      this.collection = false;
    } else if (/mywork/gi.test(to.path)) {
      this.flag1 = true;
      this.flag2 = false;
      this.flag3 = false;
      this.flag4 = false;
      this.collection = false;
    } else if (/mylike/gi.test(to.path)) {
      this.flag1 = false;
      this.flag2 = true;
      this.flag3 = false;
      this.flag4 = false;
      this.collection = false;
    } else if (/mycollection/gi.test(to.path)) {
      this.flag1 = false;
      this.flag2 = false;
      this.flag3 = false;
      this.flag4 = false;
      this.collection = true;
    } else if (/detail/gi.test(to.path)) {
      this.flag1 = false;
      this.flag2 = false;
      this.flag3 = true;
      this.flag4 = false;
      this.collection = false;
    } else if (/myalbum/gi.test(to.path)) {
      this.flag1 = false;
      this.flag2 = false;
      this.flag3 = true;
      this.flag4 = true;
      this.collection = false;
    }
    }
  },
  methods: {
    getmsg(data){
      this.follows = data
    },
    getinfo(){
      this.$http.get('/api/basicInfo',{params:{uid:localStorage.getItem('uid')}},{emulateJSON:true})
     .then(result=>{
       if (result.body.username) {
          this.username=result.body.username;
       }else{
          this.username='注册用户';
       }
       this.introduction=result.body.introduce;
       this.txurl=this.$store.state.HOST+result.body.head_image;
       this.follows=result.body.follow;
       this.fans=result.body.fans;
     })
    },
    createalbum(){
      this.newalbum=true;
    },
    createsubmit(){
      this.$http.post('/api/createAlbum',{uid:this.uid,albumName:this.alname,status:this.status,description:this.alinfo},{emulateJSON:true})
      .then(res=>{
        if (res.body.message=="创建成功") {
          this.$message({
              message: "创建成功",
              type: "success",
              customClass: "zIndex"
            });
          this.$refs.child.getalbum()
          this.newalbum=false;
        }else{
          this.$message({
              message: "创建失败",
              type: "danger",
              customClass: "zIndex"
            });
          this.newalbum=false;
        }
      })
    },
    showalbum(){
      this.$router.push({path: "/community/mycommunity/myalbum",query:{uid:this.uid}})
    },
    showwork(){
      this.$router.push({path: "/community/mycommunity/mywork",query:{my:true,uid:this.uid}})
    },
    showlike(){
      this.$router.push({path: "/community/mycommunity/mylike",query:{my:true,uid:this.uid}})
    },
    showcollection(){
      this.$router.push({path: "/community/mycommunity/mycollection",query:{my:true,uid:this.uid}})
    },
    showfans(){
      this.$router.push({ path: "/community/mycommunity/fans",query:{fans:this.fans,uid:this.uid,follows:this.follows} });
    },
    showfollows(){
      this.$router.push({ path: "/community/mycommunity/follows",query:{follows:this.follows,uid:this.uid} });
    },
    show(data) {
      this.comName = data;
      if (/follows/gi.test(this.comName)) {
        this.flag1 = false;
        this.flag2 = false;
        this.flag3 = false;
        this.flag4 = false;
        this.collection = false;
      } else if (/fans/gi.test(this.comName)) {
        this.flag1 = false;
        this.flag2 = false;
        this.flag3 = false;
        this.flag4 = false;
        this.collection = false;
      } else if (/mywork/gi.test(this.comName)) {
        this.flag1 = true;
        this.flag2 = false;
        this.flag3 = false;
        this.flag4 = false;
        this.collection = false;
      } else if (/mylike/gi.test(this.comName)) {
        this.flag1 = false;
        this.flag2 = true;
        this.flag3 = false;
        this.flag4 = false;
        this.collection = false;
      } else if (/mycollection/gi.test(this.comName)) {
        this.flag1 = false;
        this.flag2 = false;
        this.flag3 = false;
        this.flag4 = false;
        this.collection = true;
      } else if (/detail/gi.test(this.comName)) {
        this.flag1 = false;
        this.flag2 = false;
        this.flag3 = true;
        this.flag4 = false;
        this.collection = false;
      } else if (/myalbum/gi.test(this.comName)) {
        this.flag1 = false;
        this.flag2 = false;
        this.flag3 = true;
        this.flag4 = true;
        this.collection = false;
      }
    }
  }
};
</script>

<style>
.community {
  min-height: 600px;
  height: auto;
}
.head {
  height: 350px;
  width: 100%;
  background-repeat: no-repeat;
  background-size: cover;
  overflow: hidden;
}
.head-bg {
  height: 350px;
  width: 100%;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.5));
  overflow: hidden;
}
.community-ul {
  list-style: none;
  margin: 13% 0 0 0;
}
.community-ul li {
  color: #ffffff;
  text-align: center;
}
.community-tx {
  width: 120px;
  height: 120px;
  display: inline-block;
}
.community-tx img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
.community-li2 {
  display: inline-block;
  position: relative;
  top: -20px;
}
.community-btn-editfm {
  display: inline-block;
}
.community-ul2 {
  list-style: none;
  margin: 0;
  padding: 0;
}
.community-name {
  font-size: 1.5rem;
  font-weight: bolder;
  margin-bottom: 15px;
}
.community-intr {
  margin-top: 15px;
  font-size: 13px;
}
.community-ul3 {
  margin: 0;
  list-style: none;
}
.community-ul3 span {
  color: #ffffff !important;
}
.community-ul3 li {
  display: inline;
  font-size: 14px;
}
.community-ul3 li:before {
  content: "/";
  color: #e0e0e0;
  font-size: 14px;
  margin: 0 10px;
}
.community-btn-editfm {
  float: right;
  margin-right: 50px;
  position: relative;
  bottom: -65px;
}
.community-btn-editfm span {
  color: #ffffff !important;
}
.community-head {
  border-bottom: #bbbbbb solid 1px;
}
.community-btn {
  text-align: center;
  font-family: "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", tahoma, arial,
    simsun, "宋体";
}
.community-like {
  margin: 0 40px 0 50px;
}
.community-collection {
  margin: 0 40px 0 0;
}
.community-head-btn {
  position: relative;
  right: -31%;
}

.community-btn span {
  font-size: 0.8rem;
  color: #000000;
}
.community-btn-act {
  border-bottom: #41b93b solid 2px;
}
.community-btn-act span {
  color: #41b93b;
}
.mycommunity-main {
  min-height: 500px;
  height: auto;
  width: auto;
  background-color: #f9f9f9;
}
.mycommunity-btn-al {
  margin-top: 8px;
  border: #009688 solid 1px !important;
}
.mycommunity-btn-al span {
  color: #009688;
}
</style>

