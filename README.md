# permissionManage
### 自己开发一套权限管理

* 为什么自己写

  * 满足框架的要求进行配置
  * 没有界面操作和查看
  * 期望更细致的管理

* 目标

  * 基于扩展的RBAC实现
  * 易于扩展能灵活的适应需求的变化
  * 所有的管理都有界面操作方便

* 主要内容

  * 开发功能确定
  * 详细表结构设计
  * 编码实现

* 需要开发的功能

  * 配置管理类功能（RBAC管理界面）
    * 用户、权限、角色的管理界面（扩展用：部门、权限模块）
    * 角色-用户管理、角色-权限管理
    * 权限更新日志管理
  * 权限拦截类功能
    * 在切面（Filter）做权限拦截
    * 确定用户是否拥有某个权限
  * 辅助类功能：缓存（Redis）、各种树结构生成
    * 缓存（Redis）的封装和使用
    * 各种树：部门数、权限模块树、角色权限数、用户权限树 构建
    * 权限操作恢复

* 详细表结构设计

  * 部门表

    ```mssql
    -- ----------------------------
    --  Table structure for `sys_dept`
    -- ----------------------------
    DROP TABLE IF EXISTS `sys_dept`;
    CREATE TABLE `sys_dept` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门id',
      `name` varchar(20) NOT NULL DEFAULT '' COMMENT '部门名称',
      `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级部门id',
      `level` varchar(200) NOT NULL DEFAULT '' COMMENT '部门层级',
      `seq` int(11) NOT NULL DEFAULT '0' COMMENT '部门在当前层级下的顺序，由小到大',
      `remark` varchar(200) DEFAULT '' COMMENT '备注',
      `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
      `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次操作时间',
      `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新操作者的ip地址',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;
    ```

    ​

  * 部门表

    ```mysql
    -- ----------------------------
    --  Table structure for `sys_user`
    -- ----------------------------
    DROP TABLE IF EXISTS `sys_user`;
    CREATE TABLE `sys_user` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
      `username` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名称',
      `telephone` varchar(13) NOT NULL DEFAULT '' COMMENT '手机号',
      `mail` varchar(20) NOT NULL DEFAULT '' COMMENT '邮箱',
      `password` varchar(40) NOT NULL DEFAULT '' COMMENT '加密后的密码',
      `dept_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户所在部门的id',
      `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，1：正常，0：冻结状态，2：删除',
      `remark` varchar(200) DEFAULT '' COMMENT '备注',
      `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
      `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
      `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip地址',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
    ```

  * 权限模块表

    ```mysql
    -- ----------------------------
    --  Table structure for `sys_acl_module`
    -- ----------------------------
    DROP TABLE IF EXISTS `sys_acl_module`;
    CREATE TABLE `sys_acl_module` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限模块id',
      `name` varchar(20) NOT NULL DEFAULT '' COMMENT '权限模块名称',
      `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级权限模块id',
      `level` varchar(200) NOT NULL DEFAULT '' COMMENT '权限模块层级',
      `seq` int(11) NOT NULL DEFAULT '0' COMMENT '权限模块在当前层级下的顺序，由小到大',
      `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，1：正常，0：冻结',
      `remark` varchar(200) DEFAULT '' COMMENT '备注',
      `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
      `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次操作时间',
      `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新操作者的ip地址',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
    ```

    ​

  * 权限表

    ```mysql
    -- ----------------------------
    --  Table structure for `sys_acl`
    -- ----------------------------
    DROP TABLE IF EXISTS `sys_acl`;
    CREATE TABLE `sys_acl` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限id',
      `code` varchar(20) NOT NULL DEFAULT '' COMMENT '权限码',
      `name` varchar(20) NOT NULL DEFAULT '' COMMENT '权限名称',
      `acl_module_id` int(11) NOT NULL DEFAULT '0' COMMENT '权限所在的权限模块id',
      `url` varchar(100) NOT NULL DEFAULT '' COMMENT '请求的url, 可以填正则表达式',
      `type` int(11) NOT NULL DEFAULT '3' COMMENT '类型，1：菜单，2：按钮，3：其他',
      `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，1：正常，0：冻结',
      `seq` int(11) NOT NULL DEFAULT '0' COMMENT '权限在当前模块下的顺序，由小到大',
      `remark` varchar(200) DEFAULT '' COMMENT '备注',
      `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
      `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
      `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一个更新者的ip地址',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;
    ```

  * 用户表

    ```mysql
    -- ----------------------------
    --  Table structure for `sys_role`
    -- ----------------------------
    DROP TABLE IF EXISTS `sys_role`;
    CREATE TABLE `sys_role` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
      `name` varchar(20) NOT NULL,
      `type` int(11) NOT NULL DEFAULT '1' COMMENT '角色的类型，1：管理员角色，2：其他',
      `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，1：可用，0：冻结',
      `remark` varchar(200) DEFAULT '' COMMENT '备注',
      `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
      `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新的时间',
      `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip地址',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
    ```

  * 角色相关表（角色用户关联表）

    ```mysql
    DROP TABLE IF EXISTS `sys_role_user`;
    CREATE TABLE `sys_role_user` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `role_id` int(11) NOT NULL COMMENT '角色id',
      `user_id` int(11) NOT NULL COMMENT '用户id',
      `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
      `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新的时间',
      `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip地址',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4;
    ```

  * 角色权限相关表

    ```mysql
    -- ----------------------------
    --  Table structure for `sys_role_acl`
    -- ----------------------------
    DROP TABLE IF EXISTS `sys_role_acl`;
    CREATE TABLE `sys_role_acl` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `role_id` int(11) NOT NULL COMMENT '角色id',
      `acl_id` int(11) NOT NULL COMMENT '权限id',
      `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
      `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新的时间',
      `operate_ip` varchar(200) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;
    ```

  * 权限相关更新记录表

    ```mysql
    -- ----------------------------
    --  Table structure for `sys_log`
    -- ----------------------------
    DROP TABLE IF EXISTS `sys_log`;
    CREATE TABLE `sys_log` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `type` int(11) NOT NULL DEFAULT '0' COMMENT '权限更新的类型，1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系',
      `target_id` int(11) NOT NULL COMMENT '基于type后指定的对象id，比如用户、权限、角色等表的主键',
      `old_value` text COMMENT '旧值',
      `new_value` text COMMENT '新值',
      `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作者',
      `operate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次更新的时间',
      `operate_ip` varchar(20) NOT NULL DEFAULT '' COMMENT '最后一次更新者的ip地址',
      `status` int(11) NOT NULL DEFAULT '0' COMMENT '当前是否复原过，0：没有，1：复原过',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;
    ```

    ​

* 表结构设计习惯

  * 每个表都要有自己的主键
  * 每个字段尽量定义为NOT NULL，如果NULL字段被索引，需要额外一个字节，且NULL索引时较复杂
  * 尽量为每个字段添加备注
  * 数据库字段统一小写，单词之前使用下划线分割
  * 使用InnoDB存储引擎
  * 可以使用varchar的字段尽可能不适用TEXT、BLOB
  * 表字符集选择UTF-8

  ![这里写图片描述](http://img.blog.csdn.net/20180111231437919?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbXVwZW5nZmVpNjY4OA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

* 权限管理开发--工具相关

  * 校验工具--validator
  * Json转化工具--jackson convert
  * 获取Spring上下文--applicationContext
  * Http请求前后监听--interceptor