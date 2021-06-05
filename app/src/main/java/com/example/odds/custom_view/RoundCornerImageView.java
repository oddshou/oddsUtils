/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class RoundCornerImageView extends AppCompatImageView
{

    private int mWidth;
    private int mHeight;
    private Context mContext;

    private final Paint paint = new Paint( );

    public RoundCornerImageView(Context context )
    {
	super( context );
	mContext = context;
    }

    public RoundCornerImageView(Context context , AttributeSet attrs )
    {
	super( context , attrs );
	mContext = context;
    }

    public RoundCornerImageView(Context context , AttributeSet attrs , int defStyle )
    {
	super( context , attrs , defStyle );
	mContext = context;
    }

    //    @Override
    //    public void setImageBitmap( Bitmap bm )
    //    {
    //
    //	//	if( getWidth( ) != 0 )
    //	//	{
    //	//	    mWidth = getWidth( );
    //	//	    mHeight = getHeight( );
    //	//	}
    //	mWidth = mHeight = mContext.getResources( ).getDimensionPixelSize( R.dimen.hsgame_icon_size );
    //
    //	//	Bitmap backgroudBitmap = BitmapUtils.createColorBitmap( mWidth - 4 , mHeight - 4 , 0xcc000000 );
    //	//	Bitmap paddingBitmap = BitmapUtils.createColorBitmap( mWidth , mHeight , 0xccffffff );
    //	//	bm = BitmapUtils.big( bm , mWidth - 4 , mHeight - 4 );
    //	//	bm = BitmapUtils.combineBitmap( backgroudBitmap , bm );
    //	//	bm = BitmapUtils.getRoundedBitmap( mContext , bm );
    //	//
    //	//	bm = BitmapUtils.combineBitmap( paddingBitmap , bm );
    //	//	bm = BitmapUtils.getRoundedBitmap( mContext , bm );
    //
    //	Bitmap backgroudBitmap = BitmapUtils.createColorBitmap( mWidth - 4 , mHeight - 4 , 0xcc000000 );
    //	bm = Bitmap.createBitmap( bm , 3 , 3 , bm.getWidth( ) - 6 , bm.getHeight( ) - 6 );
    //	bm = BitmapUtils.big( bm , mWidth , mHeight );
    //	bm = BitmapUtils.combineBitmap( backgroudBitmap , bm );
    //	bm = BitmapUtils.getRoundedBitmap( mContext , bm );
    //
    //	super.setImageBitmap( bm );
    //    }

    @Override
    protected void onDraw( Canvas canvas )
    {
	int width;
	int height;
	width = getWidth( );
	height = getHeight( );
	Drawable drawable = getDrawable( );

	if(width > 0 && height > 0 && (drawable instanceof BitmapDrawable))
	{
	    Bitmap bitmap = ( (BitmapDrawable)drawable ).getBitmap( );
	    bitmap = big( bitmap , width , height );
//	    bitmap = BitmapUtils.getRoundedBitmap( mContext , BitmapUtils.bounaryBitmapAplah( bitmap ) ? BitmapUtils.getBitmap( bitmap , BitmapUtils.averageRGB( bitmap ) ) : bitmap );
	    bitmap = getRoundedBitmap( mContext , bitmap );
	    final Rect rect = new Rect( 0 , 0 , bitmap.getWidth( ) , bitmap.getHeight( ) );
	    paint.reset( );
	    canvas.drawBitmap( bitmap , rect , rect , paint );
	}
	else
	{
	    super.onDraw( canvas );
	}
    }

	public static Bitmap getRoundedBitmap(Context context , Bitmap bm )
	{

		Bitmap bg = Bitmap.createBitmap( bm.getWidth( ) , bm.getHeight( ) , Bitmap.Config.ARGB_8888 );

		Canvas canvas = new Canvas( bg );
		Paint paint = new Paint( );
		//	Log.i( "test" , "+++++++++++,Height" + bg.getHeight( ) + ",Width" + bg.getWidth( ) );
		Rect rect = new Rect( 0 , 0 , bg.getWidth( ) , bg.getHeight( ) );
		RectF rectF = new RectF( rect );
		float roundR = 12 * 3;
		paint.setAntiAlias( true );

		canvas.drawRoundRect( rectF , roundR , roundR , paint );

		paint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.SRC_IN ) );

		canvas.drawBitmap( bm , rect , rect , paint );

		canvas.setBitmap( null );

//	if (null != bm && !bm.isRecycled()) {
//		bm.recycle();
//		bm = null;
//	}

		return bg;
	}

	public static Bitmap big(Bitmap b , float x , float y )
	{
		int w = b.getWidth( );
		int h = b.getHeight( );
		float sx = x / w;
		float sy = y / h;
		Matrix matrix = new Matrix( );
		matrix.postScale( sx , sy ); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap( b , 0 , 0 , w , h , matrix , true );
//	if (null != b && b.isRecycled()) {
//		b.recycle();
//		b = null;
//	}
		return resizeBmp;
	}

}
