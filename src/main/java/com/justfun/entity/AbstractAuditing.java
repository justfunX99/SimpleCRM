package com.justfun.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditing implements Serializable {
	private static final long serialVersionUID = 1L;

	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private String created_by;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
    @Column(name = "created_at", updatable = false)
	private Date created_at;
	
	@LastModifiedBy
	@Column(name = "updated_by")
	private String updated_by;
	
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name = "updated_at")
	private Date updated_at;
	
}
