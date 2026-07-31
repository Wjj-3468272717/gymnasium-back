# Dubbo 分布式改造 Phase 1：基础设施搭建 + API 模块抽取

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 引入 Dubbo + Nacos 依赖，创建 gym-api 模块，抽取所有 RPC 接口和 DTO，为 Phase 2 服务拆分打下基础。

**Architecture:** 在父 POM 中统一管理 Dubbo/Nacos 版本依赖；新建 `gym-api` 子模块承载所有 RPC 接口定义和可序列化 DTO；`gym-service-web` 中的 ServiceImpl 同时实现原有 MyBatis-Plus Service 接口和新的 RPC 接口，现阶段本地调用，后续通过 Dubbo 远程调用。

**Tech Stack:** Spring Boot 2.4.4, Dubbo 2.7.8, Nacos 1.4.1, MyBatis-Plus 3.4.1, Java 8

## Global Constraints

- Java 8，所有 DTO 必须 `implements Serializable`
- Dubbo 版本锁定 2.7.8（与 Spring Boot 2.4.4 兼容的最高版本）
- Nacos Client 1.4.1（与 Dubbo 2.7.8 匹配）
- RPC 接口不再继承 MyBatis-Plus `IService<T>`，为纯 Java 接口
- gym-api 模块不依赖 MyBatis-Plus，只依赖 `gym-common`（共用 ResultVo/StatusCode）
- gym-service-web 同时依赖 gym-api + MyBatis-Plus，现阶段双接口实现
- 不修改任何现有业务逻辑，本次仅为结构重构
- 所有文件使用与现有代码一致的包名风格 `com.v1.*`

---

### Task 1: 父 POM 增加 Dubbo + Nacos 依赖管理

**Files:**
- Modify: `pom.xml`

**Produces:** 父 POM 中 `<dependencyManagement>` 和 `<properties>` 包含 Dubbo/Nacos 版本定义

- [ ] **Step 1: 在 `<properties>` 中增加版本号**

在 `pom.xml` 的 `<properties>` 块末尾追加：

```xml
<dubbo.version>2.7.8</dubbo.version>
<dubbo-nacos.version>2.7.8</dubbo-nacos.version>
<nacos-client.version>1.4.1</nacos-client.version>
```

- [ ] **Step 2: 在 `<dependencyManagement>` 中增加 Dubbo/Nacos 依赖**

在 `pom.xml` 的 `<dependencyManagement>` → `<dependencies>` 末尾追加：

```xml
<!-- Dubbo Spring Boot Starter -->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>${dubbo.version}</version>
</dependency>
<!-- Dubbo Nacos 注册中心适配 -->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-registry-nacos</artifactId>
    <version>${dubbo-nacos.version}</version>
</dependency>
<!-- Nacos Client -->
<dependency>
    <groupId>com.alibaba.nacos</groupId>
    <artifactId>nacos-client</artifactId>
    <version>${nacos-client.version}</version>
</dependency>
```

- [ ] **Step 3: 验证 POM 格式正确**

```bash
cd gym-parent-project && mvn validate
```

Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add pom.xml
git commit -m "feat: add Dubbo 2.7.8 and Nacos 1.4.1 dependency management to parent POM"
```

---

### Task 2: 创建 gym-api 模块

**Files:**
- Create: `gym-api/pom.xml`
- Create: `gym-api/src/main/java/com/v1/api/package-info.java`
- Modify: `pom.xml` (添加 `<module>gym-api</module>`)

**Produces:** gym-api 模块骨架，只依赖 gym-common 和 lombok

- [ ] **Step 1: 在父 POM 中注册新模块**

在 `pom.xml` 的 `<modules>` 块中，`gym-common` 之后插入：

```xml
<module>gym-api</module>
```

- [ ] **Step 2: 创建 gym-api/pom.xml**

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.v1</groupId>
        <artifactId>gym-parent-project</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <artifactId>gym-api</artifactId>
    <name>gym-api</name>
    <description>Dubbo RPC interfaces and DTOs for gym services</description>

    <dependencies>
        <dependency>
            <groupId>com.v1</groupId>
            <artifactId>gym-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 3: 创建包目录结构**

```bash
mkdir -p gym-api/src/main/java/com/v1/api/{course,dto,equipment,goods,goods_order,home,image,login,lost,member,member_apply,member_card,member_course,member_recharge,member_role,suggest,sys_menu,sys_role,sys_role_menu,sys_user,sys_user_role}
mkdir -p gym-api/src/main/java/com/v1/api/dto/{course,equipment,goods,goods_order,home,login,lost,member,member_apply,member_card,member_course,member_recharge,member_role,suggest,sys_menu,sys_role,sys_user}
```

- [ ] **Step 4: 验证模块可编译**

```bash
cd gym-parent-project && mvn compile -pl gym-api -am
```

Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add pom.xml gym-api/
git commit -m "feat: create gym-api module skeleton for Dubbo RPC interfaces"
```

