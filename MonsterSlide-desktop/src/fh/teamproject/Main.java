package fh.teamproject;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
	    String density = "";
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MonsterSlide";
		cfg.useGL20 = true;
		//density = "xlarge";
		//density = "large";
		//density = "medium";
		//density = "small";
		if (density.equals("xlarge")) {
	        cfg.width = 960;
	        cfg.height = 720;
		} else if (density.equals("large")) {
	        cfg.width = 640;
	        cfg.height = 480;
		} else if (density.equals("medium")) {
            cfg.width = 470;
            cfg.height = 320;
        } else if (density.equals("small")) {
            cfg.width = 426;
            cfg.height = 320;
        } else {
            cfg.width = 1280;
            cfg.height = 720;
        }
		new LwjglApplication(new MonsterSlide(), cfg);
	}
}
