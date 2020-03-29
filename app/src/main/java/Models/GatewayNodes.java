package Models;

public class GatewayNodes {
    private String handicappedUp;
    private String handicappedDown;
    private String nonHandicappedUp;
    private String nonHandicappedDown;
    private String outside;

    public GatewayNodes(String handicappedUp, String handicappedDown, String nonHandicappedUp, String nonHandicappedDown, String outside) {
        this.handicappedUp = handicappedUp;
        this.handicappedDown = handicappedDown;
        this.nonHandicappedUp = nonHandicappedUp;
        this.nonHandicappedDown = nonHandicappedDown;
        this.outside = outside;
    }

    public String getHandicappedUp() { return handicappedUp; }
    public String getHandicappedDown() { return handicappedDown; }
    public String getNonHandicappedUp() { return nonHandicappedUp; }
    public String getNonHandicappedDown() { return nonHandicappedDown; }
    public String getOutside() { return outside; }
}
