package com.project.chatter.bean;

public class ProductBean extends BaseBean {
    private int diamond;
    private double money;
    private long productId;
    private int type;
    private String desc;
    private String discountDesc;
    private int  preDiamond;
    private int  diffDiamond;
    private String productIosId;
    private String regionUnitDesc;
    private String regionMoney;
    private double regionRate;

    private int drawableInt;

    private String oneDay;

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProductIosId() {
        return productIosId;
    }

    public void setProductIosId(String productIosId) {
        this.productIosId = productIosId;
    }

    public int getPreDiamond() {
        return preDiamond;
    }

    public void setPreDiamond(int preDiamond) {
        this.preDiamond = preDiamond;
    }

    public int getDiffDiamond() {
        return diffDiamond;
    }

    public void setDiffDiamond(int diffDiamond) {
        this.diffDiamond = diffDiamond;
    }

    public String getRegionUnitDesc() {
        return regionUnitDesc;
    }

    public void setRegionUnitDesc(String regionUnitDesc) {
        this.regionUnitDesc = regionUnitDesc;
    }

    public int getDrawableInt() {
        return drawableInt;
    }

    public void setDrawableInt(int drawableInt) {
        this.drawableInt = drawableInt;
    }


    public double getRegionRate() {
        return regionRate;
    }

    public void setRegionRate(double regionRate) {
        this.regionRate = regionRate;
    }

    public String getRegionMoney() {
        return regionMoney;
    }

    public void setRegionMoney(String regionMoney) {
        this.regionMoney = regionMoney;
    }


    public String getOneDay() {
        return oneDay;
    }

    public void setOneDay(String oneDay) {
        this.oneDay = oneDay;
    }

    public String getDiscountDesc() {
        return discountDesc;
    }

    public void setDiscountDesc(String discountDesc) {
        this.discountDesc = discountDesc;
    }
}
