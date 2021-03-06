package jp.redmine.gttnl.entity;

import java.util.Date;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class RedminePriority
		implements IConnectionRecord
		,IMasterRecord
{
	public final static String ID = "id";
	public final static String CONNECTION = RedmineConnection.CONNECTION_ID;
	public final static String PRIORITY_ID = "priority_id";
	public final static String NAME = "name";

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(uniqueIndexName="priority_target", columnName = RedmineConnection.CONNECTION_ID)
    private Integer connection_id;
    @DatabaseField(uniqueIndexName="priority_target")
    private int priority_id;
    @DatabaseField
    private String name;
    @DatabaseField
    private boolean is_default;
    @DatabaseField
    private Date created;
    @DatabaseField
    private Date modified;


    @Override
    public String toString(){
    	return name;
    }


    /**
	 * @param id セットする id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	////////////////////////////////////////////////////////
	/**
	 * @param name セットする 名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return name 名称
	 */
	public String getName() {
		return name;
	}

	@Override
	public void setRedmineConnection(RedmineConnection connection) {
		this.setConnectionId(connection.getId());
	}
	/**
	 * @param created セットする created
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	/**
	 * @return created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param modified セットする modified
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}
	/**
	 * @return modified
	 */
	public Date getModified() {
		return modified;
	}


	/**
	 * @param status_id セットする status_id
	 */
	public void setPriorityId(int priority_id) {
		this.priority_id = priority_id;
	}


	/**
	 * @return status_id
	 */
	public int getPriorityId() {
		return priority_id;
	}

	@Override
	public void setConnectionId(Integer connection_id) {
		this.connection_id = connection_id;
	}
	@Override
	public Integer getConnectionId() {
		return connection_id;
	}


	@Override
	public void setRemoteId(Long id) {
		setPriorityId((id==null)? null : id.intValue());
	}


	@Override
	public Long getRemoteId() {
		return (long)getPriorityId();
	}

	/**
	 * @return is_default
	 */
	public boolean isDefault() {
		return is_default;
	}


	/**
	 * @param is_default セットする is_default
	 */
	public void setDefault(boolean is_default) {
		this.is_default = is_default;
	}


}
