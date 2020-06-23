package com.mygame.coincollector.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;
//-----------------------------------------------------------------------------------
public class CoinCollector extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture dizzy;
	int manstate=0;
	int pause=0;
	float gravity = 0.2f;
	float velocity=0;
	int manY=0;
	//-----------------------------------------------------------------------------------
	ArrayList<Integer> coinX = new ArrayList<>();
	ArrayList<Integer> coinY = new ArrayList<>();
	Texture coin;
	Random random;
	int coincount;
	//-----------------------------------------------------------------------------------
	ArrayList<Integer> bombX = new ArrayList<>();
	ArrayList<Integer> bombY = new ArrayList<>();
	Texture bomb;
	int bombcount;
	//-----------------------------------------------------------------------------------
	ArrayList<Rectangle> coinRect = new ArrayList<>();

	ArrayList<Rectangle> bombRect = new ArrayList<>();

	com.badlogic.gdx.math.Rectangle manRect;
	//-----------------------------------------------------------------------------------
	int score = 0;
	BitmapFont font;
	//-----------------------------------------------------------------------------------
	int gameState =0;
	//-----------------------------------------------------------------------------------
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		manY= Gdx.graphics.getHeight()/2;
		random = new Random();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		dizzy=new Texture("dizzy-1.png");
	}
	//-----------------------------------------------------------------------------------
	public void makeCoin()
	{
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinY.add((int)height);

		coinX.add(Gdx.graphics.getWidth());
	}
	public void makeBomb()
	{
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombY.add((int)height);

		bombX.add(Gdx.graphics.getWidth());
	}
	//-----------------------------------------------------------------------------------
	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if (gameState ==1)//game is running
		{
			//-----------------------------------------------------------------------------------
			if (bombcount<250)
			{
				bombcount++;
			}
			else
			{
				bombcount=0;
				makeBomb();
			}
			bombRect.clear();
			for (int i=0;i<bombX.size();i++)
			{
				batch.draw(bomb,bombX.get(i),bombY.get(i));
				bombX.set(i,bombX.get(i)-8);
				bombRect.add(new Rectangle(bombX.get(i),bombY.get(i),bomb.getWidth(),bomb.getHeight()));
			}
			//-----------------------------------------------------------------------------------
			if (coincount<150)
			{
				coincount++;
			}
			else
			{
				coincount=0;
				makeCoin();
			}
			coinRect.clear();
			for (int i=0;i<coinX.size();i++)
			{
				batch.draw(coin,coinX.get(i),coinY.get(i));
				coinX.set(i,coinX.get(i)-4);
				coinRect.add(new Rectangle(coinX.get(i),coinY.get(i),coin.getWidth(),coin.getHeight()));
			}

			//-----------------------------------------------------------------------------------
			if (Gdx.input.justTouched())
			{
				velocity-=10;
			}

			if (pause<8)
			{
				pause++;
			}else {
				pause = 0;

				if (manstate < 3) {
					manstate++;
				} else {
					manstate = 0;
				}
			}
			//-----------------------------------------------------------------------------------
			if (manY<=0)
			{
				manY=0;
			}
			int g = Gdx.graphics.getHeight()-man[manstate].getHeight()/2;
			if (manY>=g)
			{
				manY=Gdx.graphics.getHeight()-man[manstate].getHeight()/2;
			}

			//-----------------------------------------------------------------------------------

			velocity += gravity;
			manY -= velocity;
//-----------------------------------------------------------------------------------
		}else if (gameState ==0)//about to start
		{
			if (Gdx.input.justTouched())
			{
				gameState=1;
			}
//-----------------------------------------------------------------------------------
		}else if(gameState ==2)//game over
		{
			if (Gdx.input.justTouched())
			{
				gameState=1;
				manY= Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;

				coinX.clear();
				coinY.clear();
				coinRect.clear();
				coincount=0;

				bombX.clear();
				bombY.clear();
				bombRect.clear();
				bombcount=0;
			}
		}

		//-----------------------------------------------------------------------------------
		if(gameState==2)
		{
			batch.draw(dizzy,Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);
		}else
		{
			batch.draw(man[manstate],Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);
		}
		//-----------------------------------------------------------------------------------



		

		manRect = new Rectangle(Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY,man[manstate].getWidth(),man[manstate].getHeight());

		for (int i=0;i<coinRect.size();i++)
		{
			if (Intersector.overlaps(manRect,coinRect.get(i)))
			{
				score++;
				coinRect.remove(i);
				coinY.remove(i);
				coinX.remove(i);
				break;
			}
		}
		for (int i=0;i<bombRect.size();i++)
		{
			if (Intersector.overlaps(manRect,bombRect.get(i)))
			{
				gameState=2;
			}
		}
		font.draw(batch,"Score: ",100,200);
		font.draw(batch,String.valueOf(score),100+(font.draw(batch,"Score: ",100,200)).width,200);

		batch.end();
	}
	//-----------------------------------------------------------------------------------
	@Override
	public void dispose () {
		batch.dispose();
	}
}
