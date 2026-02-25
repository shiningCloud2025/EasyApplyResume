# 创建校验类并修改Service实现

## 任务目标
为8个业务模块创建校验工具类，并修改对应的Service实现类，使用新的校验工具类进行表单校验。

## 业务模块
1. 常见问题（AdminFaq）
2. 使用指南（AdminUserGuide）
3. 项目介绍（AdminProjectIntroduce）
4. 团队介绍（AdminTeamIntroduce）
5. 发展历程（AdminDevelopHistory）
6. 加入我们（AdminJoinUs）
7. 合作伙伴（AdminPartnerIntroduce）
8. 媒体报道（AdminMediaReport）
9. 客服管理（AdminCustomerService）

## 校验逻辑
- 所有模块的校验逻辑相同
- 新增操作：校验标题和内容不为空
- 修改操作：校验ID不为空且大于0，标题和内容不为空
- 所有字段去除首尾空格

## 实施步骤
1. 创建adminvalidator包
2. 为每个业务模块创建对应的校验工具类
3. 修改每个业务模块的Service实现类，使用新的校验工具类
4. 测试校验逻辑是否正确

## 技术实现
- 参考公告和广告模块的校验方式
- 每个校验工具类包含validateForAdd和validateForUpdate方法
- 校验失败时抛出BusException异常
- 使用AdminCodeEnum.PARAM_ERROR作为错误码