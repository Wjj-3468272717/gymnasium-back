package com.v1.web.sys_user.controller;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.sys_user.SysUserDTO;
import com.v1.api.sys_user.SysUserRpcService;
import com.v1.api.sys_user_role.SysUserRoleRpcService;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.common.entity.SelectType;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @DubboReference
    private SysUserRpcService sysUserRpcService;
    @DubboReference
    private SysUserRoleRpcService sysUserRoleRpcService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 新增用户
     */
    @PostMapping
    public ResultVo addUser(@RequestBody SysUserDTO sysUser){
        SysUserDTO existingUser = sysUserRpcService.loadUser(sysUser.getUsername());
        if(existingUser != null){
            return ResultUtils.error("用户名已经存在!");
        }
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUser.setIsAdmin("0");

        Boolean saved = sysUserRpcService.saveUser(sysUser);
        if(Boolean.TRUE.equals(saved)){
            return ResultUtils.success("新增用户成功");
        }else{
            return ResultUtils.error("新增用户失败");
        }
    }

    /**
     * 编辑用户
     */
    @PutMapping
    public ResultVo editUser(@RequestBody SysUserDTO sysUser){
        SysUserDTO existingUser = sysUserRpcService.loadUser(sysUser.getUsername());
        if(existingUser != null && !sysUser.getUserId().equals(existingUser.getUserId())){
            return ResultUtils.error("用户名已经被占用!");
        }
        if(StringUtils.isNotEmpty(sysUser.getPassword())){
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        }
        Boolean updated = sysUserRpcService.updateUser(sysUser);
        if(Boolean.TRUE.equals(updated)){
            return ResultUtils.success("编辑用户成功");
        }else{
            return ResultUtils.error("编辑用户失败");
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    public ResultVo deleteUser(@PathVariable("userId") Long userId){
        Boolean deleted = sysUserRpcService.deleteUser(userId);
        if(Boolean.TRUE.equals(deleted)){
            return ResultUtils.success("删除用户成功");
        }else{
            return ResultUtils.error("删除用户失败");
        }
    }

    /**
     * 查询用户列表
     */
    @GetMapping("/list")
    public ResultVo getList(Long currentPage, Long pageSize, String nickName, String phone){
        PageDTO page = new PageDTO();
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        PageResultDTO<SysUserDTO> result = sysUserRpcService.listUsers(page, nickName, phone);
        if(result.getRecords() != null){
            result.getRecords().forEach(item -> item.setPassword(""));
        }
        return ResultUtils.success("用户查询成功", result);
    }

    @GetMapping("/role")
    public ResultVo getRole(Long userId){
        Long roleId = sysUserRoleRpcService.getUserRoleId(userId);
        return ResultUtils.success("查询成功", roleId);
    }

    @GetMapping("getTeacher")
    public ResultVo getTeacher(){
        List<SysUserDTO> list = sysUserRpcService.getTeachers();
        List<SelectType> selectTypes = new ArrayList<>();
        if(list.size() > 0){
            list.stream().forEach(item ->{
                SelectType selectType = new SelectType();
                selectType.setLabel(item.getNickName());
                selectType.setValue(item.getUserId());
                selectTypes.add(selectType);
            });
        }
        return ResultUtils.success("查询成功", selectTypes);
    }

}
