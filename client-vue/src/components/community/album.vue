<template>
  <div class="al">
    <!--我的相册-->
    <el-row class="al-row" v-if="myalbum">
        <vue-loading type="spiningDubbles"  class ="wait-loading " :class="{loadingDisplay: !isLoading}" color="#9e9e9e" :size="{ width: '50px', height: '50px' }"></vue-loading>

        <el-col :span="5" class="al-col" v-for="item in datalist" :key="item.gid">
       <div class="al-two" v-if="item.num==0">
          <div class="al-one" :style="{backgroundImage:'url('+bgurl+')'}" @click="detail(item.gid)"></div>
        </div>
        <div class="al-two" v-else-if="item.num==1">
          <div class="al-one" :style="{backgroundImage:'url('+hostUrl+item.list[0].position+')'}" @click="detail(item.gid)"></div>
        </div>
        <div class="al-two" v-else-if="item.num>1" :style="{backgroundImage:'url('+hostUrl+item.list[1].position+')'}">
          <div class="al-one" :style="{backgroundImage:'url('+hostUrl+item.list[0].position+')'}" @click="detail(item.gid)"></div>
        </div>

        <ul class="al-ul">
          <li class="al-name">{{item.gallery_name}}</li>
          <li class="alli-btn"><el-button class="al-btn" icon="el-icon-edit" @click="changeal(item.gid,item.gallery_name,item.status,item.description)"></el-button></li>
          <li class="al-num">{{item.num}}张</li>
          <li class="al-open" v-if="item.status==0">公开</li>
          <li class="al-open" v-else>私密</li>
        </ul>
         <ul class="al-ul">
             <li class="al-desc">{{item.description}}</li>
         </ul>
      </el-col>
    </el-row>
      <!--其他用户的相册-->
      <el-row class="al-row" v-else>
      <el-col :span="5" class="al-col" v-for="item in datalist" v-if="item.status==0" :key="item.gid">
       <div class="al-two" v-if="item.num==0">
          <div class="al-one" :style="{backgroundImage:'url('+bgurl+')'}" @click="detail(item.gid)"></div>
        </div>
        <div class="al-two" v-else-if="item.num==1">
          <div class="al-one" :style="{backgroundImage:'url('+hostUrl+item.list[0].position+')'}" @click="detail(item.gid)"></div>
        </div>
        <div class="al-two" v-else-if="item.num>1" :style="{backgroundImage:'url('+hostUrl+item.list[1].position+')'}">
          <div class="al-one" :style="{backgroundImage:'url('+hostUrl+item.list[0].position+')'}" @click="detail(item.gid)"></div>
        </div>
        <ul class="al-ul">
          <li class="al-name">{{item.gallery_name}}</li>
          <li class="al-num">{{item.num}}张</li>
            <li class="al-open"></li>
        </ul>
          <ul class="al-ul">
              <li class="al-desc">{{item.description}}</li>
          </ul>
      </el-col>
    </el-row>
    <el-dialog :visible.sync="changealbum">
      <el-form class="album-dia">
            <el-form-item label="相册名称" :label-width="formLabelWidth1">
              <el-input v-model="albumname" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="相册描述" :label-width="formLabelWidth1">
              <el-input v-model="albuminfo" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="是否公开" :label-width="formLabelWidth2">
              <el-select v-model="status" placeholder="请选择是否公开">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="changealbum = false">取 消</el-button>
            <el-button type="danger" @click="deletealbum(gid)">删 除</el-button>
            <el-button type="primary" @click="edit(gid)">确 定</el-button>
          </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: "album",
  data() {
    return {
      hostUrl:this.$store.state.HOST,
      style: "",
      bgurl: require("../../assets/nopic.jpg"),
      comName: this.$route.path,
      uid:this.$route.query.uid,
      datalist:[],
      changealbum:false,
      albumname:'',
      albuminfo:'',
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
      status:0,
      gid:'',
      formLabelWidth1: "100px",
      formLabelWidth2: "100px",
      myalbum:this.$route.query.uid==localStorage.getItem('uid')?true:false,
        isLoading: true
    };
  },
  created(){
      this.getalbum()
  },
  methods: {
     getalbum(){
         this.isLoading = true
      this.$http.get('/api/albumList',{params:{uid:this.uid}},{emulateJSON:true})
      .then(res=>{
          if (res.body.message=="获取成功") {
              this.datalist = []
              let albums=Object.assign(res.body.albums);
              let picturesInAlbum=Object.assign(res.body.pictures);
              let picturelist = []
              let index = 0
              for(let album of albums) {
                  picturelist = picturesInAlbum[index]
                  let item = {
                      gid:album.albumId,
                      num:picturelist.length,
                      list:picturelist,
                      gallery_name:album.albumName,
                      status: Number(album.status),
                      description: album.description
                  }
                  index++
                  this.datalist.push(item)
              }
          } else {
              this.$message({
                  message: "获取失败",
                  type: "error",
                  customClass: "zIndex"
              });
          }
          this.isLoading = false
      })
    },
    changeal(gid,name,status,desc){
      this.gid=gid;
      this.changealbum=true;
      this.albumname=name;
      this.status=status;
      this.albuminfo=desc;
    },
    edit(gid){
      this.$http.put('/api/albumUpdate',{gid:gid,uid:this.uid,albumName:this.albumname,status:this.status,description:this.albuminfo},{emulateJSON:true})
      .then(res=>{
        if (res.body.message=="修改成功") {
          this.$message({
              message: "修改成功",
              type: "success",
              customClass: "zIndex"
            });
          this.getalbum()
          this.changealbum=false;
        }else{
          this.$message({
              message: "修改失败",
              type: "danger",
              customClass: "zIndex"
            });
          this.changealbum=false;
        }
      })
    },
    deletealbum(gid){
      this.$http.delete('/api/albumDelete',{params:{gid:gid,uid:this.uid}},{emulateJSON:true})
      .then(res=>{
        if (res.body.message=="删除成功") {
          this.$message({
              message: "删除成功",
              type: "success",
              customClass: "zIndex"
            });
          this.getalbum()
          this.changealbum=false;
        }else{
          this.$message({
              message: "删除失败",
              type: "danger",
              customClass: "zIndex"
            });
        }
      })
    },
    detail(gid) {
      if (/others/gi.test(this.comName)) {
        this.$router.push({ path: "/community/others/album/detail",query:{my:false,gid:gid,uid:this.uid}});
      } else {
        this.$router.push({ path: "/community/mycommunity/myalbum/detail" ,query:{my:true,gid:gid,uid:this.uid}});
      }
    }
  }
};
</script>
<style>
.al {
  margin-top: 10px;
}
.al-row {
  margin-left: 10px;
}
.al-col {
  width: 300px;
  height: 240px;
  margin: 10px 10px 10px 10px;
}
.al-one {
  width: 280px;
  height: 200px;
  background: no-repeat;
  background-size: cover;
  position: relative;
  left: 15px;
  top: 10px;
  cursor: pointer;
}
.al-two {
  width: 280px;
  height: 200px;
  background: no-repeat;
  background-size: cover;
}
.al-ul {
  margin: 15px 0 0 0;
  padding: 0;
}
.al-ul li {
  list-style: none;
  display: inline-block;
}
.al-name {
  float: left;
  font-size: 15px;
  color: #000;
}
.al-desc {
    float: left;
    font-size: 15px;
    color: #000;
}
.al-num {
  font-size: 5px;
  color: #85888a;
  float: right;
}
.al-open{
  font-size: 5px;
  color: #85888a;
  margin-left: 10px;
}
.alli-btn{
  margin-left: 10px;
}
.al-btn {
  margin: 0 !important;
  padding: 0 !important;
  width: 100% !important;
  height: 100% !important;
}
.album-dia{
  margin-top: 30px;
}
.wait-loading{
    margin-left:210%!important;
}
.loadingDisplay{
    display: none;
}
</style>
