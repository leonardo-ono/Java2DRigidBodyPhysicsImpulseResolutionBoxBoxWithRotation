

/**
 *
 * @author leonardo
 */
public class Collision {
    
    private static final Box W_BOX_A = new Box();
    private static final Box W_BOX_B = new Box();

    private static final SupportingPointResult SUP_POINT_RESULT_A = new SupportingPointResult();
    private static final SupportingPointResult SUP_POINT_RESULT_B = new SupportingPointResult();
    
    public static Contact checkBoxBox(Body ba, Body bb) {
        if (!(ba.getShape() instanceof Box) || !(bb.getShape() instanceof Box)) {
            return null;
        }
        
        Box boxA = (Box) ba.getShape();
        Box boxB = (Box) bb.getShape();
        
        W_BOX_A.convertToWorldSpace(boxA, ba.getTransform());
        W_BOX_B.convertToWorldSpace(boxB, bb.getTransform());

        getClosestSupportingPoint(W_BOX_A, W_BOX_B, SUP_POINT_RESULT_A);
        getClosestSupportingPoint(W_BOX_B, W_BOX_A, SUP_POINT_RESULT_B);
        
        if (SUP_POINT_RESULT_A.supportingPoint == null || SUP_POINT_RESULT_B.supportingPoint == null) {
            return null;
        }
        
        Vec2 contactPoint = null;
        Vec2 normal = new Vec2();
        double penetration = -Integer.MAX_VALUE;
        
        if (SUP_POINT_RESULT_A.supportingPoint != null) {
            contactPoint = SUP_POINT_RESULT_A.supportingPoint;
            penetration = SUP_POINT_RESULT_A.minDistance;
            normal.set(SUP_POINT_RESULT_A.normal);
        }

        if (SUP_POINT_RESULT_B.supportingPoint != null 
                && SUP_POINT_RESULT_B.minDistance > penetration) {
            
            contactPoint = SUP_POINT_RESULT_B.supportingPoint;
            penetration = SUP_POINT_RESULT_B.minDistance;
            normal.set(SUP_POINT_RESULT_B.normal);
        }
        
        return new Contact(ba, bb, -penetration, normal, contactPoint);
    }
    
    private static final SupportingPointResult SUP_POINT_RESULT_TMP = new SupportingPointResult();
    private static final Vec2 V_TMP = new Vec2();
    
    // boxA -> edges / boxB -> vertices
    private static void getClosestSupportingPoint(Box boxA, Box boxB, SupportingPointResult supportingPointResult) {
        supportingPointResult.set(null, null, -Integer.MAX_VALUE);
        for (Box.Edge edge : boxA.edges) {
            SUP_POINT_RESULT_TMP.set(null, null, 0);
            Vec2 normal = edge.normal;
            boolean allPositive = true;
            for (Vec2 v : boxB.vertices) {
                V_TMP.set(v);
                V_TMP.sub(edge.a.x, edge.a.y);
                double distance = normal.dot(V_TMP);
                if (distance < SUP_POINT_RESULT_TMP.minDistance) {
                    allPositive = false;
                    SUP_POINT_RESULT_TMP.set(v, normal, distance);
                }
            }
            // not collides - all vertices dot normal are positive
            if (allPositive) {
                supportingPointResult.set(null, null, 0);
                return;
            }
            else if (SUP_POINT_RESULT_TMP.minDistance > supportingPointResult.minDistance) {
                supportingPointResult.set(SUP_POINT_RESULT_TMP.supportingPoint
                        , SUP_POINT_RESULT_TMP.normal, SUP_POINT_RESULT_TMP.minDistance);
            }
        }
    }
    
    private static class SupportingPointResult {
        Vec2 supportingPoint;
        Vec2 normal;
        double minDistance;
        
        void set(Vec2 supportingPoint, Vec2 normal, double minDistance) {
            this.supportingPoint = supportingPoint;
            this.normal = normal;
            this.minDistance = minDistance;
        }
    }
    
}
