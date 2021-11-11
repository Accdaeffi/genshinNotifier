package util.response;

public abstract class Response<T> {

	protected final T responseContent;
	
	public Response(T responseContent) {
		this.responseContent = responseContent;
	}
	
	public T getContent() {
		return responseContent;
	}
}
