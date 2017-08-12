package me.iszhenyu.ifiji.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by xiaoz on 2017/5/11.
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class BaseDO implements Serializable {
	private static final long serialVersionUID = 5017546373479056007L;
	protected Long id;
	protected Boolean deleted; 	// 删除标记
	protected Date gmtCreated; 	// 创建时间
	protected Date gmtModified; // 修改时间
}
