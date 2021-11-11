package util.response;

public class StringResponse<T> extends Response<String> {
	
	public StringResponse(String responseContent) {
		super(responseContent);
	}
	
	public String getContent() {
		return responseContent;
	}
}
