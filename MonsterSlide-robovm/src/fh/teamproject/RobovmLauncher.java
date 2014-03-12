package fh.teamproject;

import org.robovm.cocoatouch.coregraphics.CGRect;
import org.robovm.cocoatouch.foundation.NSAutoreleasePool;
import org.robovm.cocoatouch.foundation.NSDictionary;
import org.robovm.cocoatouch.uikit.UIApplication;
import org.robovm.cocoatouch.uikit.UIButton;
import org.robovm.cocoatouch.uikit.UIButtonType;
import org.robovm.cocoatouch.uikit.UIColor;
import org.robovm.cocoatouch.uikit.UIControl;
import org.robovm.cocoatouch.uikit.UIControlState;
import org.robovm.cocoatouch.uikit.UIEvent;
import org.robovm.cocoatouch.uikit.UIScreen;
import org.robovm.cocoatouch.uikit.UIWindow;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;

public class RobovmLauncher extends IOSApplication.Delegate {
	// @Override
	// protected IOSApplication createApplication() {
	// IOSApplicationConfiguration config = new IOSApplicationConfiguration();
	// config.orientationLandscape = true;
	// config.orientationPortrait = false;
	// return new IOSApplication(new MonsterSlide(), config);
	// }
	//
	// public static void main(String[] argv) {
	// NSAutoreleasePool pool = new NSAutoreleasePool();
	// UIApplication.main(argv, null, RobovmLauncher.class);
	// pool.drain();
	// }

	private UIWindow window = null;
	private int clickCount = 0;

	@Override
	public boolean didFinishLaunching(UIApplication application,
			NSDictionary launchOptions) {

		final UIButton button = UIButton.fromType(UIButtonType.RoundedRect);
		button.setFrame(new CGRect(115.0f, 121.0f, 91.0f, 37.0f));
		button.setTitle("Click me!", UIControlState.Normal);

		button.addOnTouchUpInsideListener(new UIControl.OnTouchUpInsideListener() {
			@Override
			public void onTouchUpInside(UIControl control, UIEvent event) {
				button.setTitle("Click #" + (++clickCount), UIControlState.Normal);
			}
		});

		window = new UIWindow(UIScreen.getMainScreen().getBounds());
		window.setBackgroundColor(UIColor.lightGrayColor());
		window.addSubview(button);
		window.makeKeyAndVisible();

		return true;
	}

	public static void main(String[] args) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(args, null, RobovmLauncher.class);
		pool.drain();
	}

	@Override
	protected IOSApplication createApplication() {
		// TODO Auto-generated method stub
		return null;
	}
}
