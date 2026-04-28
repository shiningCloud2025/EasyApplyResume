# EasyApplyResume
## 项目介绍
EasyApplyResume(易投简历)是作者准备的毕设项目。作者一直对做一个开源项目十分感兴趣,但是由于课业深度不足、实习的项目不允许开源等问题一直没有机会做,因此作者趁着做毕设的机会,正好满足一下作者做开源项目的心。本项目采用主流的后端技术和前端技术来实现，该项目主要包括用户端、管理端和监控与广告端。<br>
用户端主要面向网站的访问用户,为用户提供简历模板、制作简历、投递简历、查询招聘公司、AI求职助手等功能。<br>
管理端主要面向网站的管理者,为管理者提供网站管理、文章管理、招聘管理、简历管理、反馈管理等功能。<br>
监控与广告端主要面向网站的管理者，为管理者提供公告管理、广告管理、用户监测管理、管理监测管理、服务器管理等功能。
## 开发进度
用户端、管理的、监控与广告端基础业务均已开发完成。目前主要是进行扩展业务的开发<br>
用户端: 主要围绕用户端的核心业务，也就是围绕简历模板、制作简历、投递简历、查询招聘公司<br>
(1).为简历模版提供一个AI大模型智能体,给用户提供简历代码、或者简历文字等<br>
(2).为制作简历提供一个AI大模型智能体,给用户提供简历评分、简历关键字提取等<br>
(3).为投递简历制作一个Agent-flow,来实现自动投递简历,核心技术是RPA+Agent-Flow<br>
管理端:暂无开发计划<br>
广告语监测端:想做一个开发连接远程服务器,然后进行操作的模块。实现完类似于低配版Xshell(作者由于精力有限,暂无开发计划,有兴趣的可以联系我做commit)<br>
OSS优化问题:由于KODO中间件,需要域名加速器,所以要等我的域名正式完成才能上传文件！
## 项目设计
架构设计:单体多实例<br>
前端核心技术:react(用户端)和vue3(管理端和检测与广告端)<br>
网关核心技术:Nginx(仅目前,后续可能会考虑自已制作一个网关或者使用Higress、GateWay等成熟的解决方案)<br>
后端核心技术:JDK21+SpringBoot3全家桶+SpringAI+MySQL、Redis等主流技术和中间件<br>
数据库核心技术:MySQL5.7、Redis7.2、PGVector16<br>
网站安全核心技术:SpringSecurity+Jwt<br>
网站监控核心技术:Actuator+Admin+Prometheus+Grafana+SpringBootAdmin<br>
OSS核心技术:KODO、MinIO<br>
部署为:centos7+docker+docker-compose<br>
常见问题：OSS孤儿数据、Redis数据同步、业务流转、基于Coze搭建智能体、Agent-flow、SpringAI、大模型相关业务、双Token、KODO配置与使用
## 核心功能
(1).用户端:<br>
1.门户页:
![img.png](../ReadMeImages/SystemPicture/user/img.png)
2.我的简历
![img_1.png](../ReadMeImages/SystemPicture/user/img_1.png)
3.简历模版
![img_2.png](../ReadMeImages/SystemPicture/user/img_2.png)
4.招聘信息
![img_3.png](../ReadMeImages/SystemPicture/user/img_3.png)
5.求职攻略
![img_4.png](../ReadMeImages/SystemPicture/user/img_4.png)
6.AI简历助手<br>
6.1 AI智能问答助手
![img_5.png](../ReadMeImages/SystemPicture/user/img_5.png)
6.2 AI智能体助手
![img_6.png](../ReadMeImages/SystemPicture/user/img_6.png)
7.笔试专项<br>
7.1 根据笔试大类查询
![img_7.png](../ReadMeImages/SystemPicture/user/img_7.png)
7.2 根据笔试小类查询
![img_8.png](../ReadMeImages/SystemPicture/user/img_8.png)
7.3 查询所有题库
![img_9.png](../ReadMeImages/SystemPicture/user/img_9.png)
8.用户反馈
![img_10.png](../ReadMeImages/SystemPicture/user/img_10.png)
9.帮助中心<br>
9.1 使用指南
![img_11.png](../ReadMeImages/SystemPicture/user/img_11.png)
9.2 常见问题
![img_12.png](../ReadMeImages/SystemPicture/user/img_12.png)
9.3 联系客服
![img_13.png](../ReadMeImages/SystemPicture/user/img_13.png)
10.关于我们<br>
10.1 项目介绍
![img_14.png](../ReadMeImages/SystemPicture/user/img_14.png)
10.2 团队介绍
![img_15.png](../ReadMeImages/SystemPicture/user/img_15.png)
10.3 发展历程
![img_16.png](../ReadMeImages/SystemPicture/user/img_16.png)
10.4 加入我们
![img_17.png](../ReadMeImages/SystemPicture/user/img_17.png)
10.5 合作伙伴
![img_18.png](../ReadMeImages/SystemPicture/user/img_18.png)
10.6 媒体报道
![img_19.png](../ReadMeImages/SystemPicture/user/img_19.png)


