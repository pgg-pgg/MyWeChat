package com.pgg.mywechatem.Domian;

import java.util.List;

/**
 * Created by PDD on 2017/11/19.
 */

public class WalletBean {
    private List<FromOtherService> fromOtherServices;
    private List<RecommendAct> recommendActs;
    private List<TXServiceInfoBean> txServiceInfoBeen;

    public List<FromOtherService> getFromOtherServices() {
        return fromOtherServices;
    }

    public void setFromOtherServices(List<FromOtherService> fromOtherServices) {
        this.fromOtherServices = fromOtherServices;
    }

    public List<RecommendAct> getRecommendActs() {
        return recommendActs;
    }

    public void setRecommendActs(List<RecommendAct> recommendActs) {
        this.recommendActs = recommendActs;
    }

    public List<TXServiceInfoBean> getTxServiceInfoBeen() {
        return txServiceInfoBeen;
    }

    public void setTxServiceInfoBeen(List<TXServiceInfoBean> txServiceInfoBeen) {
        this.txServiceInfoBeen = txServiceInfoBeen;
    }

    public static class FromOtherService{
        private String url;
        private String name;
        private int image;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }
    }

    public static class RecommendAct{
        private String url;
        private String name;
        private int image;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }
    }

    public static class TXServiceInfoBean{
        private String url;
        private String name;
        private int image;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }
    }


}
