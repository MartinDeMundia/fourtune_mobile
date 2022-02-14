/*
 * Copyright 2013 Simon Willeke
 * contact: hamstercount@hotmail.com
 */



package com.coreictconsultancy.blockinger.pieces;

import android.content.Context;

import com.coreictconsultancy.blockinger.Square;

public class TPiece extends Piece3x3 {

	private com.coreictconsultancy.blockinger.Square tSquare;

	public TPiece(Context c) {
		super(c);
		tSquare = new com.coreictconsultancy.blockinger.Square(Piece.type_T,c);
		pattern[1][0] = tSquare;
		pattern[1][1] = tSquare;
		pattern[1][2] = tSquare;
		pattern[2][1] = tSquare;
		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[1][0] = tSquare;
		pattern[1][1] = tSquare;
		pattern[1][2] = tSquare;
		pattern[2][1] = tSquare;
		reDraw();
	}

}
