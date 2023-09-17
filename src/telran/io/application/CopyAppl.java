package telran.io.application;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.io.*;
public class CopyAppl {

	private static final int BUFFER_SIZE = 1024 * 1024 * 1024;

	public static void main(String[] args) {
		//args[0] - source file
		//args[2] - destination file
		//args[3] - "overwrite"
		//args[1] - destination file
		//args[2] - "overwrite" optional
		//TODO write application for copying from source file to destination file
		//Implementation Requirement: to use while cycle with read call
		//main must not contain throws declaration
		if(args.length < 2 ) {
			System.out.println("too few arguments ;  usage: <args[0] - source file;"
					+ "args[1] - destination file; "
					+ "args[2] - \"overwrite\" optional>");
		} else {
			if(!Files.exists(Path.of(args[0]))) {
				System.out.println(args[0] + " file doesn't exist");
			} else if(Files.exists(Path.of(args[1])) && (args.length < 3 || !args[2].equals("overwrite"))) {
				System.out.println(args[1] + " file  exists and cannot be overwritten");
			} else {
				try {
					copy(args[0], args[1]);
				} catch (RuntimeException e) {
					e.printStackTrace();
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

	}

	private static void copy(String source, String destination) throws Exception{
		try(InputStream input = new FileInputStream(source); OutputStream output = new FileOutputStream(destination)){
			long total = 0;
			int length = 0;
			byte buffer[] = new byte[BUFFER_SIZE];
			Instant start = Instant.now();
			while((length = input.read(buffer)) > 0) {
				total += length;
				output.write(buffer, 0, length);
			}
			System.out.printf("total bytes: %d; time: %d\n", total, ChronoUnit.MILLIS.between(start, Instant.now()));
		}

	}

}
