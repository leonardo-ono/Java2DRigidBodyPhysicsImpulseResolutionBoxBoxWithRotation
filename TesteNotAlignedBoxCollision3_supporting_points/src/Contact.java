
import java.awt.Color;
import java.awt.Graphics2D;


/**
 *
 * @author leonardo
 */
public class Contact {

    public Body ra;
    public Body rb;
    
    public double penetration;
    public final Vec2 point = new Vec2();
    public final Vec2 normal = new Vec2();

    public Contact(Body ra, Body rb, double penetration, Vec2 normal, Vec2 point) {
        this.ra = ra;
        this.rb = rb;
        this.penetration = penetration;
        this.normal.set(normal);
        this.point.set(point);
    }

    public Body getRa() {
        return ra;
    }

    public Body getRb() {
        return rb;
    }

    public double getPenetration() {
        return penetration;
    }

    public Vec2 getNormal() {
        return normal;
    }

    public Vec2 getPoint() {
        return point;
    }

    public void resolveCollision() {
        // TODO
    }
    
    public void drawDebug(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawLine((int) point.x, (int) point.y, (int) (point.x + normal.x * penetration), (int) (point.y + normal.y * penetration));
        g.setColor(Color.RED);
        g.fillOval((int) (point.x - 4), (int) (point.y - 4), 8, 8);
    }
    
}
