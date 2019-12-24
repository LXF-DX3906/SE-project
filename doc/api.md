## api文档
### fabu
url | 方法 | 后端接收数据 | 前端接收数据 
:-: | :-: | :-: | --- 
**/upload** | **post** | **image** | **message<br />image(此为url)<br />weight<br />height** 
/pictureUpload | post | uid<br />position(url)<br />type_name<br />description<br />weight<br />height<br /> | message 
### header
url | 方法 | 后端接收数据 | 前端接收数据 
:-: | :-: | :-: | --- 
/basicInfo | post | uid | username<br />phone<br />email 
/phoneLogin | post | phone<br />password | message<br />uid 
/emailLogin | post | email<br />password | message<br />uid 
/phoneRegister | post | phone<br />password | message<br />uid 
/emailRegister | post | email<br />password | message<br />uid 
### others
url | 方法 | 后端接收数据 | 前端接收数据 
:-: | :-: | :-: | --- 
/addFocus | post | uid<br />uuid(别人的uid) | message 
/basicInfo | post | uid(别人的id) | username<br />phone<br />email<br />introduce<br />headImage<br />fans<br />follow 
### upload
url | 方法 | 后端接收数据 | 前端接收数据 
:-: | :-: | :-: | --- 
**/upload** | **post** | **image** | **message<br />image<br />weight<br />height** 
**/adminUpload** | **post** | **tid<br />description<br />position(url)<br />weight<br />height** |  **message** 

### album
url | 方法 | 后端接收数据 | 前端接收数据 
:-: | :-: | :-: | --- 
/galleryList | post | uid | gid(galleryId)<br />num(number)<br />list(图片列表:内容为url)<br />galleryName<br />status<br />description 
/galleryUpdate | post | gid<br />uid<br />galleryName<br />status<br />description | message 
/galleryDelete | post | gid<br />uid | message 
### albumdetail
url | 方法 | 后端接收数据 | 前端接收数据 
:-: | :-: | :-: | --- 
**/upload** | **post** | **image** | **message<br />image<br />weight<br />height** 
/galleryUpload | post | uid<br />gid<br />position<br />weight<br />height<br />description | message 
/galleryDetail | post | gid | **id(人为编造的顺序id)**<br />**pid(在图库中的id)**<br />position 
/delete | post | uid<br />pid | message 
### collection
url | 方法 | 后端接收数据 | 前端接收数据 
:-: | :-: | :-: | --- 
/collect | post | uid |   **id(人为编造的顺序id)**<br />**pid(在图库中的id)**<br />position 
/pictureCollectDelete | post | uid<br />pid | message 
### fansfollows
url | 方法 | 后端接收数据 | 前端接收数据 
:-: | :-: | :-: | --- 
/focus | post | uid | uid<br />username<br />headImage 
/deleteFocus | post | uid<br />uuid | message(前端没有相应判断) 
### guanzhu
url | 方法 | 后端接收数据 | 前端接收数据 
:-: | :-: | :-: | --- 
/userFocus | post | uid | **cid(人为编造的顺序id)**<br />position(图片url)<br />headImage(作者头像)<br />uid(作者uid)<br />username(作者)<br />pid(图片在图库id)<br />likeNum(图片被关注数)<br />comments(评论)<br />decription(图片描述)<br />comment(评论者组,具体为:fromHeadImage,username,content) 
/userComment | post | pid<br />uid<br />uuid<br />content | message(前端代码只有body) 
/pictureLike | post | uid<br />pid | message 

### Like

