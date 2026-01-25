package com.wellsfargo.counselor.utils;

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
 
    public String getDisplayName() {
        return displayName;
    }
    
    public static boolean isValid(String displayName) {
    	
    	for(SecurityType type: values()) {
    		if(type.getDisplayName().equalsIgnoreCase(displayName)) {
    			return true;
    		};
    	}
    	return false;
    }
}
