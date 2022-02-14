/*
 * Copyright 2013 Simon Willeke
 * contact: hamstercount@hotmail.com
 */



package com.coreictconsultancy.blockinger.pieces;

import android.content.Context;

import com.coreictconsultancy.blockinger.Square;

public class LPiece extends Piece3x3 {

	private Square lSquare;
	
	public LPiece(Context c) {
		super(c);
		lSquare = new Square(Piece.type_L,c);
		pattern[1][0] = lSquare;
		pattern[1][1] = lSquare;
		pattern[1][2] = lSquare;
		pattern[2][0] = lSquare;
		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[1][0] = lSquare;
		pattern[1][1] = lSquare;
		pattern[1][2] = lSquare;
		pattern[2][0] = lSquare;
		reDraw();
	}

}