---

### Task 3: 创建通用 DTO（分页 + 通用参数）

**Files:**
- Create: `gym-api/src/main/java/com/v1/api/dto/PageDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/PageResultDTO.java`

**Produces:** 通用分页请求/响应 DTO，替代 MyBatis-Plus 的 IPage，所有业务 DTO 复用

- [ ] **Step 1: 创建 PageDTO（分页请求参数）**

```java
package com.v1.api.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class PageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long currentPage;
    private Long pageSize;
}
```

- [ ] **Step 2: 创建 PageResultDTO（分页响应结果）**

```java
package com.v1.api.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class PageResultDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long currentPage;
    private Long pageSize;
    private Long total;
    private List<T> records;

    public static <T> PageResultDTO<T> empty() {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setRecords(Collections.emptyList());
        result.setTotal(0L);
        return result;
    }
}
```

- [ ] **Step 3: 验证编译**

```bash
cd gym-parent-project && mvn compile -pl gym-api -am
```

Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add gym-api/src/main/java/com/v1/api/dto/PageDTO.java gym-api/src/main/java/com/v1/api/dto/PageResultDTO.java
git commit -m "feat: add generic PageDTO and PageResultDTO for Dubbo RPC"
```

---

### Task 4: 抽取用户/认证域 RPC 接口 + DTO（sys_user, sys_role, sys_menu, sys_role_menu, sys_user_role, login）

**Files:**
- Create: `gym-api/src/main/java/com/v1/api/dto/sys_user/SysUserDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/sys_role/SysRoleDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/sys_menu/SysMenuDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/login/LoginDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/login/LoginResultDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/login/UserInfoDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/sys_user/SysUserRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/sys_role/SysRoleRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/sys_menu/SysMenuRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/sys_role_menu/SysRoleMenuRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/sys_user_role/SysUserRoleRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/login/LoginRpcService.java`

**Produces:** 用户认证域的完整 RPC 接口和 DTO

- [ ] **Step 1: 创建 SysUserDTO**

```java
package com.v1.api.dto.sys_user;

import lombok.Data;
import java.io.Serializable;

@Data
public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String password;
    private String nickName;
    private String phone;
    private String email;
    private String sex;
    private String isAdmin;
    private String status;
}
```

- [ ] **Step 2: 创建 SysRoleDTO**

```java
package com.v1.api.dto.sys_role;

import lombok.Data;
import java.io.Serializable;

@Data
public class SysRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long roleId;
    private String roleName;
    private String remark;
}
```

- [ ] **Step 3: 创建 SysMenuDTO**

```java
package com.v1.api.dto.sys_menu;

import lombok.Data;
import java.io.Serializable;

@Data
public class SysMenuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long menuId;
    private Long parentId;
    private String title;
    private String code;
    private String type;
    private String path;
    private String icon;
    private Integer orderNum;
}
```

- [ ] **Step 4: 创建 LoginDTO、LoginResultDTO、UserInfoDTO**

LoginDTO：
```java
package com.v1.api.dto.login;

import lombok.Data;
import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String code;
    private String userType;
}
```

LoginResultDTO：
```java
package com.v1.api.dto.login;

import lombok.Data;
import java.io.Serializable;

