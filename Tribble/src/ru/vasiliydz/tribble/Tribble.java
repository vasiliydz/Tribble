package ru.vasiliydz.tribble;

public class Tribble {
	
	double x,y,speed;
	int angle,health,maxHealth,size;
	
	public static final double SIZE_COEFF = 0.01;
	public static final double MIN_RADIUS = 0.02;

	Tribble(double x, double y, double speed, int health) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		angle = 0;
		this.health = health;
		this.maxHealth = health;
		size = 0;
		
	}

		public void Move() {
			x = x + speed*Math.cos(angle*Math.PI/180);
			y = y - speed*Math.sin(angle*Math.PI/180);
		}
		
		public void Rotate(int r) {
			angle = angle + r;
			if (angle>180) {
				angle-=360;
			} 
			if (angle<-180) {
				angle+=360;
			}
		}
		
		public double getRadius() {
			return size*SIZE_COEFF + MIN_RADIUS;
		}

}
