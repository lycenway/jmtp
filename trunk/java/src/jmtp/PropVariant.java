/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jmtp;

/**
 *
 * @author Pieter De Rycke
 */
class PropVariant {
    
    static final int VT_LPWSTR = 31;
    
    private int vt;
    private String pwszVal;

    public PropVariant(int vt, String pwszVal) {
        this.vt = vt;
        this.pwszVal = pwszVal;
    }
    
    public int getVt() {
        return vt;
    }
    
    public String getPwszVal() {
        return pwszVal;
    }
}
