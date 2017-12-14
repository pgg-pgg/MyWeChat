package com.pgg.mywechatem.Uitils;



import com.pgg.mywechatem.Domian.Moments;
import com.pgg.mywechatem.Domian.MyMessageBean;
import com.pgg.mywechatem.Domian.WalletBean;
import com.pgg.mywechatem.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by PDD on 2017/11/18.
 */

public class CreateDataUtils {
//    private static String name[]=new String[]{"浅唱","瑞瑞","徐启坤","李思明","贾哲","瞿安锟","何敏飞","安元浩","党攀","李根"};
//    private static String msg[]=new String[]{"你若不能度我成佛，我便引你入魔","你若安好，便是晴天","分享一次特别成功的外卖"
//            ,"忘带u盘真的是坑，这2.0的速度","随着心得方向去做自己爱做的事","就像留白一样，空盘才能给人以丰富的画面感与想象力"
//            ,"看着窗外熟悉的一片灰茫，思乡心切，还有两周，再挺一挺，就能回去了","等不到下课了...我真的饿狠了","天天在外面跑，很长时间没有呵护","这几件好看么"};
//    public static ArrayList<Moments> createMoments(){
//        ArrayList<Moments> momentses=new ArrayList<>();
//        Random random=new Random();
//        for (int i=0;i<10;i++){
//            Moments moments=new Moments();
//            moments.user_name=name[i];
//            moments.message=msg[i];
//            int time=random.nextInt(50);
//            moments.time=time+"分钟前";
//            momentses.add(moments);
//        }
//        return momentses;
//    }
    private static String[] tx_url=new String[]{
            "http://www.rong360.com/credit/sq?rtm_ke=68178195029&rtm_cr=17885323259&utm_source=baidu&utm_medium=semxyk&utm_campaign=hun",
            "http://re.jd.com/search?keyword=%E6%89%8B%E6%9C%BA%E5%85%85%E5%80%BC&keywordid=27357842342&re_dcp=202m0QjIIg==&traffic_source=1004&test=1&enc=utf8&cu=true&utm_source=baidu-search&utm_medium=cpc&utm_campaign=t_262767352_baidusearch&utm_term=27357842342_0_121cd3f29fa24bb68177b693dc798c21",
            "https://qian.qq.com/index.shtml?stat_data=oum62ppcsy004&ADTAG=SCQD.PINZ.PC.CZZ1",
            "http://life.ule.com/",
            "https://pay.qq.com/",
            "https://city.weixin.qq.com/index.html",
            "http://gongyi.qq.com/"
    };
    private static String[] recomment_url=new String[]{
            "https://mobike.com/cn/"
    };
    private static String[] from_url=new String[]{
            "http://www.12306.cn/mormhweb/",
            "http://www.didichuxing.com/",
            "http://search.jd.com/Search?keyword=%E4%BA%AC%E4%B8%9C%E4%BC%98%E9%80%89&enc=utf-8&spm=2.1.0",
            "http://waimai.meituan.com/?utm_campaign=baidu&utm_source=1522",
            "http://www.wepiao.com/",
            "http://www.zgchwlw.roboo.com/",
            "http://www.meixin.cn/Home/Application_jd?aid=209937",
            "http://www.mogujie.com/book/clothing/",
            "http://m.daojia.com/tg/sem16_3/?hmsr=baidu_sem_71558939575_18719180009_1_cl1_1"
    };
    private static int[] tx_img=new int[]{
            R.drawable.wallet_xykhd, R.drawable.wallet_pay,R.drawable.wallet_licaitong,
            R.drawable.wallet_shjf,R.drawable.wallet_qq,R.drawable.wallet_city,
            R.drawable.wallet_heart
    };
    private static int[] recomment_img=new int[]{
            R.drawable.wallet_mobike
    };
    private static int[] from_img=new int[]{
            R.drawable.wallet_car,R.drawable.wallet_didi,R.drawable.wallet_jdyx,
            R.drawable.wallet_meituan,R.drawable.wallet_movie,R.drawable.wallet_eat,
            R.drawable.wallet_jiudian,R.drawable.wallet_jie,R.drawable.wallet_58
    };
    private static String[] tx_name=new String[]{
            "信用卡还贷","手机充值","理财通",
            "生活缴费","Q币充值","城市服务",
            "腾讯公益"
    };
    private static String[] recomment_name=new String[]{
            "摩拜单车"
    };
    private static String[] from_name=new String[]{
            "火车票机票","滴滴出行","京东优选",
            "美团外卖","电影演出赛事","吃喝玩乐",
            "酒店","蘑菇街女装","58到家"
    };

