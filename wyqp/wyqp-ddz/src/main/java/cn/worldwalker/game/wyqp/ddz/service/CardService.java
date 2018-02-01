package cn.worldwalker.game.wyqp.ddz.service;

import cn.worldwalker.game.wyqp.ddz.card.union.*;
import cn.worldwalker.game.wyqp.ddz.constant.DdzConstant;

import java.util.*;

public class CardService {

    private static CardService instance = new CardService();

    public static CardService getInstance(){
        return instance;
    }

    private CardService(){}

    public int cardValue(Integer index){
        return index >> 2;
    }

    public List<BaseCardUnion> getBaseList(List<Integer> cardList){

        Map<Integer,List<Integer>> valueCardMap = new HashMap<>(14);
        for (Integer index: cardList){
            int value = cardValue(index);
            List<Integer> list = valueCardMap.get(value);
            if (list == null){
                list = new ArrayList<>(4);
                valueCardMap.put(value,list);
            }
            list.add(index);
        }

        List<BaseCardUnion> baseCardUnionList = new ArrayList<>(valueCardMap.size());
        for (Map.Entry<Integer,List<Integer>> entry : valueCardMap.entrySet()){
            List<Integer> list = entry.getValue();
            for (int i=0; i<list.size(); i++){
                BaseCardUnion baseCardUnion = BaseCardUnion.valueOf(list.subList(0,i+1));
                if (baseCardUnion!=null){
                    baseCardUnionList.add(baseCardUnion);
                }else {
                    System.out.println("ERRO:" + list + "," +i );
                }

            }
        }
        Collections.sort(baseCardUnionList);
        return baseCardUnionList;
    }

    public List<ShunCardUnion> getShunList2(List<BaseCardUnion> baseCardUnionList){
        Map<Integer, List<BaseCardUnion>> map = new HashMap<>(4);

        for (BaseCardUnion baseCardUnion: baseCardUnionList){
            if (baseCardUnion.getValue() == DdzConstant.KING_VALUE)
                continue;
            Integer size = baseCardUnion.getCardList().size();
            List<BaseCardUnion> list = map.get(size);
            if (list == null){
                list = new ArrayList<>(16);
                map.put(size, list);
            }
            list.add(baseCardUnion);

        }
        List<ShunCardUnion> shunCardUnionList = new ArrayList<>(20);
        for (ShunCardUnion.ShunEnum shunEnum : ShunCardUnion.ShunEnum.values()){
            List<BaseCardUnion> list = map.get(shunEnum.getBasicCardCount());
            if (list != null){
                int maxLen = list.size();
                for (int len=shunEnum.getMinLen(); len<=maxLen; len++){
                    for (int i=0; i+len<=maxLen; i++){
                        if (list.get(i).getValue() + len - 1 == list.get(i+len-1).getValue()){
                            shunCardUnionList.add(new ShunCardUnion(
                                    list.subList(i, i+len)));
                        }
                    }
                }
            }
        }
        return shunCardUnionList;
    }

    public List<ShunCardUnion> getShunList(List<BaseCardUnion> baseCardUnionList){
        return getShunList2(baseCardUnionList);
        /*
        List<ShunCardUnion> shunCardUnionList = new ArrayList<>(20);
        if (baseCardUnionList.size() > 1){
            for (ShunCardUnion.ShunEnum shunEnum : ShunCardUnion.ShunEnum.values()){
                int len = 1;
                for (int i=1 ; i< baseCardUnionList.size(); i++) {
                    BaseCardUnion base1 = baseCardUnionList.get(i-1);
                    BaseCardUnion base2 = baseCardUnionList.get(i);
                    if (base1.getCardList().size() == shunEnum.getBasicCardCount()
                            && base2.getCardList().size() == shunEnum.getBasicCardCount()
                            && base1.getValue()+1 == base2.getValue()
                            && base2.getValue() != DdzConstant.KING_VALUE){
                        len ++;
                    } else {
                        //begin + len - 1  = end
                        for (int shunSize=shunEnum.getMinLen(); shunSize<=len; shunSize++){
                            for (int shunLastIndex=i; shunLastIndex-shunSize>=i-len; shunLastIndex--) {
                                shunCardUnionList.add(new ShunCardUnion(
                                        baseCardUnionList.subList(shunLastIndex-shunSize, shunLastIndex)));
                            }
                        }
                        len = 1;
                    }
                    if (i == baseCardUnionList.size() -1){
                        for (int shunSize=shunEnum.getMinLen(); shunSize<=len; shunSize++){
                            for (int shunLastIndex=i; shunLastIndex-shunSize>=i-len; shunLastIndex--) {
                                shunCardUnionList.add(new ShunCardUnion(
                                        baseCardUnionList.subList(shunLastIndex-shunSize, shunLastIndex)));
                            }
                        }

                    }
                }
            }
        }
        return shunCardUnionList;
        */
    }