(2).管理端:<br>
1.门户页
![img.png](../ReadMeImages/SystemPicture/admin/img.png)
2.首页
![img_1.png](../ReadMeImages/SystemPicture/admin/img_1.png)
3.网站管理<br>
3.1 管理员管理
![img_1.png](../ReadMeImages/SystemPicture/img_11.png)
3.2 角色管理
![img_2.png](../ReadMeImages/SystemPicture/img_12.png)
3.3 权限管理
![img_3.png](../ReadMeImages/SystemPicture/img_13.png)
4.文章管理<br>
4.1 求职攻略文章管理
![img_4.png](../ReadMeImages/SystemPicture/img_14.png)
5.招聘管理<br>
5.1 招聘岗位管理
![img.png](../ReadMeImages/SystemPicture/img_15.png)
5.2 招聘信息管理
![img_1.png](../ReadMeImages/SystemPicture/img_16.png)
6.简历管理<br>
6.1 简历模版管理
![img_2.png](../ReadMeImages/SystemPicture/img_31.png)
6.2 系统删除简历管理
![img_3.png](../ReadMeImages/SystemPicture/img_32.png)
7.Map管理
7.1 行业Map管理
![img_4.png](../ReadMeImages/SystemPicture/img_19.png)
8.AI助手<br>
8.1 AI智能问答助手
![img_5.png](../ReadMeImages/SystemPicture/img_20.png)
8.2 AI智能体助手
![img_6.png](../ReadMeImages/SystemPicture/img_21.png)
9.反馈管理<br>
9.1 用户端反馈管理
![img_7.png](../ReadMeImages/SystemPicture/img_22.png)
9.2 管理端反馈管理
![img_8.png](../ReadMeImages/SystemPicture/img_23.png)
9.3 用户端反馈记录
![img_9.png](../ReadMeImages/SystemPicture/img_24.png)
9.4 管理端反馈记录
![img_10.png](../ReadMeImages/SystemPicture/img_25.png)
10.内部系统<br>
10.1 易投简历用户端
![img_11.png](../ReadMeImages/SystemPicture/img_26.png)
10.2 易投简历监测与广告端
![img_12.png](../ReadMeImages/SystemPicture/img_27.png)
11. 外部API(以高德为代表)
![img_13.png](../ReadMeImages/SystemPicture/img_28.png)
12.API文档中心
12.1 API对外文档中心
![img_14.png](../ReadMeImages/SystemPicture/img_29.png)
12.2 API对内文档中心
![img_15.png](../ReadMeImages/SystemPicture/img_30.png)


监测与广告端:<br>
1.门户页
![img.png](../ReadMeImages/SystemPicture/img_33.png)
2.首页
![img_1.png](../ReadMeImages/SystemPicture/img_34.png)
3.公告管理(以管理的公告管理为代表)
![img_2.png](../ReadMeImages/SystemPicture/img_35.png)
4.广告管理<br>
4.1 图片广告管理(以管理端广告管理为代表)<br>
![img_3.png](../ReadMeImages/SystemPicture/img_36.png)
4.2 视频广告管理(暂未开发)<br>
5.用户监测管理<br>
5.1 网站管理
![img_4.png](../ReadMeImages/SystemPicture/img_37.png)
5.2 日志管理<br>
6.管理监测管理<br>
6.1网站管理(和用户端基本一样)<br>
6.2日志管理<br>
7.中间件监测管理<br>
7.1 MySQL管理(未开发)<br>
7.2 Redis管理(未开发)<br>
7.3 MinIO管理(参考文档中心)<br>
8.服务器管理<br>
8.1 设备管理
![img_5.png](../ReadMeImages/SystemPicture/img_38.png)
8.2 设备监控
![img_6.png](../ReadMeImages/SystemPicture/img_39.png)
9.网站安全管理
9.1 SpringBootAdmin
![img_7.png](../ReadMeImages/SystemPicture/img_40.png)
9.2 Prometheus
![img_8.png](../ReadMeImages/SystemPicture/img_41.png)
9.3 Grafana
![img_9.png](../ReadMeImages/SystemPicture/img_42.png)
## 项目适用人群
**本项目适用于:想学习SpringBoot全家桶的编程小白,想学习SpringAI框架的程序员、编程小白,对Java感兴趣的其他行业从业者等等**
## 项目使用和二开
**本项目可以直接使用、二开但是需遵守MIT协议!**
## 项目贡献
### 谁可以参与贡献
任何一个对项目感兴趣的开发者都可以对项目进行贡献!在这里先提前感谢您的贡献！
### 贡献类型
首先感谢您对代码做出贡献的想法,针对于当前项目而言,主要接受以下类型的贡献:
1.代码类：新增功能、修复 Bug、重构代码、性能优化等。
2.文档类：完善 README、补充 API 文档、翻译说明、修正错别字等。
3.测试类：编写单元测试、集成测试、优化测试用例等。
### 代码贡献流程
首先感谢您对代码做出贡献的想法,但是希望您能按照下面的流程来进行贡献,从而规范流程提高效率节省时间！<br>
流程如下:<br>
1.提交 Issue（必要前置）
- 新增功能、修复未记录的 Bug、提出优化建议等，需先在 Issue 区描述背景、需求和方案，避免重复开发。<br>
- 标题格式：[类型] 具体描述（如[Feature] 新增用户角色管理功能、[Bug] 登录接口空参数返回500错误）。<br>

2.分支管理
- 主分支（如prod、dev）仅用于合并稳定代码，贡献需基于子分支开发。<br>
- 子分支命名：类型/Issue编号-简短描述（如feature/123-user-role、fix/456-login-error）。<br>

3.代码编写与提交
- 代码风格：需遵循项目统一规范（如 Java 的 Alibaba规范、前端的 ESLint 规则），提交前执行格式化工具（如mvn spotless:apply、npm run lint）。<br>
- Git 提交信息：格式为[类型] 描述（关联#Issue编号）（如[Fix] 修复登录空参数500错误（#456）），类型包括Feature/Fix/Docs/Test等。<br>

4.提交 Pull Request（PR）
- 目标分支：默认选择feature。
- PR 内容：需关联 Issue、说明功能 / 修复逻辑、测试情况、兼容性影响等，方便审核者快速理解。
