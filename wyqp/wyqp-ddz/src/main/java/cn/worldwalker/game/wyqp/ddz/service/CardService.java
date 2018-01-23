package cn.worldwalker.game.wyqp.ddz.service;

import cn.worldwalker.game.wyqp.ddz.card.DdzCard;
import cn.worldwalker.game.wyqp.ddz.card.union.*;

import java.util.*;

public class CardService {

    private static CardService instance = new CardService();

    public static CardService getInstance(){
        return instance;
    }

    private CardService(){}

    public List<BaseCardUnion> getBaseList(List<DdzCard> ddzCardList){
        Set<Integer> valueSet = new HashSet<>(ddzCardList.size());
        for (DdzCard ddzCard : ddzCardList){
            valueSet.add(ddzCard.getValue());
        }
        int size = valueSet.size();
        Map<Integer,Integer> map = new HashMap<>(size);
        for (DdzCard ddzCard : ddzCardList) {
            Integer count = map.get(ddzCard.getValue());
            map.put(ddzCard.getValue(), (count == null) ? 1 : count + 1);
        }
        List<BaseCardUnion> baseCardUnionList = new ArrayList<>(size);
        for (Map.Entry<Integer,Integer> entry: map.entrySet()){
            for (int i=0; i<entry.getValue(); i++){
                baseCardUnionList.add(new BaseCardUnion(entry.getKey(),i+1));
            }
        }
        return baseCardUnionList;

    }

    public List<ShunCardUnion> getShunList(List<BaseCardUnion> baseCardUnionList){
        List<ShunCardUnion> shunCardUnionList = new ArrayList<>(20);
        Collections.sort(baseCardUnionList);
        for (ShunCardUnion.ShunEnum shunEnum : ShunCardUnion.ShunEnum.values()){
            int count = shunEnum.getBasicCardCount();
            List<Integer> valueList = new ArrayList<>(20);
            for (BaseCardUnion baseCardUnion : baseCardUnionList){
                if (baseCardUnion.getCount() >= count){
                    valueList.add(baseCardUnion.getValue());
                }
            }
            valueList.add(Integer.MAX_VALUE);
            Collections.sort(valueList);
            int len = 0, lastValue = 0;
            for (Integer value: valueList){
                if (lastValue + 1 != value ){
                    if (len >= shunEnum.getMinLen()){
                        ShunCardUnion shunCardUnion =
                                new ShunCardUnion(new BaseCardUnion(lastValue+1-len,count),len);
                        shunCardUnionList.add(shunCardUnion);
                    }
                    len = 0;
                }
                len++;
                lastValue = value;
            }
        }
        return shunCardUnionList;
    }

    public List<DaiCardUnion> getDaiList(List<BaseCardUnion> baseCardUnionList){
        List<DaiCardUnion> daiCardUnionList = new ArrayList<>(20);
        Collections.sort(baseCardUnionList);
        for (DaiCardUnion.DaiEnum daiEnum : DaiCardUnion.DaiEnum.values()){
            for (int i = baseCardUnionList.size()-1; i>0 ; i--){
                BaseCardUnion mainBaseCardUnion = baseCardUnionList.get(i);
                if (mainBaseCardUnion.getCount() ==  daiEnum.getMainTypeCount()){
                    for (BaseCardUnion withBaseCardUnion : baseCardUnionList) {
                        int withTypeCnt = daiEnum.getWithTypeCnt();
                        if (withBaseCardUnion.getCount() == daiEnum.getWithTypeCount()) {
                            withTypeCnt--;
                            if (withTypeCnt == 0) {
                                DaiCardUnion daiCardUnion = new DaiCardUnion(mainBaseCardUnion, withBaseCardUnion,
                                        daiEnum.getWithTypeCnt());
                                daiCardUnionList.add(daiCardUnion);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return daiCardUnionList;
    }

    public List<FeijiCardUnion> getFeiJiList(List<ShunCardUnion> shunCardUnionList,
                                         List<BaseCardUnion> baseCardUnionList){
        List<FeijiCardUnion> feijiCardUnionList = new ArrayList<>(20);
        for (FeijiCardUnion.FeijiEnum feijiEnum : FeijiCardUnion.FeijiEnum.values()){
            for (ShunCardUnion shunCardUnion : shunCardUnionList){
                if (shunCardUnion.getBaseType().getCount() == feijiEnum.getMainTypeCount()
                        && shunCardUnion.getSize() == feijiEnum.getMainTypeLen()){
                    int cnt = 0;
                    for (BaseCardUnion baseCardUnion : baseCardUnionList){
                        if (baseCardUnion.getCount() == feijiEnum.getWithTypeCount()){
                            cnt++;
                            if (cnt == feijiEnum.getWithTypeCnt()){
                                feijiCardUnionList.add(new FeijiCardUnion(
                                        shunCardUnion, baseCardUnion,cnt));
                                break;
                            }
                        }
                    }

                }
            }
        }
        return feijiCardUnionList;
    }


    public List<CardUnion> getUionList(List<DdzCard> ddzCardList){
        List<BaseCardUnion> baseCardUnionList = getBaseList(ddzCardList);
        List<ShunCardUnion> shunCardUnionList = getShunList(baseCardUnionList);
        List<DaiCardUnion> daiCardUnionList = getDaiList(baseCardUnionList);
        List<FeijiCardUnion> feijiCardUnionList = getFeiJiList(shunCardUnionList,baseCardUnionList);

        List<CardUnion> unionList = new ArrayList<>(100);
        unionList.addAll(baseCardUnionList);
        unionList.addAll(shunCardUnionList);
        unionList.addAll(daiCardUnionList);
        unionList.addAll(feijiCardUnionList);
        return unionList;
    }
}
