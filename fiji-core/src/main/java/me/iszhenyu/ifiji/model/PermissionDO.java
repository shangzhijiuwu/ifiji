package me.iszhenyu.ifiji.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhen.yu
 * @since 2017/6/7
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class PermissionDO extends BaseDO {
    private String name;
    private String url;
    private String permission;

}