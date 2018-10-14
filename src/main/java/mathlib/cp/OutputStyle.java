package mathlib.cp;

public enum OutputStyle {
	JSON(0), PRETTY_JSON(1), TEXT(2), CSV(3) ;
	
	OutputStyle(int val) { this.value = val;}
	private final int value;
    public int value() { return value; }
}
