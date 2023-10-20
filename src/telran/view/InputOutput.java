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
		writeLine(object.toString());
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
		return readObject(String.format("%s[%d - %d] ", prompt, min, max), errorPrompt,
				string -> {

			int res = Integer.parseInt(string);
			if (res < min) {
				throw new IllegalArgumentException("must be not less than " + min);
			}
			if (res > max) {
				throw new IllegalArgumentException("must be not greater than " + max);
			}
			return res;

		});
	};
	default Long readLong(String prompt, String errorPrompt) {
		
		return readObject(prompt, errorPrompt, Long::parseLong);
	};
	default Long readLong(String prompt, String errorPrompt, long min, long max) {
		return readObject(String.format("%s[%d - %d] ", prompt, min, max), errorPrompt, str -> {
			long res = Long.parseLong(str);
			if (res < min) {
				throw new IllegalArgumentException("must be not less than " + min);
			}
			if (res > max) {
				throw new IllegalArgumentException("must be not greater than " + max);
			}
			return res;});
		};
	default Double readDouble(String prompt, String errorPrompt) {
		
		return readObject(prompt, errorPrompt, Double::parseDouble);
	};
	default String readString(String prompt, String errorPrompt, Predicate<String> pattern){
		return readObject(prompt, errorPrompt, str -> {
			if(!pattern.test(str)){
				throw new IllegalArgumentException("");
			}	
			return str;
		});
		
	};	default String readString(String prompt, String errorPrompt, HashSet<String> options) {
		return readObject(prompt, errorPrompt, str -> {
			if(!options.contains(str)){
				throw new RuntimeException("Enter option is incorrect");
			}	
			return str;
		});
		
	};
	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		
		return readObject(prompt, errorPrompt, LocalDate::parse);
	};
	default LocalDate readIsoDate(String prompt, String errorPrompt, LocalDate min, LocalDate max) {
		return readObject(prompt, errorPrompt, str -> {
		LocalDate res = LocalDate.parse(str);
		if(res.isBefore(min) || res.isAfter(max)) {
			throw new IllegalArgumentException(String.format("Date should be in the range from %s to %s", min, max));
		}
		return res;});
	};
}
