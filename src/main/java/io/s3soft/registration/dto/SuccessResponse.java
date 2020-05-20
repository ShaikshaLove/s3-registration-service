package io.s3soft.registration.dto;

public class SuccessResponse<T> {
    T content;
    int status;
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public SuccessResponse(T content, int status) {
		super();
		this.content = content;
		this.status = status;
	}
	@Override
	public String toString() {
		return "SuccessResponse [content=" + content + ", status=" + status + "]";
	}
    
}
