package com.project.chatter.bean;


/**
 * Created by gigabud on 18-2-2.
 */

public class UserBean extends BaseBean {

    private String id;
    private long uid;
    private String loginId;
    private String nickname;
    private String avatar;
    private String smallAvatar;
    private int gender;
    private int age;
    private String tag;
    private String phoneNumber;
    private String deviceCode;
    private int deviceType;
    private String thirdPlatform;
    private String loginTime;
    private String createdAt;
    private String updatedAt;
    private String thirdOpenid;
    private String firebaseCode;
    private String regionCode;
    private String shareCodeRegisterDevice;
    private String ownShareCode;
    private String shareCode;
    private String address;
    private int status;
    private double longitude;
    private double latitude;
    private String email;
    private String mobile;
    private int followNum;
    private int followerNum;
    private int tagStatus;
    private String myCode;
    private int chatStatus;  //0表示不在线 1空闲 2表示忙
    private int chatRealStatus;  //20-真实在线
    private int diamond;
    private int preMinuteDiamond;
    private String extraImg;
    private String remark;
    private int userType;//3,4为主播
    private int minConsumeDiamond;
    private int userStatus;//0为普通状态，1为一日通
    private String registeFrom;
//    private List<MediaUrlBean> extraInfoList;
    private String showVideo;
    private String chatNo;//socket chatNum

    private String hobby;
    private String genderInterest;
    private String likeType;
    private String lookFor;
    private String popularity;
    private String distance;

    private boolean isAnonymous;
    private String fakeUrl;
    private int chatNums;
    private int followStar;

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(int chatStatus) {
        this.chatStatus = chatStatus;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getPreMinuteDiamond() {
        return preMinuteDiamond;
    }

    public void setPreMinuteDiamond(int preMinuteDiamond) {
        this.preMinuteDiamond = preMinuteDiamond;
    }

    public String getExtraImg() {
        return extraImg;
    }

    public void setExtraImg(String extraImg) {
        this.extraImg = extraImg;
    }

    public String getMyCode() {
        return myCode;
    }

    public void setMyCode(String myCode) {
        this.myCode = myCode;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getSmallAvatar() {
        return smallAvatar;
    }

    public void setSmallAvatar(String smallAvatar) {
        this.smallAvatar = smallAvatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getThirdPlatform() {
        return thirdPlatform;
    }

    public void setThirdPlatform(String thirdPlatform) {
        this.thirdPlatform = thirdPlatform;
    }

    public String getThirdOpenid() {
        return thirdOpenid;
    }

    public void setThirdOpenid(String thirdOpenid) {
        this.thirdOpenid = thirdOpenid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOwnShareCode() {
        return ownShareCode;
    }

    public void setOwnShareCode(String ownShareCode) {
        this.ownShareCode = ownShareCode;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(int tagStatus) {
        this.tagStatus = tagStatus;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getFirebaseCode() {
        return firebaseCode;
    }

    public void setFirebaseCode(String firebaseCode) {
        this.firebaseCode = firebaseCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getShareCodeRegisterDevice() {
        return shareCodeRegisterDevice;
    }

    public void setShareCodeRegisterDevice(String shareCodeRegisterDevice) {
        this.shareCodeRegisterDevice = shareCodeRegisterDevice;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(int followerNum) {
        this.followerNum = followerNum;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getMinConsumeDiamond() {
        return minConsumeDiamond;
    }

    public void setMinConsumeDiamond(int minConsumeDiamond) {
        this.minConsumeDiamond = minConsumeDiamond;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getRegisteFrom() {
        return registeFrom;
    }

    public void setRegisteFrom(String registeFrom) {
        this.registeFrom = registeFrom;
    }
//
//    public List<MediaUrlBean> getExtraInfoList() {
//        return extraInfoList;
//    }
//
//    public void setExtraInfoList(List<MediaUrlBean> extraInfoList) {
//        this.extraInfoList = extraInfoList;
//    }

    public String getShowVideo() {
        return showVideo;
    }

    public void setShowVideo(String showVideo) {
        this.showVideo = showVideo;
    }

    public String getChatNo() {
        return chatNo;
    }

    public void setChatNo(String chatNo) {
        this.chatNo = chatNo;
    }

    public int getChatRealStatus() {
        return chatRealStatus;
    }

    public void setChatRealStatus(int chatRealStatus) {
        this.chatRealStatus = chatRealStatus;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getGenderInterest() {
        return genderInterest;
    }

    public void setGenderInterest(String genderInterest) {
        this.genderInterest = genderInterest;
    }

    public String getLikeType() {
        return likeType;
    }

    public void setLikeType(String likeType) {
        this.likeType = likeType;
    }

    public String getLookFor() {
        return lookFor;
    }

    public void setLookFor(String lookFor) {
        this.lookFor = lookFor;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getFakeUrl() {
        return fakeUrl;
    }

    public void setFakeUrl(String fakeUrl) {
        this.fakeUrl = fakeUrl;
    }

    public int getChatNums() {
        return chatNums;
    }

    public void setChatNums(int chatNums) {
        this.chatNums = chatNums;
    }

    public int getFollowStar() {
        return followStar;
    }

    public void setFollowStar(int followStar) {
        this.followStar = followStar;
    }
}
