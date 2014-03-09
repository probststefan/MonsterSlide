package fh.teamproject.interfaces;

import fh.teamproject.game.Score;
import fh.teamproject.game.entities.Coins;
import fh.teamproject.physics.PhysixManager;
import fh.teamproject.screens.GameScreen;

public interface IWorld {

	public ISlide getSlide();

	public Coins getCoins();

	public IPlayer getPlayer();

	public void render();

	public PhysixManager getPhysixManager();

	public GameScreen getGameScreen();

	public Score getScore();

}