@Data
public class LoginResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private Long userId;
    private String username;
    private String userType;
}
```

UserInfoDTO：
```java
package com.v1.api.dto.login;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String name;
    private String[] permissions;
}
```

- [ ] **Step 5: 创建 SysUserRpcService**

```java
package com.v1.api.sys_user;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.sys_user.SysUserDTO;

public interface SysUserRpcService {
    PageResultDTO<SysUserDTO> listUsers(PageDTO page, String nickName, String phone);

    SysUserDTO loadUser(String username);

    SysUserDTO getUserById(Long userId);
}
```

- [ ] **Step 6: 创建 SysRoleRpcService**

```java
package com.v1.api.sys_role;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.sys_role.SysRoleDTO;
import java.util.List;

public interface SysRoleRpcService {
    PageResultDTO<SysRoleDTO> listRoles(PageDTO page, String roleName);

    void saveRole(SysRoleDTO role);

    void updateRole(SysRoleDTO role);

    void deleteRole(Long roleId);

    List<SysRoleDTO> getAllRoles();
}
```

- [ ] **Step 7: 创建 SysMenuRpcService**

```java
package com.v1.api.sys_menu;

import com.v1.api.dto.sys_menu.SysMenuDTO;
import java.util.List;

public interface SysMenuRpcService {
    List<SysMenuDTO> getMenuByMemberId(Long memberId);

    List<SysMenuDTO> getMenuByUserId(Long userId);

    List<SysMenuDTO> getAllMenus();

    List<SysMenuDTO> getParentMenus();

    void saveMenu(SysMenuDTO menu);

    void updateMenu(SysMenuDTO menu);

    void deleteMenu(Long menuId);
}
```

- [ ] **Step 8: 创建 SysRoleMenuRpcService**

```java
package com.v1.api.sys_role_menu;

import java.util.List;

public interface SysRoleMenuRpcService {
    void saveRoleMenus(Long roleId, List<Long> menuIds);
}
```

- [ ] **Step 9: 创建 SysUserRoleRpcService**

```java
package com.v1.api.sys_user_role;

public interface SysUserRoleRpcService {
    void assignRole(Long userId, Long roleId);
}
```

- [ ] **Step 10: 创建 LoginRpcService**

```java
package com.v1.api.login;

import com.v1.api.dto.login.LoginDTO;
import com.v1.api.dto.login.LoginResultDTO;
import com.v1.api.dto.login.UserInfoDTO;

public interface LoginRpcService {
    LoginResultDTO login(LoginDTO loginParam);

    UserInfoDTO getUserInfo(Long userId, String userType);

    String generateCaptcha();
}
```

- [ ] **Step 11: 验证编译**

```bash
cd gym-parent-project && mvn compile -pl gym-api -am
```

Expected: BUILD SUCCESS

- [ ] **Step 12: Commit**

```bash
git add gym-api/src/main/java/com/v1/api/sys_user/ gym-api/src/main/java/com/v1/api/sys_role/ gym-api/src/main/java/com/v1/api/sys_menu/ gym-api/src/main/java/com/v1/api/sys_role_menu/ gym-api/src/main/java/com/v1/api/sys_user_role/ gym-api/src/main/java/com/v1/api/login/ gym-api/src/main/java/com/v1/api/dto/sys_user/ gym-api/src/main/java/com/v1/api/dto/sys_role/ gym-api/src/main/java/com/v1/api/dto/sys_menu/ gym-api/src/main/java/com/v1/api/dto/login/
git commit -m "feat: add user/auth domain RPC interfaces and DTOs"
```

---

### Task 5: 抽取会员域 RPC 接口 + DTO（member, member_card, member_apply, member_recharge, member_role, member_course）

**Files:**
- Create: `gym-api/src/main/java/com/v1/api/dto/member/MemberDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/member_card/MemberCardDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/member_apply/MemberApplyDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/member_recharge/MemberRechargeDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/member_role/MemberRoleDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/member_course/MemberCourseDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/member/MemberRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/member_card/MemberCardRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/member_apply/MemberApplyRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/member_recharge/MemberRechargeRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/member_role/MemberRoleRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/member_course/MemberCourseRpcService.java`

**Produces:** 会员域的完整 RPC 接口和 DTO

- [ ] **Step 1: 创建 MemberDTO**

```java
package com.v1.api.dto.member;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long memberId;
    private Long roleId;
    private String name;
    private String sex;
    private String phone;
    private Integer age;
    private String birthday;
    private Integer height;
    private Integer weight;
    private Integer waist;
    private String joinTime;
    private String endTime;
    private String username;
    private String password;
    private String status;
    private String cardType;
    private Integer cardDay;
    private BigDecimal price;
    private BigDecimal money;
}
```

- [ ] **Step 2: 创建 MemberCardDTO**

```java
package com.v1.api.dto.member_card;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MemberCardDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long cardId;
    private String title;
    private String cardType;
    private Integer cardDay;
    private BigDecimal price;
    private String status;
}
```

- [ ] **Step 3: 创建 MemberApplyDTO, MemberRechargeDTO, MemberRoleDTO, MemberCourseDTO**

MemberApplyDTO：
```java
package com.v1.api.dto.member_apply;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberApplyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memberId;
    private String cardType;
    private Integer cardDay;
    private BigDecimal price;
    private Date createTime;
}
```

MemberRechargeDTO：
```java
package com.v1.api.dto.member_recharge;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberRechargeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memberId;
    private BigDecimal money;
    private Date createTime;
}
```

MemberRoleDTO：
```java
package com.v1.api.dto.member_role;

