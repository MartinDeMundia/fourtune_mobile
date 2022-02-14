package com.coreictconsultancy.mario.mario.objects.creatures;

import android.graphics.Bitmap;

import com.coreictconsultancy.mario.mario.core.MarioResourceManager;
import com.coreictconsultancy.mario.mario.core.animation.Animation;
import com.coreictconsultancy.mario.mario.core.tile.TileMap;
import com.coreictconsultancy.mario.mario.objects.base.Creature;


public class LevelComplete extends Creature {
	
	private Animation level;

	
	public LevelComplete(int pixelX, int pixelY) {
		super(pixelX, pixelY);
		setIsItem(true);
		setIsAlwaysRelevant(true);
		Bitmap shroom = MarioResourceManager.levelComplete;
		level = new Animation();
		level.addFrame(shroom, 1000);
		level.addFrame(shroom, 1000);
		setAnimation(level);
	}
	
	@Override
	public void updateCreature(TileMap map, int time) {
	
	}
}

