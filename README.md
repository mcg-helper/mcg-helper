# mcg-helper研发助手
## mcg-helper是什么?

定位一款研发个化性流程自动化工具，提供一套可视化”流程自动化“的规范，丰富的拖拽控件，良好的交互性，基于流程图实现自动化，能够很好感知到可控性、实时性、可追溯性。更好的解决在研发工作中较典型的代码生成、应用部署、环境搭建、模拟环境、自动发现与检测、数据处理等需求场景，从而达到减少简化工作量。其核心亮点在于打造自己的流程控件，实现自定义需求。  

## mcg-helper的优势？
* 轻量级、无浸入性、部署简单（无需安装数据库或特定配置，jdk8、tomcat8即可独立运行） 
* 跨平台性、可移植性、可扩展性，基于B/S架构，通过浏览器即可访问  
* 实现一套流程自动化规范，更加便捷的实现自动化  
* 二次开发，打造自定义流程控件或其它需求，让流程自动化更为强大  

## mcg-helper能够做什么？

以研发工作中的实际需求场景为例：  
一、代码生成：快速构建代码生成功能，通过可视化界面，采用拖拽式控件及连接线绘制流程图，自定义实现生成任何想要的代码。  
二、环境搭建：以java应用来讲，比如安装jdk，tomcat，zookeeper，mq，hbase，mysql等等都可以实现自动化。  
三、应用部署：实现Linux SSH连接，轻松实现切换用户等交互性需求，数据初始化，多应用的启动顺序或特定条件触发，均可实现自动化操作。    
四、模拟操作：接口调试，数据模拟，功能自测等等，适用于测试场景。  
五、自动检测：基于流程循环，可实现主动触发、检测、控制的需求场景，如服务器性能检测，应用存活检测，数据库数据比对等。  
六、数据处理：如开发环境定期同步数据到测试环境，或多个数据库数据加工过滤等需求。  
  
整体上讲，控件使用简单，基于流程图清晰易维护，易掌控，学习无门槛。系统“主页”里面有视频教程链接，相信能够很好的上手和玩转mcg-helper。

## 学习资源
QQ交流群：619815829  
[mcg-helper博客链接](http://blog.csdn.net/loginandpwd)  最新的动态资讯、图文教程、场景案例  
[第一节、视频教程内容介绍](https://edu.csdn.net/course/play/5954)  探讨研发工作中典型的重复繁杂工作，可通过流程自动化实现代替的常见场景  
[第二节、mcg-helper初步认识教程](https://edu.csdn.net/course/play/5954/300130) mcg-helper概述、核心设计、基本知识、工作原理、入门示例、使用场景等方面来认识与熟悉工具  
[第三节、开始、文本、结束控件讲解](https://edu.csdn.net/course/play/5954/300416)  了解流程控件运行原理，介绍与演示控件的功能与作用  
[第四节、data、json控件讲解](https://edu.csdn.net/course/play/5954/305640)  全局变量的使用、动态读取表结构信息、输出指定内容到文件  
[第五节、js、java、python控件讲解](https://edu.csdn.net/course/play/5954/309934)  在流程中使用多种计算机语言，发挥各自优势，让流程更为灵活强大  
[第六节、sql查询、sql执行控件讲解](https://edu.csdn.net/course/play/5954/310142)  实现常用关系型数据库的交互功能  
[第七节、Linux、循环、子流程控件讲解](https://edu.csdn.net/course/play/5954/324751)  Linux SSH连接，流程循环功能，以及流程拆分复用与组合。  

## 初步认识mcg-helper
  进入主页，里面包含QQ群、csdn博客、视频教程、开源地址、版本信息。
![主页](https://github.com/mcg-helper/mcg-helper/raw/master/image/home.png)

![代码生成](https://github.com/mcg-helper/mcg-helper/raw/master/image/genCode.jpg)

![环境搭建](https://github.com/mcg-helper/mcg-helper/raw/master/image/build.jpg)

![应用发布](https://github.com/mcg-helper/mcg-helper/raw/master/image/publish.jpg)

## mcg-helper控制台
在流程执行时会输出每个控件的日志相关信息，这样在绘制和调试流程时极为有用，且伴随日志的不断输出，整个流程执行的顺序与过程变得更加清晰。
![控制台](https://github.com/mcg-helper/mcg-helper/raw/master/image/console.png)

## 问题反馈
在使用中遇到问题或有更好的建议，欢迎大家反馈:

* QQ交流群: 619815829
* 邮箱地址: mcg-helper@qq.com