import lombok.Data;
import java.io.Serializable;

@Data
public class MemberRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memberId;
    private Long roleId;
}
```

MemberCourseDTO：
```java
package com.v1.api.dto.member_course;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class MemberCourseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memberId;
    private Long courseId;
    private Date createTime;
}
```

- [ ] **Step 4: 创建 MemberRpcService**

```java
package com.v1.api.member;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member.MemberDTO;
import com.v1.api.dto.member_role.MemberRoleDTO;
import java.math.BigDecimal;

public interface MemberRpcService {
    PageResultDTO<MemberDTO> listMembers(PageDTO page, String name, String phone, String username, Long memberId, String userType);

    void addMember(MemberDTO member);

    void editMember(MemberDTO member);

    void deleteMember(Long memberId);

    MemberRoleDTO getRoleByMemberId(Long memberId);

    MemberDTO loadUser(String username);

    MemberDTO getMemberById(Long memberId);

    void joinCard(Long memberId, Long cardId);

    void recharge(Long memberId, BigDecimal money);
}
```

- [ ] **Step 5: 创建 MemberCardRpcService**

```java
package com.v1.api.member_card;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_card.MemberCardDTO;

public interface MemberCardRpcService {
    PageResultDTO<MemberCardDTO> listCards(PageDTO page, String title);

    MemberCardDTO getCardById(Long cardId);

    void saveCard(MemberCardDTO card);

    void updateCard(MemberCardDTO card);

    void deleteCard(Long cardId);
}
```

- [ ] **Step 6: 创建 MemberApplyRpcService, MemberRechargeRpcService, MemberRoleRpcService, MemberCourseRpcService**

MemberApplyRpcService：
```java
package com.v1.api.member_apply;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_apply.MemberApplyDTO;

public interface MemberApplyRpcService {
    PageResultDTO<MemberApplyDTO> list(PageDTO page, Long memberId);
}
```

MemberRechargeRpcService：
```java
package com.v1.api.member_recharge;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_recharge.MemberRechargeDTO;

public interface MemberRechargeRpcService {
    PageResultDTO<MemberRechargeDTO> getRechargeList(PageDTO page);

    PageResultDTO<MemberRechargeDTO> getRechargeByMember(PageDTO page, Long memberId);
}
```

MemberRoleRpcService：
```java
package com.v1.api.member_role;

import com.v1.api.dto.member_role.MemberRoleDTO;

public interface MemberRoleRpcService {
    MemberRoleDTO getByMemberId(Long memberId);

    void save(MemberRoleDTO role);
}
```

MemberCourseRpcService：
```java
package com.v1.api.member_course;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_course.MemberCourseDTO;

public interface MemberCourseRpcService {
    void joinCourse(MemberCourseDTO memberCourse);

