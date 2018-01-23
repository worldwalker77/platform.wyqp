package cn.worldwalker.game.wyqp.common.domain.base;

public class RedisRequest {
	public String operation;
	public String key;
	public String field;
	public Object value;
	public int start;
	public int end;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	
}
