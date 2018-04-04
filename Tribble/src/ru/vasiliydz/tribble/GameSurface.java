package ru.vasiliydz.tribble;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView {

	SurfaceHolder surfaceHolder;
	Canvas canvas;
	Paint paint = new Paint();
	Bitmap[] bitmapTribble = new Bitmap[6];
	Random random = new Random();
	int screenX, screenY;
	boolean press = false;
	boolean counterclockwise;
	int eated = 0;
	int flash_food = 0;
	
	public Tribble[] tribble = new Tribble[1000];
	int n = -1;
	int difficulty;
	double foodX, foodY, fieldX;
	double fieldY = 1;
	
	public static final double SPEED = 0.00875;
	public static final int HEALTH = 500;
	public static final double FOOD_RADIUS = 0.01;

	public GameSurface(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		surfaceHolder = getHolder();
		paint.setStrokeWidth(15);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(30);
		bitmapTribble[5] = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	}
	
	public void Start(int difficulty, int screenX, int screenY) {
		// TODO Auto-generated method stub
		this.screenX = screenX;
		this.screenY = screenY;
		this.difficulty = difficulty;
		fieldX = (double)screenX/screenY;
		for (int i = 0; i <= 5; i++) {
			bitmapTribble[i] = Bitmap.createScaledBitmap(bitmapTribble[5], (int)Math.round((i*Tribble.SIZE_COEFF+Tribble.MIN_RADIUS)*screenY*2), (int)Math.round((i*Tribble.SIZE_COEFF+Tribble.MIN_RADIUS)*screenY*2), false);
		}
		MakeTribble(random.nextDouble()*fieldX, random.nextDouble(), difficulty);
		if (difficulty == 1) {
			tribble[0].speed = SPEED*1.5;
			tribble[0].health = 2*HEALTH;
			tribble[0].maxHealth = 2*HEALTH;
		}
		MakeFood();
	}
	
	public void MakeTribble(double x, double y, int difficulty) {
		n++;
		switch (difficulty) {
		case 1:
			tribble[n] = new Tribble(x, y, SPEED, HEALTH);
			break;
		case 2:
			tribble[n] = new Tribble(x, y, SPEED, HEALTH);
			break;
		case 3:
			tribble[n] = new Tribble(x, y, SPEED, HEALTH*2/3);
			break;
		default:
			break;
		}
	}
	
	public void MakeFood() {
		double newFoodX,newFoodY;
		newFoodX = random.nextDouble()*(fieldX-2*FOOD_RADIUS)+FOOD_RADIUS;
		newFoodY = random.nextDouble()*(fieldY-2*FOOD_RADIUS)+FOOD_RADIUS;
		for (int i = 0; i <= n; i++) {
			if (distantionTo(i, newFoodX, newFoodY)<=tribble[i].getRadius()+FOOD_RADIUS) {
				newFoodX = random.nextDouble()*(fieldX-2*FOOD_RADIUS)+FOOD_RADIUS;
				newFoodY = random.nextDouble()*(fieldY-2*FOOD_RADIUS)+FOOD_RADIUS;
				i = -1;
			}
		}
		foodX = newFoodX;
		foodY = newFoodY;
		flash_food = 5;
	}
	
	public double distantionTo(int i, double targetX, double targetY) {
		return Math.sqrt((targetX-tribble[i].x)*(targetX-tribble[i].x) + (targetY-tribble[i].y)*(targetY-tribble[i].y));
	}
	
	public void CPUTribbleRotate() {
		double targetAngle;
		for (int i = 1; i <= n; i++) {
			targetAngle = Math.acos((foodX-tribble[i].x)/distantionTo(i, foodX, foodY))*180/Math.PI;
			if (tribble[i].y < foodY) {targetAngle = - targetAngle;}
			if (tribble[i].angle - targetAngle > 0 & tribble[i].angle - targetAngle < 180)
			{tribble[i].Rotate(-10);}
			else
			{tribble[i].Rotate(10);}
		}
	}
	
	public void Eating() {
		for (int i = 0; i <= n; i++) {
			if ((distantionTo(i, foodX, foodY) < tribble[i].getRadius()+FOOD_RADIUS)) {
				if (tribble[i].health > 0) {
					if (i == 0) {eated++;}
					if (tribble[i].size == 5) {
						tribble[i].size = 0;
						tribble[i].x += Tribble.MIN_RADIUS/2;
						MakeTribble(tribble[i].x - Tribble.MIN_RADIUS/2, tribble[i].y, difficulty);
					}
					else {tribble[i].size++;
					}
					tribble[i].health = tribble[i].maxHealth;
					MakeFood();
				}
				else {
					foodX = (foodX - tribble[i].x)*(tribble[i].getRadius() + FOOD_RADIUS)/tribble[i].getRadius() + tribble[i].x;
					foodY = (foodY - tribble[i].y)*(tribble[i].getRadius() + FOOD_RADIUS)/tribble[i].getRadius() + tribble[i].y;
					if (foodX < FOOD_RADIUS) {foodX = FOOD_RADIUS;}
					if (foodY < FOOD_RADIUS) {foodY = FOOD_RADIUS;}
					if (foodX > fieldX - FOOD_RADIUS) {foodX = fieldX - FOOD_RADIUS;}
					if (foodY > fieldY - FOOD_RADIUS) {foodY = fieldY - FOOD_RADIUS;}
				}
			}
		}
	}

	public void GameDynamic() {
		CPUTribbleRotate();
		if (press) {
			if (counterclockwise) {tribble[0].Rotate(10);}
			else {tribble[0].Rotate(-10);}
		}
		for (int i = 0; i <= n; i++) {
			if(tribble[i].health > 0) {
				tribble[i].Move();
				tribble[i].health--;
				
			}
			if (tribble[i].x < tribble[i].getRadius()) {tribble[i].x = tribble[i].getRadius();}
			if (tribble[i].y < tribble[i].getRadius()) {tribble[i].y = tribble[i].getRadius();}
			if (tribble[i].x > fieldX - tribble[i].getRadius()) {tribble[i].x = fieldX - tribble[i].getRadius();}
			if (tribble[i].y > fieldY - tribble[i].getRadius()) {tribble[i].y = fieldY - tribble[i].getRadius();}
			for (int j = 0; j < i; j++) {
				if (distantionTo(i, tribble[j].x, tribble[j].y) < tribble[i].getRadius() + tribble[j].getRadius()) {
					double collision_center_x = (tribble[i].x*tribble[j].getRadius() + tribble[j].x*tribble[i].getRadius())/(tribble[i].getRadius() + tribble[j].getRadius());
					double collision_center_y = (tribble[i].y*tribble[j].getRadius() + tribble[j].y*tribble[i].getRadius())/(tribble[i].getRadius() + tribble[j].getRadius());
					double dist = distantionTo(i, collision_center_x, collision_center_y);
					tribble[i].x = (tribble[i].x - collision_center_x)*tribble[i].getRadius()/dist + collision_center_x;
					tribble[i].y = (tribble[i].y - collision_center_y)*tribble[i].getRadius()/dist + collision_center_y;
					dist = distantionTo(j, collision_center_x, collision_center_y);
					tribble[j].x = (tribble[j].x - collision_center_x)*tribble[j].getRadius()/dist + collision_center_x;
					tribble[j].y = (tribble[j].y - collision_center_y)*tribble[j].getRadius()/dist + collision_center_y;
				}
			}
		}
		Eating();
	}
	
	public void DrawScreen() {
		canvas = getHolder().lockCanvas();
		if (canvas != null) {
			canvas.drawColor(getResources().getColor(R.color.background_green));
			for (int i = 0; i <= n; i++) {
				if (tribble[i].health == 0) {
					canvas.drawBitmap(bitmapTribble[tribble[i].size], Math.round((tribble[i].x-tribble[i].getRadius())*screenY), Math.round((tribble[i].y-tribble[i].getRadius())*screenY), paint);
				}
			}
			for (int i = 0; i <= n; i++) {
				if (tribble[i].health > 0) {
					canvas.drawBitmap(bitmapTribble[tribble[i].size], Math.round((tribble[i].x-tribble[i].getRadius())*screenY), Math.round((tribble[i].y-tribble[i].getRadius())*screenY), paint);
				}
			}
			paint.setColor(Color.GRAY);
			canvas.drawLine(30, 50, screenX-170, 50, paint);
			paint.setColor(Color.RED);
			canvas.drawLine(30, 50, (screenX-200)*tribble[0].health/tribble[0].maxHealth+30, 50, paint);
			paint.setColor(Color.BLUE);
			canvas.drawCircle(Math.round(foodX*screenY), Math.round(foodY*screenY), Math.round(FOOD_RADIUS*screenY), paint);
			if (flash_food > 0) {
				paint.setColor(Color.WHITE);
				paint.setAlpha(flash_food*20);
				for (int i = 7; i > 0; i--) {
					canvas.drawCircle(Math.round(foodX*screenY), Math.round(foodY*screenY), Math.round(FOOD_RADIUS*screenY*i/2), paint);
				}
				flash_food--;
				paint.setAlpha(255);
			}
			paint.setColor(getResources().getColor(R.color.text_green));
			canvas.drawText(String.valueOf(eated), screenX-120, 60, paint);
			if (tribble[0].health == 0) {
				paint.setColor(Color.RED);
				canvas.drawText(getResources().getString(R.string.you_dead), screenX/2, screenY/2, paint);
			}
			getHolder().unlockCanvasAndPost(canvas);
		}
	}

}
