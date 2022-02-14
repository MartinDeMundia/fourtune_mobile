package com.coreictconsultancy.mario.mario.objects.creatures;


import android.graphics.Bitmap;
import android.graphics.Point;

import com.coreictconsultancy.mario.mario.core.MarioResourceManager;
import com.coreictconsultancy.mario.mario.core.animation.Animation;
import com.coreictconsultancy.mario.mario.core.tile.TileMap;
import com.coreictconsultancy.mario.mario.objects.base.Creature;


public class Virus extends Creature {
	
	private Animation waddle, flip;
	private int initY=0, span;
	private static Bitmap[]v;
	private static boolean initialized=false;

	public Virus(int x, int y) {
		
		super(x, y);
		if (!initialized){
			v = MarioResourceManager.Virus;
			initialized=true;
		}
		initY=y;
		waddle = new Animation(200).addFrame(v[0]).addFrame(v[1]);
		flip = new Animation().addFrame(v[2]).addFrame(v[2]);
		setAnimation(waddle);
		dx=0;
	}
	
	@Override
	public void xCollide(Point p) {
		
	}
	
	@Override
	public void creatureXCollide() {
		
	}
	
	public void wakeUp(boolean isLeft) {
		super.wakeUp();
		dy =0.05f;//(float) (-0.5f+ Math.random()*0.9);
		span=20+(int) (Math.random()*20);
	}
	
	@Override
	public void updateCreature(TileMap map, int time) {
		if (isFlipped()){
			super.updateCreature(map, time);
		}else{
			y=y+dy*time;
			if (y>initY+span || y<initY-span)dy=-dy;
			super.update(time);
		}
	}
	
	public void jumpedOn() {
		//setAnimation(dead);
		//setIsCollidable(false);
		//dx = 0;
	}
	
	public void flip() {
		setAnimation(flip);
		setIsFlipped(true);
		setIsCollidable(false);
		dy = -.1f;
		dx = 0;
	}
	
	

}
