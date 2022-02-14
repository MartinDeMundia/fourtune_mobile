/*
 * Copyright 2013 Simon Willeke
 * contact: hamstercount@hotmail.com
 */



package com.coreictconsultancy.blockinger.pieces;

import android.content.Context;

import com.coreictconsultancy.blockinger.Square;

public class IPiece extends Piece4x4 {
	
	private Square iSquare;

	public IPiece(Context c) {
		super(c);
		iSquare = new Square(Piece.type_I,c);
		pattern[2][0] = iSquare;
		pattern[2][1] = iSquare;
		pattern[2][2] = iSquare;
		pattern[2][3] = iSquare;
		reDraw();
	}
	
	@Override
	public void reset(Context c) {
		super.reset(c);
		pattern[2][0] = iSquare;
		pattern[2][1] = iSquare;
		pattern[2][2] = iSquare;
		pattern[2][3] = iSquare;
		reDraw();
	}

}
