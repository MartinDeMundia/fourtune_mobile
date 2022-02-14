/*
 * Copyright 2013 Simon Willeke
 * contact: hamstercount@hotmail.com
 */



package com.coreictconsultancy.blockinger.components;

import com.coreictconsultancy.blockinger.activities.GameActivity;

public abstract class Component {

	protected GameActivity host;
	
	public Component(GameActivity ga) {
		host = ga;
	}

	public void reconnect(GameActivity ga) {
		host = ga;
	}

	public void disconnect() {
		host = null;
	}
	
}
