package me.iszhenyu.ifiji.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class BaseDO {
	protected Long id;
	protected Boolean deleted; 	// 删除标记
	protected Date gmtCreate; 	// 创建时间
	protected Date gmtModified; // 修改时间
}