|        url         | 描述                                         | 方法 |                   后端接收数据                    | 前端接收数据                                                 |
| :----------------: | -------------------------------------------- | :--: | :-----------------------------------------------: | ------------------------------------------------------------ |
|     /addFocus      | 添加关注                                     | post |   uid(当前用户id)<br/>uuid（要关注的用户的id）    | message（'关注成功'/其他）                                   |
| /pictureLikeDelete | 取消点赞图片                                 | post |        pid（图片id）<br/>uid（当前用户id）        | message('取消点赞成功'/其他）                                |
|     /basicInfo     | 获取图片对应的用户信息（点击查看图片详情时） | post |              uid（图片对应的用户id）              | body（赋给了一个useritem的数组？useritem有head_image和username两个属性） |
|   /pictureDetail   | 获取图片详细信息                             | post |                   pid（图片id）                   | body（赋给了一个picdetail的数组？picdetail有comment，from_head_image，from_username，content属性） |
|    /userComment    | 用户添加评论                                 | post | pid<br/>uid<br/>uuid<br/>content（评论内容）<br/> | message('评论成功'/其他）                                    |

### myshequ

|      url       | 描述             | 方法 |                         后端接收数据                         | 前端接收数据                                                 |
| :------------: | ---------------- | :--: | :----------------------------------------------------------: | ------------------------------------------------------------ |
|   /basicInfo   | 获取当前用户信息 | post |                             uid                              | body，有username，introduce，head_image，follow，fans四个属性 |
| /createGallery | 创建相册         | post | uid<br/>gallery_name（相册名）<br/>status（相册状态）<br/>description（相册描述）<br/> | message('创建成功'/其他）                                    |

### mywork

|      url       | 描述                                 | 方法 |            后端接收数据             | 前端接收数据                                                 |
| :------------: | ------------------------------------ | :--: | :---------------------------------: | ------------------------------------------------------------ |
|  /myPictures   | 获取当前用户的所有作品（图片）的信息 | post |           uid(当前用户id)           | body（赋给一个imgs的数组，有id，position和pid属性）          |
| /pictureDetail | 获取图片详情                         | post |            pid（图片id）            | body（赋给了一个picdetail的数组？picdetail有comment，from_head_image，from_username，content属性） |
|    /deletes    | 删除图片                             | post | pid（图片id）<br/>uid（当前用户id） | message('删除成功'/其他）                                    |

### tuijian

|        url        | 描述               | 方法 |                  后端接收数据                   | 前端接收数据                                                 |
| :---------------: | ------------------ | :--: | :---------------------------------------------: | ------------------------------------------------------------ |
|     /addFocus     | 添加关注           | post |  uid(当前用户id)<br/>uuid（要关注的用户的id）   | message（'关注成功'/其他）                                   |
| /pictureUsersList | 获取推荐的图片列表 | post |                      null                       | body（赋给一个imgs数组，有id，position，description，head_image，uid，username属性） |
|    /basicInfo     | 获取当前用户信息   | post |                uid（当前用户id）                | body，有username，head_image属性                             |
|  /pictureDetail   | 获取图片详细信息   | post |                       pid                       | body（赋给了一个picdetail的数组？picdetail有comment，from_head_image，from_username，content属性） |
|   /userComment    | 用户添加评论       | post | pid<br />uid<br />uuid<br />content（评论内容） | message('评论成功'/其他）                                    |
|   /pictureLike    | 用户点赞图片       | post |                   uid<br/>pid                   | message('点赞成功'/其他）                                    |

### gallary

|          url          | 描述             | 方法 |            后端接收数据             | 前端接收数据                                                 |
| :-------------------: | ---------------- | :--: | :---------------------------------: | ------------------------------------------------------------ |
|      /typeSearch      | 根据类型搜索图片 | post |             tid(类型id)             | body（赋给一个imgs数组，有pid，weigth，heigth，position属性） |
| /pictureCollectDelete | 获取收藏信息     | post | pid（图片id）<br/>uid（当前用户id） | message('未点赞'/其他）                                      |
|    /pictureCollect    | 收藏图片         | post | pid（图片id）<br/>uid（当前用户id） | message('收藏成功'/其他）                                    |
| /pictureCollectDelete | 取消收藏图片     | post | pid（图片id）<br/>uid（当前用户id） | message('取消收藏成功'/其他）                                |

### search

|       url       | 描述               | 方法 |          后端接收数据           | 前端接收数据                                                 |
| :-------------: | ------------------ | :--: | :-----------------------------: | ------------------------------------------------------------ |
| /keywordSearch  | 根据关键字搜索图片 | post |        keywords(关键字)         | body（赋给一个imgs数组，有pid，weigth，heigth，position属性） |
| /pictureCollect | 收藏图片           | post | pid（图片id） uid（当前用户id） | message('收藏成功'/其他）                                    |

### notice

|    url    | 描述         | 方法 |  后端接收数据   | 前端接收数据                                             |
| :-------: | ------------ | :--: | :-------------: | -------------------------------------------------------- |
| /pushList | 获取item数据 | post | uid(当前用户id) | body（赋给一个items数组，有id、introduce、position属性） |

### account

|     url      | 描述             | 方法 |                后端接收数据                 | 前端接收数据               |
| :----------: | ---------------- | :--: | :-----------------------------------------: | -------------------------- |
|  /basicInfo  | 获取用户基本信息 | post |               uid(当前用户id)               | body（有email、phone属性） |
| /updateEmail | 保存邮箱         | post |  emali(当前用户邮箱)<br/>uid（当前用户id）  | message('编辑成功'/其他）  |
| /updatePhone | 保存手机号       | post | phone(当前用户手机号)<br/>uid（当前用户id） | message('编辑成功'/其他）  |

### profile

|       url        | 描述         | 方法 |                         后端接收数据                         | 前端接收数据                                                 |
| :--------------: | ------------ | :--: | :----------------------------------------------------------: | ------------------------------------------------------------ |
|    /basicInfo    | 获取用户信息 | post |                       uid(当前用户id)                        | body（有username、sex、introduce、city、province、birthday、head_image属性） |
|   /updateInfo    | 更新用户信息 | post | uid(当前用户id)<br/>username(当前用户名字)<br/>sex(当前用户性别)<br/>birthday(当前用户生日)<br/>introduce(当前用户介绍)<br/>province(当前用户省份)<br/>city(当前用户城市)<br/> | message('编辑成功'/其他）                                    |
|     /upload      | 上传头像     | post |                       image(头像文件)                        | uid(用户id)<br/>image(头像路径)                              |
| /headimageUpload | 更新头像     | post |            uid(当前用户id)<br/>position(头像路径)            | message('修改成功'/其他）                                    |

