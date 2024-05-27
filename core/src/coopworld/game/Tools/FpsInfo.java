package coopworld.game.Tools;

public class FpsInfo{
    double avg_fps;
    String arr;

    public FpsInfo(double avg_fps, String arr) {
        this.avg_fps = avg_fps;
        this.arr = arr;
    }

    public double getAvg_fps() {
        return avg_fps;
    }

    public String getArr() {
        return arr;
    }
}