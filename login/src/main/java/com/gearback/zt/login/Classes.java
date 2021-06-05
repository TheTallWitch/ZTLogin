package com.gearback.zt.login;

public class Classes {

    public class User {
        private int ID;
        private String username;
        private String usertoken;
        private String publictoken;
        private String userfullname;
        private String usermail;
        private String usercell;
        private String iscellvalid;
        private String useravatar;
        private String usergender;
        private String usercity;
        private String uvalid;
        private String isguest;
        private String logedin;
        private String currencyid;
        private String date_register;
        private String date_lasttime;
        private int active_acc;

        public User(String username, String usertoken, String publictoken, String userfullname, String usermail, String usercell, String iscellvalid, String useravatar, String usergender, String usercity, String uvalid, String isguest, String logedin, String currencyid, String date_register, String date_lasttime, int active_acc, int ID) {
            this.username = username;
            this.usertoken = usertoken;
            this.publictoken = publictoken;
            this.userfullname = userfullname;
            this.usermail = usermail;
            this.usercell = usercell;
            this.iscellvalid = iscellvalid;
            this.useravatar = useravatar;
            this.usergender = usergender;
            this.usercity = usercity;
            this.uvalid = uvalid;
            this.isguest = isguest;
            this.logedin = logedin;
            this.currencyid = currencyid;
            this.date_register = date_register;
            this.date_lasttime = date_lasttime;
            this.active_acc = active_acc;
            this.ID = ID;
        }

        public String getUsername() {return username;}
        public String getUsertoken() {return usertoken;}
        public String getPublictoken() {return publictoken;}
        public String getUserfullname() {return userfullname;}
        public String getUsermail() {return usermail;}
        public String getUsercell() {return usercell;}
        public String getIscellvalid() {return iscellvalid;}
        public String getUseravatar() {return useravatar;}
        public String getUsergender() {return usergender;}
        public String getUsercity() {return usercity;}
        public String getUvalid() {return uvalid;}
        public String getIsguest() {return isguest;}
        public String getLogedin() {return logedin;}
        public String getCurrencyid() {return currencyid;}
        public String getDate_register() {return date_register;}
        public String getDate_lasttime() {return date_lasttime;}
        public int getID() {return ID;}
        public int getActive_acc() {return active_acc;}
    }
}