    public static WalletBean createWalletBean(){
        WalletBean walletBean=new WalletBean();
        ArrayList<WalletBean.TXServiceInfoBean> txServiceInfoBeens=new ArrayList<>();
        ArrayList<WalletBean.RecommendAct> recommendActs=new ArrayList<>();
        ArrayList<WalletBean.FromOtherService> fromOtherServices=new ArrayList<>();
        for (int i=0;i<tx_img.length;i++){
            WalletBean.TXServiceInfoBean txServiceInfoBean=new WalletBean.TXServiceInfoBean();
            txServiceInfoBean.setImage(tx_img[i]);
            txServiceInfoBean.setName(tx_name[i]);
            txServiceInfoBean.setUrl(tx_url[i]);
            txServiceInfoBeens.add(txServiceInfoBean);
        }
        for (int i=0;i<recomment_img.length;i++){
            WalletBean.RecommendAct recommendAct=new WalletBean.RecommendAct();
            recommendAct.setImage(recomment_img[i]);
            recommendAct.setName(recomment_name[i]);
            recommendAct.setUrl(recomment_url[i]);
            recommendActs.add(recommendAct);
        }
        for (int i=0;i<from_img.length;i++){
            WalletBean.FromOtherService fromOtherService=new WalletBean.FromOtherService();
            fromOtherService.setImage(from_img[i]);
            fromOtherService.setName(from_name[i]);
            fromOtherService.setUrl(from_url[i]);
            fromOtherServices.add(fromOtherService);
        }
        walletBean.setFromOtherServices(fromOtherServices);
        walletBean.setRecommendActs(recommendActs);
        walletBean.setTxServiceInfoBeen(txServiceInfoBeens);
        return walletBean;
    }

    private static String day[] =new String[]{
            "04","09","06","13","10","08","06","11"
    };
    private static String[] month=new String[]{
            "9月","6月","3月","4月","8月","10月","5月","7月"
    };
    private static int[] img=new int[]{
            R.drawable.f_static_00,R.drawable.f_static_01,R.drawable.f_static_02,R.drawable.f_static_03,
            R.drawable.f_static_04,R.drawable.f_static_05,R.drawable.f_static_06,R.drawable.f_static_07
    };
    private static String[] message=new String[]{
            "《春晓》作者：孟浩然   春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。",
            "《鹿柴》作者：王维     空山不见人，但闻人语响。返影入深林，复照青苔上。",
            "《春晓》作者：孟浩然   春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。",
            "《春晓》作者：孟浩然   春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。",
            "《春晓》作者：孟浩然   春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。",
            "《春晓》作者：孟浩然   春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。",
            "《春晓》作者：孟浩然   春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。",
            "《春晓》作者：孟浩然   春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。",
            "《春晓》作者：孟浩然   春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。"
    };
    public static ArrayList<MyMessageBean> createMyMessage(){
        ArrayList<MyMessageBean> messageBeen=new ArrayList<>();
        for (int i=0;i<img.length;i++){
            MyMessageBean bean=new MyMessageBean();
            bean.setDay(day[i]);
            bean.setMonth(month[i]);
            bean.setImg(img[i]);
            bean.setMessage(message[i]);
            messageBeen.add(bean);
        }
        return messageBeen;
    }


}
