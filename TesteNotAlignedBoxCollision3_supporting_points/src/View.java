import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author leonardo
 */
public class View extends JPanel {
    
    private final Vec2 mousePosition = new Vec2();
    
    private final Body bodyA = new Body(80);
    private final Body bodyB = new Body(100);

    private final Stroke stroke = new BasicStroke(3);
    
    
    public View() {
        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        
        Graphics2D g2d = (Graphics2D) g;
        //g.drawLine(0, 0, getWidth(), getHeight());
        
        g2d.setStroke(stroke);
        
        bodyA.position.set(400, 300);
        bodyA.angle = Math.PI / 3;
        bodyA.drawDebug(g2d);

        bodyB.position.set(mousePosition);
        //bodyB.angle = Math.PI / 4;
        bodyB.drawDebug(g2d);
        
        Contact c = Collision.checkBoxBox(bodyB, bodyA);
        if (c != null) {
            c.drawDebug(g2d);
            
            g2d.setColor(Color.BLUE);
            g2d.drawString("COLLIDES", 50, 50);
            g2d.drawString("Collision penetration depth: " + ((int) c.penetration), 50, 70);
            g2d.drawString("Collision point: (" + ((int) c.point.x) + ", " + ((int) c.point.y) + ")", 50, 90);
        }
    }
    
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            mousePosition.set(e.getX(), e.getY());
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent me) {
            bodyB.angle += 0.1;
            repaint();
        }
        
        
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                View view = new View();
                JFrame frame = new JFrame();
                frame.setTitle("2D Physics test - Box vs Box Collision detection / SAT + Supporting points");
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
