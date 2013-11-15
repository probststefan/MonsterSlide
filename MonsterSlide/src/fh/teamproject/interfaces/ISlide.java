package fh.teamproject.interfaces;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

public interface ISlide extends IEntity {

	public void addSlidePart();

	public ArrayList<ISlidePart> getSlideParts();

	public void removeSlideParty(ISlidePart slidePart);

	public void update(Vector3 playerPosition);

}
