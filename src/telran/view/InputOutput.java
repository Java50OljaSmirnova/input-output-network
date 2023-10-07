package telran.view;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);
	void writeString(String string);
	default void writeLine(String string) {
		writeString(string + "\n");
	}
	default void writeObject(Object object) {
		writeString(object.toString());
	}
	default void writeObjectLine(Object object) {
		writeLine(object + "\n");
	};
	default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		boolean running = false;
		T res = null;
		do {
			running = false;
			String string = readString(prompt);
			try {
				res = mapper.apply(string);
				
			}catch(RuntimeException e) {
				writeLine(errorPrompt + ": " + e.getMessage());
				running = true;			
			}
			
		}while(running);
		return res;
	};
	default Integer readInt(String prompt, String errorPrompt) {
		
		return readObject(prompt, errorPrompt, Integer::parseInt);
	};
	default Integer readInt(String prompt, String errorPrompt, int min, int max) {
//		int res = readInt(prompt, errorPrompt);
		int res = Integer.parseInt(prompt);
		if(res < min || res > max) {
			throw new RuntimeException(errorPrompt);
		}
		return res;
	};
	default Long readLong(String prompt, String errorPrompt) {
		
		return readObject(prompt, errorPrompt, Long::parseLong);
	};
	default Long readLong(String prompt, String errorPrompt, long min, long max) {
//		long res = readLong(prompt, errorPrompt);
		long res = Long.parseLong(prompt);
		if(res < min || res > max) {
			throw new RuntimeException(errorPrompt);
		}
		return res;
	};
	default Double readDouble(String prompt, String errorPrompt) {
		
		return readObject(prompt, errorPrompt, Double::parseDouble);
	};
	default String readString(String prompt, String errorPrompt, Predicate<String> pattern){
		
		String res = prompt;
		if(!pattern.test(res)){
			throw new RuntimeException(errorPrompt);
		}
		
		return res;
	};
	default String readString(String prompt, String errorPrompt, HashSet<String> options) {
		String res = prompt;
		if(!options.contains(res)){
			throw new RuntimeException(errorPrompt);
		}
		
		return res;
	};
	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		
		return readObject(prompt, errorPrompt, LocalDate::parse);
	};
	default LocalDate readIsoDate(String prompt, String errorPrompt, LocalDate min, LocalDate max) {
		LocalDate res = LocalDate.parse(prompt);
		if(res.isBefore(min) || res.isAfter(max)) {
			throw new RuntimeException(errorPrompt);
		}
		return res;
	};
}
