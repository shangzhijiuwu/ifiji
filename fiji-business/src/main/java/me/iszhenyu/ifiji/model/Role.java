package me.iszhenyu.ifiji.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.iszhenyu.ifiji.constant.RoleType;
import me.iszhenyu.ifiji.core.model.BaseDO;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zhen.yu
 * @since 2017/6/7
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class Role extends BaseDO {
    private static final long serialVersionUID = 3162650930769908350L;

    private String name;
    private RoleType type;
    private String description;
    private List<Permission> permissions = new LinkedList<>();
}
