
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


/**
 *
 * @author leonardo
 */
public class Box extends Shape {
    
    public final Vec2[] vertices = { new Vec2(), new Vec2(), new Vec2(), new Vec2() };
    
    public final Edge[] edges = { 
        new Edge(vertices[0], vertices[1]), 
        new Edge(vertices[1], vertices[2]), 
        new Edge(vertices[2], vertices[3]), 
        new Edge(vertices[3], vertices[0]) };
    
    public static class Edge {
        
        final Vec2 a;
        final Vec2 b;
        final Vec2 normal = new Vec2();
        
        public Edge(Vec2 a, Vec2 b) {
            this.a = a;
            this.b = b;
        }
        
        void recalculateNormal() {
            normal.set(b);
            normal.sub(a);
            normal.set(-normal.y, normal.x);
            normal.normalize();
        }
        
        void draw(Graphics2D g) {
            g.setColor(Color.DARK_GRAY);
            g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
            // draw normal
            g.setColor(Color.GRAY);
            double nx1 = a.x + (b.x - a.x) * 0.5;
            double ny1 = a.y + (b.y - a.y) * 0.5;
            double nx2 = nx1 + normal.x * 10;
            double ny2 = ny1 + normal.y * 10;
            g.drawLine((int) nx1, (int) ny1, (int) nx2, (int) ny2);
        }
    }

    public Box() {
        this(0);
    }

    public Box(double size) {
        vertices[0].set(-size, size);
        vertices[1].set(size, size);
        vertices[2].set(size, -size);
        vertices[3].set(-size, -size);
        recalculateEdgeNormals();
    }
    
    private void recalculateEdgeNormals() {
        for (Edge edge : edges) {
            edge.recalculateNormal();
        }
    }
    
    public Vec2[] getVertices() {
        return vertices;
    }

    public Edge[] getEdges() {
        return edges;
    }
    
    private static final Point2D P_SRC = new Point2D.Double();
    private static final Point2D P_DST = new Point2D.Double();
    
    public void convertToWorldSpace(Box box, AffineTransform transform) {
        for (int v = 0; v < box.vertices.length; v++) {
            Vec2 vertex = box.vertices[v];
            P_SRC.setLocation(vertex.x, vertex.y);
            transform.transform(P_SRC, P_DST);
            vertices[v].set(P_DST.getX(), P_DST.getY());
        }
        recalculateEdgeNormals();
    }
    
    @Override
    public void drawDebug(Graphics2D g) {
        for (Edge edge : edges) {
            edge.draw(g);
        }
    }
    
}
