<template>
  <div class="detail-body">
    <div class="aldetail">
      <el-row type="flex" justify="center">
        <el-col :span="23">
          <div class="aldetail-head" v-if="my">
<!--            <input type="file" @change="changeimg($event)" ref="imgInput" style="display:none;">-->
            <el-button size="mini" class="aldetail-head-btn" @click="uploadimg">上传图片</el-button>
          </div>
          <waterfall
            class="aldetail-div"
            :col="col"
            :width="itemWidth"
            :gutterWidth="gutterWidth"
            :data="imgs"
            @loadmore="loadmore"
            @scroll="scroll"
          >
            <template>
              <div class="aldetail-item" v-for="img in imgs" :key="img.pid">
                <div class="aldetail-img">
                  <img :src="img.position">
                </div>
                <div class="aldetail-shadow">
                  <div class="aldetail-det">
                    <el-button type="text" @click="show(img)">查看详情</el-button>
                  </div>
                  <div v-if="my" class="aldetail-line">
                    <div class="aldetail-btn">
                      <el-button type="text" @click="deletedetail(img.pid)">删除</el-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </waterfall>
        </el-col>
      </el-row>
    </div>
    <el-dialog :visible.sync="dialogVisible" width="70%">
      <div class="colec-dia-cont">
        <img :src="diaitem.position">
      </div>
    </el-dialog>

    <el-dialog :visible.sync="uploadToAlbum">
      <el-form class="album-dia" v-model="form">
        <el-checkbox-group v-model="form.adds">
          <el-row>
            <el-col :span="8" v-for="pic in pics" :key="pic.pid" >
              <el-card :body-style="{ padding: '0px' }">
                <el-checkbox-button :label="pic.pid"><img :src="pic.position" class="image"></el-checkbox-button>
              </el-card>
            </el-col>
          </el-row>
        </el-checkbox-group>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="uploadToAlbum = false">取 消</el-button>
        <el-button type="primary" @click="galleryUpload">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "albumdetail",
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
      gid: this.$route.query.gid,
      uid: this.$route.query.uid,
      my: ifMy,
      col: 5,
      imgs: [],
      uploadToAlbum:false,
      pics:[],
      form:{
        adds:[],
      },
    };
  },
  created() {
    this.getalbumdetail();
  },
  computed: {
    itemWidth() {
      return 138 * 0.5 * (document.documentElement.clientWidth / 310);
    },
    gutterWidth() {
      return 9 * 0.5 * (document.documentElement.clientWidth / 400);
    }
  },
  methods: {
    // changeimg(e) {
    //   // var file = e.target.files[0];
    //   // var image = new FormData();
    //   // image.append("file", file);
    //   // this.$http.post("/api/upload", image).then(res => {
    //   //   if (res.body.message == "上传成功") {
    //   //     this.galleryUpload(res.body.image, res.body.weight, res.body.height);
    //   //   }
    //   // });
    //   console.log(this.form.adds)
    // },
    uploadimg() {
      this.$http.post("/api/albumAddPic",{gid: this.gid,uid: this.uid},{emulateJSON:true})
      .then(res=> {
        if (res.body.message=="获取成功") {
          this.pics = [];
          let items = Object.assign(res.body.result)
          for (let item of items) {
            let new_item = {
              pid: item.pictureId,
              position: this.$store.state.HOST + item.position,
            }
            this.pics.push(new_item)
          }
        } else {
          this.$message({
            message: "获取失败",
            type: "error",
            customClass: "zIndex"
          });
        }
      })
      this.form.adds=[];
      this.uploadToAlbum=true
    },
    galleryUpload() {
      this.changealbum=false;
      console.log(JSON.parse(JSON.stringify(this.form.adds)))
      this.$http
        .post(
          "/api/albumUpload",
          {
            pids: this.form.adds,
            gid: this.gid,
          }
        )
        .then(res => {
          if (res.body.message == "添加成功") {
            this.$message({
              message: "添加成功",
              type: "success",
              customClass: "zIndex"
            });
          } else {
            this.$message({
              message: "添加失败",
              type: "error",
              customClass: "zIndex"
            });
          }
          this.getalbumdetail();
          this.form.adds=[];
          this.uploadToAlbum=false
        });
    },
    scroll() {},
    loadmore() {},
    getalbumdetail() {
      this.$http
        .post("/api/albumDetail", { gid: this.gid }, { emulateJSON: true })
        .then(res => {
          if (res.body.message == "获取成功") {
            this.imgs = [];
            let items = Object.assign(res.body.pictures)
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
        });
    },
    deletedetail(pid) {
      this.$http
        .post("/api/deleteFromAlbum", { gid: this.gid, pid: pid }, { emulateJSON: true })
        .then(res => {
          if (res.body.message == "删除成功") {
            this.$message({
              message: "删除成功",
              type: "success",
              customClass: "zIndex"
            });
            this.getalbumdetail();
          } else {
            this.$message({
              message: "删除失败",
              type: "warning",
              customClass: "zIndex"
            });
          }
        });
    },
    show(item) {
      this.dialogVisible = true;
      this.diaitem = item;
    }
  }
};
</script>

<style>
.detail-body {
  min-height: 300px;
  height: auto;
  display: flex;
  width: auto;
  margin: 0 auto;
}
.aldetail-head {
  display: block;
  margin: 10px auto;
  float: right;
  position: relative;
  left: -0.8%;
}
.aldetail-head-btn {
  margin-top: 8px;
  border: #d1b200 solid 1px !important;
}
.aldetail-head-btn span {
  color: #d1b200 !important;
}
.aldetail {
  height: auto;
  width: auto;
  margin: 0 auto;
}
.aldetail-div {
  margin: 20px auto;
}
.aldetail-item {
  text-align: center;
  margin-bottom: 20px;
  position: relative;
}
.aldetail-img img {
  width: 100%;
  height: 100%;
}

.aldetail-shadow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0);
  opacity: 0;
  transition: all 0.2s;
}
.aldetail-shadow:hover {
  background: rgba(0, 0, 0, 0.4);
  opacity: 1;
}
.aldetail-det {
  color: #fff;
  position: absolute;
  top: 35%;
  left: 40%;
}
.aldetail-det span {
  color: #fff;
}
.aldetail-line {
  position: absolute;
  top: 75%;
  width: 100%;
}
.aldetail-lc {
  color: #fff;
  font-size: 12px;
  display: inline-block;
}
.my-work-space {
  display: inline-block;
  width: 50%;
}
.aldetail-btn {
  display: inline-block;
}
.aldetail-btn span {
  color: #fff;
  font-size: 12px;
}

.el-dialog__body {
  padding-top: 0;
}
.el-tabs__content {
  padding: 0 !important;
}

.detail-dia-cont {
  margin: 0 auto;
  width: auto;
  height: auto;
  text-align: center;
}

.image {
  width: 100%;
  display: block;
}


</style>
