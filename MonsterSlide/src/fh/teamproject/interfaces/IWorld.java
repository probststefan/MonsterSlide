package fh.teamproject.interfaces;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public interface IWorld {

	public ISlide getSlide();

	public IPlayer getPlayer();

	public void render();

}
