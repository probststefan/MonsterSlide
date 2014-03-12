package fh.teamproject.utils;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import fh.teamproject.screens.GameScreen;

public class SlideBuilder {
	private ArrayList<Vector3> binormals = new ArrayList<Vector3>();
	private ArrayList<Vector3> normals = new ArrayList<Vector3>();
	private ArrayList<Vector3> tangents = new ArrayList<Vector3>();
	private Array<VertexInfo> vertInfo = new Array<VertexInfo>();
	private Array<Vector3> vertices = new Array<Vector3>();
	private MeshBuilder builder = new MeshBuilder();
	private Mesh mesh;
	Node node;
	/* Das Splitting mit dem die Spline diskretisiert wird */
	private float splitting;
	/*
	 * Die Punkte zum Rendering in alternierender Reihenfolge (links, rechts,
	 * links, rechts)
	 */
	private Array<Vector3> graphicsVertices = new Array<Vector3>();

	public SlideBuilder() {
		// TODO Auto-generated constructor stub
	}

	public Node createSlidePart(CatmullRomSpline<Vector3> spline, int spanCount) {
		begin();
		/**
		 * 1. Punkte auf letzem span interpolieren 2. Tangent, Normale und
		 * Binormale berechnen 3. Punkte duplizieren um rechtecke zu bilden 4.
		 * mesh bauen, material bauen
		 */
		for (int i = 0; i < spanCount; i++) {
			createInterpolatedVertices(spline, spline.spanCount - 1);
		}
		createVertexInfos();
		createNode();
		return node;
	}

	private void begin() {
		normals.clear();
		binormals.clear();
		tangents.clear();
		vertices.clear();
		vertInfo.clear();
		graphicsVertices.clear();
		vertices.clear();
		mesh = null;
		node = null;
	}

	private void createInterpolatedVertices(CatmullRomSpline<Vector3> spline, int span) {
		/* Anzahl diskreter Intervalle */
		splitting = 1f / 12f;

		Vector3 interpolatedVertex = new Vector3();
		float epsilon = 0.01f;
		for (float i = 0; i <= (1 + epsilon); i += splitting) {
			/* Damit werden die Endstücke kleiner */
			float t = Interpolation.sine.apply(i);
			spline.valueAt(interpolatedVertex, span, t);
			// 1. und 2. Ableitung bilden.
			Vector3 derivation = new Vector3();
			derivation = spline.derivativeAt(derivation, span, t);

			Vector3 tangent = derivation.cpy().nor();
			/*
			 * Mit dem upVector wird das Problem der springenden Normalen
			 * behoben.
			 * 
			 * @link
			 * http://www.it.hiof.no/~borres/j3d/explain/frames/p-frames.html
			 */
			Vector3 upVector = new Vector3(0.0f, -1.0f, 0.0f);
			Vector3 binormal = derivation.cpy().crs(upVector).nor();
			Vector3 normal = tangent.cpy().crs(binormal);
			// warum wird derivation nicht normiert aber tangent schon
			//
			normals.add(normal);
			binormals.add(binormal);
			tangents.add(tangent);
			vertices.add(new Vector3(interpolatedVertex));
		}
	}

	private void createNode() {
		// NOTE: Attribute vom MeshBuilder erzeugen lassen (sonst komische
		// Probleme)
		builder.begin(MeshBuilder.createAttributes(Usage.Position | Usage.Normal
				| Usage.TextureCoordinates));
		for (int i = 0; i <= (graphicsVertices.size - 4); i += 4) {
			builder.triangle(vertInfo.get(i + 2), vertInfo.get(i + 3),
					vertInfo.get(i + 1));
			builder.triangle(vertInfo.get(i + 1), vertInfo.get(i), vertInfo.get(i + 2));
		}
		mesh = builder.end();

		Material material = new Material();
		TextureAttribute texAttr = TextureAttribute.createDiffuse(new Texture(Gdx.files
				.internal("data/slide/stone.png")));
		material.set(texAttr);

		MeshPart meshPart = new MeshPart("meshPart1", mesh, 0, mesh.getNumVertices(),
				GL20.GL_TRIANGLES);
		NodePart nodePart = new NodePart(meshPart, material);
		node = new Node();
		node.parts.add(nodePart);

	}

	private void createVertexInfos() {

		for (int i = 0; i < vertices.size; ++i) {
			Vector3 v = vertices.get(i);
			Vector3 binormal = binormals.get(i);
			binormal.scl(GameScreen.settings.SLIDE_WIDTH);

			graphicsVertices.add(new Vector3(v.x, v.y, v.z));
			graphicsVertices.add(new Vector3(v.x + binormal.x, v.y + binormal.y, v.z
					+ binormal.z));
			if ((i == 0) || (i == (vertices.size - 1))) {
				continue;
			}
			graphicsVertices.add(new Vector3(v.x, v.y, v.z));
			graphicsVertices.add(new Vector3(v.x + binormal.x, v.y + binormal.y, v.z
					+ binormal.z));
		}

		for (int i = 0; i <= (graphicsVertices.size - 4); i += 4) {

			Vector3 normal = normals.get(i / 4);

			float uMin = 0f, uMax = 1f, vMin = 0f, vMax = 1f;

			VertexInfo info = new VertexInfo();
			info.setPos(graphicsVertices.get(i)).setNor(normal)
					.setUV(new Vector2(uMin, vMin));
			vertInfo.add(info);

			info = new VertexInfo();
			info.setPos(graphicsVertices.get(i + 1)).setNor(normal)
					.setUV(new Vector2(uMax, vMin));
			vertInfo.add(info);

			info = new VertexInfo();
			info.setPos(graphicsVertices.get(i + 2)).setNor(normal)
					.setUV(new Vector2(uMin, vMax));
			vertInfo.add(info);

			info = new VertexInfo();
			info.setPos(graphicsVertices.get(i + 3)).setNor(normal)
					.setUV(new Vector2(uMax, vMax));
			vertInfo.add(info);

		}

	}
}
