/*
 * Copyright 2013 Simon Willeke
 * contact: hamstercount@hotmail.com
 */



package com.coreictconsultancy.blockinger.pieces;

import android.content.Context;

import com.coreictconsultancy.blockinger.Square;

public class SPiece extends Piece3x3 {

	private com.coreictconsultancy.blockinger.Square sSquare;
	
	public SPiece(Context c) {
		super(c);
		sSquare = new com.coreictconsultancy.blockinger.Square(Piece.type_S,c);
		pattern[1][1] = sSquare;
		pattern[1][2] = sSquare;
		pattern[2][0] = sSquare;
		pattern[2][1] = sSquare;
		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[1][1] = sSquare;
		pattern[1][2] = sSquare;
		pattern[2][0] = sSquare;
		pattern[2][1] = sSquare;
		reDraw();
	}

}
