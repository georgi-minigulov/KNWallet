package org.gfm.wallet.model;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.gfm.wallet.data.ActionTypes;
import org.gfm.wallet.model.Wallet.CurrencyConverter;
import org.springframework.util.StringUtils;


/**
 * History record
 * 
 * @author Gerodot
 */
@Entity
@Table(name = "t_history")
public class HistoryRecord extends DataItemBase {
	@Convert(converter = CurrencyConverter.class)
	@Enumerated(EnumType.STRING)
	@Transient
	private ActionTypes actionType;
	@Column(name = "action_type")
	private String actionTypeString;

	@Column(name = "source_name")
	private String sourceName;
	@Column(name = "source_id")
	private Long sourceId;

	@Column(name = "target_name")
	private String targetName;
	@Column(name = "target_id")
	private Long targetId;

	@Column(name = "message")
	private String message;
	@Column(name = "value")
	private String value;

	public HistoryRecord() {
		super();
	}
	
	public HistoryRecord(ActionTypes actionType, DataItemBase source, DataItemBase target, String message, String value) {
		this();
		setActionType(actionType);
		this.sourceName = source==null ? null : source.getClass().getSimpleName().toLowerCase();
		this.sourceId = source==null ? null :source.getId(); 
		this.targetName = target==null ? null : target.getClass().getSimpleName().toLowerCase();
		this.targetId = target==null ? null :target.getId(); 
		this.message = StringUtils.hasText(message) ?  message.trim().substring(0, Math.min(message.trim().length(), 255)) : null;
		this.value = value == null ? null : value.trim();
	}
		
	public void setActionTypeString(String val) {
		actionTypeString = val;
		actionType = StringUtils.hasText(val) ? ActionTypes.valueOf(val) : null;
	}	

	public void setActionType(ActionTypes val) {
		actionType = val;
		actionTypeString = val == null ? null : val.name();
	}
	
	public String getSourceName() {
		return sourceName;
	}
	public Long getSourceId() {
		return sourceId;
	}
	public ActionTypes getActionType() {
		return actionType;
	}
	public String getTargetName() {
		return targetName;
	}
	public Long getTargetId() {
		return targetId;
	}
	public String getMessage() {
		return message;
	}
	public String getValue() {
		return value;
	}
	
	@Override
    public int hashCode() {
		return 31*31*31*31*31*31*31*super.hashCode()+Objects.hash(actionType, sourceName, sourceId, targetName, targetId, message, value);
	}
	
	
	@Converter(autoApply = true)
	protected class ActionTypeConverter implements AttributeConverter<ActionTypes, String> {
	    @Override
	    public String convertToDatabaseColumn(ActionTypes val) {
	    	return val == null ? null : val.name();
	    }

	    @Override
	    public ActionTypes convertToEntityAttribute(String val) {
	    	return StringUtils.hasText(val) ? ActionTypes.valueOf(val) : null;
	    }
	}	

	@Override
    public String toString() {
        return "HistoryItem [id=" + String.valueOf(id)+
        				  ", actionType="+ String.valueOf(actionType) + 
        				  ", sourceName="+ String.valueOf(sourceName) + 
        				  ", sourceId="+ String.valueOf(sourceId) + 
        				  ", targetName="+ String.valueOf(targetName) + 
        				  ", targetId="+ String.valueOf(targetId) + 
        				  ", message="+ String.valueOf(message) + 
        				  ", value="+ String.valueOf(value) + 
        		          ", created=" + String.valueOf(created) + "]";
    }

}