    PageResultDTO<MemberCourseDTO> getMyCourseList(PageDTO page, Long memberId);
}
```

- [ ] **Step 7: 验证编译**

```bash
cd gym-parent-project && mvn compile -pl gym-api -am
```

Expected: BUILD SUCCESS

- [ ] **Step 8: Commit**

```bash
git add gym-api/src/main/java/com/v1/api/member/ gym-api/src/main/java/com/v1/api/member_card/ gym-api/src/main/java/com/v1/api/member_apply/ gym-api/src/main/java/com/v1/api/member_recharge/ gym-api/src/main/java/com/v1/api/member_role/ gym-api/src/main/java/com/v1/api/member_course/ gym-api/src/main/java/com/v1/api/dto/member/ gym-api/src/main/java/com/v1/api/dto/member_card/ gym-api/src/main/java/com/v1/api/dto/member_apply/ gym-api/src/main/java/com/v1/api/dto/member_recharge/ gym-api/src/main/java/com/v1/api/dto/member_role/ gym-api/src/main/java/com/v1/api/dto/member_course/
git commit -m "feat: add member domain RPC interfaces and DTOs"
```

---

### Task 6: 抽取课程/商品/订单域 RPC 接口 + DTO（course, goods, goods_order, equipment）

**Files:**
- Create: `gym-api/src/main/java/com/v1/api/dto/course/CourseDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/goods/GoodsDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/goods_order/GoodsOrderDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/goods_order/OrderItemDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/equipment/MaterialDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/course/CourseRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/goods/GoodsRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/goods_order/GoodsOrderRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/equipment/MaterialRpcService.java`

**Produces:** 课程/商品/订单域的完整 RPC 接口和 DTO

- [ ] **Step 1: 创建 CourseDTO**

```java
package com.v1.api.dto.course;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long courseId;
    private String courseName;
    private String image;
    private String teacherName;
    private Integer courseHour;
    private String courseDetails;
    private BigDecimal coursePrice;
    private Long teacherId;
}
```

- [ ] **Step 2: 创建 GoodsDTO, GoodsOrderDTO, OrderItemDTO, MaterialDTO**

GoodsDTO：
```java
package com.v1.api.dto.goods;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long goodsId;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String status;
}
```

GoodsOrderDTO：
```java
package com.v1.api.dto.goods_order;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GoodsOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Long userId;
    private String username;
    private BigDecimal totalPrice;
    private Date createTime;
    private String status;
    private List<OrderItemDTO> items;
}
```

OrderItemDTO：
```java
package com.v1.api.dto.goods_order;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long orderId;
    private Long goodsId;
    private String goodsName;
    private Integer quantity;
    private BigDecimal price;
}
```

MaterialDTO：
```java
package com.v1.api.dto.equipment;

import lombok.Data;
import java.io.Serializable;

@Data
public class MaterialDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String image;
    private Integer quantity;
    private String status;
}
```

- [ ] **Step 3: 创建 CourseRpcService**

```java
package com.v1.api.course;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.course.CourseDTO;

public interface CourseRpcService {
    PageResultDTO<CourseDTO> listCourses(PageDTO page, String courseName, String teacherName);

    CourseDTO getCourseById(Long courseId);

    void addCourse(CourseDTO course);

    void updateCourse(CourseDTO course);

    void deleteCourse(Long courseId);
}
```

- [ ] **Step 4: 创建 GoodsRpcService**

```java
package com.v1.api.goods;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.goods.GoodsDTO;

public interface GoodsRpcService {
    PageResultDTO<GoodsDTO> listGoods(PageDTO page, String name);

    GoodsDTO getGoodsById(Long goodsId);

    void addGoods(GoodsDTO goods);

    void updateGoods(GoodsDTO goods);

    void deleteGoods(Long goodsId);
}
```

- [ ] **Step 5: 创建 GoodsOrderRpcService**

```java
package com.v1.api.goods_order;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.goods_order.GoodsOrderDTO;
import java.util.List;

public interface GoodsOrderRpcService {
    PageResultDTO<GoodsOrderDTO> listOrders(PageDTO page, String username, String userType, Long userId);

    void createOrder(GoodsOrderDTO order);

