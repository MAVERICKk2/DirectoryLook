import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegexPatternMatcher {
	private String name;
	private String regexPattern;
	
	RegexPatternMatcher(){
		this.name = "rtest.xls";
		this.regexPattern = "test.xls";
	}
	
	RegexPatternMatcher(String name, String regexPattern){
		this.name = name;
		this.regexPattern = regexPattern;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setregexPattern(String regexPattern){
		this.regexPattern = regexPattern;
	}
	
	public String getregexPatter(){
		return this.regexPattern;
	}
	
	// This functions returns true if the file match with the pattern defined
	public boolean find() {
		Pattern pattern = Pattern.compile(this.regexPattern);

		Matcher matcher = pattern.matcher(this.name);
		
		// Check if Match
		if (matcher.find()) {
			//System.out.println(matcher.group());
			return true;
		}
		else {
			return false;
		}
	}
}