package com.coreictconsultancy.mario.mario.screens;

import com.coreictconsultancy.mario.framework.Game;
import com.coreictconsultancy.mario.framework.Graphics;
import com.coreictconsultancy.mario.framework.Screen;

public class SplashLoadingScreen extends Screen {
    
	
	
	public SplashLoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
       Graphics g = game.getGraphics();
       game.setScreen(new LoadingScreen(game));
    }

    @Override
    public void paint(float deltaTime) {
    	 
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    	
    }

    @Override
    public void onBackPressed() {

    }
}