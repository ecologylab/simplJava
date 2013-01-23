package ecologylab.translators.javascript.test;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

	public class Player extends ElementState {
        @simpl_scalar String name;
        @simpl_scalar int strength;
        @simpl_scalar int speed;
        @simpl_scalar int skin;
        public Player(String name, int strength, int speed, int skin) {
                super();
                this.name = name;
                this.strength = strength;
                this.speed = speed;
                this.skin = skin;
        }
}//

