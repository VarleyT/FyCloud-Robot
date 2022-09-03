<div style="text-align: center;">

# FyBot-Spring
![language](https://img.shields.io/github/languages/top/VarleyT/FyCloud-Robot)
![last commit](https://img.shields.io/github/last-commit/VarleyT/FyCloud-Robot)
![repo-size](https://img.shields.io/github/repo-size/VarleyT/FyCloud-Robot)
![lecense](https://img.shields.io/github/license/VarleyT/FyCloud-robot)

基于[Simbot-v2](https://github.com/ForteScarlet/simpler-robot/tree/v2-dev)的**Springboot**机器人项目  
API帮助文档 参阅：[BOT-v2开发手册](https://www.yuque.com/simpler-robot/simpler-robot-doc)  
项目地址[GitHub](https://github.com/VarleyT/FyCloud-Robot)
</div>

***
## 运行环境
* Maven
* Jdk-1.8 及以上
* Springboot 建议版本`2.5.0`及以上

## 快速配置
在`/src/main/resources/`目录下新建`application-dev.yml`文件，配置项如下：

| 配置名称 | 描述 |
| --- | --- |
| DB.host | 数据库地址 |
| DB.port | 数据库端口 |
| DB.database | 数据库名称 |
| DB.username | 数据库用户名 |
| DB.password | 数据库密码 |
| BOT.code | 机器人账号 |
| BOT.passwd | 机器人密码 |
| BOT.admin | 机器人管理员 |
| APIs.* | API相关配置 |

**Tips**：当然，你也可以在`/src/main/resources/simbot-bots`下创建`*.bot`文件来配置bot，这里不再详细描述 参阅[BOT配置](https://www.yuque.com/simpler-robot/simpler-robot-doc/fk6o3e#iUKbX)


## 功能列表
**管理功能**
>运行状态  
>群开关机  
>消息存储功能（默认开启）

**娱乐功能**
>原神模拟抽卡  
>抖音视频解析  
>看图功能  
>唱歌功能  
>聊天机器人

## TODO
- [ ] 批量撤回
- [ ] 象棋游戏
- [ ] 随机群事件

## 开源许可
[GNU Lesser General Public License v2.1](https://github.com/VarleyT/FyCloud-Robot/blob/master/LICENSE)


