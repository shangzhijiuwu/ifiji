package me.iszhenyu.ifiji.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import me.iszhenyu.ifiji.core.model.BaseDO;

/**
 * @author zhen.yu
 * @since 2017/6/7
 */
@Setter
@Getter
@ToString(callSuper = true)
@NoArgsConstructor
public class Permission extends BaseDO {
    private static final long serialVersionUID = 6743485905828813917L;
    private String name;  // 操作名称
    private String url;   // 操作url
}
