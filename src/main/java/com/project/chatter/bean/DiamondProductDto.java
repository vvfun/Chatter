package com.project.chatter.bean;

//@ApiModel("商品配置信息")
public class DiamondProductDto {
//    @ApiModelProperty("商品id")
    private Long productId;
//    @ApiModelProperty("iosid")
    private String productIosId;
//    @ApiModelProperty("可聊次数")
    private Integer callNums ;
//    @ApiModelProperty("可聊天数")
    private Integer callDays ;
//    @ApiModelProperty("名字")
    private String name ;

//    @ApiModelProperty("钻石")
    private Integer diamond = 0;// 200

//    @ApiModelProperty("原钻石") // 100
    private Integer preDiamond = 0 ;
//    @ApiModelProperty("钻石")
    private Integer diffDiamond = 0; // 100
//    @ApiModelProperty("价格")
    private Double money;
//    @ApiModelProperty("原价格")
    private Double preMoney;

//    @ApiModelProperty("当地价格")
    private Double regionMoney;
//    @ApiModelProperty("当地原价格")
    private Double regionPreMoney;
//    @ApiModelProperty("当地价格单位")
    private String regionUnitDesc ;


    private double regionRate;

//    @ApiModelProperty("类型，0普通，1hot，2特殊")
    private Integer type = 0;
//    @ApiModelProperty("描述")
    private String desc ;
//    @ApiModelProperty("展示类型，0不展示1展示,二进制位数有意义，最右为普通列表，第二个为促销列表")
    private Integer status = 1;
//    @ApiModelProperty("排序类型，越小越在前")
    private Integer sort = 100;
//    @ApiModelProperty("vip群组")
    private int vipType ;
//    @ApiModelProperty("appCode")
    private String appCode ;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductIosId() {
        return productIosId;
    }

    public void setProductIosId(String productIosId) {
        this.productIosId = productIosId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }

    public Integer getPreDiamond() {
        return preDiamond;
    }

    public void setPreDiamond(Integer preDiamond) {
        this.preDiamond = preDiamond;
    }

    public Integer getDiffDiamond() {
        return diffDiamond;
    }

    public void setDiffDiamond(Integer diffDiamond) {
        this.diffDiamond = diffDiamond;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getRegionUnitDesc() {
        return regionUnitDesc;
    }

    public void setRegionUnitDesc(String regionUnitDesc) {
        this.regionUnitDesc = regionUnitDesc;
    }

    public Double getRegionMoney() {
        return regionMoney;
    }

    public void setRegionMoney(Double regionMoney) {
        this.regionMoney = regionMoney;
    }

    public double getRegionRate() {
        return regionRate;
    }

    public void setRegionRate(double regionRate) {
        this.regionRate = regionRate;
    }
}
