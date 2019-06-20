# 饺子视频改版  (基本完结)
欢迎转载(转载请附链接 https://github.com/L1145418863/jzExpand )<br/>欢迎评论,欢迎吐槽,欢迎提意见 咱邮箱是:1145418863@qq.com<br/>
        如有某位大佬偶然看到 还望得到指点<br/>
饺子视频改版(jiaozivideoplayer) ----- 基于饺子视频缝补<br/>
[该空间 还包括一个基于Android mediaPlayer 的一个音频播放  (mymusicplayer)  属于我自己写的 最近项目需要就自己写了个]<br/>
[部分图标比较比较难看 建议使用时更替  尤其是饺子视频 我并没有替换成套的图标 有些地方巨丑 全屏后的更多设置弹框 第二个是改变亮度的 因为没有合适的图 暂时拿着音量的去展示的 谅解]<br/>
  添加倍速<br/>
  添加分集<br/>
  弹框样式修改<br/>
  进度条样式修改<br/>

     虽然并不太完善 但是咱还是有提升空间的
     也建议大家给我这个菜鸡点个start ☆ 也好有点动力 谢谢~~
     如有问题请大家指正 万分感谢


全屏所有弹框 皆基于横屏开发 全屏竖屏状态会比较丑 待改~<br/>
增加一条属性(饺子原带)可避免全屏时视频竖屏播放<br/>

        //设置全屏播放
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;  //横向


  使用示例   更具体用法 请看 \app\src\main\java\com\example\administrator\newjz\MainActivity <br/>

    布局引用与原版一致

    //变量
    private String[] mediaName = {"普通", "原画"};//清晰度  如果不做清晰度可直接当做选集使用 属于饺子视频原
    private List<Object[]> list = new ArrayList<Object[]>();//选集  清晰度和选集可同时呈现
    //使用示例
    Object[] objects = new Object[3];
            LinkedHashMap map = new LinkedHashMap();
            for (int i = 0; i < 2; i++) {
                map.put(mediaName[i], "视频地址");
            }
            objects[0] = map;
            objects[1] = false;
            objects[2] = new HashMap<>();
            ((HashMap) objects[2]).put("key", "value");
    //--------------------------------------------------------------------------------
            Object[] objects1 = new Object[3];
            LinkedHashMap map1 = new LinkedHashMap();
            for (int i = 0; i < 2; i++) {
                map1.put(mediaName[i], "视频地址");
            }
            objects1[0] = map1;
            objects1[1] = false;
            objects1[2] = new HashMap<>();
            ((HashMap) objects1[2]).put("key", "value");
    //--------------------------------------------------------------------------------
            list.add(objects1);
            list.add(objects);

            jzvideoplayerstandard.addSelection(list);//不使用则无选集功能

            Glide.with(this)//图片 饺子视频的封面图
                    .load("http://image.onlyboss.com/39/9ccef74e0006e114e1d894ae60ec7a.jpg")
                    .into(jzvideoplayerstandard.thumbImageView);
            /**
             * 清晰度切换
             */
            jzvideoplayerstandard.setUp(list.get(0), 0
                    , JZVideoPlayerStandard.SCROLL_AXIS_HORIZONTAL, "饺子视频播放器功能添加");
            /**
             * 单视频播放  该播放模式 将无法呈现清晰度切换
             */
            /*jzvideoplayerstandard.setUp("https://onlyboss.oss-cn-beijing.aliyuncs.com/video/17floor_trailer.mp4"
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);*/

            jzvideoplayerstandard.setonVideoEndLinstener(new JZVideoPlayerStandard.onVideoEndLinstener() {
                @Override
                public void videoEndListener(int index) {//结束监听
                    Toast.makeText(MainActivity.this, "第"+(index+1)+"集播放结束", Toast.LENGTH_SHORT).show();
                    jzvideoplayerstandard.setUp(list.get(index), 0
                            , JZVideoPlayerStandard.SCROLL_AXIS_HORIZONTAL, "饺子视频播放器功能添加");
                }
            });

 ps:
     在项目中需要将某些图标更替 可直接在drawable中以同名形式替换</br>
     饺子视频在使用的时候需要大面积的UI更改可重写布局文件 jz_layout_standard.xml (饺子原有id建议不要删除 请用逻辑隐藏或者view隐藏)</br>
     [限于从新的饺子源码改动 本源码不仅仅是改变了布局 部分id增加特多 如需在原本基础上更改 可参考 已增加部分注释(个人理解版 可能和原创有出入 错误地方请指正 谢谢)]</br>
     将JZVideoPlayerStandard.Java中的R.layout.jz_layout_standard替换为你自己重新写的布局(如果是在原布局上更改 则不需替换)</br>
     音频播放 同上</br>

     欢迎转载,欢迎评论,欢迎吐槽,欢迎提意见 咱邮箱是:1145418863@qq.com
     如有某位大佬偶然看到 还望得到指点

此链接是饺子源码(比源码仅仅多一个倍速功能)<br/>
https://github.com/L1145418863/JzVideo_beisu<br/>

  感谢饺子视频团队(或某大佬)提供的开源框架 万分感谢 再次感谢~<br/>
