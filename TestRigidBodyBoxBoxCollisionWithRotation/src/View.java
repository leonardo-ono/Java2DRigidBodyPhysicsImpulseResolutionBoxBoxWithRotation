import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author leonardo
 */
public class View extends JPanel {
    
    public static final Vec2 gravity = new Vec2(0, 1);
    
    private final List<Body> bodies = new ArrayList<Body>();
    private final Set<Contact> contacts = new HashSet<Contact>();
    
    private final Stroke stroke = new BasicStroke(3);
    
    public View() {
        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        
        createAllBodies();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                update();
                repaint();
            }
        }, 100, 1000 / 30);
    }
    
    private void createAllBodies() {
        // static walls
        
        Body wl = new Body(300, 99999999999d, 99999999999d);
        Body wr = new Body(300, 99999999999d, 99999999999d);
        Body wt = new Body(360, 99999999999d, 99999999999d);
        Body wb = new Body(360, 99999999999d, 99999999999d);
        
        wl.position.set(-280, 300);
        wr.position.set(800 + 280, 300);
        wt.position.set(400, -340);
        wb.position.set(400, 600 + 340 - 20);
        
        // bodies
        
        Body bodyA = new Body(75, 2, 7500);
        Body bodyB = new Body(60, 1.5, 3000);
        Body bodyC = new Body(80, 2.5, 10000);
        //Body bodyD = new Body(100, 3, 12000);
        
        bodyA.position.set(600, 300);
        bodyA.angle = Math.PI / 3;
        bodyA.velocity.set(-3, -4);
        bodyA.angularVelocity = 0.075;
        
        bodyB.position.set(200, 350);
        bodyB.angle = Math.PI / 5;
        bodyB.velocity.set(4, 4);
        bodyB.angularVelocity = 0.1;

        bodyC.position.set(400, 150);
        bodyC.angle = Math.PI / 2;
        bodyC.velocity.set(0, 3);
        bodyC.angularVelocity = 0.12;

//        bodyD.position.set(400, 450);
//        bodyD.angle = Math.PI / 2;
//        bodyD.velocity.set(0, 3);
//        bodyD.angularVelocity = 0.25;
        
        bodies.add(bodyA);
        bodies.add(bodyB);
        bodies.add(bodyC);
        //bodies.add(bodyD);
        
        bodies.add(wl);
        bodies.add(wr);
        bodies.add(wt);
        bodies.add(wb);
    }

    private final Vec2 impulse = new Vec2();
    
    public void update() {
        if (Mouse.pressed) {
            //Mouse.pressed = false;
            impulse.set(10, 0);
            bodies.get(0).applyForce(impulse, Mouse.getPosition());
        }
        
        updateIntegration();
        checkCollisions();
        resolveCollisions();
        correctPositions();
    }
    
    private void updateIntegration() {
        for (Body body : bodies) {
            body.update();
        }
    }

    private void checkCollisions() {
        contacts.clear();
        for (Body b1 : bodies) {
            for (Body b2 : bodies) {
                if (b1 == b2) {
                    continue;
                }
                Contact c = Collision.checkBoxBox(b1, b2);
                if (c != null && !contacts.contains(c)) {
                    contacts.add(c);
                }
            }
        }
    }
    
    private void resolveCollisions() {
        for (Contact contact : contacts) {
            contact.resolveCollision();
        }
    }

    private void correctPositions() {
        for (Contact contact : contacts) {
            contact.correctPosition();
        }
    }
    
    public void draw(Graphics2D g) {
        for (Body body : bodies) {
            body.drawDebug(g);
        }
        for (Contact contact : contacts) {
            contact.drawDebug(g);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(stroke);
        draw(g2d);
    }
    
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            Mouse.x = e.getX();
            Mouse.y = e.getY();
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent me) {
            Mouse.pressed = true;
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Mouse.pressed = false;
            repaint();
        }
        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                View view = new View();
                JFrame frame = new JFrame();
                frame.setTitle("2D Physics - Box vs Box / Impulse resolution test (with rotation)");
                frame.getContentPane().add(view);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setVisible(true);
                view.requestFocus();
            }
        });
    }    
    
}
