package org.gfm.wallet.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * Base class for Entity item 
 * 
 * @author Gerodot
 */
@MappedSuperclass
public abstract class DataItemBase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "dataIncrementor")
	@GenericGenerator(name = "dataIncrementor", strategy = "increment")
	@Column(name = "id")
	protected Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "created")
	protected Date created;
	
	public DataItemBase () {
		id = null;
		created = new Date();
	}
	
	public Long getId() {
		return id;
	}

	public Date getCreated() {
		return created;
	}

	@Override
    public int hashCode() {
		return Objects.hash(id.hashCode());		
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return this.hashCode()==obj.hashCode();
	}

	@Override
    public String toString() {
        return "DataItem [id=" + String.valueOf(id)+ ", created=" + String.valueOf(created) + "]";
    }
}
