package util.response;

import java.io.File;

public class FileResponse<T> extends Response<File> {

	public FileResponse(File responseContent) {
		super(responseContent);
	}
	
	public File getContent() {
		return responseContent;
	}
}
