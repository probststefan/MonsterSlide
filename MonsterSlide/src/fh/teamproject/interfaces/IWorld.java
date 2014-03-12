package fh.teamproject.interfaces;

import fh.teamproject.entities.Coins;

public interface IWorld {

	public ISlide getSlide();

	public Coins getCoins();

	public IPlayer getPlayer();

	public void render();

}
