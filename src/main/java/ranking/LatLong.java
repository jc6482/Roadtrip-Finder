package ranking;

/**
 * Created by dean on 3/28/16.
 */
public class LatLong {
    private double Lat = 0;
    private double Long = 0;



    public double getLat() {
        return Lat;
    }


    public double getLong() {
        return Long;
    }

    public LatLong(double Lat, double Long){
        this.Lat = Lat;
        this.Long = Long;
    }


	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LatLong latLong = (LatLong) o;

        if (Double.compare(latLong.Lat, Lat) != 0) return false;
        return Double.compare(latLong.Long, Long) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(Lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Long);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
