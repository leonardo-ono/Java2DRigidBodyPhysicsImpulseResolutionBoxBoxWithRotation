
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author leonardo
 */
public class Body {
    
    public final Shape shape;
    public final AffineTransform transform = new AffineTransform();
    
    public final Vec2 position = new Vec2();
    public double angle = 0;
    
    public Body(double boxSize) {
        this.shape = new Box(boxSize);
    }

    public Shape getShape() {
        return shape;
    }

    public Vec2 getPosition() {
        return position;
    }

    public double getAngle() {
        return angle;
    }
    
    public AffineTransform getTransform() {
        transform.setToIdentity();
        transform.translate(position.x, position.y);
        transform.rotate(angle);
        return transform;
    }
    
    public void drawDebug(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.rotate(angle);
        shape.drawDebug(g);
        g.setTransform(at);
    }
    
}
