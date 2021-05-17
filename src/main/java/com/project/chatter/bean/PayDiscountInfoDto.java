package com.project.chatter.bean;


import java.util.List;


public class PayDiscountInfoDto {

    private String title ;
    private String remark ;
    private List<DiamondProductDto> productList ;
    private List<DayDiamond> dayDiamondList ;

  /*  public PayDiscountInfoDto(String title, String remark, List<DiamondProductDto> productList, List<DayDiamond> dayDiamondList) {
        this.title = title;
        this.remark = remark;
        this.productList = productList;
        this.dayDiamondList = dayDiamondList;
    }

    public PayDiscountInfoDto(String title, String remark) {
        this.title = title;
        this.remark = remark;
    }
*/
    public static class DayDiamond{

        private String dayDesc ;
        private String diamond ;



        public String getDayDesc() {
            return dayDesc;
        }

        public void setDayDesc(String dayDesc) {
            this.dayDesc = dayDesc;
        }

        public String getDiamond() {
            return diamond;
        }

        public void setDiamond(String diamond) {
            this.diamond = diamond;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<DiamondProductDto> getProductList() {
        return productList;
    }

    public void setProductList(List<DiamondProductDto> productList) {
        this.productList = productList;
    }

    public List<DayDiamond> getDayDiamondList() {
        return dayDiamondList;
    }

    public void setDayDiamondList(List<DayDiamond> dayDiamondList) {
        this.dayDiamondList = dayDiamondList;
    }
}
