package telran.io;

public class ReadBufferCopy implements CopyFile {
	int bufferSize;
	
	public ReadBufferCopy(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	@Override
	public void copyFiles(String sourceFile, String destinationFile) {
		
	}

}
