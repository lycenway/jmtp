package be.derycke.pieter.com;

/**
 *
 * @author Pieter De Rycke
 */
public class COMException extends Exception {
	/*
	#define FACILITY_AAF               18  \\ 00000010010
	#define FACILITY_ACS               20  \\ 00000010100
	#define FACILITY_BACKGROUNDCOPY    32  \\ 00000100000
	#define FACILITY_CERT              11  \\ 00000001011
	#define FACILITY_COMPLUS           17  \\ 00000010001
	#define FACILITY_CONFIGURATION     33  \\ 00000100001
	#define FACILITY_CONTROL           10  \\ 00000001010
	#define FACILITY_DISPATCH           2  \\ 00000000010
	#define FACILITY_DPLAY             21  \\ 00000010101
	#define FACILITY_HTTP              25  \\ 00000011001
	#define FACILITY_INTERNET          12  \\ 00000001100
	#define FACILITY_ITF                4  \\ 00000000100
	#define FACILITY_MEDIASERVER       13  \\ 00000001101
	#define FACILITY_MSMQ              14  \\ 00000001110
	#define FACILITY_NULL               0  \\ 00000000000
	#define FACILITY_RPC                1  \\ 00000000001
	#define FACILITY_SCARD             16  \\ 00000010000
	#define FACILITY_SECURITY           9  \\ 00000001001
	#define FACILITY_SETUPAPI          15  \\ 00000001111
	#define FACILITY_SSPI               9  \\ 00000001001
	#define FACILITY_STORAGE            3  \\ 00000000011
	#define FACILITY_SXS               23  \\ 00000010111
	#define FACILITY_UMI               22  \\ 00000010110
	#define FACILITY_URT               19  \\ 00000010011
	#define FACILITY_WIN32              7  \\ 00000000111
	#define FACILITY_WINDOWS            8  \\ 00000001000
	#define FACILITY_WINDOWS_CE        24  \\ 00000011000
	
	bron: http://msdn2.microsoft.com/en-us/library/ms694497(VS.85).aspx
	*/
	
	public static final int FACILITY_WIN32 = 7;
	public static final int FACILITY_WINDOWS = 8;
	public static final int FACILITY_WPD = 42;
    
    private int hresult;
    
    public COMException(String message, int hresult) {
        super(message);
        this.hresult = hresult;
    }
    
    public COMException(int hresult) {
    	this.hresult = hresult;
    }
    
    public long getHresult() {
        return hresult & 0xffffffffl;
    }
    
    //(((hr) >> 31) & 0x1)
    public int getSeverityCode() {
    	return (hresult >> 31) & 0x1;
    }
    
    //#define FACILITY_WPD 42
    //#define HRESULT_FACILITY(hr)  (((hr) >> 16) & 0x1fff)
    public int getFacilityCode() {
    	return (hresult >> 16) & 0x1fff;
    }
    
    public int getErrorCode() {
    	return hresult & 0xffff;
    }
    
    public String toString() {
    	return this.getClass().getCanonicalName() + ": " + getMessage() + " (0x" + Long.toHexString(getHresult()) + ")";
    }
}
