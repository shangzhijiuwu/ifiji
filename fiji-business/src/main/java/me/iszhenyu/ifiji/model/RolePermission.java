package me.iszhenyu.ifiji.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhen.yu
 * @since 2017/8/12
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 4892503378960254129L;

    private Long roleId;
    private Long permissionId;
}
