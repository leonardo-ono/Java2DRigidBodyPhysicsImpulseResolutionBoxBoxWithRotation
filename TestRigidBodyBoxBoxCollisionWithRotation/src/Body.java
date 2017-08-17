
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 *
 * @author leonardo
 */
public class Body {
    
    public final Shape shape;
    public final AffineTransform transform = new AffineTransform();
    
    public final double mass;
    public final double inertia;
    
    public final Vec2 position = new Vec2();
    
    public final Vec2 velocity = new Vec2();
    public final Vec2 acceleration = new Vec2();
    public final Vec2 force = new Vec2();

    public double angle = 0;
    public double angularVelocity = 0;
    public double angularAcceleration = 0;
    public double torque = 0;

    public final Vec2 vTmp = new Vec2();
    public final Point2D pTmp = new Point2D.Double();
    
    public Body(double boxSize, double mass, double inertia) {
        this.shape = new Box(boxSize);
        this.mass = mass;
        this.inertia = inertia;
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

    public double getMass() {
        return mass;
    }

    public double getInertia() {
        return inertia;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public Vec2 getAcceleration() {
        return acceleration;
    }

    public Vec2 getForce() {
        return force;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    public double getTorque() {
        return torque;
    }
    
    public AffineTransform getTransform() {
        transform.setToIdentity();
        transform.translate(position.x, position.y);
        transform.rotate(angle);
        return transform;
    }

    public AffineTransform getInvertTransform() {
        transform.setToIdentity();
        transform.rotate(-angle);
        transform.translate(-position.x, -position.y);
        return transform;
    }
    
    public void applyForce(Vec2 f, Vec2 worldSpacePosition) {
        // linear
        this.force.add(f);
        
        // angular
        vTmp.set(worldSpacePosition);
        vTmp.sub(position);
        applyTorque(vTmp.cross(f));
    }
    
    public void applyTorque(double torque) {
        this.torque += torque;
    }

    public void applyImpulse(Vec2 impulse, Vec2 worldSpacePosition) {
        // linear
        vTmp.set(impulse);
        vTmp.scale(1 / mass);
        velocity.add(vTmp);
        
        // angular
        vTmp.set(worldSpacePosition);
        vTmp.sub(position);
        angularVelocity += (vTmp.cross(impulse) / inertia);
    }
    
    public void update() {
        position.add(velocity);
        velocity.add(acceleration);
        // force.add(View.gravity);
        force.scale(1 / mass);
        acceleration.set(force);
        
        angle += angularVelocity;
        angularVelocity += angularAcceleration;
        angularAcceleration = torque / inertia;
        
        force.set(0, 0);
        torque = 0;
    }
    
    public void drawDebug(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        g.rotate(angle);
        shape.drawDebug(g);
        g.setTransform(at);
    }
    
}
