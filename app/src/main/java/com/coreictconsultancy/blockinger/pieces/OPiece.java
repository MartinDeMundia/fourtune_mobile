/*
 * Copyright 2013 Simon Willeke
 * contact: hamstercount@hotmail.com
 */



package com.coreictconsultancy.blockinger.pieces;

import android.content.Context;

import com.coreictconsultancy.blockinger.Square;

public class OPiece extends Piece4x4 {

	private com.coreictconsultancy.blockinger.Square oSquare;
	
	public OPiece(Context c) {
		super(c);
		oSquare = new com.coreictconsultancy.blockinger.Square(Piece.type_O,c);
		pattern[1][1] = oSquare;
		pattern[1][2] = oSquare;
		pattern[2][1] = oSquare;
		pattern[2][2] = oSquare;
		reDraw();
	}

	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[1][1] = oSquare;
		pattern[1][2] = oSquare;
		pattern[2][1] = oSquare;
		pattern[2][2] = oSquare;
		reDraw();
	}

}
