package com.wellsfargo.counselor.utils;

/**
 * Represents supported security types used within the application.
 * Each security type includes a user friendly dusplay name.
*/
public enum SecurityType {

	STOCK("Stock / Equity"),
    BOND("Bond / Fixed Income"),
    MUTUAL_FUND("Mutual Fund"),
    ETF("ETF / Index Fund"),
    DERIVATIVE("Derivative"),
    COMMODITY("Commodity"),
    CRYPTOCURRENCY("Cryptocurrency / Digital Asset"),
    PRIVATE_EQUITY("Private Equity / Venture Capital"),
    REIT("REIT / Real Estate Investment"),
    CASH("Cash / Cash Equivalent"),
    HEDGE_FUND("Hedge Fund / Alternative Investment");

    private final String displayName;

    SecurityType(String displayName) {
        this.displayName = displayName;
    }

    /**
	 * Returns the user friendly display name for this security type.
	 * 
	 * @return display name of the security type
	*/

    public String getDisplayName() {
        return displayName;
    }
    
	/**
	 * Check whether the provided input matches the valid security type.
	 * The input may match either the enum name or its display name (case sensitive).
	 * 
	 * @param input security type value to validate
	 * @return true if the input matches a valid security type,false otherwise
	*/
    public static boolean isValid(String input) {
        for (SecurityType type : values()) {
            // check enum name or display name
            if (type.name().equalsIgnoreCase(input) || type.getDisplayName().equalsIgnoreCase(input)) {
                return true;
            }
       }
        return false;
    }
}