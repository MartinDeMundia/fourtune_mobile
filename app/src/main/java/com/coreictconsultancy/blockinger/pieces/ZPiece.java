/*
 * Copyright 2013 Simon Willeke
 * contact: hamstercount@hotmail.com
 */



package com.coreictconsultancy.blockinger.pieces;

import android.content.Context;

import com.coreictconsultancy.blockinger.Square;

public class ZPiece extends Piece3x3 {

	private com.coreictconsultancy.blockinger.Square zSquare;

	public ZPiece(Context c) {
		super(c);
		zSquare = new com.coreictconsultancy.blockinger.Square(Piece.type_Z,c);
		pattern[1][0] = zSquare;
		pattern[1][1] = zSquare;
		pattern[2][1] = zSquare;
		pattern[2][2] = zSquare;
		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[1][0] = zSquare;
		pattern[1][1] = zSquare;
		pattern[2][1] = zSquare;
		pattern[2][2] = zSquare;
		reDraw();
	}

}
