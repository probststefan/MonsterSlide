package fh.teamproject;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "MonsterSlide";
		configuration.useGL20 = true;
		configuration.width = 1280;
		configuration.height = 720;

		new LwjglApplication(new MonsterSlide(), configuration);
	}
}
