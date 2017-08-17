
/**
 *
 * @author leonardo
 */
public class Mouse {
    
    public static Vec2 position = new Vec2();
    public static double x;
    public static double y;
    public static boolean pressed;
    
    public static Vec2 getPosition() {
        position.set(x, y);
        return position;
    }
    
}
