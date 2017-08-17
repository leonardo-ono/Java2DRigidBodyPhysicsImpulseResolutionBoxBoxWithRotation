
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
        // vap1
        Vec2 vap1 = new Vec2();
        vap1.set(ra.velocity);
        
        Vec2 r1 = new Vec2();
        r1.set(point);
        r1.sub(ra.position);
                
        Vec2 val1 = new Vec2();
        val1.cross(ra.angularVelocity, r1);
        vap1.add(val1);
        
        // vap2
        Vec2 vap2 = new Vec2();
        vap2.set(rb.velocity);
        
        Vec2 r2 = new Vec2();
        r2.set(point);
        r2.sub(rb.position);
                
        Vec2 val2 = new Vec2();
        val2.cross(rb.angularVelocity, r2);
        vap2.add(val2);
        
        // relative velocity
        Vec2 vrel = new Vec2();
        vrel.set(vap1);
        vrel.sub(vap2);
        
        if (vrel.dot(normal) > 0) {
            return;
        }
        
        double e = 0.5; // restitution hard coded
        
        double raxn = r1.cross(normal);
        double rbxn = r2.cross(normal);
        
        double totalMass = (1 / ra.mass) + (1 / rb.mass) + ((raxn * raxn) / ra.inertia) + ((rbxn * rbxn) / rb.inertia);
        
        double j = (-(1 + e) * vrel.dot(normal)) / totalMass;
        
        Vec2 impulse = new Vec2();
        impulse.set(normal);
        impulse.scale(j);
        
        ra.applyImpulse(impulse, point);
        impulse.scale(-1);
        rb.applyImpulse(impulse, point);
    }
    
    public void correctPosition() {
        double f = penetration / (ra.mass + rb.mass);
        Vec2 p = new Vec2();
        
        p.set(normal);
        p.scale(f * rb.mass * 0.8);
        ra.position.add(p);
        
        p.set(normal);
        p.scale(f * ra.mass * 0.8);
        rb.position.sub(p);
    }
    
    public void drawDebug(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawLine((int) point.x, (int) point.y, (int) (point.x + normal.x * penetration), (int) (point.y + normal.y * penetration));
        g.setColor(Color.RED);
        g.fillOval((int) (point.x - 4), (int) (point.y - 4), 8, 8);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Contact c = (Contact) obj;
        if (ra == c.ra && rb == c.rb) {
            return true;
        }
        else if (ra == c.rb && rb == c.ra) {
            return true;
        }
        return false;
    }
    
}
