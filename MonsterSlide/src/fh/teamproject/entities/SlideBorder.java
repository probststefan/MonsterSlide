package fh.teamproject.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class SlideBorder extends CollisionEntity implements Poolable {
	static float diameter = 2.5f;
	static Pool<ModelInstance> spherePool = new Pool<ModelInstance>() {
		Model sphereModel = new ModelBuilder().createSphere(SlideBorder.diameter,
				SlideBorder.diameter, SlideBorder.diameter, 4, 4,
				new Material(
						ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position);

		@Override
		protected ModelInstance newObject() {
			return new ModelInstance(sphereModel);
		}
	};

	public SlideBorder(Vector3 position) {
		instance = SlideBorder.spherePool.obtain();
		instance.transform.setToTranslation(position);
		setCollisionShape(new btSphereShape(SlideBorder.diameter / 2f));
		setEntityWorldTransform(instance.transform);
		setLocalInertia(new Vector3(0, 0, 0));

	}

	@Override
	public void dispose() {
		super.dispose();
		SlideBorder.spherePool.free(instance);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initPhysix() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initGraphix() {
		// TODO Auto-generated method stub

	}

}
