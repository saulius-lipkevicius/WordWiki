package com.example.wordwiki.ui_intro.account.models;

public class KnownLanguageHelper {
    private String languageName;
    private int flag;
    private boolean a1, a2, b1, b2, c1, c2, isNative;

    public KnownLanguageHelper(String languageName, int flag, boolean a1, boolean a2, boolean b1, boolean b2, boolean c1, boolean c2, boolean isNative) {
        this.languageName = languageName;
        this.flag = flag;
        this.a1 = a1;
        this.a2 = a2;
        this.b1 = b1;
        this.b2 = b2;
        this.c1 = c1;
        this.c2 = c2;
        this.isNative = isNative;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isA1() {
        return a1;
    }

    public void setA1(boolean a1) {
        this.a1 = a1;
    }

    public boolean isA2() {
        return a2;
    }

    public void setA2(boolean a2) {
        this.a2 = a2;
    }

    public boolean isB1() {
        return b1;
    }

    public void setB1(boolean b1) {
        this.b1 = b1;
    }

    public boolean isB2() {
        return b2;
    }

    public void setB2(boolean b2) {
        this.b2 = b2;
    }

    public boolean isC1() {
        return c1;
    }

    public void setC1(boolean c1) {
        this.c1 = c1;
    }

    public boolean isC2() {
        return c2;
    }

    public void setC2(boolean c2) {
        this.c2 = c2;
    }

    public boolean isNative() {
        return isNative;
    }

    public void setNative(boolean aNative) {
        isNative = aNative;
    }

    public void setChosen(int givenNr) {
        setFalseAll();
        if (givenNr==1){
            setA1(true);
        } else if (givenNr==2) {
            setA2(true);
        } else if (givenNr==3) {
            setB1(true);
        } else if (givenNr==4) {
            setB2(true);
        } else if (givenNr==5) {
            setC1(true);
        } else if (givenNr==6) {
            setC2(true);
        } else if (givenNr==7) {
            setNative(true);
        }
    }

    public void setFalseAll() {
        setA1(false);
        setA2(false);
        setB1(false);
        setB2(false);
        setC1(false);
        setC2(false);
        setNative(false);
    }
}
