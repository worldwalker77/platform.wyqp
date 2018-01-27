package cn.worldwalker.game.wyqp.ddz.card;

/**
 * Created by zhangmin on 2018/1/12.
 */
public class DdzCard implements Comparable<DdzCard>{
    private int value;

    public DdzCard(){

    }

    public DdzCard(int value) {
        this.value = value;
    }


    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(DdzCard o) {
        return this.value - o.value;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DdzCard ddzCard = (DdzCard) o;

        return value == ddzCard.value;
    }

    @Override
    public int hashCode() {
        return value;
    }


    /* todo cardCache
    private static Class CardCache{
        DdzCard[] caches;
        static{
            caches = new DdzCard[54];
            for (int i=0; i<4; i++){
                for (int j=0; j<14; j++){
                    caches[i*13+j] = new DdzCard(i+1,j+1);
                }
            }
        }

    }
    */

}
