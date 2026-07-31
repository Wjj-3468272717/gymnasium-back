package com.v1.web.sys_user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.service.user.sys_role.entity.SelectType;
import com.v1.service.user.sys_user.entity.PageParam;
import com.v1.service.user.sys_user.entity.SysUser;
import com.v1.service.user.sys_user.service.SysUserService;
import com.v1.service.user.sys_user_role.entiry.SysUserRole;
import com.v1.service.user.sys_user_role.service.SysUserRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 新增用户
     * @param sysUser
     * @return
     */
    @PostMapping
    public ResultVo addUser(@RequestBody SysUser sysUser){
        QueryWrapper<SysUser> q = new QueryWrapper<>();
        q.lambda().eq(SysUser::getUsername,sysUser.getUsername());
        SysUser one = sysUserService.getOne(q);
        //用户名是否被占用
        if(one != null){
            return ResultUtils.error("用户名已经存在!");
        }
        //密码加密
//        if(StringUtils.isNotEmpty(sysUser.getPassword())){
//            sysUser.setPassword(DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes()));
//        }
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUser.setIsAdmin("0");
        sysUser.setCreateTime(new Date());

        boolean updated = sysUserService.save(sysUser);
        if(updated){
            return ResultUtils.success("新增用户成功");
        }else{
            return ResultUtils.error("新增用户失败");
        }
    }

    /**
     * 编辑用户
     * @param sysUser
     * @return
     */
    @PutMapping
    public ResultVo editUser(@RequestBody SysUser sysUser){
        QueryWrapper<SysUser> q = new QueryWrapper<>();
        q.lambda().eq(SysUser::getUsername,sysUser.getUsername());
        SysUser one = sysUserService.getOne(q);
        //用户名是否被占用
        if(one != null && sysUser.getUserId() != one.getUserId()){
            return ResultUtils.error("用户名已经被占用!");
        }
        //密码加密
        if(StringUtils.isNotEmpty(sysUser.getPassword())){
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        }
        sysUser.setUpdateTime(new Date());
        boolean updated = sysUserService.updateById(sysUser);
        if(updated){
            return ResultUtils.success("编辑用户成功");
        }else{
            return ResultUtils.error("编辑用户失败");
        }
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResultVo deleteUser(@PathVariable("userId") Long userId){
        boolean updated = sysUserService.removeById(userId);
        if(updated){
            return ResultUtils.success("删除用户成功");
        }else{
            return ResultUtils.error("删除用户失败");
        }
    }

    /**
     * 查询用户列表
     * @param pageParam
     * @return
     */
    @GetMapping("/list")
    public ResultVo getList(PageParam pageParam){
        IPage<SysUser> list = sysUserService.list(pageParam);
        list.getRecords().stream().forEach(item ->{
            item.setPassword("");
        });
        return ResultUtils.success("用户查询成功",list);
    }

    @GetMapping("/role")
    public ResultVo getRole(Long userId){
        QueryWrapper<SysUserRole> q = new QueryWrapper<>();
        q.lambda().eq(SysUserRole::getUserId,userId);
        SysUserRole one = sysUserRoleService.getOne(q);
        return ResultUtils.success("查询成功",one);
    }

    //查询教师
    @GetMapping("getTeacher")
    public ResultVo getTeacher(){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getUserType,"2");
        List<SysUser> list = sysUserService.list(queryWrapper);
        //组装数据
        List<SelectType> selectTypes = new ArrayList<>();
        if(list.size()>0){
            list.stream().forEach(item ->{
                SelectType selectType = new SelectType();
                selectType.setLabel(item.getNickName());
                selectType.setValue(item.getUserId());
                selectTypes.add(selectType);
            });
        }
        return ResultUtils.success("查询成功",selectTypes);
    }

}
