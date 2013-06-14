package jonathan.geoffroy.androidstrategic.model.fighters;

public class Thief extends Human {

	@Override
	protected void initializeStats() {
		hp = hpMax = 20;
		strength = 5;
		magic = 1;
		speed = 11;
		luck = 5;
		defense = 4;
		resistance = 0;
		movement = movementMax = 7;
		constitution = 6;
		weight = 6;
		
		knifeClass = 1;
	}

	@Override
	protected String defaultName() {
		return "Thief";
	}
}