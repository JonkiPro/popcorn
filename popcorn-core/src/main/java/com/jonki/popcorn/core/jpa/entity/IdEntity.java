package com.jonki.popcorn.core.jpa.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Base class which only provides an ID. For use cases when we don't care about audit fields like
 * created, entity version etc.
 */
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
@MappedSuperclass
public class IdEntity implements Serializable {

    private static final long serialVersionUID = 2266079352696950754L;

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;
}
