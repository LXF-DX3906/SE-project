<template>
  <div class="search">
    <div class="search-row">
    <el-row type="flex" justify="center" style="height:50px;">
      <el-col :span="11" class="search-col">
        <input type="file" @change="changeImg($event)" id="fileInput" style="display:none;">
        <el-input placeholder="请输入内容" v-model="input">
          <el-button slot="append" style="fontSize:20px;" icon="el-icon-search" @click="getdata(input)"></el-button>
          <label slot="suffix" for="fileInput">
            <img  src="../../assets/camera.png" class="camera-button">
          </label>>
        </el-input>
      </el-col>
    </el-row>
    </div>
    <vue-loading type="spiningDubbles"  class ="wait-loading " :class="{loadingDisplay: !isLoading}" color="#9e9e9e" :size="{ width: '50px', height: '50px' }"></vue-loading>
    <div class="search-result">
      <div
        class="search-result-div"
        v-for="(img) in imgs"
        :key="img.pid"
        :style="{width:img.weight*200/img.height+'px',flexGrow:img.weight*200/img.height}"
      >
        <i :style="{paddingBottom:img.height/img.weight*100+'%'}"></i>
        <img v-lazy="img.position" @click="showdia(img)">
      </div>
    </div>
    <el-dialog :visible.sync="dialogVisible" width="70%">
        <div class="dia-cont">
          <img v-lazy="diaitem.position">
        </div>
        <el-button type="text" @click="docollect(diaitem.pid)">收藏</el-button>
      </el-dialog>
  </div>
</template>
<script>
export default {
  name: "search",
  data() {
    return {
      uid:localStorage.getItem('uid'),
      input:this.$route.query.keywords,
      dialogVisible: false,
      diaitem:[],
      imgs: [],
      infoByImage: this.$route.query.imageSearch,
      isLoading: true
    };
  },
  created(){
    this.getdata(this.input)
  },
  methods: {
    getDataByImage(infoByImage) {
      console.log(infoByImage)
      this.imgs = []
      let imgs = Object.assign(infoByImage.result);
      for (let item of imgs) {
        let new_item = {
          pid: item.pictureId,
          position: this.$store.state.HOST + item.position,
          weight: item.width,
          height: item.height,
          description: item.description
        }
        this.imgs.push(new_item)
      }
    },
    getdata(keywords){
      this.$http.post('/api/keywordSearch',{keyword:keywords},{emulateJSON:true})
      .then(res=>{
        this.imgs = []
        let imgs=Object.assign(res.body.result);
        for (let item of  imgs) {
          let new_item = {
            pid: item.pictureId,
            position: this.$store.state.HOST + item.position,
            weight: item.weight,
            height: item.height,
            description: item.description
          }
          this.imgs.push(new_item)
          this.isLoading = false
        }
        let query = this.$router.history.current.query;
        let path = this.$router.history.current.path;
        //对象的拷贝
        let newQuery = JSON.parse(JSON.stringify(query));
        newQuery.keywords = keywords;
        this.$router.push({ path, query: newQuery });
        this.getDataByImage(this.infoByImage)
      })
    },
    showdia(item) {
      this.dialogVisible = true;
      this.diaitem = item;
    },
    docollect(pid){
      this.$http.post('/api/pictureCollect',{uid:this.uid,pid:pid},{emulateJSON:true})
      .then(res=>{
        if(res.body.message=="收藏成功"){
          this.$message({
              message: "收藏成功",
              type: "success",
              customClass: "zIndex"
            });
        }else{
          this.$message({
              message: "您已经收藏",
              type: "danger",
              customClass: "zIndex"
            });
        }
      })
    },
    changeImg(e) {
      this.isLoading = true
      console.log(e)
      var file = e.target.files[0];
      var image = new FormData();
      image.append("img", file);
      this.$http.post("/api/searchImgByImg", image).then(res => {
        if (res.body.message == "上传成功") {
          console.log(res)
          this.imgs = []
          let imgs=Object.assign(res.body.result);
          for (let item of  imgs) {
            let new_item = {
              pid: item.pictureId,
              position: this.$store.state.HOST + item.position,
              weight: item.width,
              height: item.height,
              description: item.description
            }
            this.imgs.push(new_item)
            this.isLoading = false
          }
          console.log(this.imgs)
        }
      });
    },
  }
};
</script>
<style>
.search {
  min-height: 550px;
  height: auto;
  background-color: #f5f5f5;
}
.search-row {
  background-color: #fff;
  height: 60px;
  position: fixed;
  top: 47px;
  width: 100%;
  z-index: 9999;
}
.search-col {
  position: absolute;
  margin-top: 10px;
}
.search-result {
  min-height: 300px;
  height: auto;
  display: flex;
  flex-wrap: wrap;
  width: 90%;
  margin: 0 auto;
  padding-top: 80px;
}
.search-result::after {
  content: "";
  flex-grow: 999999999;
}

.search-result-div {
  margin: 2px;
  background-color: violet;
  position: relative;
  overflow: hidden;
}

.search-result-div i {
  display: block;
}

.search-result-div img {
  position: absolute;
  top: 0;
  width: 100%;
  vertical-align: bottom;
  cursor: pointer;
  transition: all 0.6s;
}
.search-result-div img:hover {
  transform: scale(1.2);
}
.camera-button{
  margin-top: 7px;
  height: 25px;
  width: 25px;
}
.camera-button:hover{
  cursor: pointer;
}
.wait-loading{
  margin-left:210%!important;
}
.loadingDisplay{
  display: none;
}
</style>

