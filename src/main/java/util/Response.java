package util;

public class Response<T> {

	private final T responseContent;
	
	public Response(T responseContent) {
		this.responseContent = responseContent;
	}
	
	public T getContent() {
		return responseContent;
	}
}