    List<GoodsOrderDTO> getEChartData();
}
```

- [ ] **Step 6: 创建 MaterialRpcService**

```java
package com.v1.api.equipment;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.equipment.MaterialDTO;

public interface MaterialRpcService {
    PageResultDTO<MaterialDTO> listMaterials(PageDTO page, String name);

    MaterialDTO getMaterialById(Long id);

    void addMaterial(MaterialDTO material);

    void updateMaterial(MaterialDTO material);

    void deleteMaterial(Long id);
}
```

- [ ] **Step 7: 验证编译**

```bash
cd gym-parent-project && mvn compile -pl gym-api -am
```

Expected: BUILD SUCCESS

- [ ] **Step 8: Commit**

```bash
git add gym-api/src/main/java/com/v1/api/course/ gym-api/src/main/java/com/v1/api/goods/ gym-api/src/main/java/com/v1/api/goods_order/ gym-api/src/main/java/com/v1/api/equipment/ gym-api/src/main/java/com/v1/api/dto/course/ gym-api/src/main/java/com/v1/api/dto/goods/ gym-api/src/main/java/com/v1/api/dto/goods_order/ gym-api/src/main/java/com/v1/api/dto/equipment/
git commit -m "feat: add course/goods/order/equipment domain RPC interfaces and DTOs"
```

---

### Task 7: 抽取剩余域 RPC 接口 + DTO（home, suggest, lost, image）

**Files:**
- Create: `gym-api/src/main/java/com/v1/api/dto/home/EChartDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/home/EChartItemDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/home/TotalCountDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/suggest/SuggestDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/dto/lost/LostDTO.java`
- Create: `gym-api/src/main/java/com/v1/api/home/HomeRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/suggest/SuggestRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/lost/LostRpcService.java`
- Create: `gym-api/src/main/java/com/v1/api/image/ImageRpcService.java`

**Produces:** 剩余所有域的完整 RPC 接口和 DTO

- [ ] **Step 1: 创建所有剩余 DTO**

EChartDTO：
```java
package com.v1.api.dto.home;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class EChartDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> xData;
    private List<EChartItemDTO> series;
}
```

EChartItemDTO：
```java
package com.v1.api.dto.home;

import lombok.Data;
import java.io.Serializable;

@Data
public class EChartItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String type;
    private List<Long> data;
}
```

TotalCountDTO：
```java
package com.v1.api.dto.home;

import lombok.Data;
import java.io.Serializable;

@Data
public class TotalCountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long memberCount;
    private Long courseCount;
    private Long goodsCount;
    private Long orderCount;
    private Long revenue;
}
```

SuggestDTO：
```java
package com.v1.api.dto.suggest;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SuggestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private Date dateTime;
    private String status;
}
```

LostDTO：
```java
package com.v1.api.dto.lost;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class LostDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String lostName;
    private String image;
    private String description;
    private Date lostTime;
    private String status;
}
```

- [ ] **Step 2: 创建 HomeRpcService**

```java
package com.v1.api.home;

import com.v1.api.dto.home.EChartDTO;
import com.v1.api.dto.home.TotalCountDTO;

public interface HomeRpcService {
    TotalCountDTO getTotalCount();

    EChartDTO getEChartData();

    void resetPassword(Long userId, String userType, String newPassword);
}
```

- [ ] **Step 3: 创建 SuggestRpcService, LostRpcService, ImageRpcService**

SuggestRpcService：
```java
package com.v1.api.suggest;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.suggest.SuggestDTO;

public interface SuggestRpcService {
    PageResultDTO<SuggestDTO> list(PageDTO page, String title);

    void add(SuggestDTO suggest);

    void delete(Long id);
}
```

LostRpcService：
```java
package com.v1.api.lost;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.lost.LostDTO;

public interface LostRpcService {
    PageResultDTO<LostDTO> list(PageDTO page, String lostName);

    void add(LostDTO lost);

    void update(LostDTO lost);

    void delete(Long id);
}
```

ImageRpcService：
```java
package com.v1.api.image;

