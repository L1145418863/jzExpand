# jzExpand

饺子视频改版(简易改) ----- 基于饺子视频进行二次修改<br/>
(该空间 还包括一个基于Android mediaPlayer 的一个音频播放 属于我自己写的 最近项目需要就自己写了个)<br/>
  添加倍速<br/>
  添加分集<br/>
  弹框样式修改<br/>
  进度条样式修改<br/>

     虽然并不太完善 但是咱还是有提升空间的
     也建议大家给我这个菜鸡点个start ☆ 也好有点动力 谢谢~~
     如有问题请大家指正 万分感谢

  使用示例<br/>

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

            jzvideoplayerstandard.addSelection(list);

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

  感谢饺子视频团队(或某大佬)提供的开源框架 万分感谢 再次感谢~
