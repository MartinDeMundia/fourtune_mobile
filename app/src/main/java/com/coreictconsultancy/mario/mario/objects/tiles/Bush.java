package com.coreictconsultancy.mario.mario.objects.tiles;



import android.graphics.Bitmap;

import com.coreictconsultancy.mario.mario.core.MarioResourceManager;
import com.coreictconsultancy.mario.mario.core.animation.Animation;
import com.coreictconsultancy.mario.mario.core.tile.GameTile;

public class Bush extends GameTile {
	
	private static Bitmap[] c = {MarioResourceManager.Bush_1, MarioResourceManager.Bush_2};
	public static Animation swing = new Animation(1200).addFrame(c[0]).addFrame(c[1]);

	public Bush(int pixelX, int pixelY) {
		super(pixelX, pixelY,null,null);
		setIsCollidable(false);
		setAnimation(swing);
	}
	
}
