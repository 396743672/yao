package com.y3tu.cloud.back.model.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.y3tu.cloud.back.model.entity.SysRole;
import lombok.Data;

@Data
public class SysRoleQuery extends Page<SysRole> {


    /**
     * 角色code用于springsecurity角色标识码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

}
