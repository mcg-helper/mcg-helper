# **项目已转移到** ：https://gitee.com/mcg-helper/mcg-robot ，拥有更丰富的功能！

# mcg-helper研发助手
## mcg-helper是什么?

定位一款研发个化性流程自动化系统，提供一套可视化”流程自动化“的规范，丰富的拖拽控件，良好的交互性，基于流程图实现自动化，能够很好感知到可控性、实时性、可追溯性。更好的便于理解和证明可行性，选择以研发工作需求场景实现自动化，把较典型的代码生成、应用部署、环境搭建、模拟环境、自动发现与检测、数据处理等场景，通过设计一系列自定义控件来实现自动化，从而达到减少简化工作量。其核心亮点在于打造自己的流程控件，实现自定义需求。  

## mcg-helper的优势？
* 轻量级、无浸入性、部署简单（无需安装数据库或特定配置，jdk8、tomcat8即可独立运行） 
* 跨平台性、可移植性、可扩展性，基于B/S架构，通过浏览器即可访问  
* 一套流程自动化规范，轻松快捷实现流程自动化  
* 二次开发，打造自定义拖拽流程控件，让流程自动化更贴近需求  
* WEB SSH，通过浏览器即可与Linux交互，支持google身份认证

## mcg-helper能够做什么？

快速让你实现流程自动化，内置控件作为抛砖引玉，选择以研发工作中的实际需求场景：  
一、代码生成：快速构建代码生成功能，通过可视化界面，采用拖拽式控件及连接线绘制流程图，自定义实现生成任何想要的代码。  
二、环境搭建：以java应用来讲，比如安装jdk，tomcat，zookeeper，mq，hbase，mysql等等都可以实现自动化。  
三、应用部署：GIT代码拉取，MAVEN打包，SFTP上传，Linux脚本执行，自动化一气呵成，且轻松实现切换用户等交互性需求，数据初始化，多应用的启动顺序或特定条件触发，均可实现自动化操作。    
四、模拟操作：接口调试，数据模拟，功能自测等等，适用于测试场景。  
五、自动检测：基于流程循环，实现主动触发、检测、控制的需求场景，如服务器性能检测，应用存活检测，循环扫描操作等。  
六、数据处理：如开发环境定期同步数据到测试环境，或多个数据库数据加工过滤等需求。  
七、WEB SSH：通过浏览器操作Linux，简单便捷。
  
整体上讲，基于流程图清晰易直观，且控件使用简单，易掌控，学习门槛低。系统“主页”里面有视频教程链接，能够很好的上手和玩转mcg-helper，熟悉以后可以自定义控件，满足自己的自动化需求。

## 学习资源
QQ交流群：619815829  
视频教程：  
[第一节、视频教程内容介绍](https://edu.csdn.net/course/play/5954)  探讨研发工作中典型的重复繁杂工作，可通过流程自动化实现代替的常见场景。  
[第二节、mcg-helper初步认识教程](https://edu.csdn.net/course/play/5954/300130) mcg-helper概述、核心设计、基本操作、工作原理、入门示例、使用场景等。  
[第三节、开始、文本、结束控件讲解](https://edu.csdn.net/course/play/5954/300416)  了解流程控件运行原理，介绍与演示控件的功能与作用。  
[第四节、data、json控件讲解](https://edu.csdn.net/course/play/5954/305640)  全局变量的使用、动态读取表结构信息、输出指定内容到文件。  
[第五节、js、java、python控件讲解](https://edu.csdn.net/course/play/5954/309934)  在流程中使用多种计算机语言，发挥各自优势，让流程更为灵活强大。  
[第六节、sql查询、sql执行控件讲解](https://edu.csdn.net/course/play/5954/310142)  实现常用关系型数据库的交互功能。  
[第七节、Linux、循环、子流程控件讲解](https://edu.csdn.net/course/play/5954/324751)  Linux SSH连接，流程循环功能，以及流程拆分复用与组合。  
[第八节、万能代码生成讲解](https://edu.csdn.net/course/play/5954/365298)  以一款java开源框架jeecg为例子，实现一键代码生成。  
[第九节、自定义控件讲解](https://edu.csdn.net/course/play/5954/466072)  实现自定义控件的思路以及完整走读代码实现。  

## 初步认识mcg-helper
  进入主页，里面包含QQ群、csdn博客、视频教程、开源地址、版本信息。 
![主页](https://images.gitee.com/uploads/images/2020/0621/235415_7e2bb27d_1598361.png "home.png")

自定义生成代码
![代码生成](https://images.gitee.com/uploads/images/2020/0621/235456_47a055d3_1598361.jpeg "genCode.jpg")

轻松搭建各种环境
![环境搭建](https://images.gitee.com/uploads/images/2020/0621/235527_9980062a_1598361.png "build.png")

快速实现应用发布
![应用发布](https://images.gitee.com/uploads/images/2020/0621/235556_5e7e7ee7_1598361.png "publish.png")

WEB SSH 实现Linux交互
![WEB SSH](https://images.gitee.com/uploads/images/2020/0621/235623_27eea7a6_1598361.png "wssh.png")

流程执行
![流程执行效果](https://images.gitee.com/uploads/images/2020/0622/000450_498b01db_1598361.png "execute.png")

## mcg-helper控制台
在流程执行时会输出每个控件的执行相关信息，这样在绘制和调试流程时极为有用，整个流程执行的顺序与过程变得更加清晰。
以下为流程执行时，控制台输出部分截图：
![控件台](https://images.gitee.com/uploads/images/2020/0621/235701_b62d3b71_1598361.png "console.png")

![控制台2](https://images.gitee.com/uploads/images/2020/0622/001014_bce9c02e_1598361.png "console_2.png")

## 问题反馈
在使用中遇到问题或有更好的建议，欢迎大家反馈:

* QQ交流群: 619815829
* 邮箱地址: mcg-helper@qq.com

## 打赏支持
对技术走着痴迷，深知国内工具软件的贫瘠，希望觉得有用并且在用的同行，给我一点点支持，维护这一难得的星星之火，同时也希望自己每日的付出没有白费

![微信](https://images.gitee.com/uploads/images/2020/0717/095320_781ccbca_1598361.jpeg "WeChat.jpg")
微信


![支持宝](https://images.gitee.com/uploads/images/2020/0717/095345_f0f4d25e_1598361.jpeg "Alipay.jpg")
支付宝

# **项目已转移到** ：https://gitee.com/mcg-helper/mcg-robot ，拥有更丰富的功能！
