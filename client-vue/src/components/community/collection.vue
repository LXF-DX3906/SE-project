<template>
  <div class="collection">
    <el-row type="flex" justify="center">
      <el-col :span="23">
        <waterfall class="collec-div" :col="col" :width="itemWidth" :gutterWidth="gutterWidth" :data="imgs" @loadmore="loadmore"
      @scroll="scroll">
      <template>
        <div class="collec-item" v-for="img in imgs" :key="img.pid">
          <div class="collec-img">
            <img v-lazy="img.position">
          </div>
          <div class="collec-shadow">
          <div class="collec-det">
            <el-button type="text" @click="show(img)">查看详情</el-button>
          </div>
          <div v-if="my" class="collec-line">
            <div class="collec-btn"><el-button type="text" @click="deletecollection(img.pid)">取消收藏</el-button></div>
          </div>
      </div>
        </div>
      </template>
    </waterfall>
      </el-col>
    </el-row>

    <el-dialog :visible.sync="dialogVisible" width="70%">
      <div class="colec-dia-cont">
        <img v-lazy="diaitem.position">
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "collection",
  data() {
    let ifMy
    if (this.$route.query.my != undefined) {
      if (this.$route.query.my == "true") {
        ifMy = true
      } else if (this.$route.query.my == "false") {
        ifMy = false
      } else {
        ifMy = this.$route.query.my
      }
      console.log(this.$route.query.my)
    } else {
      console.log(this.$route.query.my)
      ifMy = false
    }
    return {
      dialogVisible: false,
      diaitem: [],
      activeName: "comments",
      comment: "",
      uid:this.$route.query.uid,
      col:4,
      imgs:[],
      my:ifMy
    };
  },
  created(){
    this.getmycollection()
  },
  computed: {
    itemWidth() {
      return 138 * 0.5 * (document.documentElement.clientWidth /310);
    },
    gutterWidth() {
      return 9 * 0.5 * (document.documentElement.clientWidth /400);
    }
  },
  methods: {
    scroll(scrollData) {
    },
    loadmore(index) {
    },
    getmycollection(){
      this.$http.get('/api/collect',{params:{uid:this.uid}},{emulateJSON:true})
      .then(res=>{
        if (res.body.message == "获取成功") {
          this.imgs = [];
          let items = Object.assign(res.body.result)
          for (let item of items) {
            let new_item = {
              pid: item.pictureId,
              position: this.$store.state.HOST + item.position,
            }
            this.imgs.push(new_item)
          }
        } else {
          this.$message({
            message: "获取失败",
            type: "error",
            customClass: "zIndex"
          });
        }
      })
    },
    deletecollection(pid){
      this.$http.delete('/api/pictureCollectDelete',{params:{uid:this.uid,pid:pid}},{emulateJSON:true})
      .then(res=>{
        if (res.body.message=="取消收藏成功") {
          this.$message({
              message: "取消收藏成功",
              type: "success",
              customClass: "zIndex"
            });
          this.getmycollection()
        }else{
          this.$message({
              message: "取消收藏失败",
              type: "danger",
              customClass: "zIndex"
            });
        }
      })
    },
    show(item) {
      this.dialogVisible = true;
      this.diaitem = item;
    }
  }
};
</script>

<style>
.collection {
  min-height: 300px;
  height: auto;
  /*display: flex;*/
  width: auto;
  margin: 0 auto;
}
.collec-div {
  margin: 20px auto;
}
.collec-item{
  text-align: center;
  margin-bottom: 20px;
  position: relative;
}
.collec-img img{
  width: 100%;
  height: 100%;
}

.collec-shadow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0);
  opacity: 0;
  transition: all 0.2s;
}
.collec-shadow:hover {
  background: rgba(0, 0, 0, 0.4);
  opacity: 1;
}
.collec-det {
  color: #fff;
  position: absolute;
  top: 35%;
  left: 40%;
}
.collec-det span {
  color: #fff;
}
.collec-line{
  position: absolute;
  top: 75%;
  width: 100%;
}
.collec-lc {
  color: #fff;
  font-size: 12px;
  display: inline-block;
}
.my-work-space{
  display: inline-block;
  width: 50%;
}
.collec-btn {
  display: inline-block;
}
.collec-btn span {
  color: #fff;
  font-size: 12px;
}

.el-dialog__body {
  padding-top: 0;
}

.colec-dia-cont {
  margin: 20px auto;
  width: auto;
  height: auto;
  text-align: center;
}
.colec-dia-cont img{
  width: 100%;
  height: 100%;
}
</style>


