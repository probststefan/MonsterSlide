package fh.teamproject.utils;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
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
	private Mesh slideMesh, borderMesh;
	private float uMin = 0f, uMax = 1f, vMin = 0f, vMax = 1f;

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

	public Node createSlidePart(CRSpline spline, int spanCount) {
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
		node = new Node();
		createSlideMesh();
		createBorderMesh(spline, false);
		createBorderMesh(spline, true);

		return node;
	}

	private void begin() {
		normals.clear();
		binormals.clear();
		tangents.clear();
		vertices.clear();
		vertInfo.clear();
		graphicsVertices.clear();
		borderVertices.clear();
		slideMesh = null;
		node = null;
	}

	private void createInterpolatedVertices(CatmullRomSpline<Vector3> spline, int span) {
		/* Anzahl diskreter Intervalle */
		splitting = 1f / 12f;

		Vector3 interpolatedVertex = new Vector3();
		float epsilon = 0.0f;
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

	private void createSlideMesh() {
		// NOTE: Attribute vom MeshBuilder erzeugen lassen (sonst komische
		// Probleme)
		builder.begin(MeshBuilder.createAttributes(Usage.Position | Usage.Normal
				| Usage.TextureCoordinates));
		for (int i = 0; i <= (graphicsVertices.size - 4); i += 4) {
			builder.triangle(vertInfo.get(i + 2), vertInfo.get(i + 3),
					vertInfo.get(i + 1));
			builder.triangle(vertInfo.get(i + 1), vertInfo.get(i), vertInfo.get(i + 2));
		}
		slideMesh = builder.end();

		Material material = new Material();

		Texture texture = new Texture(Gdx.files.internal("data/slide/stone.png"), true);
		texture.setFilter(TextureFilter.MipMapNearestLinear, TextureFilter.Linear);
		TextureAttribute texAttr = TextureAttribute.createDiffuse(texture);

		material.set(texAttr);

		MeshPart meshPart = new MeshPart("meshPart1", slideMesh, 0,
				slideMesh.getNumVertices(), GL20.GL_TRIANGLES);
		NodePart nodePart = new NodePart(meshPart, material);
		node.parts.add(nodePart);
	}

	private Array<VertexInfo> borderVertices = new Array<MeshPartBuilder.VertexInfo>();

	private void createBorderMesh(CRSpline spline, boolean isLeft) {
		float borderHeight = 5f;
		/* create Vertex Infos */
		VertexInfo vertex;
		Vector3 binormal;
		Vector3 normal;
		Vector3 originVector = new Vector3();
		for (int i = 0; i < vertices.size - 1; ++i) {
			binormal = binormals.get(i).nor();
			normal = normals.get(i);

			/* Get first point */
			originVector.set(vertices.get(i));
			if (isLeft) {
				originVector.add(binormal.cpy().scl(GameScreen.settings.SLIDE_WIDTH));
			}
			vertex = new VertexInfo();
			vertex.setPos(originVector).setNor(binormal).setUV(new Vector2(uMin, vMin));
			borderVertices.add(vertex);
			// der obere punkt wird entlang der normalen verschoben
			vertex = new VertexInfo();
			vertex.setPos(originVector.cpy().add(normal.cpy().scl(borderHeight)))
					.setNor(binormal).setUV(new Vector2(uMax, vMin));
			borderVertices.add(vertex);

			/* Get the next point */
			binormal = binormals.get(i + 1).nor();
			originVector.set(vertices.get(i + 1));
			if (isLeft) {
				originVector.add(binormal.cpy().scl(GameScreen.settings.SLIDE_WIDTH));
			}
			vertex = new VertexInfo();
			vertex.setPos(originVector).setNor(binormal).setUV(new Vector2(uMin, vMax));
			borderVertices.add(vertex);

			vertex = new VertexInfo();
			vertex.setPos(originVector.cpy().add(normal.cpy().scl(borderHeight)))
					.setNor(binormal).setUV(new Vector2(uMax, vMax));
			borderVertices.add(vertex);
		}

		borderVertices.shrink();
		/* create Mesh */
		builder.begin(MeshBuilder.createAttributes(Usage.Position | Usage.Normal
				| Usage.TextureCoordinates));
		for (int i = 0; i <= (borderVertices.size - 4); i += 4) {
			// FIXME: könnte non indexed, non triangulated mesh erzeugen -> dann
			// bullet fehler, verbessert durch erzwingen des ersten borderparts
			/* Erzeuge Randstücke zufällig */
			if (MathUtils.randomBoolean(0.3f) || i == 0 || i == borderVertices.size - 4) {

				VertexInfo p1 = borderVertices.get(i + 1);
				VertexInfo p2 = borderVertices.get(i + 2);
				VertexInfo p3 = borderVertices.get(i);
				VertexInfo p4 = borderVertices.get(i + 3);
				binormal = binormals.get(i / 4).nor()
						.scl(GameScreen.settings.SLIDE_WIDTH);

				if (!isLeft) {
					builder.triangle(p1, p2, p3);
					builder.triangle(p1, p4, p2);
				} else {
					p1.normal.scl(-1f);
					p2.normal.scl(-1f);
					p3.normal.scl(-1f);
					p4.normal.scl(-1f);

					builder.triangle(p3, p2, p1);
					builder.triangle(p2, p4, p1);
				}
			} else {
				// Gdx.app.log("SlideBuilder", "Omitting border part " + i);
			}
		}

		Material material = new Material();
		Texture texture = new Texture(Gdx.files.internal("data/slide/snow.jpg"), true);
		texture.setFilter(TextureFilter.MipMapNearestLinear, TextureFilter.Nearest);
		TextureAttribute texAttr = TextureAttribute.createDiffuse(texture);
		material.set(texAttr);
		borderMesh = builder.end();
		MeshPart meshPart = new MeshPart("borderMeshPart", borderMesh, 0,
				borderMesh.getNumVertices(), GL20.GL_TRIANGLES);
		NodePart nodePart = new NodePart(meshPart, material);
		node.parts.add(nodePart);
		borderVertices.clear();
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
