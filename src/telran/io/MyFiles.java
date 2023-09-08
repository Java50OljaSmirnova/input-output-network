package telran.io;

import java.io.IOException;
import java.nio.file.*;

public class MyFiles {
public static void displayDir(String path, int maxDepth) throws IOException {
	//TODO
	//Throwing IllegalArgumentException in the case the given is not a directory
	//Printing out the directory content using appropriate offsets showing which node belong to which directory
	//printing type of node, for example 
	//                              input-output-network - dir
	//                               src - dir
	//                                 telran - dir
	//                                   io - dir
	//                                    test - dir
	//                                      FileSystemTests.java - file
	try {
		Files.walk(Path.of(path).toAbsolutePath().normalize(), maxDepth).forEach(p -> System.out.println(p.getFileName()));
	} catch (IOException e) {

	}
}
}
