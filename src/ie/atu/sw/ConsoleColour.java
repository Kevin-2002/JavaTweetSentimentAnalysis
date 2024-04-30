package ie.atu.sw;

/*
 * ANSI escape sequences are a standard for controlling cursor location, colour, 
 * font styling, and other options on DOS, Mac and Linux terminals. The ANSI escape 
 * codes are formatted as follows:
 * 
 *  	[<PREFIX>];[<COLOR>];[<TEXT DECORATION>]
 *  
 *  See https://en.wikipedia.org/wiki/ANSI_escape_code for a decent description.
 */
public enum ConsoleColour { 
	//Reset
    RESET						("Reset",						"0"),

    //Regular Colours
    BLACK						("Black [Regular]",				"0;30"),
    RED							("Red [Regular]", 				"0;31"),
    GREEN						("Green [Regular]",				"0;32"),
    YELLOW						("Yellow [Regular]", 			"0;33"),
    BLUE						("Blue [Regular]", 				"0;34"),
    PURPLE						("Purple [Regular]", 			"0;35"),
    CYAN						("Cyan [Regular]", 				"0;36"),
    WHITE						("White [Regular]", 			"0;37"),

    //Bold
    BLACK_BOLD					("Black [Bold]", 				"1;30"),
    RED_BOLD					("Red [Bold]", 					"1;31"),
    GREEN_BOLD					("Green [Bold]", 				"1;32"),
    YELLOW_BOLD					("Yellow [Bold]", 				"1;33"),
    BLUE_BOLD					("Blue [Bold]", 				"1;34"),
    PURPLE_BOLD					("Purple [Bold]", 				"1;35"),
    CYAN_BOLD					("Cyan [Bold]", 				"1;36"),
    WHITE_BOLD					("White [Bold]", 				"1;37"),

    //Underline
    BLACK_UNDERLINED			("Black [Underline]",			"4;30"),
    RED_UNDERLINED				("Red [Underline]", 			"4;31"),
    GREEN_UNDERLINED			("Green [Underline]", 			"4;32"),
    YELLOW_UNDERLINED			("Yellow [Underline]", 			"4;33"),
    BLUE_UNDERLINED				("Blue [Underline]", 			"4;34"),
    PURPLE_UNDERLINED			("Purple [Underline]", 			"4;35"),
    CYAN_UNDERLINED				("Cyan [Underline]", 			"4;36"),
    WHITE_UNDERLINED			("White [Underline]", 			"4;37"),

    //Background
    BLACK_BACKGROUND			("Black [Background]",			"40"),
    RED_BACKGROUND				("Red [Background]", 			"41"),
    GREEN_BACKGROUND			("Green [Background]", 			"42"),
    YELLOW_BACKGROUND			("Yellow [Background]", 		"43"),
    BLUE_BACKGROUND				("Blue [Background]", 			"44"),
    PURPLE_BACKGROUND			("Purple [Background]", 		"45"),
    CYAN_BACKGROUND				("Cyan [Background]", 			"46"),
    WHITE_BACKGROUND			("White [Background]", 			"47"),

    //High intensity
    BLACK_BRIGHT				("Black [High Intensity]", 		"0;90"),
    RED_BRIGHT					("Red [High Intensity]", 		"0;91"),
    GREEN_BRIGHT				("Green [High Intensity]", 		"0;92"),
    YELLOW_BRIGHT				("Yellow [High Intensity]", 	"0;93"),
    BLUE_BRIGHT					("Blue [High Intensity]", 		"0;94"),
    PURPLE_BRIGHT				("Purple [High Intensity]",		"0;95"),
    CYAN_BRIGHT					("Cyan [High Intensity]", 		"0;96"),
    WHITE_BRIGHT				("White [High Intensity]", 		"0;97"),

    //Bold high intensity
    BLACK_BOLD_BRIGHT			("Black [Bold High Intensity]", "1;90"),
    RED_BOLD_BRIGHT				("Red [Bold High Intensity]", 	"1;91"),
    GREEN_BOLD_BRIGHT			("Green [Bold High Intensity]", "1;92"),
    YELLOW_BOLD_BRIGHT			("Yellow [Bold High Intensity]","1;93"),
    BLUE_BOLD_BRIGHT			("Blue [Bold High Intensity]", 	"1;94"),
    PURPLE_BOLD_BRIGHT			("Purple [Bold High Intensity]","1;95"),
    CYAN_BOLD_BRIGHT			("Cyan [Bold High Intensity]", 	"1;96"),
    WHITE_BOLD_BRIGHT			("White [Bold High Intensity]", "1;97"),

    //High Intensity backgrounds
    BLACK_BACKGROUND_BRIGHT		("Black [High Intensity BG]", 	"0;100"),
    RED_BACKGROUND_BRIGHT		("Red [High Intensity BG]", 	"0;101"),
    GREEN_BACKGROUND_BRIGHT		("Green [High Intensity BG]",	"0;102"),
    YELLOW_BACKGROUND_BRIGHT	("Yellow [High Intensity BG]",	"0;103"),
    BLUE_BACKGROUND_BRIGHT		("Blue [High Intensity BG]", 	"0;104"),
    PURPLE_BACKGROUND_BRIGHT	("Purple [High Intensity BG]", 	"0;105"),
    CYAN_BACKGROUND_BRIGHT		("Cyan [High Intensity BG]", 	"0;106"),
    WHITE_BACKGROUND_BRIGHT		("White [High Intensity BG]", 	"0;107");
	
	//Control Sequence Introducer. ASCII Octal = \033, ASCII Hex = \0x1B, Shell = \e
	private static final String CTRL_SEQ_INTRO = "\033[";  
	private static final String CTRL_SEQ_END = "m"; //Terminates control  
	private final String description;
	private final String colour;
	
	ConsoleColour(String description, String colour) {
		this.description = description;
		this.colour = colour;
	}
	
	public String description() { 
		return this.description; 
	}

	public String colour() { 
		return toString();
	}
	
	@Override
    public String toString() {
        return CTRL_SEQ_INTRO + this.colour + CTRL_SEQ_END; 
    }
}