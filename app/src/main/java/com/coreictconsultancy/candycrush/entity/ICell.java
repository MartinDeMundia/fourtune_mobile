package com.coreictconsultancy.candycrush.entity;

import com.coreictconsultancy.candycrush.constants.IConstants;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


/**
 * Initialize the Cell X/Y position and the Width, Height
 */
public abstract class ICell extends Sprite implements IConstants {

	public ICell(final int pCellX, final int pCellY, final int pWidth, final int pHeight,
                 final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
	    super(pCellX, pCellY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);	    
	  }
}