    public List<DaiCardUnion> getDaiList(List<BaseCardUnion> baseCardUnionList){
        List<DaiCardUnion> daiCardUnionList = new ArrayList<>(20);
//        Collections.sort(baseCardUnionList);
        for (DaiCardUnion.DaiEnum daiEnum : DaiCardUnion.DaiEnum.values()){
            for (int i = baseCardUnionList.size()-1; i>0 ; i--){
                BaseCardUnion mainBaseCardUnion = baseCardUnionList.get(i);
                if (mainBaseCardUnion.getCardList().size() ==  daiEnum.getMainTypeCount()){
                    int withTypeCnt = daiEnum.getWithTypeCnt();
                    List<BaseCardUnion> withTypeList = new ArrayList<>(2);
                    for (BaseCardUnion withBaseCardUnion : baseCardUnionList) {
                        if (withBaseCardUnion.getCardList().size() == daiEnum.getWithTypeCount()
                                && !withBaseCardUnion.getValue().equals(mainBaseCardUnion.getValue())) {
                            withTypeList.add(withBaseCardUnion);
                            withTypeCnt--;
                            if (withTypeCnt == 0) {
                                DaiCardUnion daiCardUnion = new DaiCardUnion(mainBaseCardUnion,
                                        withTypeList);
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

    public List<FeijiCardUnion> getFeiJiList(List<ShunCardUnion> shunCardUnionList, List<BaseCardUnion> baseCardUnionList){
        List<FeijiCardUnion> feijiCardUnionList = new ArrayList<>(20);
        for (FeijiCardUnion.FeijiEnum feijiEnum : FeijiCardUnion.FeijiEnum.values()){
            for (ShunCardUnion shunCardUnion : shunCardUnionList){
                if (shunCardUnion.getBaseCardUnionList().get(0).getCardList().size() == feijiEnum.getMainTypeCount()
                        && shunCardUnion.getBaseCardUnionList().size() == feijiEnum.getMainTypeLen()){
                    int cnt = 0;
                    List<BaseCardUnion> baseCardUnionList1 = new ArrayList<>(2);
                    for (BaseCardUnion baseCardUnion : baseCardUnionList){
                        if (baseCardUnion.getCardList().size() == feijiEnum.getWithTypeCount()
                               && !baseCardUnion.getValue().equals(shunCardUnion.getValue())
                                //飞机是两个三张
                                && !baseCardUnion.getValue().equals(shunCardUnion.getValue()+1)
                                ){
                            cnt++;
                            baseCardUnionList1.add(baseCardUnion);
                            if (cnt == feijiEnum.getWithTypeCnt()){
                                feijiCardUnionList.add(new FeijiCardUnion(shunCardUnion, baseCardUnionList1));
                                break;
                            }
                        }
                    }

                }
            }
        }
        return feijiCardUnionList;
    }

    public List<CardUnion> getUnionList(List<Integer> ddzCardList){
        List<BaseCardUnion> baseCardUnionList = getBaseList(ddzCardList);
        List<ShunCardUnion> shunCardUnionList = getShunList(baseCardUnionList);
        List<DaiCardUnion> daiCardUnionList = getDaiList(baseCardUnionList);
        List<FeijiCardUnion> feijiCardUnionList = getFeiJiList(shunCardUnionList,baseCardUnionList);

        List<CardUnion> unionList = new ArrayList<>(50);
        unionList.addAll(baseCardUnionList);
        unionList.addAll(shunCardUnionList);
        unionList.addAll(daiCardUnionList);
        unionList.addAll(feijiCardUnionList);
        return unionList;
    }

    public boolean isBigger(CardUnion cardUnion1, CardUnion cardUnion2){
        if (cardUnion1.getType().equals(cardUnion2.getType())){
            return cardUnion1.getValue() > cardUnion2.getValue();
        } else if (cardUnion1.isBomb() && !cardUnion2.isBomb()){
            return true;
        }
        return false;
    }
}
