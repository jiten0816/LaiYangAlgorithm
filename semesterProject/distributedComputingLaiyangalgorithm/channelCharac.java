package distributedComputingLaiyangalgorithm;

import java.util.HashMap;
import java.util.Map;


public class channelCharac {

	int amount;
	Color color;
	
	public channelCharac(int amount, Color color) {
		super();
		this.amount = amount;
		this.color = color;
	}

	public int getAmount() {
		return amount;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return "| amount=" + amount + " | color=" + color + "|";
	}
	
	

}