public interface ImageRpcService {
    String uploadImage(byte[] fileBytes, String fileName, String contentType);
}
```

- [ ] **Step 4: 验证编译**

```bash
cd gym-parent-project && mvn compile -pl gym-api -am
```

Expected: BUILD SUCCESS

- [ ] **Step 5: Commit**

```bash
git add gym-api/src/main/java/com/v1/api/home/ gym-api/src/main/java/com/v1/api/suggest/ gym-api/src/main/java/com/v1/api/lost/ gym-api/src/main/java/com/v1/api/image/ gym-api/src/main/java/com/v1/api/dto/home/ gym-api/src/main/java/com/v1/api/dto/suggest/ gym-api/src/main/java/com/v1/api/dto/lost/
git commit -m "feat: add home/suggest/lost/image domain RPC interfaces and DTOs"
```

---

### Task 8: gym-service-web 增加 gym-api 依赖 + Dubbo 配置

**Files:**
- Modify: `gym-service-web/pom.xml`
- Modify: `gym-service-web/src/main/resources/application-dev.yml`

**Produces:** gym-service-web 可引用 gym-api 接口，Dubbo 配置就绪（暂时禁用，Phase 2 启用）

- [ ] **Step 1: gym-service-web/pom.xml 增加 gym-api 依赖**

在 `gym-service-web/pom.xml` 的 `<dependencies>` 中，`gym-common` 依赖之后追加：

```xml
<dependency>
    <groupId>com.v1</groupId>
    <artifactId>gym-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

- [ ] **Step 2: gym-service-web/pom.xml 增加 Dubbo 依赖**

在同一文件的 `<dependencies>` 末尾追加：

```xml
<!-- Dubbo -->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-registry-nacos</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.nacos</groupId>
    <artifactId>nacos-client</artifactId>
</dependency>
```

- [ ] **Step 3: application-dev.yml 增加 Dubbo 配置（注释状态）**

在 `application-dev.yml` 末尾追加：

```yaml
# Dubbo 配置（Phase 2 启用，当前注释）
# dubbo:
#   application:
#     name: gym-service-web
#   registry:
#     address: nacos://127.0.0.1:8848
#   protocol:
#     name: dubbo
#     port: 20880
#   consumer:
#     check: false
#     timeout: 3000
```

- [ ] **Step 4: 验证编译**

```bash
cd gym-parent-project && mvn compile
```

Expected: BUILD SUCCESS for all modules

- [ ] **Step 5: Commit**

```bash
git add gym-service-web/pom.xml gym-service-web/src/main/resources/application-dev.yml
git commit -m "feat: add gym-api dependency and Dubbo config placeholder to gym-service-web"
```

---

### Task 9: 验证全量编译 + 最终检查

**Files:** (none modified, verification only)

- [ ] **Step 1: 全量编译**

```bash
cd gym-parent-project && mvn clean compile
```

Expected: BUILD SUCCESS

- [ ] **Step 2: 跳过测试打包验证**

```bash
cd gym-parent-project && mvn package -DskipTests
```

Expected: BUILD SUCCESS

- [ ] **Step 3: 检查 gym-api 模块的 JAR 是否包含所有接口和 DTO**

```bash
jar tf gym-api/target/gym-api-1.0-SNAPSHOT.jar | grep -E "RpcService|DTO" | sort
```

Expected: 列出所有 RpcService 和 DTO 的 .class 文件（约 20+ 接口，30+ DTO）

- [ ] **Step 4: Commit（如有未提交文件）**

```bash
git status
```

---

## Self-Review Checklist

**1. Spec coverage:**
- [x] 搭建 Nacos 注册中心 → Task 1, Task 8（依赖引入 + 配置占位）
- [x] 引入 Dubbo 依赖 → Task 1（父 POM）, Task 8（子模块引用）
- [x] 抽取 gym-api 模块（接口 + DTO）→ Task 2~7（全部 20 个域）

**2. Placeholder scan:**
- 无 TBD/TODO
- 所有代码步骤包含完整实现
- 所有接口方法签名明确定义

**3. Type consistency:**
- PageDTO/PageResultDTO 在 Task 3 定义，Task 4~7 引用一致
- 所有 DTO 均 implements Serializable 并声明 serialVersionUID
- RPC 接口包名与 DTO 包名对应一致
