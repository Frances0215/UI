package com.example.ui;

public class UserData {
        private String userName;                  //用户名
        private String userPwd;                   //用户密码
        private String userSex;
        private String userTelephone;
        private String userWorkplace;
        private String userPhoto;
        private String userBirthday;

        public int pwdResetFlag=0;
        //获取用户名
        public String getUserName() {             //获取用户名
            return userName;
        }
        //设置用户名
        public void setUserName(String userName) {  //输入用户名
            this.userName = userName;
        }
        //获取用户密码
        public String getUserPwd() {                //获取用户密码
            return userPwd;
        }


    public String getUserPhoto() {
        return userPhoto;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public String getUserSex() {
        return userSex;
    }

    public String getUserWorkplace() {
        return userWorkplace;
    }

    public String getBirthday() {
        return userBirthday;
    }


    //设置用户密码
        public void setUserPwd(String userPwd) {     //输入用户密码
            this.userPwd = userPwd;
        }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public void setUserWorkplace(String userWorkplace) {
        this.userWorkplace = userWorkplace;
    }

    public void setBirthday(String birthday) {
        this.userBirthday = birthday;
    }

    public UserData(String userTelephone, String userPwd) {  //这里只采用用户名和密码
            super();
            this.userTelephone = userTelephone;
            this.userPwd = userPwd;
        }

        public UserData(String name,String sex,String birthday,String telephone,String workplace,String photo){
            super();
            this.userName = name;
            this.userBirthday = birthday;
            this.userPhoto = photo;
            this.userSex = sex;
            this.userWorkplace = workplace;
            this.userTelephone = telephone;
        }
}
