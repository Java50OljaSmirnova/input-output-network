package telran.io.performance;

import telran.io.CopyFile;
import telran.performance.PerformanceTest;

public class CopyFilePerformanceTest extends PerformanceTest {
	
	String sourceFile;
	String destinationFile;
	CopyFile copyFiles;
	public CopyFilePerformanceTest(String testName,int nRuns, String sourceFile, String destinationFile, CopyFile copyFiles) {
		super(testName, nRuns);
		this.sourceFile = sourceFile;
		this.destinationFile = destinationFile;
		this.copyFiles = copyFiles;
	}

	@Override
	protected void runTest() {
		// TODO Auto-generated method stub

	}

}
